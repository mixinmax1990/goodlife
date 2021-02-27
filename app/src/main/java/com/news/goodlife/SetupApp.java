package com.news.goodlife;

import android.util.Log;

import com.news.goodlife.Data.Local.Models.Financial.AccountModel;
import com.news.goodlife.Data.Local.Models.Financial.BalanceModel;
import com.news.goodlife.Data.Local.Models.Financial.CounterPartyModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.AccountDataModels.KlarnaTransactionModel;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Processing.LogicController;
import com.news.goodlife.Singletons.SingletonClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SetupApp {

    SingletonClass singletonClass = SingletonClass.getInstance();
    SuccessCallback startAcivityCallback;
    boolean test = true;

    public SetupApp(SuccessCallback startAcivityCallback) {
        this.startAcivityCallback = startAcivityCallback;

        singletonClass.setSubscribed(checkSubscription());

        //Load Test Data
        if(test){

            startLogic();
            /*
           TestData(new SuccessCallback() {
                @Override
                public void success() {
                    startLogic();
                }

                @Override
                public void error() {

                }
            });*/
        }
        else{
            checkBankConsent(new SuccessCallback() {
                @Override
                public void success() {

                    startLogic();

                }

                @Override
                public void error() {
                    startAcivityCallback.error();

                }
            });

        }

        singletonClass.setCurrencySymbol("€");
    }

    private void TestData(SuccessCallback callback){

        String accountID = "1234";
        AccountModel testAccount = new AccountModel();
        testAccount.setAccount_number(accountID);
        testAccount.setAccount_type("Some");
        testAccount.setAlias("Some");
        testAccount.setBank_address_country("null");
        testAccount.setBic("null");
        testAccount.setIban("null");
        testAccount.setKlarna_id(accountID);
        testAccount.setTransfer_type("null");

        singletonClass.getDatabaseController().AccountsController.addAccount(testAccount);


        MyAccountData myData = new MyAccountData(accountID);

        for(TransactionModel transaction: myData.getMyTestTransactions()){

            singletonClass.getDatabaseController().TransactionController.addTransaction(transaction);

        }
        callback.success();

    }

    LogicController logicController;
    private void startLogic() {
        logicController = new LogicController(new SuccessCallback() {
            @Override
            public void success() {
                // Day Logic Data is Ready to be Used
                startAcivityCallback.success();
            }

            @Override
            public void error() {

                //startAcivityCallback.error();
            }
        });
    }

    private boolean checkSubscription(){
        boolean connected = false;

        return connected;
    }

    int noOfAccounts = 0;
    private void checkBankConsent(SuccessCallback callback) {
        POSTgetConsentDataModel consentData = singletonClass.getDatabaseController().KlarnaConsentDBController.getConsent();



        if(consentData == null){

            Log.i("Get Consent", "No Consent YET");
            callback.error();

        }
        else{
            //We have consent lets check accounts
            List<AccountModel> allAccounts = singletonClass.getDatabaseController().AccountsController.getAllAccounts();

            if(allAccounts != null){
                noOfAccounts = allAccounts.size();
                //Get Latest Transactions Fromm All The Accounts
                for(AccountModel account: allAccounts){

                    BalanceModel latestBalance = singletonClass.getDatabaseController().BalanceController.getLatestBalance(account.getKlarna_id());


                    if(latestBalance == null){
                        getBalance(account.getKlarna_id());
                    }
                    else{
                        //Check how old the Balance is

                        int balanceAge = getBalanceAge(latestBalance.getTimestamp());

                        if(balanceAge > 6){
                            getBalance(account.getKlarna_id());
                        }

                        Log.i("Latest Balance", "Exists");
                        // Parse
                    }



                    getLatestTransactions(account.getKlarna_id(), callback);

                }
            }
            else{

                Log.i("Accountis", "Null");
                callback.error();

            }
        }
    }

    private int getBalanceAge(String timestamp) {
        //Timestamp format yyyy-MM-dd
        int age = 0;

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date balanceday = parser.parse(timestamp);
            Date today = new Date();

            long diff = today.getTime() - balanceday.getTime();

            age = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.i("Day Age", " - "+age);

            return age;


        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("Exception", "Day Parser");

            return 10;
        }

    }

    private void getBalance(String AccountID){
        singletonClass.getKlarna().getFlowsController().getBalance(AccountID, new KlarnaResponseCallback() {
            @Override
            public void success() {
                Log.i("Get Balnce", "Works");
            }

            @Override
            public void error() {
                Log.i("Get Balance", "Fail");
            }
        });
    }

    private void getLatestTransactions(String accountID, SuccessCallback callback){

        singletonClass.getKlarna().getFlowsController().getTransactions(accountID, new KlarnaResponseCallback() {
            @Override
            public void success() {

                Log.i("Transaction", "Latest Getting");

                List<KlarnaTransactionModel> allTransactions = singletonClass.getKlarna().getFlowsController().getLatestTransactions().getData().getResult().getTransactions();
                if(allTransactions == null){


                    scannedTransactions(callback, true);

                }
                else{

                    for(KlarnaTransactionModel transaction : allTransactions){

                        //First check if transaction is already in Database


                        String transactionID = transaction.getTransaction_id();
                        String counterPartyID = transaction.getCounter_party().getId();

                        boolean transactionStored = singletonClass.getDatabaseController().TransactionController.getTransactionByTransactionID(transactionID);
                        CounterPartyModel counterPartyModel = singletonClass.getDatabaseController().CounterPartyController.getCounterPartyByCounterPartyID(counterPartyID);

                        if(!transactionStored){
                            //Log.i("Transaction", "Not in Database");
                            //Check if CounterParty is Stored Already
                            if(counterPartyModel == null){
                                //counterPartyModel = storeCounterParty(transaction);
                            }

                            storeTransaction(transaction, accountID);
                        }
                        else{
                            //Log.i("Transaction", "Transaction is already in Database");
                        }
                    }
                    scannedTransactions(callback, false);
                }


            }
            @Override
            public void error() {
               // Log.i("Getting Transactions", "-null-");
                scannedTransactions(callback, true);
            }
        });

    }

    int scannedAccounts = 0;

    private void scannedTransactions(SuccessCallback callback, boolean error){
        scannedAccounts ++;

        if(scannedAccounts == noOfAccounts){
            callback.success();
        }
    }

    private CounterPartyModel storeCounterParty(KlarnaTransactionModel transaction) {

        CounterPartyModel counterPartyModel = new CounterPartyModel();

        counterPartyModel.setCounterparty_id(transaction.getCounter_party().getId().isEmpty() ? "null" : transaction.getCounter_party().getId());

        counterPartyModel.setCountry(transaction.getCounter_party().getHolder_address().getCountry().isEmpty() ? "null" : transaction.getCounter_party().getHolder_address().getCountry());

        counterPartyModel.setRegion(transaction.getCounter_party().getHolder_address().getRegion() == null ? "null" : transaction.getCounter_party().getHolder_address().getRegion());

        counterPartyModel.setCity(transaction.getCounter_party().getHolder_address().getCity() == null ? "null" : transaction.getCounter_party().getHolder_address().getCity());

        counterPartyModel.setPostalcode(transaction.getCounter_party().getHolder_address().getPostalcode() == null ? "null" : transaction.getCounter_party().getHolder_address().getPostalcode());

        counterPartyModel.setStreet_address(transaction.getCounter_party().getHolder_address().getStreet_address() == null ? "null" : transaction.getCounter_party().getHolder_address().getStreet_address());

        counterPartyModel.setIban(transaction.getCounter_party().getIban() == null ? "null" : transaction.getCounter_party().getIban());

        counterPartyModel.setAccount_number(transaction.getCounter_party().getAccount_number() == null ? "null" : transaction.getCounter_party().getAccount_number());

        counterPartyModel.setAlias(transaction.getCounter_party().getAlias() == null ? "null" : transaction.getCounter_party().getAlias());

        counterPartyModel.setHolder_name(transaction.getCounter_party().getHolder_name() == null ? "null" : transaction.getCounter_party().getHolder_name());

        singletonClass.getDatabaseController().CounterPartyController.addCounterParty(counterPartyModel);

        return counterPartyModel;
    }

    private void storeTransaction(KlarnaTransactionModel transaction, String account_id) {

        com.news.goodlife.Data.Local.Models.Financial.TransactionModel transactionModel = new com.news.goodlife.Data.Local.Models.Financial.TransactionModel();

        transactionModel.setAmount_currency(transaction.getAmount().getCurrency());
        transactionModel.setAmount(""+transaction.getAmount().getAmount());
        transactionModel.setBooking_date(transaction.getBooking_date());
        transactionModel.setMethod(transaction.getMethod());
        transactionModel.setType(transaction.getType());
        transactionModel.setState(transaction.getType());
        transactionModel.setValue_date(transaction.getValue_date());
        transactionModel.setDate(transaction.getDate());
        transactionModel.setCounterparty_id(transaction.getCounter_party().getId());
        transactionModel.setReference(transaction.getReference());
        transactionModel.setTransaction_id(transaction.getTransaction_id());
        transactionModel.setAccount_id(account_id);

        singletonClass.getDatabaseController().TransactionController.addTransaction(transactionModel);
    }


}
