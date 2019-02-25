package com.emusafir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.emusafir.fragments.LowerDeckFragment;
import com.emusafir.fragments.UpperDeckFragment;
import com.emusafir.interfaces.NotifyBusLayout;
import com.emusafir.model.buslayoutmodel.BusLayoutModel;
import com.emusafir.model.buslayoutmodel.SeatModel;
import com.emusafir.service.ApiClient;
import com.emusafir.service.ApiInterface;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.emusafir.adapter.SeatLayoutAdapter;

public class BusLayoutActivity extends AppCompatActivity implements NotifyBusLayout {

    private static final String TAG = BusLayoutActivity.class.getSimpleName();
    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    //    private List<LowerDack> mLowerList;
    private List<String> deckList;
    private MyProgressDialog mMyProgressDialog;
    private TabLayout tabLayout;
    private CardView mCardViewBookNow;
    private TextView tvSeatNumber, tvTotal, tvBookNow;
    private BusLayoutModel mBusLayoutModel;
    private List<SeatModel> mSeatModelList;
    private int mTotalPrice = 0;
    private List<String> mSeatNoList;
    List<SeatModel> mySeatArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_layout);
        mMyProgressDialog = new MyProgressDialog(BusLayoutActivity.this, R.drawable.two_dots_loader);

//        mLowerList = new ArrayList<>();
        deckList = new ArrayList<>();
        mSeatModelList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);


        mCardViewBookNow = findViewById(R.id.mCardViewBookNow);
        tvSeatNumber = findViewById(R.id.tvSeatNumber);
        tvBookNow = findViewById(R.id.tvBookNow);
        tvTotal = findViewById(R.id.tvTotal);

        tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tvBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusLayoutActivity.this, BoardingDroppingActivity.class);
                intent.putExtra("mBusLayoutModel", mBusLayoutModel);
                intent.putExtra("mTotalPrice", mTotalPrice);
                intent.putExtra("mySeatArray", (Serializable) mySeatArray);
                startActivity(intent);
            }
        });
        bus_layout();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void bus_layout() {
//        mSwipeRefreshLayout.setRefreshing(true);
        mMyProgressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();


        if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress() != null) {
            if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ONE_WAY)) {
                hm.put("bus_id", App.getInstance().getPrefManager().getOneWayBus().getBusId());
                hm.put("book_date", App.getInstance().getPrefManager().getOneWayBus().getBookDate());
                hm.put("route_id", App.getInstance().getPrefManager().getOneWayBus().getRouteId());
            } else if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP)) {
                hm.put("bus_id", App.getInstance().getPrefManager().getRoundTripBus().getBusId());
                hm.put("book_date", App.getInstance().getPrefManager().getRoundTripBus().getBookDate());
                hm.put("route_id", App.getInstance().getPrefManager().getRoundTripBus().getRouteId());
            }
        }
        Log.e(TAG, hm.toString());
        apiService.bus_layout(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mMyProgressDialog.dismiss();
//                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String respo = response.body().string();
                    JSONObject object = new JSONObject(respo);
                    Log.e(TAG, "bus_layout " + respo);
                    if (response.code() == 200) {
//                        tvInfo.setVisibility(View.INVISIBLE);
//                        JSONObject resultObject = object.getJSONObject("result");
//                        JSONArray onwardArray = resultObject.getJSONArray("onward_list");
//                        JSONArray roundArray = resultObject.getJSONArray("round_list");
//                        if (onwardArray.length() == 0) {
//                            tvInfo.setVisibility(View.VISIBLE);
//                            tvInfo.setText(getResources().getString(R.string.no_data_found));
//                        }

//                        List<BusLayoutModel> itemsOnward = new Gson().fromJson(String.valueOf( object.getJSONObject("result")), new TypeToken<List<BusLayoutModel>>() {
//                        }.getType());
                        mBusLayoutModel = new Gson().fromJson(object.getJSONObject("result").toString(), BusLayoutModel.class);

//                        mLowerList.clear();
//                        mLowerList.add(mBusLayoutModel.getLowerDack());
//                        for (int i = 0; i < mLowerList.size(); i++) {
                        if (mBusLayoutModel.getLowerDack() != null)
                            deckList.add("Lower Deck");
                        if (mBusLayoutModel.getUpperDack() != null)
                            deckList.add("Upper Deck");
//                            for (LowerDack model : mLowerList) {
//                                if (model.getLeft() != null) {
//                                    for (int k = 0; k < model.getLeft().size(); k++) {
//                                        List<Left> mLeft = model.getLeft().get(k);
//                                        for (Left left : mLeft)
//                                            Log.e(TAG, "testss " + left.getSeatNo());
//                                    }
//                                }
//                            }
//                        }
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        tabLayout.setupWithViewPager(mViewPager);
                    } else {
                        switch (response.code()) {
                            case 500:
                                Utils.showMessage("Internal Server Error", BusLayoutActivity.this);
                                break;
                            default:
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if (jsonObject.has("message")) {
                                    Utils.showMessage(jsonObject.getString("message"), BusLayoutActivity.this);
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
//                mSwipeRefreshLayout.setRefreshing(false);
                mMyProgressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void notifyBusLayout(SeatModel seat, int position) {
        Log.e(TAG, seat.toString());

        if (!Utils.doesSeatExist(mSeatModelList, seat))
            mSeatModelList.add(seat);
        else if (!seat.isSelected())
            mSeatModelList.remove(seat);
        for (SeatModel model : mSeatModelList) {
            Log.e(TAG, model.getSeatNo() + "< start");
        }
//        String strSeatNumber = tvSeatNumber.getText().toString();
        mTotalPrice = 0;
        mySeatArray = new ArrayList<>();
        mSeatNoList = new ArrayList<>();
        if (mSeatModelList.size() > 0) {
            mCardViewBookNow.setVisibility(View.VISIBLE);
        } else {
            mTotalPrice = 0;
            tvSeatNumber.setText(null);
            tvTotal.setText(null);
            mCardViewBookNow.setVisibility(View.GONE);
        }
        for (SeatModel model : mSeatModelList) {
//            Log.e(TAG, model.getSeatNo() + "< last");
            if (model.isSelected()) {
                mySeatArray.add(model);
                if (model.getPrice() != null)
                    mTotalPrice = mTotalPrice + Integer.parseInt(model.getPrice());

                mSeatNoList.add(model.getSeatNo());
//                    mSeatNo = mSeatNo + ", " + model.getSeatNo();
                tvTotal.setText(getResources().getString(R.string.total) + " " + getResources().getString(R.string.rupee) + mTotalPrice);
                tvSeatNumber.setText(getResources().getString(R.string.seat_no) + mSeatNoList);
            }
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return LowerDeckFragment.newInstance(mBusLayoutModel);
                case 1:
                    return UpperDeckFragment.newInstance(mBusLayoutModel);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return deckList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return deckList.get(position);
        }
    }
}