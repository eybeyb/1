package com.ApiConnect;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String API_KEY = "FZSmISbxNoGvvw9vqETHuSOM";
    public static final String SECRET_KEY = "BtlDBMpX4hkM2UXEqJDAZXM3MI2bebDd";
    private static RetrofitClient instance;
    private Retrofit retrofit;
    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
            .baseUrl("https://aip.baidubce.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
    public static RetrofitClient getInstance(){
        if (instance == null)
        {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public ApiService getApiService(){
        return retrofit.create(ApiService.class);
    }

}
