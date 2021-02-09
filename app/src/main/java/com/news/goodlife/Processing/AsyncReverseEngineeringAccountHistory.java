package com.news.goodlife.Processing;

import android.os.AsyncTask;
import android.util.Log;
import android.util.TimingLogger;

import com.news.goodlife.Data.Local.Models.Financial.BalanceModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Processing.Models.AccountHistory;
import com.news.goodlife.Processing.Models.DayDataModel;
import com.news.goodlife.Processing.Models.DayTransactionModel;
import com.news.goodlife.Singletons.SingletonClass;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AsyncReverseEngineeringAccountHistory extends AsyncTask<AccountHistory, Void, List<DayDataModel>> {


    SuccessCallback callback;
    AccountHistory accountHistory;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public AsyncReverseEngineeringAccountHistory(List<BalanceModel> allBalances, List<TransactionModel> allTransactions, SuccessCallback callback){

        this.callback = callback;
        accountHistory = new AccountHistory(allTransactions, allBalances);


        Log.i("AsyncTask", "Constructor");
        execute(accountHistory);

    }


    @Override
    protected List<DayDataModel> doInBackground(AccountHistory... accountHistories) {

        //calculate TIme it starts
        long startTime = System.nanoTime();

        int predictionPeriod = 365 * 20;
        int HypotheticalBalance = 0;

        List<TransactionModel> allTransactions = accountHistories[0].getAllTransactions();
        List<BalanceModel> allBalances = accountHistories[0].getAllBalances();


        List<DayDataModel> allDaysData = new ArrayList<>();

        //Get the day of First Transaction
        Date firstDay = stringToDate(allTransactions.get(0).getDate());
        Calendar processingDay = Calendar.getInstance();
        processingDay.setTime(firstDay);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String processingDayString = "";
        DayDataModel dayData = null;
        int SuperStartBalance = 0;
        int FreeToUse;

        getTodayItemPosition(firstDay);

        for(int i = 0; i < predictionPeriod; i++){
            //Iterate through all future days
            dayData = new DayDataModel();
            processingDay.setTime(firstDay);
            processingDay.add(Calendar.DATE, i);
            processingDayString = formater.format(processingDay.getTime());

            DayTransactionModel daysTransactions = getDaysTransactions(processingDayString, allTransactions);
            BalanceModel daysBalance = getDaysBalance(processingDayString, allBalances);

            HypotheticalBalance = HypotheticalBalance + daysTransactions.getDaySum();

            //GetStartBalance
            SuperStartBalance = getSuperStartBalance(HypotheticalBalance, daysBalance);
            //The Hypothetical Balance is the MAIN Input of the App Logic
            FreeToUse = AppLogic(HypotheticalBalance);

            dayData.setDayDate(processingDay.getTime());
            dayData.setHypertheticalBalance(HypotheticalBalance);
            dayData.setDayNo(""+processingDay.get(Calendar.DAY_OF_MONTH));
            dayData.setMonthNo(""+processingDay.get(Calendar.MONTH));
            dayData.setYearNo(""+processingDay.get(Calendar.YEAR));
            dayData.setMonthShort(""+processingDay.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));

            allDaysData.add(dayData);

        }

        Log.i("Measure", "" +  ((System.nanoTime()-startTime)/1000000)+ "mS\n");
        //get the Time in end
        return allDaysData;
    }

    private void getTodayItemPosition(Date firstDay){
        //today
        Calendar today = Calendar.getInstance();
        singletonClass.setTodayLogicDataPosition(getDaysBetween(firstDay, today.getTime()));
    }

    @Override
    protected void onPostExecute(List<DayDataModel> dayDataModels) {
        super.onPostExecute(dayDataModels);

        singletonClass.setLogicData(dayDataModels);
        callback.success();

    }
    private int AppLogic(int superStartBalance) {
        int FreeToUse = 200;

        return FreeToUse;
    }

    private int getSuperStartBalance(int H, BalanceModel daysBalance){

        if(daysBalance != null){
            int A, S;

            A = Integer.parseInt(daysBalance.getAmount());


            if(H > 0){
                H = -H;
            }
            else {
                H = Math.abs(H);
            }

            if(A > 0){
                S = H + A;
            }
            else{
                S = H - Math.abs(A);
            }

            return S;
        }
        else return 0;


    }

    private BalanceModel getDaysBalance(String processingDayString, List<BalanceModel> allBalances) {

        for(BalanceModel balance: allBalances){

            if(balance.getTimestamp().equals(processingDayString)){
                return balance;
            };
        }
        return null;
    }

    private DayTransactionModel getDaysTransactions(String processingDay, List<TransactionModel> allTransactions) {
        List<TransactionModel> daysTransactions = new ArrayList<>();
        int daySum = 0;
        //Make calendar have the format // yyyy-MM-dd

        for(TransactionModel transaction : allTransactions){

            if(transaction.getDate().equals(processingDay)){
                daysTransactions.add(transaction);
                daySum = calculateDay(daySum, transaction);
            }
        }

        DayTransactionModel returnData = new DayTransactionModel(daysTransactions, daySum);

        return returnData;
    }

    private int calculateDay(int daySum, TransactionModel transaction) {
        int newSum;
        int amount = Integer.parseInt(transaction.getAmount());
        if(transaction.getType().equals("DEBIT")){
            //TODO interchange depending on What DEBIT MEANS
            newSum = daySum - amount;
        }
        else{
            newSum = daySum + amount;
        }

        return newSum;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private Date stringToDate(String dateString){
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        try {

            return parser.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();

            return null;

        }
    }

    private boolean sameDay(Date one, Date two){
        return true;
    }

    private int getDaysBetween(Date startDate, Date endDate){
        int days = 0;
        long diff = endDate.getTime() - startDate.getTime();

        days = (int) (diff / (24 * 60 * 60 * 1000));
        return days;
    }
}
