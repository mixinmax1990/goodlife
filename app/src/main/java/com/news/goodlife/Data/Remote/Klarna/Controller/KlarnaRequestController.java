package com.news.goodlife.Data.Remote.Klarna.Controller;

import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestDataModel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KlarnaRequestController {

    //Anyone wanting to make request needs to listen to session ID if its null

    String sessionID = null;

    SessionRequestDataModel sessionData;

    String token = "MTYxMzY4Njc0OXx8fDkwODdmM2MwLWU2YzItNDRmNS1hZTEwLTdkNjYwODU1Y2E4MHx8fDIwNTR8fHxyaXZlcmJhbmsuKg==.acnm+WH42OrltnKstvQyk0e7nml+Je6bAA0YnywwAvo= ";
    Retrofit retrofit;


    SessionController sessionController;
    FlowsController flowsController;

    public KlarnaRequestController() {

        getClient();

        sessionController = new SessionController(token, retrofit);

    }

    private Retrofit getClient(){
        Retrofit retrofit;


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.playground.openbanking.klarna.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }




}
