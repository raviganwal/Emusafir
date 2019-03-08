package com.emusafir.booking;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.adapter.BookingSeatAdapter;
import com.emusafir.model.booking.BookingModel;
import com.emusafir.model.booking.Detail;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Constant;
import com.emusafir.utility.PermissionUtils;
import com.emusafir.utility.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BookingDetailsActivity extends AppCompatActivity {
    private static final String TAG = BookingDetailsActivity.class.getSimpleName();
    private TextView tvBusName, tvTo, tvFrom, tvPnrNo, tvDateOfJourney, tvDateOfBooking, tvBoardingPoint, tvDroppingPoint, tvName,
            tvMobile, tvEmail, tvPaymentMode, tvTransactionId, tvInsuranceAmount, tvTaxAmount, tvSubTotal, tvTotalAmount, tvSave,tvDepartureTime,tvArrivalTime;
    private RecyclerView mRecyclerView;
    private List<Detail> mList;
    private BookingSeatAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvInfo;
    private BookingModel mBookingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        mBookingModel = (BookingModel) getIntent().getSerializableExtra(Constant.OBJECT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mBookingModel.getPNRNo());
        init();

        mList = new ArrayList<>();
        mAdapter = new BookingSeatAdapter(this, mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*mRecyclerView.addItemDecoration(
                new DividerItemDecoration(CityActivity.this, R.drawable.divider));*/
        mRecyclerView.setAdapter(mAdapter);
        checkConnection();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe", "Refreshing Number");
//                getSupportActionBar().setTitle(String.valueOf(0 + " Selected"));

                checkConnection();
            }
        });


    }

    private void init() {

        tvSave = findViewById(R.id.tvSave);

        tvBusName = findViewById(R.id.tvBusName);

        tvDepartureTime = findViewById(R.id.tvDepartureTime);
        tvArrivalTime = findViewById(R.id.tvArrivalTime);

        tvTo = findViewById(R.id.tvTo);
        tvFrom = findViewById(R.id.tvFrom);
        tvPnrNo = findViewById(R.id.tvPnrNo);
        tvDateOfJourney = findViewById(R.id.tvDateOfJourney);
        tvDateOfBooking = findViewById(R.id.tvDateOfBooking);
        tvBoardingPoint = findViewById(R.id.tvBoardingPoint);
        tvDroppingPoint = findViewById(R.id.tvDroppingPoint);
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvEmail = findViewById(R.id.tvEmail);
        tvPaymentMode = findViewById(R.id.tvPaymentMode);
        tvTransactionId = findViewById(R.id.tvTransactionId);
        tvInsuranceAmount = findViewById(R.id.tvInsuranceAmount);
        tvTaxAmount = findViewById(R.id.tvTaxAmount);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        tvInfo = findViewById(R.id.tvInfo);

        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccentDark);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.hasPermission(BookingDetailsActivity.this, WRITE_EXTERNAL_STORAGE)) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getBitmapByView(mBookingModel.getPNRNo(), (LinearLayout) findViewById(R.id.ticket));
                } else {
                    PermissionUtils.requestPermissions(BookingDetailsActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            booked_ticket_details();
        } else
            Utils.showMessage(getString(R.string.not_connected_to_internet), BookingDetailsActivity.this);
//        showSnack(isConnected);
    }

    public void booked_ticket_details() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("pnr_no", mBookingModel.getPNRNo());
        apiService.booked_ticket_details(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String respo = response.body().string();
                    JSONObject object = new JSONObject(respo);
                    Log.e(TAG, "communication " + respo);
                    if (response.code() == 200) {
                        tvInfo.setVisibility(View.INVISIBLE);
                        BookingModel model = new Gson().fromJson(object.getJSONObject("result").toString(), BookingModel.class);
                        tvBusName.setText(model.getBusName() + " \nRegistration No" + model.getRegistrationNo());
                        tvTo.setText(model.getSourceName());
                        tvFrom.setText(model.getDestinationName());
                        tvPnrNo.setText(model.getPNRNo());
                        tvDateOfJourney.setText(Utils.dateFormat_dd_MMM_yy(model.getJourneyDate()));
                        tvDateOfBooking.setText(Utils.dateFormat_dd_MMM_yy(model.getCreatedDate()));
                        tvBoardingPoint.setText(model.getBoardingName());
                        tvDroppingPoint.setText(model.getDropingName());
                        tvDroppingPoint.setText(model.getDropingName());

//                        tvDepartureTime.setText(Utils.timeFormat24hrs(model.getDepartureTime());
//                        tvArrivalTime.setText(Utils.timeFormat24hrs(model.getArrivalTime()));

                        tvName.setText(model.getName());
                        tvMobile.setText(model.getMobile());
                        tvEmail.setText(model.getEmail());
                        tvPaymentMode.setText(model.getPaymentMode());
                        tvTransactionId.setText(model.getTransactionId());
                        tvInsuranceAmount.setText(model.getInsurancePrice());
                        tvTaxAmount.setText(model.getTotalTax());
                        tvSubTotal.setText(model.getTotalPrice());
                        tvTotalAmount.setText(model.getFinalTotalPrice());

                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                        mList.addAll(model.getDetails());
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scheduleLayoutAnimation();

                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", BookingDetailsActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), BookingDetailsActivity.this);
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
                mSwipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public Bitmap getBitmapByView(String pnrNo, LinearLayout scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        //get the actual height of scrollview
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.color.colorWhite);
        }
        // create bitmap with target size
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        File path = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name) + "/");
        //Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOWNLOADS + "/" + getResources().getString(R.string.app_name) + "/");
        File file = new File(path, pnrNo + ".png");
        try {
            if (!path.exists())
                path.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
//                shareTicket(file);
                shareFile(file);
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }

//    private void shareTicket(File s) {
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/png");
//        Log.e(TAG, s.toString());
//        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(s.getAbsolutePath()));
//        startActivity(Intent.createChooser(share, "Share Ticket"));
//    }
    private void shareFile(File file) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://"+file.getAbsolutePath()));

        //if you need
        //intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File Subject);
        //intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File Description");

        startActivity(Intent.createChooser(intentShareFile, "Share File"));
        mSwipeRefreshLayout.setRefreshing(false);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    PermissionUtils.goToPermission(this);
                }
                break;
        }
    }

}
