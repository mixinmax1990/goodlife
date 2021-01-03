package com.news.goodlife.Data.Remote.Klarna.Interfaces;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DELETEKlarnaFlow {

    @DELETE("flows/{flow_id}")
    Call<Response<String>> deleteFlow(@Path("flow_id") String flow_id, @Header("Authorization") String authHeader);

}
