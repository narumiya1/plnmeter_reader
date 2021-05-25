package com.example.myapplicationpln.network_retrofit;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface ApiClient {

    @Multipart
    @POST("/api/Image/DocMeter/")
    Call<PLNData> uploadImage(@Part MultipartBody.Part part, @Part("USER_ID") RequestBody requestBody1, @Part("LATITUDE") RequestBody requestBody2, @Part("LONGITUDE") RequestBody requestBody3);

    @FormUrlEncoded
    @POST("insertdata.php")
    Call<ResponseBody>insertdata(
            @Field("name") String name,
            @Field("email")int jumlah
    );

    @FormUrlEncoded
    @POST("api/Account/Login")
    Call<UserToken> login(
            @Body Login login
    );

    @FormUrlEncoded
    @POST("api/Account/Login/")
    Call<ResponseBody> insertLogin(
//            @Header("Authorization") String authToken,
            @Field("Phone") String phone,
            @Field("Password") String password
    );


    @FormUrlEncoded
    @POST("api/Account/Login")
    Call<ResponseBody> getStoryOfMe(@QueryMap HashMap<String, String> params);

}
