package com.emusafir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emusafir.fragments.LogoutDialogFragment;
import com.emusafir.interfaces.OnDateSelected;
import com.emusafir.model.CityModel;
import com.emusafir.model.SearchModel;
import com.emusafir.model.UserModel;
import com.emusafir.userauth.LoginActivity;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.SessionManager;
import com.emusafir.utility.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.Checkout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnDateSelected, LogoutDialogFragment.LogoutListener {
    private final static int CITY_CODE = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private MaterialButton btnSearch;
    private TextView tvOneWay, tvRoundTrip, tvFrom, tvTo, tvDepartDate, tvReturnDate;
    private View view3;
    private LinearLayout linReturn;
    private FloatingActionButton fabSwap;
    private boolean isRotated;
    private SessionManager mSessionManager;
    //models for source and destination cities
    private CityModel mFromModel, mToModel;
    //by default trip type is one way
    private String tripType = Constant.ONE_WAY;
    private SearchModel mSearchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSessionManager = App.getInstance().getPrefManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        App.getInstance().getPrefManager().setOneWayBookingArray(null);
        App.getInstance().getPrefManager().setRoundTripBookingArray(null);
        init();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        MaterialButton btnLogin = headerView.findViewById(R.id.btnLogin);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvMobile = headerView.findViewById(R.id.tvMobile);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        UserModel mUserModel = mSessionManager.getUser();
        if (mSessionManager.isLoggedIn()) {
            if (mUserModel.getName() != null)
                tvName.setText(mUserModel.getName());
            if (mUserModel.getMobile() != null)
                tvMobile.setText(mUserModel.getMobile());
        }
        Menu nav_Menu = navigationView.getMenu();
        if (mSessionManager.isLoggedIn()) {
            btnLogin.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            tvMobile.setVisibility(View.VISIBLE);
            nav_Menu.findItem(R.id.navLogout).setVisible(true);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            tvMobile.setVisibility(View.GONE);
            nav_Menu.findItem(R.id.navLogout).setVisible(false);
        }

    }

    private void init() {
        linReturn = findViewById(R.id.linReturn);
        view3 = findViewById(R.id.view3);
        fabSwap = findViewById(R.id.fabSwap);
        fabSwap.setOnClickListener(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        tvOneWay = findViewById(R.id.tvOneWay);
        tvOneWay.setOnClickListener(this);
        tvRoundTrip = findViewById(R.id.tvRoundTrip);
        tvRoundTrip.setOnClickListener(this);

        tvFrom = findViewById(R.id.tvFrom);
        tvFrom.setOnClickListener(this);
        tvTo = findViewById(R.id.tvTo);
        tvTo.setOnClickListener(this);

        tvDepartDate = findViewById(R.id.tvDepartDate);
        tvDepartDate.setText(Utils.setCurrentDate());
        tvReturnDate = findViewById(R.id.tvReturnDate);

        tvDepartDate.setOnClickListener(this);
        tvReturnDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSwap:
//
//                if (mFromModel == null) {
//                    Utils.showMessage(getString(R.string.please_select_source_city), this);
//                    return;
//                }
//                if (mToModel == null) {
//                    Utils.showMessage(getString(R.string.please_select_destination_city), this);
//                    return;
//                }

                CityModel mFrom = mFromModel;
                CityModel mTo = mToModel;
                mFromModel = mTo;
                mToModel = mFrom;
                String mStr1 = tvFrom.getText().toString();
                String mStr2 = tvTo.getText().toString();
                if (mFromModel != null) {
                    tvFrom.setText(mFromModel.getName());
                    Log.e("tvFrom", mFromModel.toString());
                } else
                    tvFrom.setText(null);

                if (mToModel != null) {
                    tvTo.setText(mToModel.getName());
                    Log.e("tvTo", mToModel.toString());
                } else
                    tvTo.setText(null);

//                Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
//                fabSwap.startAnimation(aniRotate);
                if (isRotated) {
                    isRotated = false;
                    fabSwap.animate().rotation(0).start();
                } else {
                    isRotated = true;
                    fabSwap.animate().rotation(180).start();
                }
                Utils.setAlphaAnimation(tvFrom);
                Utils.setAlphaAnimation(tvTo);
                break;
            case R.id.btnSearch:
                /*        hm.put("trip_type", "");
        hm.put("from_city", "");
        hm.put("to_city", "");
        hm.put("onward_date", "");
        hm.put("return_date", "");
*/
                if (mFromModel == null) {
                    Utils.showMessage(getResources().getString(R.string.please_select_source_city), MainActivity.this);
                } else if (mToModel == null) {
                    Utils.showMessage(getResources().getString(R.string.please_select_destination_city), MainActivity.this);
                } else {
                    App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(null);
                    Intent intent = new Intent(this, BusSearchTabActivity.class);
                    mSearchModel = new SearchModel();
                    mSearchModel.setTripType(tripType);
                    mSearchModel.setFromCity(mFromModel);
                    mSearchModel.setToCity(mToModel);
                    mSearchModel.setOnwardDate(Utils.dateFormat_yyyy_MM_dd(tvDepartDate.getText().toString().trim()));
//                    intent.putExtra("trip_type", tripType);
//                    intent.putExtra("from_city", mFromModel.getId());
//                    intent.putExtra("to_city", mToModel.getId());
//                    intent.putExtra("onward_date", Utils.dateFormat_yyyy_MM_dd(tvDepartDate.getText().toString().trim()));
                    if (tripType.equalsIgnoreCase(Constant.ROUND_TRIP))
                        mSearchModel.setReturnDate(Utils.dateFormat_yyyy_MM_dd(tvReturnDate.getText().toString().trim()));
//                        intent.putExtra("return_date", Utils.dateFormat_yyyy_MM_dd(tvReturnDate.getText().toString().trim()));
                    App.getInstance().getPrefManager().setSearch(mSearchModel);
//                    intent.putExtra(Constant.OBJECT, mSearchModel);
                    startActivity(intent);
                }
                break;
            case R.id.tvDepartDate:
                Utils.openDatePickerDialog(this, "tvDepartDate", null);
                break;
            case R.id.tvReturnDate:
                Utils.openDatePickerDialog(this, "tvReturnDate", tvDepartDate.getText().toString());
                break;
            case R.id.tvFrom:
                startActivityForResult(new Intent(MainActivity.this, CityActivity.class).putExtra(Constant.OBJECT, "tvFrom"), CITY_CODE);
                break;
            case R.id.tvTo:
                startActivityForResult(new Intent(MainActivity.this, CityActivity.class).putExtra(Constant.OBJECT, "tvTo"), CITY_CODE);
                break;
            case R.id.tvOneWay:
                tripType = Constant.ONE_WAY;
                tvReturnDate.setText(null);
                view3.setVisibility(View.GONE);
                linReturn.setVisibility(View.GONE);
                tvOneWay.setBackgroundResource(R.drawable.btn_accent);
                tvRoundTrip.setBackgroundResource(R.drawable.btn_primary);
                break;
            case R.id.tvRoundTrip:
                tripType = Constant.ROUND_TRIP;
                view3.setVisibility(View.VISIBLE);
                linReturn.setVisibility(View.VISIBLE);
                setIncrementedDate();
                tvOneWay.setBackgroundResource(R.drawable.btn_primary);
                tvRoundTrip.setBackgroundResource(R.drawable.btn_accent);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navLogout:
                Checkout.clearUserData(getApplicationContext());
                Checkout.clearUserData(getApplicationContext());
                mSessionManager.setLoginSession(false);
                LogoutDialogFragment addPhotoBottomDialogFragment =
                        LogoutDialogFragment.newInstance(0);
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDateSelected(String dateStr, String whichButtonDate) {
        if (whichButtonDate.equalsIgnoreCase("tvDepartDate")) {
            tvDepartDate.setText(dateStr);
            Log.e(TAG, dateStr);
            setIncrementedDate();
        }
        if (whichButtonDate.equalsIgnoreCase("tvReturnDate")) {
            tvReturnDate.setText(dateStr);
            Log.e(TAG, dateStr);
        }


    }

    private void setIncrementedDate() {
        if (tvReturnDate.isShown()) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.dd_MMM_yy);
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(tvDepartDate.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat(Constant.dd_MMM_yy);
            String str = sdf1.format(c.getTime());
            tvReturnDate.setText(str);
        }
    }

    @Override
    public void isLogoutClicked(boolean bool) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                CityModel model = (CityModel) data.getSerializableExtra(Constant.OBJECT);
                String toOrFrom = data.getStringExtra(Constant.OBJECT2);
                if (toOrFrom.equalsIgnoreCase("tvFrom")) {
                    if (model.getName().equalsIgnoreCase(tvTo.getText().toString()))
                        Utils.showMessage(getString(R.string.error_select_city), this);
                    else {
                        mFromModel = model;
                        tvFrom.setText(model.getName());
                    }

                } else if (toOrFrom.equalsIgnoreCase("tvTo")) {
                    if (model.getName().equalsIgnoreCase(tvFrom.getText().toString()))
                        Utils.showMessage(getString(R.string.error_select_city), this);
                    else {
                        mToModel = model;
                        tvTo.setText(model.getName());
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


}
