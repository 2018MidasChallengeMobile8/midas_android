package com.xema.midas.network.service;

import android.support.annotation.Keep;

import com.xema.midas.model.Posts;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@Keep
public interface AccountService {
    @GET("/add_user/")
    Call<String> signUp(@Query("id") String id, @Query("pw") String passWord);
    @POST("/get_posts/")
    Call<ArrayList<Posts>> getPosts(@Query("cnt_post") int cnt_post, @Query("last_post_id") int last_post_id);
}
