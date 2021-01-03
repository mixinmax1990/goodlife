package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.PUTKlarnaConsent;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KlarnaConsentController {

    POSTgetConsentDataModel consentDataModel;
    Retrofit retrofit;
    private String token;

    public KlarnaConsentController(String token) {
        this.token = token;
    }

    public void getConsent(String session_id, KlarnaResponseCallback callback){


        String baseUrl = "https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/"+session_id+"/";

        retrofit = getClient(baseUrl);

        PUTKlarnaConsent putKlarnaConsent = retrofit.create(PUTKlarnaConsent.class);
        Call<POSTgetConsentDataModel> call = putKlarnaConsent.getConsent("Bearer "+token);

        call.enqueue(new Callback<POSTgetConsentDataModel>() {
            @Override
            public void onResponse(Call<POSTgetConsentDataModel> call, Response<POSTgetConsentDataModel> response) {
                if(!response.isSuccessful()){
                    callback.error();

                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();


                    return;
                }

                consentDataModel = response.body();
                callback.success();

            }

            @Override
            public void onFailure(Call<POSTgetConsentDataModel> call, Throwable t) {
                callback.error();


            }
        });
    }

    private Retrofit getClient(String baseURL){

        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public POSTgetConsentDataModel getConsentDataModel() {
        return consentDataModel;
    }
}
