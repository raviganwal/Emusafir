package com.emusafir.service;


import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Rohit on 7/19/2016.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("customer_registraion")
    Call<ResponseBody> registraion(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("otp_verification")
    Call<ResponseBody> otp_verification(@FieldMap HashMap<String, String> hm);

    @GET("city_list")
    Call<ResponseBody> city_list();

    @FormUrlEncoded
    @POST("search_bus_list")
    Call<ResponseBody> search_bus_list(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("bus_layout")
    Call<ResponseBody> bus_layout(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("booking_list")
    Call<ResponseBody> booking_list(@FieldMap HashMap<String, String> hm);

    @FormUrlEncoded
    @POST("booked_ticket_details")
    Call<ResponseBody> booked_ticket_details(@FieldMap HashMap<String, String> hm);

    @POST("bookingseats")
    Call<ResponseBody> bookingseats(@Body RequestBody param);

    @FormUrlEncoded
    @POST("update_payment_status")
    Call<ResponseBody> update_payment_status(@FieldMap HashMap<String, String> hm);


}
