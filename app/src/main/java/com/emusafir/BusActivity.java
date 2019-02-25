package com.emusafir;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.emusafir.adapter.BusAdapter;
import com.emusafir.model.BusModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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

//un used
public class BusActivity extends AppCompatActivity {
    private static final String TAG = BusActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<BusModel> mOnWardList;
    private List<BusModel> mRoundList;
    private BusAdapter mAdapter;
    private TextView tvInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyProgressDialog mMyProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BusActivity.this);
//        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(CityActivity.this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                CityModel model = mOnWardList.get(position);
////                Toast.makeText(CityActivity.this, movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
//                if (model.getId() != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra(Constant.OBJECT, model);
//                    intent.putExtra(Constant.OBJECT2, getIntent().getStringExtra(Constant.OBJECT));
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();//finishing activity
//                } else {
//                    Intent intent = new Intent();
//                    setResult(Activity.RESULT_CANCELED, intent);
//                    finish();//finishing activity
//                }
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//            }
//        }));
        mOnWardList = new ArrayList<>();
        mRoundList = new ArrayList<>();
//        mAdapter = new BusAdapter(BusActivity.this, mOnWardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BusActivity.this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(
//                new DividerItemDecoration(BusActivity.this, R.drawable.divider));
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
        mMyProgressDialog = new MyProgressDialog(this, R.drawable.bus_gif);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        tvInfo = findViewById(R.id.tvInfo);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccentDark);
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            search_bus_list();
        } else
            Utils.showMessage(getString(R.string.not_connected_to_internet), BusActivity.this);
//        showSnack(isConnected);
    }

    public void search_bus_list() {
//        mSwipeRefreshLayout.setRefreshing(true);
        mMyProgressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        final String trip_type = getIntent().getStringExtra("trip_type");
        hm.put("trip_type", getIntent().getStringExtra("trip_type"));
        hm.put("from_city", getIntent().getStringExtra("from_city"));
        hm.put("to_city", getIntent().getStringExtra("to_city"));
        hm.put("onward_date", getIntent().getStringExtra("onward_date"));
        if (trip_type.equalsIgnoreCase(Constant.ROUND_TRIP))
            hm.put("return_date", getIntent().getStringExtra("return_date"));
        Log.e(TAG, hm.toString());
        apiService.search_bus_list(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mMyProgressDialog.dismiss();
//                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String respo = response.body().string();
                    JSONObject object = new JSONObject(respo);
                    Log.e(TAG, "search_bus_list " + respo);
                    if (response.code() == 200) {
                        tvInfo.setVisibility(View.INVISIBLE);
                        JSONObject resultObject = object.getJSONObject("result");
                        JSONArray onwardArray = resultObject.getJSONArray("onward_list");
                        JSONArray roundArray = resultObject.getJSONArray("round_list");
                        if (onwardArray.length() == 0) {
                            tvInfo.setVisibility(View.VISIBLE);
                            tvInfo.setText(getResources().getString(R.string.no_data_found));
                        }

                        List<BusModel> itemsOnward = new Gson().fromJson(String.valueOf(onwardArray), new TypeToken<List<BusModel>>() {
                        }.getType());
                        if (trip_type.equalsIgnoreCase(Constant.ROUND_TRIP)) {
                            List<BusModel> itemsRound = new Gson().fromJson(String.valueOf(roundArray), new TypeToken<List<BusModel>>() {
                            }.getType());
                            mRoundList.clear();
                            mRoundList.addAll(itemsRound);
                        }
                        mOnWardList.clear();
                        mAdapter.notifyDataSetChanged();
                        mOnWardList.addAll(itemsOnward);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scheduleLayoutAnimation();
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", BusActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), BusActivity.this);
                                }
                                break;
                        }
                    }
//                    else {
//                        tvInfo.setVisibility(View.VISIBLE);
//                        if (object.has("msg"))
//                            tvInfo.setText(object.getString("msg"));
//                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                mSwipeRefreshLayout.setRefreshing(false);
                mMyProgressDialog.dismiss();
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
