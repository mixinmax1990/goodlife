package com.news.goodlife.Data.Remote.Klarna.Controller;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetAccountInformation {

    Retrofit retrofit;
    String token;

    public GetAccountInformation(String token) {
        this.token = token;
    }


    private Retrofit getClient(String baseURL){
        Retrofit retrofit;


        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
