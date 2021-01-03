package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.GETSessionData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GETKlarnaSessionInterface {

    @GET("{session_id}")
    Call<GETSessionData> getSession(@Path("session_id") String session_id, @Header("Authorization") String authHeader);
}
