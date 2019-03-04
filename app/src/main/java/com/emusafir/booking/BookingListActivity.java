package com.emusafir.booking;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.adapter.CityAdapter;
import com.emusafir.interfaces.OnCitySelected;
import com.emusafir.model.CityModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Constant;
import com.emusafir.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity implements OnCitySelected {
    private static final String TAG = BookingListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<CityModel> mList;
    private CityAdapter mAdapter;
    private TextView tvInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();



        mList = new ArrayList<>();
        mAdapter = new CityAdapter(BookingListActivity.this, mList,this);
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
        tvInfo = findViewById(R.id.tvInfo);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccentDark);
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            city_list();
        } else
            Utils.showMessage(getString(R.string.not_connected_to_internet), BookingListActivity.this);
//        showSnack(isConnected);
    }

    public void city_list() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        apiService.city_list().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String respo = response.body().string();
                    JSONObject object = new JSONObject(respo);
                    Log.e(TAG, "communication " + respo);
                    if (response.code() == 200) {
                        tvInfo.setVisibility(View.INVISIBLE);

                        List<CityModel> items = new Gson().fromJson(String.valueOf(object.getJSONArray("result")), new TypeToken<List<CityModel>>() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_done) {
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onCitySelected(CityModel model) {
        if (model.getId() != null) {
            Intent intent = new Intent();
            intent.putExtra(Constant.OBJECT, model);
            intent.putExtra(Constant.OBJECT2, getIntent().getStringExtra(Constant.OBJECT));
            setResult(Activity.RESULT_OK, intent);
            finish();//finishing activity
        } else {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();//finishing activity
        }

    }
}
