package com.news.goodlife.Singletons;

import com.news.goodlife.Data.Local.Controller.DatabaseController;

public class PreferenceSingleton {

    private static volatile PreferenceSingleton sSoleInstance;

    //private constructor.
    private PreferenceSingleton(){

        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PreferenceSingleton getInstance() {
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (SingletonClass.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null) sSoleInstance = new PreferenceSingleton();
            }
        }

        return sSoleInstance;
    }
}
