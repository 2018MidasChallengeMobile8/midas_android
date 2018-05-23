package com.xema.midas.network;


import com.xema.midas.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class RetrofitClient {
    //로컬서버
    //private static final String BASE_URL = "http://192.168.0.79:8181/";
    //외부서버
    private static final String BASE_URL = "http://sgc109.pythonanywhere.com/";
    private static Retrofit retrofit = null;

    static Retrofit getClient() {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

                    //to get string response
                    //.addConverterFactory(ScalarsConverterFactory.create());
            builder.client(OkHttpGenerator.getInstance(BuildConfig.DEBUG));
            retrofit = builder.build();
        }
        return retrofit;
    }

}
