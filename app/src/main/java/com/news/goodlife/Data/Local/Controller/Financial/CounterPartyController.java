package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.CounterPartyModel;
import com.news.goodlife.Data.Local.Statements.Financial.CounterPartyTable;
import com.news.goodlife.Data.Local.Statements.Financial.TransactionTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CounterPartyController extends DatabaseHelper {

    CounterPartyTable counterPartyTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public CounterPartyController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        counterPartyTable = new CounterPartyTable();
        db.execSQL(counterPartyTable.getCreateCounterpartyTable());
    }

    String id, counterparty_id, alias, account_number, iban, street_address, postalcode, city, region, country;

    public long addCounterParty(CounterPartyModel model){

        values = new ContentValues();

        values.put(counterPartyTable.getID(), model.getId());
        values.put(counterPartyTable.getCounterpartyId(), model.getCounterparty_id());
        values.put(counterPartyTable.getHolderName(), model.getHolder_name());
        values.put(counterPartyTable.getALIAS(), model.getAlias());
        values.put(counterPartyTable.getAccountNumber(), model.getAccount_number());
        values.put(counterPartyTable.getIBAN(), model.getIban());
        values.put(counterPartyTable.getStreetAddress(), model.getStreet_address());
        values.put(counterPartyTable.getPOSTALCODE(), model.getPostalcode());
        values.put(counterPartyTable.getCITY(), model.getCity());
        values.put(counterPartyTable.getREGION(), model.getRegion());
        values.put(counterPartyTable.getCOUNTRY(), model.getCountry());

        long insert = db.insert(counterPartyTable.getTableName(), null, values);
        return insert;
    }

    public List<CounterPartyModel> getAllCounterparties(){
        db = this.getWritableDatabase();

        List<CounterPartyModel> dataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + counterPartyTable.getTableName();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                CounterPartyModel counterparty = new CounterPartyModel();

                counterparty.setId(c.getString(c.getColumnIndex(counterPartyTable.getID())));
                counterparty.setCounterparty_id(c.getString(c.getColumnIndex(counterPartyTable.getCounterpartyId())));
                counterparty.setHolder_name(c.getString(c.getColumnIndex(counterPartyTable.getHolderName())));
                counterparty.setAlias(c.getString(c.getColumnIndex(counterPartyTable.getALIAS())));
                counterparty.setAccount_number(c.getString(c.getColumnIndex(counterPartyTable.getAccountNumber())));
                counterparty.setIban(c.getString(c.getColumnIndex(counterPartyTable.getIBAN())));
                counterparty.setStreet_address(c.getString(c.getColumnIndex(counterPartyTable.getStreetAddress())));
                counterparty.setPostalcode(c.getString(c.getColumnIndex(counterPartyTable.getPOSTALCODE())));
                counterparty.setCity(c.getString(c.getColumnIndex(counterPartyTable.getCITY())));
                counterparty.setRegion(c.getString(c.getColumnIndex(counterPartyTable.getREGION())));
                counterparty.setCountry(c.getString(c.getColumnIndex(counterPartyTable.getCOUNTRY())));

                dataList.add(counterparty);
            }
            while(c.moveToNext());
        }

        return dataList;
    }

    public CounterPartyModel getCounterParty(String counterPartyID){
        db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + counterPartyTable.getTableName() + " WHERE " + counterPartyTable.getCounterpartyId()+ " = " + counterPartyID;

        Cursor c = db.rawQuery(selectQuery, null);

        CounterPartyModel counterparty = null;

        if(c != null){
            c.moveToFirst();

            counterparty = new CounterPartyModel();

            counterparty.setId(c.getString(c.getColumnIndex(counterPartyTable.getID())));
            counterparty.setCounterparty_id(c.getString(c.getColumnIndex(counterPartyTable.getCounterpartyId())));
            counterparty.setHolder_name(c.getString(c.getColumnIndex(counterPartyTable.getHolderName())));
            counterparty.setAlias(c.getString(c.getColumnIndex(counterPartyTable.getALIAS())));
            counterparty.setAccount_number(c.getString(c.getColumnIndex(counterPartyTable.getAccountNumber())));
            counterparty.setIban(c.getString(c.getColumnIndex(counterPartyTable.getIBAN())));
            counterparty.setStreet_address(c.getString(c.getColumnIndex(counterPartyTable.getStreetAddress())));
            counterparty.setPostalcode(c.getString(c.getColumnIndex(counterPartyTable.getPOSTALCODE())));
            counterparty.setCity(c.getString(c.getColumnIndex(counterPartyTable.getCITY())));
            counterparty.setRegion(c.getString(c.getColumnIndex(counterPartyTable.getREGION())));
            counterparty.setCountry(c.getString(c.getColumnIndex(counterPartyTable.getCOUNTRY())));
        }

        return counterparty;
    }

    public CounterPartyModel getCounterPartyByCounterPartyID(String CounterPartyID){
        db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + counterPartyTable.getTableName() + " WHERE " + counterPartyTable.getCounterpartyId()+ " = ?";

        Cursor c = db.rawQuery(selectQuery, new String[] {CounterPartyID});

        CounterPartyModel counterparty = null;

        if(c.getCount() <= 0){
            c.close();
            return counterparty;
        }


        c.moveToFirst();

        counterparty = new CounterPartyModel();

        counterparty.setId(c.getString(c.getColumnIndex(counterPartyTable.getID())));
        counterparty.setCounterparty_id(c.getString(c.getColumnIndex(counterPartyTable.getCounterpartyId())));
        counterparty.setHolder_name(c.getString(c.getColumnIndex(counterPartyTable.getHolderName())));
        counterparty.setAlias(c.getString(c.getColumnIndex(counterPartyTable.getALIAS())));
        counterparty.setAccount_number(c.getString(c.getColumnIndex(counterPartyTable.getAccountNumber())));
        counterparty.setIban(c.getString(c.getColumnIndex(counterPartyTable.getIBAN())));
        counterparty.setStreet_address(c.getString(c.getColumnIndex(counterPartyTable.getStreetAddress())));
        counterparty.setPostalcode(c.getString(c.getColumnIndex(counterPartyTable.getPOSTALCODE())));
        counterparty.setCity(c.getString(c.getColumnIndex(counterPartyTable.getCITY())));
        counterparty.setRegion(c.getString(c.getColumnIndex(counterPartyTable.getREGION())));
        counterparty.setCountry(c.getString(c.getColumnIndex(counterPartyTable.getCOUNTRY())));


        return counterparty;
    }
}
