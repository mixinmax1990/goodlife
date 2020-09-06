package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.FinancialEventModel;
import com.news.goodlife.Data.Local.Statements.Financial.FinancialEventTable;

import java.util.ArrayList;
import java.util.List;

public class FinancialEventController extends DatabaseHelper {

    private FinancialEventTable financialEventTable;
    private SQLiteDatabase db;
    private ContentValues values;
    public FinancialEventController(Context context) {
        super(context);

        db = this.getWritableDatabase();
        financialEventTable = new FinancialEventTable();
        db.execSQL(financialEventTable.getCreateFinancialTableEvent());
    }

    public long newCashflow(FinancialEventModel model){
        db = this.getWritableDatabase();

        values = new ContentValues();
        //values.put(testTable.getDataId(), model.getTest_data_id());
        values.put(financialEventTable.getFinancialEventValue(), model.getValue());
        values.put(financialEventTable.getFinancialEventDescription(), model.getDescription());
        values.put(financialEventTable.getFinancialEventPrefix(), model.getPositive());
        values.put(financialEventTable.getFinancialEventRepeat(), model.getRepeat());
        values.put(financialEventTable.getFinancialEventDate(), model.getDate());
        values.put(financialEventTable.getFinancialEventCreated(), model.getCreated());


        long insert = db.insert(financialEventTable.getTableName(), null, values);

        return insert;

    }
    public FinancialEventModel getCashflow(int CashflowID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + financialEventTable.TABLE_NAME + " WHERE "
                + financialEventTable.getFinancialEventId() + " = " + CashflowID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        //Log.e(LOG, selectQuery);


        FinancialEventModel cashflow = new FinancialEventModel();

        cashflow.setId(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventId())));
        cashflow.setValue(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventValue())));
        cashflow.setDescription(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventDescription())));
        cashflow.setRepeat(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventRepeat())));
        cashflow.setCreated(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventCreated())));
        cashflow.setDate(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventDate())));
        cashflow.setPositive(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventPrefix())));


        return cashflow;
    }

    public List<FinancialEventModel> getAllCashflow(){
        db = this.getWritableDatabase();

        List<FinancialEventModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + financialEventTable.TABLE_NAME +" Order By "+ financialEventTable.getFinancialEventDate()+" ASC ";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                FinancialEventModel data = new FinancialEventModel();

                data.setId(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventId())));
                data.setValue(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventValue())));
                data.setDescription(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventDescription())));
                data.setPositive(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventPrefix())));
                data.setRepeat(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventRepeat())));
                data.setDate(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventDate())));
                data.setCreated(c.getString(c.getColumnIndex(financialEventTable.getFinancialEventCreated())));

                datalist.add(data);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public void deleteCashflow(String CashflowID) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(financialEventTable.TABLE_NAME, financialEventTable.getFinancialEventId() + " = ?",
                new String[] { CashflowID });
    }


}
