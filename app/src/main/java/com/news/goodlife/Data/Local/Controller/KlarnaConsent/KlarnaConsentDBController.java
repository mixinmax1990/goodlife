package com.news.goodlife.Data.Local.Controller.KlarnaConsent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Statements.KlarnaConsent.KlarnaConsentTable;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;

public class KlarnaConsentDBController extends DatabaseHelper {

    private KlarnaConsentTable consentTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public KlarnaConsentDBController(Context context) {
        super(context);

        db = this.getWritableDatabase();
        consentTable = new KlarnaConsentTable();
        db.execSQL(consentTable.getCreateConsentTable());
    }
    public boolean updateConsent(POSTgetConsentDataModel consent){
        //Check if there is a consent

        if(getConsent() == null){
            setConsent(consent);
            Log.i("Get Consent", "See WOOOOOORKS "+getConsent().getData().getConsent_token());
        }
        else{
            deleteConsent();
            setConsent(consent);
            Log.i("Get Consent", "See WOOOOOORKS "+getConsent().getData().getConsent_token());
        }

        return true;

    }

    public long setConsent(POSTgetConsentDataModel consentModel){
        db = this.getWritableDatabase();

        values = new ContentValues();

        values.put(consentTable.getKlarnaConsentId(), consentModel.getData().getConsent_id());
        values.put(consentTable.getKlarnaConsentToken(), consentModel.getData().getConsent_token());

        long insert = db.insert(consentTable.getTableName(), null, values);

        return insert;
    }

    public POSTgetConsentDataModel getConsent(){
        POSTgetConsentDataModel consent = new POSTgetConsentDataModel();
        consent.setData();

        String selectQuery = "SELECT * FROM "+ consentTable.getTableName() + " LIMIT 1 ";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            consent.getData().setConsent_id(c.getString(c.getColumnIndex(consentTable.getKlarnaConsentId())));
            consent.getData().setConsent_token(c.getString(c.getColumnIndex(consentTable.getKlarnaConsentToken())));

        } else
        {
            // I AM EMPTY
            consent = null;
        }

        return consent;
    }

    public boolean deleteConsent(){

        boolean success;
        db = this.getWritableDatabase();

        try{
            db.execSQL("DELETE FROM "+ consentTable.getTableName());
            success = true;
        }
        catch (SQLException e){
            success = false;
        }

        return success;

    }


}
