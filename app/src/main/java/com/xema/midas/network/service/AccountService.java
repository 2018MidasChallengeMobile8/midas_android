package com.xema.midas.network.service;

import android.support.annotation.Keep;

import com.xema.midas.model.Profile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Keep
public interface AccountService {
    @GET("/add_user/")
    Call<String> signUp(@Query("id") String id, @Query("pw") String password);

    @GET("/login/")
    Call<Profile> signIn(@Query("id") String id, @Query("pw") String password);
}
