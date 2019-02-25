package com.emusafir;

import android.os.Bundle;

import com.emusafir.fragments.OneWayFragment;
import com.emusafir.fragments.RoundTripFragment;
import com.emusafir.model.SearchModel;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.Utils;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BusSearchTabActivity extends AppCompatActivity {

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memgory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    private SearchModel mSearchModel;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search_tab);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSearchModel = App.getInstance().getPrefManager().getSearch();
//        mSearchModel = (SearchModel) getIntent().getSerializableExtra(Constant.OBJECT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        getSupportActionBar().setTitle(mSearchModel.getFromCityName() + " to " + mSearchModel.getToCityName());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    getSupportActionBar().setTitle(mSearchModel.getFromCityName() + " to " + mSearchModel.getToCityName());
                else
                    getSupportActionBar().setTitle(mSearchModel.getToCityName() + " to " + mSearchModel.getFromCityName());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bus_search_tab, menu);
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
            if (mSearchModel.getTripType().equalsIgnoreCase(Constant.ONE_WAY))
                return OneWayFragment.newInstance(position + 1);
            else {
                if (position == 0)
                    return OneWayFragment.newInstance(position + 1);
                else
                    return RoundTripFragment.newInstance(position + 1);

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if (mSearchModel.getTripType().equalsIgnoreCase(Constant.ONE_WAY))
                return 1;
            else return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "One Way";
                case 1:
                    return "Round Trip";
            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress() != null && App.getInstance().getPrefManager().getOneWayOrRoundTripOnProgress().equalsIgnoreCase(Constant.ROUND_TRIP) && tabLayout.getTabAt(1).isSelected()) {
            App.getInstance().getPrefManager().setOneWayOrRoundTripOnProgress(Constant.ONE_WAY);
        }

    }
}
