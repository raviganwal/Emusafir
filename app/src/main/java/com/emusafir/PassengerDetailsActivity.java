package com.emusafir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.emusafir.model.PendingBookedModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.userauth.LoginActivity;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.emusafir.utility.Validation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emusafir.utility.Constant.GOTO_WHICH_SCREEN;

public class PassengerDetailsActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = PassengerDetailsActivity.class.getSimpleName();
    private static final int INSURANCE_AMOUNT = 15;
    private String mEmail, mName, mMobile;
    private Button btnLogin;
    private TextInputLayout tilName, tilMobile, tilEmail;
    private TextInputEditText etName, etEmail, etMobile;
    private TextView tvCancel, tvPayNow, tvSeatsOneWay, tvSeatsRoundTrip, tvCityOneWAy, tvCityRoundTrip, tvInsuranceAmount, tvTaxAmount, tvTotal, tvTotalPayableAmount;
    private CheckBox chkTandC;
    private MaterialCardView mCardViewLogin;
    private int mTotalPrice;
    private double mTotalPayablePrice, mTotalTax;
    //    private String mySeatArray;
    private JSONObject jsonObjectMain;

    private String oneWayArrayStr, roundTripArrayStr;
    private List<String> mSeatNoOneWayList, mSeatNoRoundTripList;
    private RadioGroup rgInsurance;
    private AppCompatRadioButton rbInsuranceYes, rbInsuranceNo;
    private boolean isInsured;
    private MyProgressDialog mMyProgressDialog;
    PendingBookedModel mPendingBookedModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);
        mMyProgressDialog = new MyProgressDialog(PassengerDetailsActivity.this, R.drawable.two_dots_loader);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mTotalPrice = getIntent().getIntExtra("mTotalPrice", 0);
