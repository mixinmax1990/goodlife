package com.news.goodlife.Data.Local.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.CashflowModel;
import com.news.goodlife.Data.Local.Statements.CashflowTable;

import java.util.ArrayList;
import java.util.List;

public class CashflowController extends DatabaseHelper {

    private CashflowTable cashflowTable;
    private SQLiteDatabase db;
    private ContentValues values;
    public CashflowController(Context context) {
        super(context);

        db = this.getWritableDatabase();
        cashflowTable = new CashflowTable();
        db.execSQL(cashflowTable.getCreateTableCashflow());
    }

    public long newCashflow(CashflowModel model){
        db = this.getWritableDatabase();

        values = new ContentValues();
        //values.put(testTable.getDataId(), model.getTest_data_id());
        values.put(cashflowTable.getCashflowValue(), model.getValue());
        values.put(cashflowTable.getCashflowDescription(), model.getDescription());
        values.put(cashflowTable.getCashflowPositiv(), model.getPositive());
        values.put(cashflowTable.getCashflowRepeat(), model.getRepeat());
        values.put(cashflowTable.getCashflowDate(), model.getDate());
        values.put(cashflowTable.getCashflowCreated(), model.getCreated());


        long insert = db.insert(cashflowTable.getTableName(), null, values);

        return insert;

    }
    public CashflowModel getCashflow(int CashflowID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + cashflowTable.TABLE_NAME + " WHERE "
                + cashflowTable.getCashflow_id() + " = " + CashflowID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        //Log.e(LOG, selectQuery);


        CashflowModel cashflow = new CashflowModel();

        cashflow.setId(c.getString(c.getColumnIndex(cashflowTable.getCashflow_id())));
        cashflow.setValue(c.getString(c.getColumnIndex(cashflowTable.getCashflowValue())));
        cashflow.setDescription(c.getString(c.getColumnIndex(cashflowTable.getCashflowDescription())));
        cashflow.setRepeat(c.getString(c.getColumnIndex(cashflowTable.getCashflowRepeat())));
        cashflow.setCreated(c.getString(c.getColumnIndex(cashflowTable.getCashflowCreated())));
        cashflow.setDate(c.getString(c.getColumnIndex(cashflowTable.getCashflowDate())));
        cashflow.setPositive(c.getString(c.getColumnIndex(cashflowTable.getCashflowPositiv())));


        return cashflow;
    }

    public List<CashflowModel> getAllCashflow(){
        db = this.getWritableDatabase();

        List<CashflowModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + cashflowTable.TABLE_NAME +" Order By "+ cashflowTable.getCashflowDate()+" ASC ";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                CashflowModel data = new CashflowModel();

                data.setId(c.getString(c.getColumnIndex(cashflowTable.getCashflow_id())));
                data.setValue(c.getString(c.getColumnIndex(cashflowTable.getCashflowValue())));
                data.setDescription(c.getString(c.getColumnIndex(cashflowTable.getCashflowDescription())));
                data.setPositive(c.getString(c.getColumnIndex(cashflowTable.getCashflowPositiv())));
                data.setRepeat(c.getString(c.getColumnIndex(cashflowTable.getCashflowRepeat())));
                data.setDate(c.getString(c.getColumnIndex(cashflowTable.getCashflowDate())));
                data.setCreated(c.getString(c.getColumnIndex(cashflowTable.getCashflowCreated())));

                datalist.add(data);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public void deleteCashflow(String CashflowID) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(cashflowTable.TABLE_NAME, cashflowTable.getCashflow_id() + " = ?",
                new String[] { CashflowID });
    }


}
