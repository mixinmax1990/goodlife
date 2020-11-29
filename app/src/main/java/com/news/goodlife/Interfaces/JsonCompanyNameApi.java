package com.news.goodlife.Interfaces;

import com.news.goodlife.Models.CompanyWebsite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface JsonCompanyNameApi {

    @GET()
    Call<CompanyWebsite> getCompanyNames(@Header("Authorization") String auth, @Url String url);

}
