package com.itvrach.services;

import com.itvrach.model.ResponseShipping;
import com.itvrach.model.Shipping;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;



public interface ShippingService {
    @GET("shipping/findAll")
    Call<ResponseShipping> getFindAll(@Query("session_id") String session_id);


    //   @GET("transactiondetail/findAll")
    // Call<ResponseTransaction> getFindAll();


    @POST("shipping/create")
    Call<ResponseShipping> Create(@Body Shipping shipping);

    @Multipart
    @POST("shipping/upload")
    Call<ResponseShipping> Upload(@Part MultipartBody.Part file, @Part("user_id") RequestBody user_id
    );

}
