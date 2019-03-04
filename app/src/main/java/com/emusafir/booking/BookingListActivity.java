package com.emusafir.booking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.adapter.BookingAdapter;
import com.emusafir.model.booking.BookingModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.App;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity {
    private static final String TAG = BookingListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<BookingModel> mList;
    private BookingAdapter mAdapter;
    private TextView tvInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();


        mList = new ArrayList<>();
        mAdapter = new BookingAdapter(BookingListActivity.this, mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookingListActivity.this, RecyclerView.VERTICAL, false);
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
        mRecyclerView = findViewById(R.id.mRecyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvInfo = findViewById(R.id.tvInfo);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccentDark);


    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            if (App.getInstance().getPrefManager().isLoggedIn())
                booking_list();
        } else
            Utils.showMessage(getString(R.string.not_connected_to_internet), BookingListActivity.this);
//        showSnack(isConnected);
    }

    public void booking_list() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
//        pnr_no:15516759801
//ticket_no:
//mobile_no:9993269783
//user_id:
        if (App.getInstance().getPrefManager().isLoggedIn())
            params.put("user_id", App.getInstance().getPrefManager().getUser().getId());
        else {
            params.put("pnr_no", etSearch.getText().toString().trim());
            params.put("ticket_no", etSearch.getText().toString().trim());
            params.put("mobile_no", etSearch.getText().toString().trim());
        }
        apiService.booking_list(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String respo = response.body().string();
                    JSONObject object = new JSONObject(respo);
                    Log.e(TAG, "communication " + respo);
                    if (response.code() == 200) {
                        tvInfo.setVisibility(View.INVISIBLE);

                        List<BookingModel> items = new Gson().fromJson(String.valueOf(object.getJSONArray("result")), new TypeToken<List<BookingModel>>() {
                        }.getType());
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                        mList.addAll(items);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scheduleLayoutAnimation();
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", BookingListActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), BookingListActivity.this);
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
