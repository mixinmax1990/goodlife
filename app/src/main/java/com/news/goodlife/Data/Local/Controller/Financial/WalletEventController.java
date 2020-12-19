package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Data.Local.Models.WalletEventDayOrderModel;
import com.news.goodlife.Data.Local.Statements.Financial.WalletEventTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WalletEventController extends DatabaseHelper {

    private WalletEventTable walletEventTable;
    private SQLiteDatabase db;
    private ContentValues values;
    public WalletEventController(Context context) {
        super(context);

        db = this.getWritableDatabase();
        walletEventTable = new WalletEventTable();
        db.execSQL(walletEventTable.getCreateFinancialTableEvent());
    }

    public long newCashflow(WalletEventModel model){
        db = this.getWritableDatabase();

        values = new ContentValues();
        //values.put(testTable.getDataId(), model.getTest_data_id());
        values.put(walletEventTable.getFinancialEventValue(), model.getValue());
        values.put(walletEventTable.getFinancialEventDescription(), model.getDescription());
        values.put(walletEventTable.getFinancialEventPrefix(), model.getPositive());
        values.put(walletEventTable.getFinancialEventRepeat(), model.getRepeat());
        values.put(walletEventTable.getFinancialEventDate(), model.getDate());
        values.put(walletEventTable.getFinancialEventCreated(), model.getCreated());


        long insert = db.insert(walletEventTable.getTableName(), null, values);

        return insert;

    }

    public WalletEventModel getCashflow(int CashflowID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + walletEventTable.TABLE_NAME + " WHERE "
                + walletEventTable.getFinancialEventId() + " = " + CashflowID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        //Log.e(LOG, selectQuery);


        WalletEventModel cashflow = new WalletEventModel();

        cashflow.setId(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventId())));
        cashflow.setValue(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventValue())));
        cashflow.setDescription(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDescription())));
        cashflow.setRepeat(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventRepeat())));
        cashflow.setCreated(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventCreated())));
        cashflow.setDate(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDate())));
        cashflow.setPositive(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventPrefix())));


        return cashflow;
    }

    public List<WalletEventModel> getAllCashflow(){
        db = this.getWritableDatabase();

        List<WalletEventModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + walletEventTable.TABLE_NAME +" Order By "+ walletEventTable.getFinancialEventDate()+" ASC ";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                WalletEventModel data = new WalletEventModel();

                data.setId(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventId())));
                data.setValue(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventValue())));
                data.setDescription(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDescription())));
                data.setPositive(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventPrefix())));
                data.setRepeat(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventRepeat())));
                data.setDate(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDate())));
                data.setCreated(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventCreated())));

                data.setDateOBJ(getDateFromTimestanp(data.getDate()));

                datalist.add(data);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public JSONObject getAllByRange(Date firstDay){


        JSONObject dayData = new JSONObject();

        db = this.getWritableDatabase();



        String selectQuery = "SELECT * FROM " + walletEventTable.TABLE_NAME +" Order By "+ walletEventTable.getFinancialEventDate()+" ASC ";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                WalletEventModel data = new WalletEventModel();

                data.setId(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventId())));
                data.setValue(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventValue())));
                data.setDescription(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDescription())));
                data.setPositive(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventPrefix())));
                data.setRepeat(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventRepeat())));
                data.setDate(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventDate())));
                data.setCreated(c.getString(c.getColumnIndex(walletEventTable.getFinancialEventCreated())));

                data.setDateOBJ(getDateFromTimestanp(data.getDate()));

                //Remove Time From Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dateWithoutTime = null;
                try {
                    dateWithoutTime = sdf.parse(sdf.format(data.DateOBJ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateOnly = ""+dateWithoutTime.getTime();
                //int daysSince = daysSinceFirst(firstDay, data.getDateOBJ());

                //check JsonObject if The day is there
                boolean exists = dayData.has(dateOnly);
                if(exists){
                    try {
                        //WalletEventDayOrderModel test = dayData.get(""+daysSince);
                        ((WalletEventDayOrderModel)dayData.get(dateOnly)).getDaysData().add(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{

                    List<WalletEventModel> day = new ArrayList<>();
                    day.add(data);
                    WalletEventDayOrderModel dayDataHolder = new WalletEventDayOrderModel();
                    //Set The Data Holder
                    dayDataHolder.setDaysData(day);
                    dayDataHolder.setPosition(1);
                    try {
                        dayData.put(dateOnly, dayDataHolder);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //if yes access the Day

                //orderedDatalist.add(data);
            }
            while(c.moveToNext());
        }

        return dayData;
    }

    private int daysSinceFirst(Date firstDay, Date thisDay) {

        long diff = thisDay.getTime() - firstDay.getTime();

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private Date getDateFromTimestanp(String date) {
        long Timestamp = Long.parseLong(date);
        Date returnDate = new Date();
        returnDate.setTime(Timestamp);

        return returnDate;
    }

    public boolean deleteCashflow(String CashflowID) {

        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(walletEventTable.TABLE_NAME, walletEventTable.getFinancialEventId() + " = ?",
                    new String[]{CashflowID});
            success = true;
        }
        catch (SQLException e) {
            success = false;
        }
        return success;
    }

    public boolean deleteAllEvents() {

        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();


        try {
            db.execSQL("delete from "+ walletEventTable.TABLE_NAME);
            success = true;
        }
        catch (SQLException e) {
            success = false;
        }
        return success;
    }


}
