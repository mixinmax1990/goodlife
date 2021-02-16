package com.news.goodlife.Singletons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.news.goodlife.CustomViews.CustomIcons.FunctionsIcon;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Remote.Klarna.Controller.KlarnaRequestController;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Models.ObservableFragmentChange;
import com.news.goodlife.Processing.Models.DayDataModel;

import java.util.List;

public class SingletonClass {

    private static volatile SingletonClass sSoleInstance;
    private static volatile KlarnaRequestController klarna;
    private String currencySymbol;
    public FunctionsIcon functionsIcon;


    private List<DayDataModel> LogicData;

    //private constructor.
    private SingletonClass(){

        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static SingletonClass getInstance() {
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (SingletonClass.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null)
                {
                    sSoleInstance = new SingletonClass();
                    klarna = new KlarnaRequestController();

                    //Check if We have consent to get Bank Account Data

                }
            }
        }

        return sSoleInstance;
    }

    public List<DayDataModel> getLogicData() {
        return LogicData;
    }

    int todayLogicDataPosition = 0;

    public int getTodayLogicDataPosition() {
        return todayLogicDataPosition;
    }

    public void setTodayLogicDataPosition(int todayLogicDataPosition) {
        this.todayLogicDataPosition = todayLogicDataPosition;
    }

    public void setLogicData(List<DayDataModel> logicData) {
        LogicData = logicData;
    }

    //Klarna Controller

    public POSTgetConsentDataModel klarnaConsent;
    public boolean providedConsent(){

        this.klarnaConsent = getDatabaseController().KlarnaConsentDBController.getConsent();

        if(klarnaConsent != null){
            return true;
        }

        return false;
    }


    public static KlarnaRequestController getKlarna() {
        return klarna;
    }

    public ObservableFragmentChange changeFragment = new ObservableFragmentChange("None");



    int DisplayWidth;
    int DisplayHeight;
    DatabaseController databaseController;

    public int getDisplayWidth() {
        return DisplayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        DisplayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return DisplayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        DisplayHeight = displayHeight;
    }

    public DatabaseController getDatabaseController() {
        return databaseController;
    }

    public void setDatabaseController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }

    View UniversalBackarrow;

    public View getUniversalBackarrow() {
        return UniversalBackarrow;
    }

    public void setUniversalBackarrow(View universalBackarrow) {
        UniversalBackarrow = universalBackarrow;
    }

    View offsetViewParent, offsetViewChild;

    public View getOffsetViewParent() {
        return offsetViewParent;
    }

    public void setOffsetViewParent(View offsetViewParent) {
        this.offsetViewParent = offsetViewParent;
    }

    public View getOffsetViewChild() {
        return offsetViewChild;
    }

    public void setOffsetViewChild(View offsetViewChild) {
        this.offsetViewChild = offsetViewChild;
    }

    FragmentManager fragmentManager;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    boolean Subscribed;

    public boolean isSubscribed() {
        return Subscribed;
    }

    public void setSubscribed(boolean connectedAccounts) {
        this.Subscribed = connectedAccounts;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public void toggleFadeView(boolean in, View view, SuccessCallback callback){
        float scale = .2f;
        if(in){
            view.setAlpha(0);
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setAlpha(1);
        }


        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        va.setDuration(250);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animVal = (float) animation.getAnimatedValue();

                if(!in){
                    animVal = 1 - animVal;
                }
                float animScale = scale * (1 - animVal);
                view.setScaleY(1f - animScale);
                view.setScaleX(1f - animScale);

                view.setAlpha(animVal);

            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                callback.success();
                if(!in){
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }

    public String monefy(String klarnaAmount){
        String moneyfied;

        if(klarnaAmount.length() > 2){
            moneyfied = getCurrencySymbol() + new StringBuilder(klarnaAmount).insert(klarnaAmount.length()-2, ".").toString();
        }
        else{
            moneyfied = "€0,00";
        }



        return moneyfied;

    }
}
