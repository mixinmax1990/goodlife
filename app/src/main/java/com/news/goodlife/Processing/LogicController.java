package com.news.goodlife.Processing;

import android.util.Log;

import com.news.goodlife.Data.Local.Models.Financial.AccountModel;
import com.news.goodlife.Data.Local.Models.Financial.BalanceModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.List;

public class LogicController {


    //Create Objects to Process the Banking Data
    //TODO Make sure this is Asynchronous

    SingletonClass singletonClass = SingletonClass.getInstance();

    public LogicController() {

        //Travel Time ufrom the point of latest Balance
        /*
        * Model Hold data of each day
        * Required Data:
        *
        * Balance on Each Account
        * Free TO use Money
        *
        * List of all Transactions
        *
        * Fixed Costs
        * Budget
        * Goals
        * Savings
        *
        * */

        //First get All the Accounts

        List<AccountModel> allAccounts = singletonClass.getDatabaseController().AccountsController.getAllAccounts();



        //if(allAccounts != null){
        for(AccountModel account: allAccounts){
                //Process each account seperately
                String AccountID = account.getKlarna_id();
                // For each Account get the Balances
                List<BalanceModel> allBalances = singletonClass.getDatabaseController().BalanceController.getAllBalances(AccountID);
                // FOr each Account get all Transactions
                List<TransactionModel> allTransactions = singletonClass.getDatabaseController().TransactionController.getAllTransactions(AccountID);

                reverseEngineerData(allBalances, allTransactions);

            }

       /* }
        else{

            Log.i("No Accounts", "Yeat");

        }*/

    }

    private void reverseEngineerData(List<BalanceModel> allBalances, List<TransactionModel> allTransactions) {



        if(allTransactions != null){
            if(allTransactions.size() != 0) {
                Log.i("AllTransERRRR", "--" + allTransactions.size());
                AsyncReverseEngineeringAccountHistory reverseEngineering = new AsyncReverseEngineeringAccountHistory(allBalances, allTransactions, new SuccessCallback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void error() {

                    }
                });
            }

        }



    }


}


