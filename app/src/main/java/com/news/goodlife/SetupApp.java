package com.news.goodlife;

import android.util.Log;

import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.TransactionModel;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class SetupApp {

    SingletonClass singletonClass = SingletonClass.getInstance();

    public SetupApp() {

        //checkBudgetCategories();
        singletonClass.setSubscribed(checkSubscription());
        checkBankConsent();
    }


    private boolean checkSubscription(){
        boolean connected = false;

        return connected;
    }

    private void checkBankConsent() {
        POSTgetConsentDataModel consentData = singletonClass.getDatabaseController().KlarnaConsentDBController.getConsent();
        if(consentData == null){
            Log.i("Consent", "No Consent");
        }
        else{
            Log.i("Consent", "You have the FOllowing Consent ID "+consentData.getData().getConsent_id());
            Log.i("Consent", "You have the FOllowing Consent Token "+consentData.getData().getConsent_token());
        }

        singletonClass.getKlarna().getFlowsController().getTransactions("NzRiOWMzOWItNDQxYi00ZGYzLWIyZjItODA5Y2Y1MWFjNzQ0", new KlarnaResponseCallback() {
            @Override
            public void success() {

                List<TransactionModel> allTransactions = singletonClass.getKlarna().getFlowsController().getLatestTransactions().getData().getResult().getTransactions();
                for(TransactionModel transaction : allTransactions){
                    Log.i("Transaction", " Reference:"+transaction.getReference() + " -- Amount:"+transaction.getAmount().getAmount());

                }

            }

            @Override
            public void error() {
                Log.i("Getting Transactions", "-null-");

            }
        });

    }



    List<BudgetCategoryModel> budgetCatData;
    private void checkBudgetCategories() {
        budgetCatData = singletonClass.getDatabaseController().BudgetCategoryController.getAllBudgetCategories();

        Log.i("DataSize BudgetCat", ""+budgetCatData.size());
        resetBaseCategories();
        if(budgetCatData.size() == 0){

            resetBaseCategories();
        }

        Log.i("After BudgetCat", ""+budgetCatData.size());

    }

    public void resetBaseCategories() {



    }




}