//        mySeatArray = getIntent().getStringExtra("mySeatArray");
        jsonObjectMain = new JSONObject();
        oneWayArrayStr = App.getInstance().getPrefManager().getOneWayBookingArray();
        roundTripArrayStr = App.getInstance().getPrefManager().getRoundTripBookingArray();
        Log.e(TAG, "oneWayArrayStr " + oneWayArrayStr);
        Log.e(TAG, "roundTripArrayStr " + roundTripArrayStr);
        try {
            if (oneWayArrayStr != null) {
                mTotalPrice = mTotalPrice + new JSONObject(oneWayArrayStr).getJSONObject("one_way").getInt("Total_Price");
                jsonObjectMain.put("one_way", new JSONObject(oneWayArrayStr).getJSONObject("one_way"));
            }
            if (roundTripArrayStr != null) {
                mTotalPrice = mTotalPrice + new JSONObject(roundTripArrayStr).getJSONObject("round_trip").getInt("Total_Price");
                jsonObjectMain.put("round_trip", new JSONObject(roundTripArrayStr).getJSONObject("round_trip"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
//        mEmail = etEmail.getText().toString().trim();
//        mMobile = etMobile.getText().toString().trim();
//        mEmail = etEmail.getText().toString().trim();
//        mName = etName.getText().toString().trim();

        mCardViewLogin = findViewById(R.id.mCardViewLogin);

        tilName = findViewById(R.id.tilName);
        tilMobile = findViewById(R.id.tilMobile);
        tilEmail = findViewById(R.id.tilEmail);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etName.addTextChangedListener(new MyTextWatcher(etName));
        etMobile.addTextChangedListener(new MyTextWatcher(etMobile));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));

        tvSeatsOneWay = findViewById(R.id.tvSeatsOneWay);
        tvSeatsRoundTrip = findViewById(R.id.tvSeatsRoundTrip);

        tvCityOneWAy = findViewById(R.id.tvCityOneWAy);
        tvCityRoundTrip = findViewById(R.id.tvCityRoundTrip);

        rgInsurance = findViewById(R.id.rgInsurance);
        rbInsuranceYes = findViewById(R.id.rbInsuranceYes);
        rbInsuranceNo = findViewById(R.id.rbInsuranceNo);

        rbInsuranceYes.setText(Html.fromHtml(getString(R.string.insurance_t_and_c)));
        rbInsuranceYes.setMovementMethod(LinkMovementMethod.getInstance());
        tvInsuranceAmount = findViewById(R.id.tvInsuranceAmount);
        tvInsuranceAmount.setText(getResources().getString(R.string.rupee) + 0);

        tvTaxAmount = findViewById(R.id.tvTaxAmount);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotalPayableAmount = findViewById(R.id.tvTotalPayableAmount);

        rgInsurance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbInsuranceYes) {
                    isInsured = true;
                    tvInsuranceAmount.setText(getResources().getString(R.string.rupee) + 15);
                    mTotalPayablePrice = mTotalPrice + mTotalTax + INSURANCE_AMOUNT;
                } else if (checkedId == R.id.rbInsuranceNo) {
                    if (isInsured) {
                        tvInsuranceAmount.setText(getResources().getString(R.string.rupee) + 0);
                        mTotalPayablePrice = mTotalPrice + mTotalTax;
                    }
                    isInsured = false;
                }
                tvTotalPayableAmount.setText(getResources().getString(R.string.rupee) + mTotalPayablePrice);
            }
        });
        try {
            JSONObject mJsonObject = new JSONObject(oneWayArrayStr);
            if (mJsonObject.has("one_way")) {
                JSONObject mOneWayJson = mJsonObject.getJSONObject("one_way");
                mSeatNoOneWayList = new ArrayList<>();
                for (int i = 0; i < mOneWayJson.getJSONArray("seat_arr").length(); i++) {
                    JSONObject jsonObject = mOneWayJson.getJSONArray("seat_arr").getJSONObject(i);
                    Log.e(TAG, "one_way " + jsonObject.getString("seat_no"));
                    mSeatNoOneWayList.add(jsonObject.getString("seat_no"));
                }
                if (mSeatNoOneWayList.size() > 0) {
                    tvCityOneWAy.setText(App.getInstance().getPrefManager().getOneWayBus().getFrom() + " To " + App.getInstance().getPrefManager().getOneWayBus().getTo());
                    tvSeatsOneWay.setText(getResources().getString(R.string.seat_no) + mSeatNoOneWayList.toString());
                }
            }

            if (roundTripArrayStr != null) {
                JSONObject mJsonObjectRound = new JSONObject(roundTripArrayStr);
                if (mJsonObjectRound.has("round_trip")) {
                    JSONObject mJsonRound = mJsonObjectRound.getJSONObject("round_trip");
                    Log.e(TAG, "mJsonRound  " + mJsonRound.toString());
                    mSeatNoRoundTripList = new ArrayList<>();
                    for (int i = 0; i < mJsonRound.getJSONArray("seat_arr").length(); i++) {
                        JSONObject jsonObject = mJsonRound.getJSONArray("seat_arr").getJSONObject(i);
                        Log.e(TAG, "round_trip " + jsonObject.getString("seat_no"));
                        mSeatNoRoundTripList.add(jsonObject.getString("seat_no"));
                    }
                }
                if (mSeatNoRoundTripList.size() > 0) {
                    tvCityRoundTrip.setVisibility(View.VISIBLE);
                    tvSeatsRoundTrip.setVisibility(View.VISIBLE);
                    tvCityRoundTrip.setText(App.getInstance().getPrefManager().getRoundTripBus().getFrom() + " To " + App.getInstance().getPrefManager().getRoundTripBus().getTo());
                    tvSeatsRoundTrip.setText(getResources().getString(R.string.seat_no) + mSeatNoRoundTripList.toString());
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTotalTax = mTotalPrice * .18;
        tvTotal.setText(getResources().getString(R.string.rupee) + mTotalPrice);
        tvTotal.setText(getResources().getString(R.string.rupee) + mTotalPrice);
        tvTaxAmount.setText(getResources().getString(R.string.rupee) + mTotalTax);
        mTotalPayablePrice = mTotalPrice + mTotalTax;
        tvTotalPayableAmount.setText(getResources().getString(R.string.rupee) + mTotalPayablePrice);


        tvPayNow = findViewById(R.id.tvPayNow);
        tvCancel = findViewById(R.id.tvCancel);
        tvPayNow.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.getInstance().getPrefManager().isLoggedIn()) {
            mCardViewLogin.setVisibility(View.GONE);
            etName.setText(App.getInstance().getPrefManager().getUser().getName());
            etEmail.setText(App.getInstance().getPrefManager().getUser().getEmail());
            etMobile.setText(App.getInstance().getPrefManager().getUser().getMobile());

        } else {
            mCardViewLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPayNow:
                mEmail = etEmail.getText().toString().trim();
                mMobile = etMobile.getText().toString().trim();
                mEmail = etEmail.getText().toString().trim();
                mName = etName.getText().toString().trim();

                if (!Validation.isValidString(mName)) {
                    mCardViewLogin.getParent().requestChildFocus(mCardViewLogin, mCardViewLogin);
                    tilName.setError(getResources().getString(R.string.error_name));
                    tilName.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidPhoneNumber(mMobile)) {
                    mCardViewLogin.getParent().requestChildFocus(mCardViewLogin, mCardViewLogin);

                    tilMobile.setError(getString(R.string.error_mobile));
                    tilMobile.startAnimation(Utils.shakeError());
                } else if (!Validation.isValidEmail(mEmail)) {
                    mCardViewLogin.getParent().requestChildFocus(mCardViewLogin, mCardViewLogin);
                    tilEmail.setError(getString(R.string.error_email));
                    tilEmail.startAnimation(Utils.shakeError());
                } else {
                    try {
                        if (App.getInstance().getPrefManager().isLoggedIn())
                            jsonObjectMain.put("User_id", App.getInstance().getPrefManager().getUser().getId());
                        else
                            jsonObjectMain.put("User_id", "0");
                        jsonObjectMain.put("Email", mEmail);
                        jsonObjectMain.put("Phone", mMobile);
                        jsonObjectMain.put("Name", mName);
                        jsonObjectMain.put("TotalTax", mTotalTax);

                        if (isInsured) {
                            mTotalPrice = mTotalPrice + INSURANCE_AMOUNT;
                            jsonObjectMain.put("insurance_agreement", INSURANCE_AMOUNT);
                        } else {
                            jsonObjectMain.put("insurance_agreement", 0);

                        }
                        jsonObjectMain.put("transaction_id", Constant.PENDING);
                        jsonObjectMain.put("payment_mode", "razorpay");
                        jsonObjectMain.put("payment_status", Constant.PENDING);
                        bookingseats();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.tvCancel:
                cancelDialog();
                break;
            case R.id.btnLogin:
                startActivity(new Intent(PassengerDetailsActivity.this, LoginActivity.class).putExtra(GOTO_WHICH_SCREEN, PassengerDetailsActivity.class.getSimpleName()));
                break;

        }
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", getResources().getString(R.string.app_name));

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Ticket No." + mPendingBookedModel.getTicketNo());

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", mTotalPrice * 100);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, "PAYMENT " + s);
        update_payment_status(s, Constant.CONFIRM);
//        try {
//            jsonObjectMain.put("transaction_id", s);
//            jsonObjectMain.put("payment_mode", "razorpay");
//            jsonObjectMain.put("payment_status", "success");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onPaymentError(int i, String s) {
        update_payment_status(s, Constant.FAILED);
//        try {
//            jsonObjectMain.put("transaction_id", "");
//            jsonObjectMain.put("payment_mode", "razorpay");
//            jsonObjectMain.put("payment_status", "failed");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public void bookingseats() {
        mMyProgressDialog.show();
//        mMyProgressDialog.setCancelable(false);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.e(TAG, "bookingseats " + jsonObjectMain.toString());
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), jsonObjectMain.toString());
        apiService.bookingseats(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mMyProgressDialog.isShowing())
                    mMyProgressDialog.dismiss();

                try {
                    String respo = response.body().string();
                    if (response.code() == 200) {
                        JSONObject jsonObject = new JSONObject(respo);
                        mPendingBookedModel = new Gson().fromJson(jsonObject.getJSONObject("result").toString(), PendingBookedModel.class);
                        startPayment();
//                        goToMainActivity();
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", PassengerDetailsActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), PassengerDetailsActivity.this);
                                }
                                break;
                        }
                    }
//                    else {
//                        tvInfo.setVisibility(View.VISIBLE);
//                        if (object.has("msg"))
//                            tvInfo.setText(object.getString("msg"));
//                    }tvFrom
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mMyProgressDialog.isShowing())
                    mMyProgressDialog.dismiss();

                t.printStackTrace();

            }
        });
    }

    public void update_payment_status(String transaction_id, String payment_status) {
        mMyProgressDialog.show();
//        mMyProgressDialog.setCancelable(false);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
//        ticket_no:2661120
//        payment_mode:aaa
//        transaction_id:3545768
//        payment_status:2

        Log.e(TAG, "ticket_no " + mPendingBookedModel.getTicketNo());
        Log.e(TAG, "payment_mode " + mPendingBookedModel.getPaymentMode());
        Log.e(TAG, "transaction_id " + transaction_id);
        Log.e(TAG, "payment_status " + payment_status);

        HashMap<String, String> params = new HashMap<>();
        params.put("ticket_no",mPendingBookedModel.getTicketNo());
        params.put("payment_mode",mPendingBookedModel.getPaymentMode());
        params.put("transaction_id",transaction_id);
        params.put("payment_status",payment_status);

        apiService.update_payment_status(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mMyProgressDialog.isShowing())
                    mMyProgressDialog.dismiss();

                try {
                    String respo = response.body().string();
                    Log.e(TAG, "respo " + respo);

                    if (response.code() == 200) {
                        goToMainActivity();
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", PassengerDetailsActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), PassengerDetailsActivity.this);
                                }
                                break;
                        }
                    }
//                    else {
//                        tvInfo.setVisibility(View.VISIBLE);
//                        if (object.has("msg"))
//                            tvInfo.setText(object.getString("msg"));
//                    }tvFrom
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mMyProgressDialog.isShowing())
                    mMyProgressDialog.dismiss();

                t.printStackTrace();

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
            }
        }
    }

    private void cancelDialog() {

        View modelBottomSheet = LayoutInflater.from(PassengerDetailsActivity.this).inflate(R.layout.cancel_booking, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(PassengerDetailsActivity.this, R.style.CustomBottomSheetDialogTheme);
        dialog.setContentView(modelBottomSheet);

        TextView tvNo = modelBottomSheet.findViewById(R.id.tvNo);
        TextView tvYes = modelBottomSheet.findViewById(R.id.tvYes);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToMainActivity();
            }
        });
        dialog.show();

    }

    private void goToMainActivity() {
        App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(null);
        Intent intent = new Intent(PassengerDetailsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
