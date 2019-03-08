package com.emusafir.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emusafir.R;
import com.emusafir.adapter.BusAdapter;
import com.emusafir.model.BusModel;
import com.emusafir.model.SearchModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.App;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneWayFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = OneWayFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<BusModel> mOnWardList;
    private BusAdapter mAdapter;
    private TextView tvInfo,tvDiscardReturn;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyProgressDialog mMyProgressDialog;
    private SearchModel mSearchModel;
    private TabLayout tabLayout;

    public OneWayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OneWayFragment newInstance(int sectionNumber) {
        OneWayFragment fragment = new OneWayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnWardList = new ArrayList<>();
//        mSearchModel = (SearchModel) getActivity().getIntent().getSerializableExtra(Constant.OBJECT);
        mSearchModel = App.getInstance().getPrefManager().getSearch();
        mAdapter = new BusAdapter(getActivity(), mOnWardList, mSearchModel, Constant.ONE_WAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus_search_tab, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        init(rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), R.drawable.divider));
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

        return rootView;
    }

    private void init(View rootView) {
        tabLayout = getActivity().findViewById(R.id.tabs);
        mMyProgressDialog = new MyProgressDialog(getActivity(), R.drawable.bus_gif);

        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        tvInfo = rootView.findViewById(R.id.tvInfo);

        tvDiscardReturn = rootView.findViewById(R.id.tvDiscardReturn);
        tvDiscardReturn.setVisibility(View.GONE);

        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccentDark);
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            search_bus_list();
        } else
            Utils.showMessage(getString(R.string.not_connected_to_internet), getActivity());
//        showSnack(isConnected);
    }

    public void search_bus_list() {
//        mSwipeRefreshLayout.setRefreshing(true);
        mMyProgressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();

        hm.put("trip_type", mSearchModel.getTripType());
        hm.put("from_city", mSearchModel.getFromCityId());
        hm.put("to_city", mSearchModel.getToCityId());
        hm.put("onward_date", mSearchModel.getOnwardDate());
        if (mSearchModel.getTripType().equalsIgnoreCase(Constant.ROUND_TRIP))
            hm.put("return_date", mSearchModel.getReturnDate());

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
                        mOnWardList.clear();
                        mAdapter.notifyDataSetChanged();
                        mOnWardList.addAll(itemsOnward);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scheduleLayoutAnimation();
                        if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress() != null && App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP))
                            tabLayout.getTabAt(1).select();

                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", getActivity());
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), getActivity());
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
}