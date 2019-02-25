package com.emusafir.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.emusafir.model.BusUserSelectedData;
import com.emusafir.model.SearchModel;
import com.emusafir.model.UserModel;


public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EMusafirPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_ONE_WAY_BOOKED = "IsOneWayBooked";
    private static final String ONE_WAY_OR_ROUND_TRIP_ON_PROGRESS = "ONE_WAY_OR_ROUND_TRIP";
    private static final String IS_ROUND_TRIP_BOOKED = "IsRoundTripBooked";

    //   Admin User Details
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String MOBILE = "Mobile";

    private static final String FCM_REGISTRATION_ID = "fcm_reg_id";

    private static final String ONE_WAY_BUS_ID = "ONE_WAY_BUS_ID";
    private static final String ONE_WAY_ROUTE_ID = "ONE_WAY_ROUTE_ID";
    private static final String ONE_WAY_BOOK_DATE = "ONE_WAY_BOOK_DATE";
    private static final String ONE_WAY_FROM = "ONE_WAY_FROM";
    private static final String ONE_WAY_TO = "ONE_WAY_TO";

    private static final String ROUND_TRIP_BUS_ID = "ROUND_TRIP_BUS_ID";
    private static final String ROUND_TRIP_ROUTE_ID = "ROUND_TRIP_ROUTE_ID";
    private static final String ROUND_TRIP_BOOK_DATE = "ROUND_TRIP_BOOK_DATE";
    private static final String ROUND_TRIP_FROM = "ROUND_TRIP_FROM";
    private static final String ROUND_TRIP_TO = "ROUND_TRIP_TO";

    private static final String ONE_WAY_BOOKING_ARRAY = "ONE_WAY_BOOKING_ARRAY";
    private static final String ROUND_TRIP_BOOKING_ARRAY = "ROUND_TRIP_BOOKING_ARRAY";

    private static final String ONE_WAY_BOARDING_POINT = "ONE_WAY_BOARDING_POINT";
    private static final String ONE_WAY_DROPPING_POINT = "ONE_WAY_DROPPING_POINT";
    private static final String ROUND_TRIP_BOARDING_POINT = "ROUND_TRIP_BOARDING_POINT";
    private static final String ROUND_TRIP_DROPPING_POINT = "ROUND_TRIP_DROPPING_POINT";


    private static final String SEARCH_TRIP_TYPE = "trip_type";
    private static final String SEARCH_ONWARD_DATE = "onwardDate";
    private static final String SEARCH_RETURN_DATE = "returnDate";
    private static final String SEARCH_TO_CITY_ID = "toCityid";
    private static final String SEARCH_TO_CITY_NAME = "toCityName";
    private static final String SEARCH_FROM_CITY_ID = "fromCityid";
    private static final String SEARCH_FROM_CITY_NAME = "fromCityName";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setSearch(SearchModel model) {
        editor.putString(SEARCH_TRIP_TYPE, model.getTripType());
        editor.putString(SEARCH_ONWARD_DATE, model.getOnwardDate());
        editor.putString(SEARCH_RETURN_DATE, model.getReturnDate());
        editor.putString(SEARCH_TO_CITY_ID, model.getToCity().getId());
        editor.putString(SEARCH_TO_CITY_NAME, model.getToCity().getName());
        editor.putString(SEARCH_FROM_CITY_ID, model.getFromCity().getId());
        editor.putString(SEARCH_FROM_CITY_NAME, model.getFromCity().getName());
        editor.commit();
    }

    public SearchModel getSearch() {
        if (pref.getString(SEARCH_TRIP_TYPE, null) != null) {
            String trip_type, onward_date, return_date, to_city_id, to_city_name, to_from_id, to_from_name;
            trip_type = pref.getString(SEARCH_TRIP_TYPE, null);
            onward_date = pref.getString(SEARCH_ONWARD_DATE, null);
            return_date = pref.getString(SEARCH_RETURN_DATE, null);
            to_city_id = pref.getString(SEARCH_TO_CITY_ID, null);
            to_city_name = pref.getString(SEARCH_TO_CITY_NAME, null);
            to_from_id = pref.getString(SEARCH_FROM_CITY_ID, null);
            to_from_name = pref.getString(SEARCH_FROM_CITY_NAME, null);
            SearchModel user = new SearchModel(trip_type, onward_date, return_date, to_city_id, to_city_name, to_from_id, to_from_name);
            return user;
        }
        return null;
    }

    public void setOneWayBus(BusUserSelectedData mUser) {
        editor.putString(ONE_WAY_BUS_ID, mUser.getBusId());
        editor.putString(ONE_WAY_ROUTE_ID, mUser.getRouteId());
        editor.putString(ONE_WAY_BOOK_DATE, mUser.getBookDate());
        editor.putString(ONE_WAY_FROM, mUser.getFrom());
        editor.putString(ONE_WAY_TO, mUser.getTo());
        editor.commit();
    }

    public BusUserSelectedData getOneWayBus() {
        if (pref.getString(ONE_WAY_BUS_ID, null) != null) {
            String bus_id, route_id, book_date, from, to;
            bus_id = pref.getString(ONE_WAY_BUS_ID, null);
            route_id = pref.getString(ONE_WAY_ROUTE_ID, null);
            book_date = pref.getString(ONE_WAY_BOOK_DATE, null);
            from = pref.getString(ONE_WAY_FROM, null);
            to = pref.getString(ONE_WAY_TO, null);
            BusUserSelectedData user = new BusUserSelectedData(bus_id, route_id, book_date, from, to);
            return user;
        }
        return null;
    }

    public void setRoundTripBus(BusUserSelectedData mUser) {
        editor.putString(ROUND_TRIP_BUS_ID, mUser.getBusId());
        editor.putString(ROUND_TRIP_ROUTE_ID, mUser.getRouteId());
        editor.putString(ROUND_TRIP_BOOK_DATE, mUser.getBookDate());
        editor.putString(ROUND_TRIP_FROM, mUser.getFrom());
        editor.putString(ROUND_TRIP_TO, mUser.getTo());
        editor.commit();
    }

    public BusUserSelectedData getRoundTripBus() {
        if (pref.getString(ROUND_TRIP_BUS_ID, null) != null) {
            String bus_id, route_id, book_date, from, to;
            bus_id = pref.getString(ROUND_TRIP_BUS_ID, null);
            route_id = pref.getString(ROUND_TRIP_ROUTE_ID, null);
            book_date = pref.getString(ROUND_TRIP_BOOK_DATE, null);
            from = pref.getString(ROUND_TRIP_FROM, null);
            to = pref.getString(ROUND_TRIP_TO, null);
            BusUserSelectedData user = new BusUserSelectedData(bus_id, route_id, book_date, from, to);
            return user;
        }
        return null;
    }

    public void setUser(UserModel mUser) {
        editor.putString(ID, mUser.getId());
        editor.putString(NAME, mUser.getName());
        editor.putString(EMAIL, mUser.getEmail());
        editor.putString(MOBILE, mUser.getMobile());
        editor.commit();
    }

    public UserModel getUser() {
        if (pref.getString(ID, null) != null) {
            String id, name, email, mobile;
            id = pref.getString(ID, null);
            name = pref.getString(NAME, null);
            email = pref.getString(EMAIL, null);
            mobile = pref.getString(MOBILE, null);
            UserModel user = new UserModel(id, name, email, mobile);
            return user;
        }
        return null;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }


    //fcm
    public void setFcmRegistrationId(String id) {
        editor.putString(FCM_REGISTRATION_ID, id);
        editor.commit();
    }

    public String getFcmRegistrationId() {
        return pref.getString(FCM_REGISTRATION_ID, null);
    }


    public void setLoginSession(boolean bool) {
        editor.putBoolean(IS_LOGIN, bool);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setOneWayBooked(boolean bool) {
        editor.putBoolean(IS_ONE_WAY_BOOKED, bool);
        editor.commit();
    }

    public boolean isOneWayBooked() {
        return pref.getBoolean(IS_ONE_WAY_BOOKED, false);
    }

    public void setRoundTripBooked(boolean bool) {
        editor.putBoolean(IS_ROUND_TRIP_BOOKED, bool);
        editor.commit();
    }

    public boolean isRoundTripBooked() {
        return pref.getBoolean(IS_ROUND_TRIP_BOOKED, false);
    }

    public void setOneWayOrRoundTripOnProgress(String bool) {
        editor.putString(ONE_WAY_OR_ROUND_TRIP_ON_PROGRESS, bool);
        editor.commit();
    }

    public String getOneWayOrRoundTripOnProgress() {
        return pref.getString(ONE_WAY_OR_ROUND_TRIP_ON_PROGRESS, null);
    }

    public void setOneWayBoardingPoint(String bool) {
        editor.putString(ONE_WAY_BOARDING_POINT, bool);
        editor.commit();
    }

    public String getOneWayBoardingPoint() {
        return pref.getString(ONE_WAY_BOARDING_POINT, null);
    }


    public void setOneWayDroppingPoint(String bool) {
        editor.putString(ONE_WAY_DROPPING_POINT, bool);
        editor.commit();
    }

    public String getOneWayDroppingPoint() {
        return pref.getString(ONE_WAY_DROPPING_POINT, null);
    }


    public void setRoundTripBoardingPoint(String bool) {
        editor.putString(ROUND_TRIP_BOARDING_POINT, bool);
        editor.commit();
    }

    public String getRoundTripBoardingPoint() {
        return pref.getString(ROUND_TRIP_BOARDING_POINT, null);
    }

    public void setRoundTripDroppingPoint(String bool) {
        editor.putString(ROUND_TRIP_DROPPING_POINT, bool);
        editor.commit();
    }

    public String getRoundTripDroppingPoint() {
        return pref.getString(ROUND_TRIP_DROPPING_POINT, null);
    }

    public void setOneWayBookingArray(String bool) {
        editor.putString(ONE_WAY_BOOKING_ARRAY, bool);
        editor.commit();
    }

    public String getOneWayBookingArray() {
        return pref.getString(ONE_WAY_BOOKING_ARRAY, null);
    }


    public void setRoundTripBookingArray(String bool) {
        editor.putString(ROUND_TRIP_BOOKING_ARRAY, bool);
        editor.commit();
    }

    public String getRoundTripBookingArray() {
        return pref.getString(ROUND_TRIP_BOOKING_ARRAY, null);
    }


}