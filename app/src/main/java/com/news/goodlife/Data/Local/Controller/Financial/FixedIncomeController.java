package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.FixedCostModel;
import com.news.goodlife.Data.Local.Models.Financial.FixedIncomeModel;
import com.news.goodlife.Data.Local.Statements.Financial.FixedIncomeTable;

import java.util.ArrayList;
import java.util.List;

public class FixedIncomeController extends DatabaseHelper {

    FixedIncomeTable fixedIncomeTable;
    private SQLiteDatabase db;
    private ContentValues values;


    String id, iban, amount, expected, start_date, end_date;

    public FixedIncomeController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        fixedIncomeTable = new FixedIncomeTable();
        db.execSQL(fixedIncomeTable.getCreateFixedIncomeTable());
        db.close();

    }

    public long addFixedIncome(FixedIncomeModel model){
        db = this.getWritableDatabase();

        values = new ContentValues();

        values.put(fixedIncomeTable.getID(), model.getId());
        values.put(fixedIncomeTable.getIBAN(), model.getIban());
        values.put(fixedIncomeTable.getNAME(), model.getName());
        values.put(fixedIncomeTable.getTransactionId(), model.getTransaction_id());
        values.put(fixedIncomeTable.getAMOUNT(), model.getIban());
        values.put(fixedIncomeTable.getEXPECTED(), model.getExpected());
        values.put(fixedIncomeTable.getStartDate(), model.getStart_date());
        values.put(fixedIncomeTable.getEndDate(), model.getEnd_date());

        long insert = db.insert(fixedIncomeTable.getTableName(), null, values);
        db.close();
        return insert;

    }

    public FixedIncomeModel getFixedIncome(String IBAN){

        db = this.getWritableDatabase();

        String selectQuery = "SELCT * FROM " + fixedIncomeTable.getTableName() + " WHERE " + fixedIncomeTable.getIBAN() + "=?";

        Cursor c = db.rawQuery(selectQuery, new String[]{IBAN});

        if(c != null){
            c.moveToFirst();
        }
        FixedIncomeModel fixedincome = new FixedIncomeModel();

        fixedincome.setId(c.getString(c.getColumnIndex(fixedIncomeTable.getID())));
        fixedincome.setIban(c.getString(c.getColumnIndex(fixedIncomeTable.getIBAN())));
        fixedincome.setName(c.getString(c.getColumnIndex(fixedIncomeTable.getNAME())));
        fixedincome.setTransaction_id(c.getString(c.getColumnIndex(fixedIncomeTable.getTransactionId())));
        fixedincome.setAmount(c.getString(c.getColumnIndex(fixedIncomeTable.getAMOUNT())));
        fixedincome.setExpected(c.getString(c.getColumnIndex(fixedIncomeTable.getAMOUNT())));
        fixedincome.setStart_date(c.getString(c.getColumnIndex(fixedIncomeTable.getStartDate())));
        fixedincome.setEnd_date(c.getString(c.getColumnIndex(fixedIncomeTable.getEndDate())));

        db.close();
        return fixedincome;

    }

    public List<FixedIncomeModel> getAllFixedIncomes(){
        db = this.getWritableDatabase();

        List<FixedIncomeModel> dataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + fixedIncomeTable.getTableName();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                FixedIncomeModel fixedIncome = new FixedIncomeModel();



                fixedIncome.setId(c.getString(c.getColumnIndex(fixedIncomeTable.getID())));
                fixedIncome.setIban(c.getString(c.getColumnIndex(fixedIncomeTable.getIBAN())));
                fixedIncome.setName(c.getString(c.getColumnIndex(fixedIncomeTable.getNAME())));
                fixedIncome.setTransaction_id(c.getString(c.getColumnIndex(fixedIncomeTable.getTransactionId())));
                fixedIncome.setAmount(c.getString(c.getColumnIndex(fixedIncomeTable.getAMOUNT())));
                fixedIncome.setExpected(c.getString(c.getColumnIndex(fixedIncomeTable.getAMOUNT())));
                fixedIncome.setStart_date(c.getString(c.getColumnIndex(fixedIncomeTable.getStartDate())));
                fixedIncome.setEnd_date(c.getString(c.getColumnIndex(fixedIncomeTable.getEndDate())));



                dataList.add(fixedIncome);
            }
            while(c.moveToNext());
        }

        db.close();
        return dataList;
    }

    public boolean deleteFixedIncome(String IBAN){
        //TODO use better conditions , IBAN could have multiple entries to choose from
        boolean success;

        db = this.getWritableDatabase();

        try{
            db.delete(fixedIncomeTable.getTableName(), fixedIncomeTable.getIBAN() + " =?", new String[]{IBAN});
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        db.close();
        return success;
    }
}
