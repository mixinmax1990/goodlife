package com.news.goodlife.Singletons;

import android.content.res.Resources;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Remote.Klarna.Controller.KlarnaRequestController;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Models.ObservableFragmentChange;

import java.util.Observable;

public class SingletonClass {

    private static volatile SingletonClass sSoleInstance;
    private static volatile KlarnaRequestController klarna;

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
}
