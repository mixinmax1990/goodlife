package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.DeleteKlarnaSession;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.GETKlarnaSessionInterface;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaGetSessionResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.GETSessionData;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.RequestBody.SessionRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.PUTStartSessionDataModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.XS2Api;
import com.news.goodlife.Singletons.SingletonClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionController {


    Retrofit retrofit;
    String token;

    private PUTStartSessionDataModel sessionData = null;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public SessionController(String token) {
        this.token = token;
    }

    public void startSession(KlarnaResponseCallback klarnaResponseCallback){

        //Check if a session is open already

        if(sessionData != null){
            //there is no session started
            Log.i("Error Here", "Somethig");
            return;
        }

        retrofit = getClient("https://api.playground.openbanking.klarna.com");

        XS2Api xs2Api = retrofit.create(XS2Api.class);
        Call<PUTStartSessionDataModel> call = xs2Api.getSession(getSessionRequestBody(), "Bearer "+token);

        //Asynchronous Task Remember

        call.enqueue(new Callback<PUTStartSessionDataModel>() {
            @Override
            public void onResponse(Call<PUTStartSessionDataModel> call, Response<PUTStartSessionDataModel> response) {
                if(!response.isSuccessful()){
                    klarnaResponseCallback.error();

                    return;
                }

                PUTStartSessionDataModel sessionData = response.body();

                setSessionData(sessionData);

                Log.i("StartedSessionID", ""+sessionData.getData().getSession_id());
                klarnaResponseCallback.success();

                //getSession();
                //startFlow(flowName);
                //sessionData.getData().getSelf();
                //loadKlarnaAppWebview(sessionData.getData().getFlows().getTransactions());
                //Log.i("Successfull API Call", "self : "+response.body().getData().getSelf());
            }

            @Override
            public void onFailure(Call<PUTStartSessionDataModel> call, Throwable t) {

                klarnaResponseCallback.error();
            }
        });

    }

    public void getSession(KlarnaGetSessionResponseCallback callback){

        retrofit = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/");

        GETKlarnaSessionInterface getKlarnaSessionInterface = retrofit.create(GETKlarnaSessionInterface.class);
        Call<GETSessionData> call = getKlarnaSessionInterface.getSession(getSessionData().getData().getSession_id(), "Bearer "+token);

        call.enqueue(new Callback<GETSessionData>() {
            @Override
            public void onResponse(Call<GETSessionData> call, Response<GETSessionData> response) {
                if(!response.isSuccessful()){
                    Log.i("HTTPCall Error", ""+response.code());
                    return;
                }

                GETSessionData data = response.body();

                callback.success(data);

                Log.i("GetSession", ""+data.getData().getState());

            }

            @Override
            public void onFailure(Call<GETSessionData> call, Throwable t) {
                Log.i("HTTPCall Exception", ""+t.getMessage());
                callback.error();
            }
        });
    }

    public void closeSession(KlarnaResponseCallback callback){
        if(sessionData != null){
            retrofit = getClient("https://api.playground.openbanking.klarna.com/xs2a/v1/sessions/");
            DeleteKlarnaSession deleteKlarnaSession = retrofit.create(DeleteKlarnaSession.class);
            Call<Response<String>> call = deleteKlarnaSession.deleteSession(getSessionData().getData().getSession_id(), "Bearer "+token);

            call.enqueue(new Callback<Response<String>>() {
                @Override
                public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                    if(!response.isSuccessful()){
                        callback.error();
                        return;
                    }

                    sessionData = null;
                    callback.success();
                }

                @Override
                public void onFailure(Call<Response<String>> call, Throwable t) {
                    callback.error();
                }
            });
        }
    }

    private SessionRequestBody getSessionRequestBody(){

        SessionRequestBody sessionRequestBody = new SessionRequestBody();
        PsuModel psuModel = new PsuModel();

        //TODO Set the correct User Body
        psuModel.setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        psuModel.setIp_address("123.123.123.123");

        sessionRequestBody.setLanguage("en");
        sessionRequestBody.setPsu(psuModel);

        return sessionRequestBody;
    }

    public PUTStartSessionDataModel getSessionData() {
        return sessionData;
    }

    public void setSessionData(PUTStartSessionDataModel sessionData) {
        this.sessionData = sessionData;
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
