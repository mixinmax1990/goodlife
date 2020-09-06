package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.FinancialProfileModel;
import com.news.goodlife.Data.Local.Statements.Financial.FinancialProfileTable;

import java.util.ArrayList;
import java.util.List;

public class FinancialProfileController extends DatabaseHelper {

    private FinancialProfileTable financialProfileTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public FinancialProfileController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        db.execSQL(financialProfileTable.getCreateFinancialProfileTable());
    }

    public long newFinancialProfile(FinancialProfileModel model){

        values = new ContentValues();

        values.put(financialProfileTable.getFinancialProfileName(), model.getName());
        values.put(financialProfileTable.getFinancialProfileInitialValue(), model.getInitial_value());
        values.put(financialProfileTable.getFinancialProfileInitialDate(), model.getInitial_date());
        values.put(financialProfileTable.getFinancialProfileTargetValue(), model.getTarget_value());
        values.put(financialProfileTable.getFinancialProfileTargetDeadline(), model.getTarget_deadline());

        long insert = db.insert(financialProfileTable.getTableName(), null, values);

        return insert;
    }

    public FinancialProfileModel getFinancialProfile(int FinancialProfileID){

        String selectQuery = "SELECT * FROM " + financialProfileTable.getTableName()+ " WHERE "
                + financialProfileTable.getFinancialProfileId() + " = " + FinancialProfileID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        FinancialProfileModel profileModel = new FinancialProfileModel();

        profileModel.setId(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileId())));
        profileModel.setName(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileName())));
        profileModel.setInitial_value(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileInitialValue())));
        profileModel.setInitial_date(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileInitialDate())));
        profileModel.setTarget_value(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileTargetValue())));
        profileModel.setTarget_deadline(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileTargetDeadline())));

        return profileModel;
    }

    public List<FinancialProfileModel> getAllProfiles(){
        List<FinancialProfileModel> datalist = new ArrayList<>();
        FinancialProfileModel profileModel = new FinancialProfileModel();

        String selectQuery = "SELECT * FROM " + financialProfileTable.getTableName() + "ORDER By" + financialProfileTable.getFinancialProfileTargetDeadline() + "ASC";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{

                profileModel.setId(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileId())));
                profileModel.setName(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileName())));
                profileModel.setInitial_value(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileInitialValue())));
                profileModel.setInitial_date(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileInitialDate())));
                profileModel.setTarget_value(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileTargetValue())));
                profileModel.setTarget_deadline(c.getString(c.getColumnIndex(financialProfileTable.getFinancialProfileTargetDeadline())));

                datalist.add(profileModel);
            }
            while(c.moveToNext());
        }
        return datalist;

    }

    public void deleteFinancialProfile(String FinancialProfileID){

        db.delete(financialProfileTable.getTableName(), financialProfileTable.getFinancialProfileId() + "= ?", new String[]{ FinancialProfileID });
    }

}
