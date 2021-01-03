package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DeleteKlarnaSession {

    @DELETE("{session_id}")
    Call<Response<String>> deleteSession(@Path("session_id") String session_id, @Header("Authorization") String authHeader);
}
