package com.news.goodlife.Fragments.SlideInFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class KlarnaApp extends Fragment {
    View root;

    WebView webview;
    ProgressBar spinner;
    SingletonClass singletonClass = SingletonClass.getInstance();

    String client_token;

    public KlarnaApp() {
    }

    public void setClient_token(String client_token) {
        //new Session Started
        this.client_token = client_token;

        loadKlarnaAppWebview();
    }

    POSTgetConsentDataModel consent;

    public void showConsent(POSTgetConsentDataModel consent){

        this.consent = consent;
        Log.i("Consent Exists", "ID:"+ consent.getData().getConsent_id());
        Log.i("Consent Exists", "Token:"+ consent.getData().getConsent_token());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.klarnawebview, container, false);
        spinner = root.findViewById(R.id.spinner);


        webview = root.findViewById(R.id.webview);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        return root;
    }

    private void loadKlarnaAppWebview(){

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.loadUrl("http://18.159.89.0/moneyappweb/klarnaconnect.html");
        webview.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){

                singletonClass.getUniversalBackarrow().setVisibility(View.GONE);

                webview.loadUrl("javascript:startAuthenticationApp('" + client_token + "')");

            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                // TODO Make sure that this is observed if errors occur on some smartphones

                klarnaAppLifecycle(consoleMessage.message());

                return super.onConsoleMessage(consoleMessage);
            }
        });


    }

    boolean success_finish = false;

    private void klarnaAppLifecycle(String consolelog){

        switch(consolelog){
            case "KlarnaOnLoad":

                break;
            case "KlarnaOnReady":
                spinner.setVisibility(View.GONE);
                webview.animate().alpha(1).setDuration(350);
                break;
            case "KlarnaOnAbort":

                singletonClass.getKlarna().clearSession();
                success_finish = false;
                break;
            case "KlarnaOnError":
                singletonClass.getKlarna().clearSession();
                success_finish = false;
                break;
            case "KlarnaOnFinished":
                //Get The Consent And save it

                //singletonClass.getKlarna().clearSession();
                success_finish = true;
                //Get The Data
                break;
            case "KlarnaOnClose":
                if (success_finish){
                    //Go on TO the next Stage
                    //Get The Consent
                    //Session ID

                    String sessionID = singletonClass.getKlarna().getSessionController().getSessionData().getData().getSession_id();
                    singletonClass.getKlarna().getConsentController().getConsent(sessionID, new KlarnaResponseCallback() {
                        @Override
                        public void success() {
                            POSTgetConsentDataModel consentData = singletonClass.getKlarna().getConsentController().getConsentDataModel();

                            Log.i("Consent_ID", ""+consentData.getData().getConsent_id());
                            //Save Consent To DataBase

                            singletonClass.getDatabaseController().KlarnaConsentDBController.updateConsent(consentData);


                            singletonClass.getKlarna().clearSession();
                        }

                        @Override
                        public void error() {

                            singletonClass.getKlarna().clearSession();

                        }
                    });
                }
                else{
                    singletonClass.getUniversalBackarrow().performClick();
                    singletonClass.getKlarna().clearSession();
                }


                break;
            default:
                break;

        }

    }
}
