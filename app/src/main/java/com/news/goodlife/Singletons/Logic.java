package com.news.goodlife.Singletons;

public class Logic {

    private static volatile Logic logic;

    public Logic() {

        if(logic != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class");

        }
    }

    public static Logic getInstance(){
        if(logic == null){
            synchronized (Logic.class){
                if(logic == null) logic = new Logic();
            }
        }

        return logic;
    }

}

