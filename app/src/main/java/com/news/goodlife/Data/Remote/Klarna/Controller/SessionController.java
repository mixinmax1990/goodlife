package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestDataModel;
import com.news.goodlife.Data.Remote.Klarna.XS2Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SessionController {

    String token;
    Retrofit retrofit;

    private SessionRequestDataModel sessionData;

    public SessionController(String token, Retrofit retrofit) {
        this.token = token;
        this.retrofit = retrofit;
    }

    public void startSession(){
        SessionRequestDataModel responseData = null;

        XS2Api xs2Api = retrofit.create(XS2Api.class);
        Call<SessionRequestDataModel> call = xs2Api.getSession(getSessionRequestBody(), "Bearer "+token);

        //Asynchronous Task Remember

        call.enqueue(new Callback<SessionRequestDataModel>() {
            @Override
            public void onResponse(Call<SessionRequestDataModel> call, Response<SessionRequestDataModel> response) {
                if(!response.isSuccessful()){
                    Log.i("HTTPCall Error", ""+response.code());
                    return;
                }

                SessionRequestDataModel sessionData = response.body();

                setSessionData(sessionData);

                //sessionData.getData().getSelf();

                //loadKlarnaAppWebview(sessionData.getData().getFlows().getTransactions());

                //Log.i("Successfull API Call", "self : "+response.body().getData().getSelf());
            }

            @Override
            public void onFailure(Call<SessionRequestDataModel> call, Throwable t) {

                Log.i("HTTPCall Exception", ""+t.getMessage());
            }
        });

    }

    private SessionRequestBody getSessionRequestBody(){

        SessionRequestBody sessionRequestBody = new SessionRequestBody();
        PsuModel psuModel = new PsuModel();

        psuModel.setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        psuModel.setIp_address("123.123.123.123");

        sessionRequestBody.setLanguage("en");
        sessionRequestBody.setPsu(psuModel);

        return sessionRequestBody;
    }

    public SessionRequestDataModel getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionRequestDataModel sessionData) {
        this.sessionData = sessionData;
    }
}
