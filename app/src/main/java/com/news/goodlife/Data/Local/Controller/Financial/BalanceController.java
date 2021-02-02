package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.BalanceModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Data.Local.Statements.Financial.BalanceTable;
import com.news.goodlife.Data.Local.Statements.Financial.BudgetTable;

import java.util.ArrayList;
import java.util.List;

public class BalanceController extends DatabaseHelper {

    BalanceTable balanceTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public BalanceController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        balanceTable = new BalanceTable();
        db.execSQL(balanceTable.getCreateBudgetTable());

    }


    public long addBalance(BalanceModel model){

        values = new ContentValues();

        values.put(balanceTable.getID(), model.getId());
        values.put(balanceTable.getAccountId(), model.getAccount_id());
        values.put(balanceTable.getAMOUNT(), model.getAmount());
        values.put(balanceTable.getCURRENCY(), model.getCurrency());
        values.put(balanceTable.getTIMESTAMP(), model.getTimestamp());

        long insert = db.insert(balanceTable.getTableName(), null, values);

        return insert;
    }

    public List<BalanceModel> getAllBalances(String AccountID){

        List<BalanceModel> dataList = new ArrayList<>();

        String selectQuery = " SELECT * FROM " + balanceTable.TABLE_NAME + " WHERE " + balanceTable.getAccountId() + " =? ORDER BY " + balanceTable.getTIMESTAMP() + " ASC";

        Cursor c = db.rawQuery(selectQuery, new String[]{AccountID});

        if(c.moveToFirst()){
            do{
                BalanceModel balance = new BalanceModel();

                balance.setId(c.getString(c.getColumnIndex(balanceTable.getID())));
                balance.setAccount_id(c.getString(c.getColumnIndex(balanceTable.getAccountId())));
                balance.setAmount(c.getString(c.getColumnIndex(balanceTable.getAMOUNT())));
                balance.setCurrency(c.getString(c.getColumnIndex(balanceTable.getCURRENCY())));
                balance.setTimestamp(c.getString(c.getColumnIndex(balanceTable.getTIMESTAMP())));

                dataList.add(balance);
            }
            while (c.moveToNext());
        }

        return dataList;

    }

    public BalanceModel getLatestBalance(String AccountID){
        //Format need to be like 2012-11-07

        BalanceModel balance = null;

        String selectQuery = "Select * FROM " + balanceTable.TABLE_NAME +" WHERE "+ balanceTable.getAccountId() + " =? ORDER BY "+ balanceTable.getTIMESTAMP()+" DESC LIMIT 1";

        Cursor c = db.rawQuery(selectQuery, new String[]{AccountID});

        if(c.getCount() <= 0){
            c.close();
            return balance;

        }

        c.moveToFirst();

        balance = new BalanceModel();

        balance.setId(c.getString(c.getColumnIndex(balanceTable.getID())));
        balance.setAccount_id(c.getString(c.getColumnIndex(balanceTable.getAccountId())));
        balance.setAmount(c.getString(c.getColumnIndex(balanceTable.getAMOUNT())));
        balance.setCurrency(c.getString(c.getColumnIndex(balanceTable.getCURRENCY())));
        balance.setTimestamp(c.getString(c.getColumnIndex(balanceTable.getTIMESTAMP())));

        c.close();

        return balance;

    }
}
