package com.news.goodlife.Data.Remote.Klarna.Controller;

import android.util.Log;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.PUTStartSessionDataModel;

import retrofit2.Retrofit;

public class KlarnaRequestController {
    //Anyone wanting to make request needs to listen to session ID if its null
    String token = "MTYxMzY4Njc0OXx8fDkwODdmM2MwLWU2YzItNDRmNS1hZTEwLTdkNjYwODU1Y2E4MHx8fDIwNTR8fHxyaXZlcmJhbmsuKg==.acnm+WH42OrltnKstvQyk0e7nml+Je6bAA0YnywwAvo=";
    String sessionID = null;
    PUTStartSessionDataModel sessionData;
    Retrofit retrofit;
    SessionController sessionController;
    FlowsController flowsController;
    KlarnaConsentController consentController;

    public KlarnaRequestController() {

        sessionController = new SessionController(token);
        flowsController = new FlowsController(token);
        consentController = new KlarnaConsentController(token);

    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public FlowsController getFlowsController() {
        return flowsController;
    }

    public KlarnaConsentController getConsentController() {
        return consentController;
    }

    public void clearSession(){
        String activeFlow = getFlowsController().activeFlow;

            if(activeFlow != null){
                flowsController.stopFlow(activeFlow, new KlarnaResponseCallback() {
                    @Override
                    public void success() {
                        Log.i("Flow Close", "Success");
                        sessionController.closeSession(new KlarnaResponseCallback() {
                            @Override
                            public void success() {
                                Log.i("Session Close", "Success");
                            }
                            @Override
                            public void error() {
                                Log.i("Session Close", "Failed");
                            }
                        });
                    }

                    @Override
                    public void error() {
                        Log.i("Session Close", "Fail");
                    }

                });
            }

            else{
                sessionController.closeSession(new KlarnaResponseCallback() {
                    @Override
                    public void success() {
                        Log.i("Session Close", "Success");
                    }

                    @Override
                    public void error() {
                        Log.i("Session Close", "Failed");
                    }
                });
            }

    }
}
