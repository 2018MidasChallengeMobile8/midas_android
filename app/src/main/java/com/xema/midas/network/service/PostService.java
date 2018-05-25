package com.xema.midas.network.service;

/**
 * Created by xema0 on 2018-05-25.
 */

import android.support.annotation.Keep;

import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Post;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

@Keep
public interface PostService {
    //cnt_post : 가져올 게시글의 갯수 - int (default : 10)
    //last_post_id : 마지막 게시글의 id - int (default : 가장 최근 등록된 글부터)
    //게시물 가져오기
    @GET("/get_posts/")
    Call<List<Post>> getPostList(@Query("cnt_post") int size, @Query("last_post_id") int lastPostIdx);

    //게시글 업로드
    @Multipart
    @POST("/upload_post/")
    Call<ApiResult> uploadPost(@Part MultipartBody.Part id, @Part MultipartBody.Part pw, @Part MultipartBody.Part title, @Part MultipartBody.Part text, @Part MultipartBody.Part image);

    //게시물 삭제
    @FormUrlEncoded
    @POST("/delete_post/")
    Call<ApiResult> deletePost(@Field("id") String id, @Field("pw") String pw, @Field("post_id") int postId);
}
