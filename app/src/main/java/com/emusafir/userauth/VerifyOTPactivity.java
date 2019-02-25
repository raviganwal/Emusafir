package com.emusafir.userauth;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emusafir.MainActivity;
import com.emusafir.PassengerDetailsActivity;
import com.emusafir.R;
import com.emusafir.model.UserModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.emusafir.utility.Validation;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOTPactivity extends AppCompatActivity {

    private EditText etOTP;
    private Button btnSubmit;
    private String TAG = VerifyOTPactivity.class.getSimpleName();

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private MyProgressDialog mDialog;
    private String whichScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);
        mDialog = new MyProgressDialog(this, R.drawable.two_dots_loader);
        etOTP = findViewById(R.id.etOTP);
        btnSubmit = findViewById(R.id.btnSubmit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        whichScreen = getIntent().getStringExtra(Constant.GOTO_WHICH_SCREEN);

        etOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    verifyOTPforSignUp();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Validation.isValidString(etOTP.getText().toString().trim()))
                    Toast.makeText(VerifyOTPactivity.this, "Please enter OTP.", Toast.LENGTH_SHORT).show();
                else {
                    verifyOTPforSignUp();
                }


            }
        });
        permissions();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                etOTP.setText(message);
                btnSubmit.performClick();
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private String displayFirebaseRegId() {
        String regId = App.getInstance().getPrefManager().getFcmRegistrationId();
        Log.e(TAG, "TAG    " + regId);
        return regId;
    }

    private void verifyOTPforSignUp() {
        mDialog.show();
        Intent intent = getIntent();
        String ID = intent.getStringExtra(Constant.OBJECT);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("mobile", ID);
        hm.put("otp", etOTP.getText().toString());
        if (Validation.isValidString(displayFirebaseRegId()))
            hm.put("device_token", displayFirebaseRegId());
        else
            hm.put("device_token", "");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.otp_verification(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String str = response.body().string();
                        Log.d(TAG, str);
                        JSONObject jsonObject = new JSONObject(str);
                        Utils.showMessage(jsonObject.getString("message"), VerifyOTPactivity.this);
                        UserModel mUserResponse = new Gson().fromJson(jsonObject.getJSONArray("result").getJSONObject(0).toString(), UserModel.class);
                        App.getInstance().getPrefManager().setLoginSession(true);
                        App.getInstance().getPrefManager().setUser(mUserResponse);
                        if (whichScreen != null && whichScreen.equalsIgnoreCase(PassengerDetailsActivity.class.getSimpleName()))
                            startActivity(new Intent(VerifyOTPactivity.this, PassengerDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        else
                            startActivity(new Intent(VerifyOTPactivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (jsonObject.has("message")) {
                            Utils.showMessage(jsonObject.getString("message"), VerifyOTPactivity.this);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mDialog.dismiss();
            }
        });


    }

//    public void recivedSms(String message) {
//        Log.e(TAG, message);
//        etOTP.setText(message);
//        btnSubmit.performClick();
//    }

    private void permissions() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(VerifyOTPactivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(VerifyOTPactivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(VerifyOTPactivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(VerifyOTPactivity.this, permissionsRequired[1])
            ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOTPactivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs receive sms and location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(VerifyOTPactivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOTPactivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs needs receive sms and location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant needs sms and location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(VerifyOTPactivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (

                    ActivityCompat.shouldShowRequestPermissionRationale(VerifyOTPactivity.this, permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(VerifyOTPactivity.this, permissionsRequired[1])

            ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOTPactivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs needs receive sms and location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(VerifyOTPactivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(VerifyOTPactivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(VerifyOTPactivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
