package com.emusafir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.emusafir.fragments.BoardingPointFragment;
import com.emusafir.fragments.DroppingPointFragment;
import com.emusafir.interfaces.NotifyPointLayout;
import com.emusafir.model.buslayoutmodel.BusLayoutModel;
import com.emusafir.model.buslayoutmodel.PointModel;
import com.emusafir.model.buslayoutmodel.SeatModel;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.Utils;
import com.emusafir.utility.Validation;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BoardingDroppingActivity extends AppCompatActivity implements NotifyPointLayout {

    private static final String TAG = BoardingDroppingActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private String tabTitles[] = new String[]{"Boarding Points", "Dropping Points"};
    private BusLayoutModel mBusLayoutModel;
    private String mBoardingPoint, mDroppingPoint;
    private int mTotalPrice;
    private List<SeatModel> mySeatArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_dropping);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBusLayoutModel = (BusLayoutModel) getIntent().getSerializableExtra("mBusLayoutModel");
        mTotalPrice = getIntent().getIntExtra("mTotalPrice", 0);
        mySeatArray = (List<SeatModel>) getIntent().getSerializableExtra("mySeatArray");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!Validation.isValidString(mBoardingPoint) && tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).select();
                    Utils.showMessage("Please select boarding point first" + tab.getPosition(), BoardingDroppingActivity.this);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void notifyPointLayout(PointModel model, boolean isBoarding) {
        Log.e(TAG, "notifyPointLayout " + model.getId() + " " + model.getName() + " " + isBoarding);
        if (isBoarding) {
            mBoardingPoint = model.getId();
            if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ONE_WAY)) {
                App.getInstance().getPrefManager().setOneWayBoardingPoint(mBoardingPoint);
            } else if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP)) {
                App.getInstance().getPrefManager().setRoundTripBoardingPoint(mBoardingPoint);
            }
            tabLayout.getTabAt(1).select();
        } else {
            mDroppingPoint = model.getId();
//            tabLayout.getTabAt(0).select();
            if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ONE_WAY)) {
                App.getInstance().getPrefManager().setOneWayDroppingPoint(mDroppingPoint);
            } else if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP)) {
                App.getInstance().getPrefManager().setRoundTripDroppingPoint(mDroppingPoint);
            }
            if (App.getInstance().getPrefManager().getSearch().getTripType().equalsIgnoreCase(Constant.ROUND_TRIP)) {
                if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ONE_WAY)) {
                    saveOneWayArray();
                    //one wat booking done now round way is in progress
                    App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(Constant.ROUND_TRIP);
                    Intent intent = new Intent(BoardingDroppingActivity.this, BusSearchTabActivity.class);
                    startActivity(intent);
                } else if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP)) {

                    saveRoundTripArray();
                    Log.e(TAG, "FINAL ARRAY ROUND_TRIP" + App.getInstance().getPrefManager().getOneWayBookingArray() + App.getInstance().getPrefManager().getRoundTripBookingArray());
                    Intent intent = new Intent(BoardingDroppingActivity.this, PassengerDetailsActivity.class);
//                intent.putExtra("mySeatArray", mySeatArray);
                    startActivity(intent);
                }
            } else if (App.getInstance().getPrefManager().getSearch().getTripType().equalsIgnoreCase(Constant.ONE_WAY)) {
                saveOneWayArray();
                Log.e(TAG, "FINAL ARRAY ONE_WAY " + App.getInstance().getPrefManager().getOneWayBookingArray());
                Intent intent = new Intent(BoardingDroppingActivity.this, PassengerDetailsActivity.class);
                startActivity(intent);
            }
        }
    }

    private void saveRoundTripArray() {
        try {
            JSONArray mArray = new JSONArray();
            JSONObject jsonRoundTrip = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            jsonRoundTrip.put("Total_Price", mTotalPrice);
            jsonRoundTrip.put("Bus_Id", App.getInstance().getPrefManager().getRoundTripBus().getBusId());
            jsonRoundTrip.put("booking_date", App.getInstance().getPrefManager().getRoundTripBus().getBookDate());
            jsonRoundTrip.put("Route_Id", App.getInstance().getPrefManager().getRoundTripBus().getRouteId());

            jsonRoundTrip.put("boarding_id", App.getInstance().getPrefManager().getRoundTripBoardingPoint());
            jsonRoundTrip.put("droping_id", App.getInstance().getPrefManager().getRoundTripDroppingPoint());

            for (SeatModel seatModel : mySeatArray) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("seat_no", seatModel.getSeatNo());
                jsonObject.put("seat_type", seatModel.getSeatType());
                jsonObject.put("price", seatModel.getPrice());
                jsonObject.put("gender", seatModel.getGender());
                mArray.put(jsonObject);
            }
            jsonRoundTrip.put("seat_arr", mArray);
            jsonMain.put("round_trip", jsonRoundTrip);

            Log.e(TAG, "ONE_WAY" + jsonMain);
            App.getInstance().getPrefManager().setRoundTripBookingArray(jsonMain.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveOneWayArray() {
        try {
            JSONArray mArray = new JSONArray();
            JSONObject jsonOneWay = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            jsonOneWay.put("Total_Price", mTotalPrice);
            jsonOneWay.put("Bus_Id", App.getInstance().getPrefManager().getOneWayBus().getBusId());
            jsonOneWay.put("booking_date", App.getInstance().getPrefManager().getOneWayBus().getBookDate());
            jsonOneWay.put("Route_Id", App.getInstance().getPrefManager().getOneWayBus().getRouteId());

            jsonOneWay.put("boarding_id", App.getInstance().getPrefManager().getOneWayBoardingPoint());
            jsonOneWay.put("droping_id", App.getInstance().getPrefManager().getOneWayDroppingPoint());

            for (SeatModel seatModel : mySeatArray) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("seat_no", seatModel.getSeatNo());
                jsonObject.put("seat_type", seatModel.getSeatType());
                jsonObject.put("price", seatModel.getPrice());
                jsonObject.put("gender", seatModel.getGender());
                mArray.put(jsonObject);
            }
            jsonOneWay.put("seat_arr", mArray);
            jsonMain.put("one_way", jsonOneWay);

            App.getInstance().getPrefManager().setOneWayBookingArray(jsonMain.toString());
            Log.e(TAG, "ONE_WAY ONLY" + jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                    return BoardingPointFragment.newInstance(mBusLayoutModel.getBoardingPoint());
                case 1:
                    return DroppingPointFragment.newInstance(mBusLayoutModel.getDroppingPoint());
            }
            return null;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
}