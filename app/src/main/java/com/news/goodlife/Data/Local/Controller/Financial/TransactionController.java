package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.CounterPartyModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Data.Local.Statements.Financial.TransactionTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TransactionController extends DatabaseHelper {

    TransactionTable transactionTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public TransactionController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        transactionTable = new TransactionTable();
        db.execSQL(transactionTable.getCreateTransactionTable());

    }

    public long addTransaction(TransactionModel model){

        values = new ContentValues();

        values.put(transactionTable.getID(), model.getId());
        values.put(transactionTable.getAccountId(), model.getAccount_id());
        values.put(transactionTable.getTransactionId(), model.getTransaction_id());
        values.put(transactionTable.getREFERENCE(), model.getReference());
        values.put(transactionTable.getCounterpartyId(), model.getCounterparty_id());
        values.put(transactionTable.getDATE(), model.getDate());
        values.put(transactionTable.getValueDate(), model.getValue_date());
        values.put(transactionTable.getBookingDate(), model.getBooking_date());
        values.put(transactionTable.getSTATE(), model.getState());
        values.put(transactionTable.getTYPE(), model.getType());
        values.put(transactionTable.getMETHOD(), model.getMethod());
        values.put(transactionTable.getAMOUNT(), model.getAmount());
        values.put(transactionTable.getAmountCurrency(), model.getAmount_currency());


        long insert = db.insert(transactionTable.getTableName(), null, values);
        return  insert;
    }

    public List<TransactionModel> getAllTransactions(String AccountID){
        db = this.getWritableDatabase();

        List<TransactionModel> dataList = new ArrayList<>();

        //Sort BY oldest first
        String selectQuery = " SELECT * FROM " + transactionTable.TABLE_NAME + " WHERE " + transactionTable.getAccountId() + " =? ORDER BY " + transactionTable.getDATE() + " ASC";

        Cursor c = db.rawQuery(selectQuery, new String[]{AccountID});

        if(c.moveToFirst()){
            do{
                TransactionModel transaction = new TransactionModel();

                transaction.setId(c.getString(c.getColumnIndex(transactionTable.getID())));
                transaction.setAccount_id(c.getString(c.getColumnIndex(transactionTable.getAccountId())));
                transaction.setTransaction_id(c.getString(c.getColumnIndex(transactionTable.getTransactionId())));
                transaction.setReference(c.getString(c.getColumnIndex(transactionTable.getREFERENCE())));
                transaction.setCounterparty_id(c.getString(c.getColumnIndex(transactionTable.getCounterpartyId())));
                transaction.setDate(c.getString(c.getColumnIndex(transactionTable.getDATE())));
                transaction.setValue_date(c.getString(c.getColumnIndex(transactionTable.getValueDate())));
                transaction.setBooking_date(c.getString(c.getColumnIndex(transactionTable.getBookingDate())));
                transaction.setState(c.getString(c.getColumnIndex(transactionTable.getSTATE())));
                transaction.setType(c.getString(c.getColumnIndex(transactionTable.getTYPE())));
                transaction.setMethod(c.getString(c.getColumnIndex(transactionTable.getMETHOD())));
                transaction.setAmount(c.getString(c.getColumnIndex(transactionTable.getAMOUNT())));
                transaction.setAmount_currency(c.getString(c.getColumnIndex(transactionTable.getAmountCurrency())));

                dataList.add(transaction);
            }
            while (c.moveToNext());
        }

        return dataList;
    }


    public boolean getTransactionByTransactionID(String TransactionID){
        db = this.getWritableDatabase();

        Log.i("Transaction ID", "" + TransactionID);

        String selectQuery = " SELECT * FROM " + transactionTable.getTableName() + " WHERE " + transactionTable.getTransactionId() + " =?";

        Cursor c = db.rawQuery(selectQuery, new String[] {TransactionID});

        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;

    }

    // Create Methods to DELETE, GET BY DATE, ETC

}
