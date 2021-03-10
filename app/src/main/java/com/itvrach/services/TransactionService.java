package com.itvrach.services;

import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Transaction;
import com.itvrach.model.Transaction_hdr;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TransactionService {


    /**
     * Created by engineer on 8/19/2018.
     */

    @GET("transaction/findAll")
    Call<ResponseTransaction> getFindAll(@Query("session_id") String session_id);

    @GET("transaction/findAllDoneTransaction")
    Call<ResponseTransactionHdr> getFindAllDoneTransaction();



    /*@POST("transaction/findTransactionByDate")
    Call<ResponseTransactionHdr> getFindTransactionByDate(@Query("transaction_date") String params1,
                                                          @Query("transaction_date") String params2);
*/



    @POST("transaction/findTransactionByDate")
    Call<ResponseTransactionHdr> getFindTransactionByDate(@Body Transaction_hdr transaction_hdr);




 //   @GET("transactiondetail/findAll")
   // Call<ResponseTransaction> getFindAll();


    @POST("transaction/create")
    Call<ResponseTransaction> Create(@Body Transaction transaction);



    /*@POST("transaction/createDtl")
    Call<ResponseTransaction> CreateDtl(@Body Transaction transaction);
*/

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "transaction/delete", hasBody = true)
    Call<ResponseTransaction> delete(@Field("item_code") String item_code, @Field("session_id") String session_id );


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "transaction/deleteuser_id", hasBody = true)
    Call<ResponseTransaction> deleteuser_id(@Field("session_id") String session_id);


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "transaction/deletetemp", hasBody = true)
    Call<ResponseTransaction> delete_temp(@Field("session_id") String session_id);



    @PUT("transaction/updateTransaction/{session_id}")
    Call<ResponseTransactionHdr> update(@Path("session_id") String session_id, @Body Transaction_hdr transaction_hdr);

    @PUT("transaction/confirmation/{session_id}")
    Call<ResponseTransactionHdr> Confirmation(@Path("session_id") String session_id, @Body Transaction_hdr transaction_hdr);



    @PUT("transaction/approved/{session_id}")
    Call<ResponseTransactionHdr> Approved(@Path("session_id") String session_id, @Body Transaction_hdr transaction_hdr);




    @GET("transaction/findInboxTransaction")
    Call<ResponseTransactionHdr> getFindInboxTransaction(@Query("user_id") String user_id);

    @GET("transaction/findApproveTransaction")
    Call<ResponseTransactionHdr> getFindApproveTransaction(@Query("user_id") String user_id);

    @GET("transaction/findShippingList")
    Call<ResponseTransactionHdr> getFindShippingList(@Query("user_id") String user_id);



    @GET("transaction/findTransaction")
    Call<ResponseTransactionHdr> getFindTransaction(@Query("user_id") String user_id);

    @POST("transaction/changeApproved")
    Call<ResponseTransactionHdr> ChangeApproved(@Body Transaction_hdr transaction_Hdr);



}


