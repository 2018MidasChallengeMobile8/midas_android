package com.xema.midas.network;


import com.xema.midas.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {
    //테스트서버
    private static final String BASE_URL = "http://192.168.0.79:8181/";

    private static Retrofit retrofit = null;

    static Retrofit getClient() {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
            builder.client(OkHttpGenerator.getInstance(BuildConfig.DEBUG));
            retrofit = builder.build();
        }
        return retrofit;
    }

}
