package com.emusafir.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emusafir.PassengerDetailsActivity;
import com.emusafir.R;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.emusafir.utility.Validation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emusafir.utility.Constant.GOTO_WHICH_SCREEN;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private String mEmail, mPassword, mName, mMobile;
    public static final String TAG = SignUpActivity.class.getSimpleName();

    private Button btnSignUp;
    private TextInputLayout tilName, tilMobile, tilEmail, tilPassword;
    private TextInputEditText etName, etEmail, etMobile, etPassword;

    private MyProgressDialog mDialog;
    private String whichScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDialog = new MyProgressDialog(this, R.drawable.spinner_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        whichScreen = getIntent().getStringExtra(Constant.GOTO_WHICH_SCREEN);
        init();
    }

    private void init() {
        tilName = findViewById(R.id.tilName);
        tilMobile = findViewById(R.id.tilMobile);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etName.addTextChangedListener(new MyTextWatcher(etName));
        etMobile.addTextChangedListener(new MyTextWatcher(etMobile));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                mEmail = etEmail.getText().toString().trim();
                mMobile = etMobile.getText().toString().trim();
                mEmail = etEmail.getText().toString().trim();
                mPassword = etPassword.getText().toString().trim();
                mName = etName.getText().toString().trim();

                if (!Validation.isValidString(mName)) {
                    tilName.setError(getResources().getString(R.string.error_name));
                    tilName.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidPhoneNumber(mMobile)) {
                    tilMobile.setError(getString(R.string.error_mobile));
                    tilMobile.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidString(mEmail)) {
                    tilEmail.setError(getString(R.string.error_email_empty));
                    tilEmail.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidEmail(mEmail)) {
                    tilEmail.setError(getString(R.string.error_email));
                    tilEmail.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidString(mPassword)) {
                    tilPassword.setError(getString(R.string.error_password_empty));
                    tilPassword.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidPassword(mPassword)) {
                    tilPassword.setError(getString(R.string.error_password));
                    tilPassword.startAnimation(Utils.shakeError());
                } else {
                    callSignUpApi();
                }
                break;

        }


    }


    private void callSignUpApi() {
        mDialog.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("name", mName);
        hm.put("password", mPassword);
        hm.put("email", mEmail);
        hm.put("mobile", mMobile);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.registraion(hm).enqueue(new Callback<ResponseBody>() {
            @Override

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mDialog.dismiss();

                try {
                    if (response.code() == 200) {
                        String str = response.body().string();
//{"status":"ok","message":"Your registration successfully search.","cookie":"Ravi|1531115399|HV6bmeEXvnC2dxaKmfY1IfMRY7JssF6RR7HBZaavzoS|792264fd2dbec14f8f0815fa38836508e8f4ac0fe96cd58eafe8244189af03e4","user_id":11}
                        Log.e(TAG, str);

                        JSONObject jsonObject = new JSONObject(str);

                        if (jsonObject.has("message")) {
                            Utils.showMessage(jsonObject.getString("message"), SignUpActivity.this);
                        }
                        Log.d(TAG, str);
                        JSONObject obj = jsonObject.getJSONObject("result");
                        if (whichScreen != null && whichScreen.equalsIgnoreCase(PassengerDetailsActivity.class.getSimpleName()))
                            startActivity(new Intent(SignUpActivity.this, VerifyOTPactivity.class)
                                    .putExtra(Constant.OBJECT, etMobile.getText().toString().trim())
                                    .putExtra(GOTO_WHICH_SCREEN, PassengerDetailsActivity.class.getSimpleName()));
                        else
                            startActivity(new Intent(SignUpActivity.this, VerifyOTPactivity.class).putExtra(Constant.OBJECT, etMobile.getText().toString().trim()));
                    } else {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (jsonObject.has("message")) {
                            Utils.showMessage(jsonObject.getString("message"), SignUpActivity.this);
                        }
                    }
                } catch (
                        Exception e) {

                    e.printStackTrace();

                }

            }


            @Override

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mDialog.dismiss();


            }

        });


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

                case R.id.etName:
                    if (!Validation.isValidString(etName.getText().toString().trim()))
                        tilName.setError(getString(R.string.error_name));
                    else {
                        tilName.setError(null);
                        tilName.setErrorEnabled(false);
                    }
                    break;
                case R.id.etMobile:
                    if (!Validation.isValidPhoneNumber(etMobile.getText().toString().trim())) {
                        tilMobile.setError(getString(R.string.error_mobile));
                    } else {
                        tilMobile.setError(null);
                        tilMobile.setErrorEnabled(false);
                    }
                    break;
                case R.id.etEmail:
                    if (!Validation.isValidString(etEmail.getText().toString().trim()))
                        tilEmail.setError(getString(R.string.error_email_empty));
                    else if (!Validation.isValidEmail(etEmail.getText().toString().trim())) {
                        tilEmail.setError(getString(R.string.error_email));
                    } else {
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