package com.news.goodlife.Fragments.SlideInFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.Data.Remote.Klarna.Models.PsuModel;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestBody;
import com.news.goodlife.Data.Remote.Klarna.Models.SessionRequestDataModel;
import com.news.goodlife.Data.Remote.Klarna.Models.XS2ASessionModel;
import com.news.goodlife.Data.Remote.Klarna.XS2Api;
import com.news.goodlife.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KlarnaAccountOne extends Fragment {
    View root;

    WebView webview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.klarnawebview, container, false);

        webview = root.findViewById(R.id.webview);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        startKlarnaSession();

        return root;
    }

    private void startKlarnaSession() {

        String token = "MTYxMzY4Njc0OXx8fDkwODdmM2MwLWU2YzItNDRmNS1hZTEwLTdkNjYwODU1Y2E4MHx8fDIwNTR8fHxyaXZlcmJhbmsuKg==.acnm+WH42OrltnKstvQyk0e7nml+Je6bAA0YnywwAvo= ";
        Retrofit retrofit = getClient();

        XS2Api xs2Api = retrofit.create(XS2Api.class);

        Call<SessionRequestDataModel> call = xs2Api.getSession(getSessionRequestBody(), "Bearer "+token);

        call.enqueue(new Callback<SessionRequestDataModel>() {
            @Override
            public void onResponse(Call<SessionRequestDataModel> call, Response<SessionRequestDataModel> response) {
                if(!response.isSuccessful()){
                    Log.i("HTTPCall Error", ""+response.code());
                    return;
                }

                SessionRequestDataModel sessionData = response.body();

                //sessionData.getData().getSelf();

                loadKlarnaAppWebview(sessionData.getData().getFlows().getTransactions());

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

    private Retrofit getClient(){
        Retrofit retrofit;


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.playground.openbanking.klarna.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private void loadKlarnaAppWebview(String client_id){

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://18.159.89.0/moneyappweb/klarnaconnect.html");
        webview.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webview.loadUrl("javascript:startAuthenticationApp('" + client_id + "')");
            }
        });


    }
}
