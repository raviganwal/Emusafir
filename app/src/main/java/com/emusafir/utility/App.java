package com.emusafir.utility;

import android.app.Application;
import android.content.ComponentCallbacks2;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.razorpay.Checkout;

//import android.content.Context;
//import android.support.multidex.MultiDex;
//import android.text.TextUtils;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//import com.onesignal.OneSignal;


/**
 * Created by ravi on 12/21/2016.
 */

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static App mInstance;
    private SessionManager pref;
    //    private RequestQueue mRequestQueue;


    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Checkout.preload(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onTrimMemory(final int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) { // Works for Activity
            // Get called every-time when application went to background.
        } else if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) { // Works for FragmentActivty
        }
    }

    public SessionManager getPrefManager() {
        if (pref == null) {
            pref = new SessionManager(this);
        }
        return pref;

    }


    //    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//
//        return mRequestQueue;
//    }
//
//
//    public <T> void addToRequestQueue(Request<T> request, String tag) {
//        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(request);
//    }
//
//    public <T> void addToRequestQueue(Request<T> request) {
//        request.setTag(TAG);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//        getRequestQueue().add(request);
//    }
//
//    public void cancelPendingRequest(Object tag) {
//        getRequestQueue().cancelAll(tag);
//    }
//
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
//        MultiDex.install(this);
//    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}