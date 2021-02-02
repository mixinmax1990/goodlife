package com.news.goodlife.Fragments.SlideInFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.Data.Local.Models.Financial.AccountModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.KlarnaAccountModel;
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

                            //Get the Consent Data
                            POSTgetConsentDataModel consentData = singletonClass.getKlarna().getConsentController().getConsentDataModel();

                            String flow_id = singletonClass.getKlarna().getFlowsController().getFlowData().getData().getFlow_id();

                            //Get Balance Data and retrieve the Account ID

                            singletonClass.getKlarna().getFlowsController().getBalanceFlow(flow_id, new KlarnaResponseCallback() {
                                @Override
                                public void success() {

                                    Log.i("Balance Data", "We should now have the Balance Data. Account Id = "+ singletonClass.getKlarna().getFlowsController().getBalanceData().getData().getResult().getAccount().getId());

                                    //Store the Balance Data into the Database

                                    singletonClass.getDatabaseController().AccountsController.addAccount(convertLocalAccount());


                                    //Then Clear the Klarna Session
                                    singletonClass.getKlarna().clearSession();
                                }

                                @Override
                                public void error() {

                                    Log.i("Balance Data", "SOmething went Wrong");

                                    singletonClass.getKlarna().clearSession();

                                }
                            });
                            //Save Consent To DataBase

                            singletonClass.getDatabaseController().KlarnaConsentDBController.updateConsent(consentData);


                            //Get Account Data here


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


    private AccountModel convertLocalAccount(){

        AccountModel account = new AccountModel();

        KlarnaAccountModel apiAccount = singletonClass.getKlarna().getFlowsController().getBalanceData().getData().getResult().getAccount();

        account.setId(null);
        account.setKlarna_id(apiAccount.getId());
        account.setAlias(apiAccount.getAlias());
        account.setAccount_number(apiAccount.getAccount_number());
        account.setIban(apiAccount.getIban());
        account.setBic(apiAccount.getBic());
        account.setBank_address_country(apiAccount.getBank_address().getCountry());
        account.setTransfer_type(apiAccount.getAccount_type());

        return account;

    }
}
