package com.itvrach.services;

import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    @POST("user/login")
    Call<ResponseModel> Login(@Body User user);


    @POST("user/logout")
    Call<ResponseModel> Logout(@Body User user);


    @POST("user/create")
    Call<ResponseModel> Create(@Body User user);

    @POST("user/forgotpassword")
    Call<ResponseModel> forgotpassword(@Body User user);

/*    @PUT("user/changepassword")
    Call<ResponseModel> changepassword(@Body User user);*/

    @PUT("user/changepassword/{reset_password_key}")
    Call<ResponseModel> changepassword(@Path("reset_password_key") String reset_password_key,  @Body User user);

    @PUT("user/update/{user_id}")
    Call<ResponseModel> update(@Path("user_id") int user_id, @Body User user);

    @POST("register/create")
    Call<ResponseModel> Register(@Body User user);

    @Multipart
    @POST("user/upload")
    Call<ResponseModel> Upload(
            @Part MultipartBody.Part file, @Part("user_id") RequestBody user_id
    );

   /* @DELETE("delete")
    Call<ResponseModel> Delete(@Body User User);*/

    @GET("user/findAll")
    Call<ResponseModel> getFindAll();

   // @GET("user/findAll")
  //  Call<User> getUser(@Header("Cookie") String cookie);
    //@HTTP(method = "DELETE", path = "groups/{groupId}/members/remove", hasBody = true)
    //Call<GroupRemoveUserModel> putRemoveUser(@Path("groupId") int groupId, @Body GroupRemoveUserModel groupRemoveUserModel);

   /* @DELETE("delete/{user_id}")
    Call<ResponseModel> delete(@Path("user_id") int user_id);*/

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "user/delete", hasBody = true)
    Call<ResponseModel> delete(@Field("user_id") int user_id);


   /* @GET("user/")
    Call<List<User>> getUsers();*/

  /*  @POST("add/")
    Call<User> addUser(@Body User user);*/
/*
    @PUT("update/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);*/




   /* @PUT("/posts/{id}")
    @FormUrlEncoded
    Call<Post> updatePost(@Path("id") long id,
                          @Field("title") String title,
                          @Field("body") String body,
                          @Field("userId") long userId);*/



   /* @Multipart
    @POST("create")
    Call<ResponseModel> Create(
            @Part("username") ResponseBody username,
            @Part("password") ResponseBody password,
            @Part("type") ResponseBody type,
            @Part("firstname") ResponseBody firstname,
            @Part("lastname") ResponseBody lastname,
            @Part("fullname") ResponseBody fullname,
            @Part("gender") ResponseBody gender,
            @Part("marital_status") ResponseBody marital_status,
            @Part("email") ResponseBody email,
            @Part MultipartBody.Part file
    );*/










   /*@FormUrlEncoded
    @POST("login")
    Call<ResponseModel> Login(@Field("username") String username,
                             @Field("password") String password
    );
    */



   /*@POST("create")
   Call<ResponseModel> create(@Field("username") String username,
                               @Field("password") String password,
                               @Field("type") String type,
                               @Field("firstname") String firstname,
                               @Field("lastname") String lastname,
                               @Field("fullname") String fullname,
                               @Field("gender") String gender,
                               @Field("email") String email,
                               @Field("create_date") String create_date,
                               @Field("updated_date") String updated_date,
                               @Field("last_login") String last_login

   );
   */

    /*@PUT("update")Signup
    Call<Boolean> update(@Body User user);
*/


}
