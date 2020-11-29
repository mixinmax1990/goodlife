package com.news.goodlife.Singletons;

import com.news.goodlife.Data.Local.Controller.DatabaseController;

public class SingletonClass {

    private static volatile SingletonClass sSoleInstance;

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
                if (sSoleInstance == null) sSoleInstance = new SingletonClass();
            }
        }

        return sSoleInstance;
    }

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
}
