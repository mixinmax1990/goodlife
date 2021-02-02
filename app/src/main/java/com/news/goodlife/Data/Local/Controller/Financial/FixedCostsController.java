package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.FixedCostModel;
import com.news.goodlife.Data.Local.Statements.Financial.FixedCostTable;

import java.util.ArrayList;
import java.util.List;

public class FixedCostsController extends DatabaseHelper {
    FixedCostTable fixedCostTable;
    private SQLiteDatabase db;
    private ContentValues values;



    String id, iban, amount, expected, start_date, end_date;

    public FixedCostsController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        fixedCostTable = new FixedCostTable();
        db.execSQL(fixedCostTable.getCreateFixedCostsTable());
        db.close();

    }

    public long addFixedCost(FixedCostModel model){
        db = this.getWritableDatabase();

        values = new ContentValues();

        values.put(fixedCostTable.getID(), model.getId());
        values.put(fixedCostTable.getIBAN(), model.getIban());
        values.put(fixedCostTable.getNAME(), model.getName());
        values.put(fixedCostTable.getTransactionId(), model.getTransaction_id());
        values.put(fixedCostTable.getAMOUNT(), model.getIban());
        values.put(fixedCostTable.getEXPECTED(), model.getExpected());
        values.put(fixedCostTable.getStartDate(), model.getStart_date());
        values.put(fixedCostTable.getEndDate(), model.getEnd_date());

        long insert = db.insert(fixedCostTable.getTableName(), null, values);
        db.close();
        return insert;

    }

    public FixedCostModel getFixedCost(String IBAN){

        db = this.getWritableDatabase();

        String selectQuery = "SELCT * FROM " + fixedCostTable.getTableName() + " WHERE " + fixedCostTable.getIBAN() + "=?";

        Cursor c = db.rawQuery(selectQuery, new String[]{IBAN});

        if(c != null){
            c.moveToFirst();
        }
        FixedCostModel fixedcost = new FixedCostModel();

        fixedcost.setId(c.getString(c.getColumnIndex(fixedCostTable.getID())));
        fixedcost.setIban(c.getString(c.getColumnIndex(fixedCostTable.getIBAN())));
        fixedcost.setName(c.getString(c.getColumnIndex(fixedCostTable.getNAME())));
        fixedcost.setTransaction_id(c.getString(c.getColumnIndex(fixedCostTable.getTransactionId())));
        fixedcost.setAmount(c.getString(c.getColumnIndex(fixedCostTable.getAMOUNT())));
        fixedcost.setExpected(c.getString(c.getColumnIndex(fixedCostTable.getAMOUNT())));
        fixedcost.setStart_date(c.getString(c.getColumnIndex(fixedCostTable.getStartDate())));
        fixedcost.setEnd_date(c.getString(c.getColumnIndex(fixedCostTable.getEndDate())));

        db.close();
        return fixedcost;

    }

    public List<FixedCostModel> getAllFixedCosts(){
        db = this.getWritableDatabase();

        List<FixedCostModel> dataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + fixedCostTable.getTableName();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                FixedCostModel fixedcost = new FixedCostModel();



                fixedcost.setId(c.getString(c.getColumnIndex(fixedCostTable.getID())));
                fixedcost.setIban(c.getString(c.getColumnIndex(fixedCostTable.getIBAN())));
                fixedcost.setName(c.getString(c.getColumnIndex(fixedCostTable.getNAME())));
                fixedcost.setTransaction_id(c.getString(c.getColumnIndex(fixedCostTable.getTransactionId())));
                fixedcost.setAmount(c.getString(c.getColumnIndex(fixedCostTable.getAMOUNT())));
                fixedcost.setExpected(c.getString(c.getColumnIndex(fixedCostTable.getAMOUNT())));
                fixedcost.setStart_date(c.getString(c.getColumnIndex(fixedCostTable.getStartDate())));
                fixedcost.setEnd_date(c.getString(c.getColumnIndex(fixedCostTable.getEndDate())));



                dataList.add(fixedcost);
            }
            while(c.moveToNext());
        }

        db.close();
        return dataList;
    }

    public boolean deleteFixedCost(String IBAN){
        //TODO use better conditions , IBAN could have multiple entries to choose from
        boolean success;

        db = this.getWritableDatabase();

        try{
            db.delete(fixedCostTable.getTableName(), fixedCostTable.getIBAN() + " =?", new String[]{IBAN});
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        db.close();
        return success;
    }
}
