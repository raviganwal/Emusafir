package com.emusafir.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    //https://www.compribene.it/maincategory.php
//    public static final String BASE_URL = "http://wpdemo.gq/bus_booking/bus_booking/";
    public static final String BASE_URL = "http://bus.eriplinfo.com/";

    //    public static final String BASE_URL = "http://192.168.10.242/luxurycars/";
    public static final String API_URL = BASE_URL + "api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient()
    {
        if (retrofit == null) {
//            CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
//            CookieJar cookieJar = new JavaNetCookieJar(cookieManager);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
//                    .cookieJar(cookieJar)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}