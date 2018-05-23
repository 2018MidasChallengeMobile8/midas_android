package com.xema.midas.network.service;

import android.support.annotation.Keep;

import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Profile;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

@Keep
public interface AccountService {
    @GET("/add_user/")
    Call<ApiResult> signUp(@Query("id") String id, @Query("pw") String password);

    @GET("/login/")
    Call<Profile> signIn(@Query("id") String id, @Query("pw") String password);

    @Multipart
    @POST("/change_profile_image/")
    Call<ApiResult> uploadProfileImage(@Part MultipartBody.Part id, @Part MultipartBody.Part pw, @Part MultipartBody.Part profileImage);
}
