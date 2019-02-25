package com.emusafir.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emusafir.utility.Constant.GOTO_WHICH_SCREEN;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String mEmail, mPassword;
    private Button btnLogin;
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private TextView tvForgetPassword;
    private boolean isPasswordVisible;
    private MyProgressDialog mDialog;
    private TextView tvSignUp;
    private String whichScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDialog = new MyProgressDialog(this, R.drawable.spinner_white);
        whichScreen = getIntent().getStringExtra(Constant.GOTO_WHICH_SCREEN);
        init();
    }

    private void init() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnLogin:
                if (!Validation.isValidPhoneNumber(mEmail)) {
                    tilEmail.setError(getString(R.string.error_mobile));
                    tilEmail.startAnimation(Utils.shakeError());
                }
//                else if (!Validation.isValidEmail(mEmail)) {
//                    tilEmail.setError(getString(R.string.error_email));
//                    tilEmail.startAnimation(Utils.shakeError());
//                }
                else if (!Validation.isValidString(mPassword)) {
                    tilPassword.setError(getString(R.string.error_password_empty));
                    tilPassword.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidPassword(mPassword)) {
                    tilPassword.setError(getString(R.string.error_password));
                    tilPassword.startAnimation(Utils.shakeError());
                } else {
                    callLoginApi();
                }
                break;
            case R.id.tvForgetPassword:
//                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                break;
            case R.id.tvSignUp:
                if (whichScreen != null && whichScreen.equalsIgnoreCase(PassengerDetailsActivity.class.getSimpleName()))
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra(GOTO_WHICH_SCREEN, PassengerDetailsActivity.class.getSimpleName()));
                else
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;

        }
    }

    private void callLoginApi() {
        mDialog.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("mobile", mEmail);
        hm.put("password", mPassword);
        if (Validation.isValidString(displayFirebaseRegId()))
            hm.put("device_token", displayFirebaseRegId());
        else
            hm.put("device_token", "");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.login(hm).enqueue(new Callback<ResponseBody>() {

            @Override

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mDialog.dismiss();

                try {
                   /*{"status":"ok","cookie":"Developer|1531133944|sdb8zOTc7qfVLMjLXVdlTTPgtnzarVvL6Rf5N288sc4|0594a04e9bffe077a92567c74de5edec8dcb8a352b17df34a4de8a38fd52ecdb","cookie_name":"wordpress_logged_in_9162d1bb4eaec75833808f80e04c04ca",
                   "user":{"id":13,"username":"Developer","nicename":"developer","email":"developer@gmail.com","url":"","registered":"2018-06-25 06:17:08","displayname":"Developer","firstname":"","lastname":"","nickname":"Developer","description":"","capabilities":{"subscriber":true},
                   "avatar":null}}*/
                    if (response.code() == 200) {
                        String str = response.body().string();
                        Log.d(TAG, str);
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getInt("response_code") == 200) {
                            if (jsonObject.has("message")) {
                                Utils.showMessage(jsonObject.getString("message"), LoginActivity.this);
                            }
                            Log.d(TAG, str);
                            UserModel mUserResponse = new Gson().fromJson(jsonObject.getJSONObject("result").toString(), UserModel.class);
                            App.getInstance().getPrefManager().setLoginSession(true);
                            App.getInstance().getPrefManager().setUser(mUserResponse);
                            if (whichScreen != null && whichScreen.equalsIgnoreCase(PassengerDetailsActivity.class.getSimpleName()))
                                onBackPressed();
                            else
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", LoginActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), LoginActivity.this);
                                }
                                break;
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

    private String displayFirebaseRegId() {
        String regId = App.getInstance().getPrefManager().getFcmRegistrationId();
        Log.e(TAG, "TAG    " + regId);
        return regId;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.etEmail:
                    if (!Validation.isValidPhoneNumber(etEmail.getText().toString().trim()))
                        tilEmail.setError(getString(R.string.error_mobile));
//                    else if (!Validation.isValidEmail(etEmail.getText().toString().trim())) {
//                        tilEmail.setError(getString(R.string.error_email));
//                    }
                    else {
                        tilEmail.setError(null);
                        tilEmail.setErrorEnabled(false);
                    }
                    break;
                case R.id.etPassword:
                    if (!Validation.isValidString(etPassword.getText().toString().trim())) {
                        tilPassword.setError(getString(R.string.error_password_empty));
                    } else if (!Validation.isValidPassword(etPassword.getText().toString().trim())) {
                        tilPassword.setError(getString(R.string.error_password));
                    } else {
                        tilPassword.setError(null);
                        tilPassword.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
