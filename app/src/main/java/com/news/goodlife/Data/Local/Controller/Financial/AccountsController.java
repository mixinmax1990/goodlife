package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.AccountModel;
import com.news.goodlife.Data.Local.Statements.Financial.AccountTable;
import com.news.goodlife.Data.Local.Statements.Financial.BudgetTable;

public class AccountsController extends DatabaseHelper {

    AccountTable accountTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public AccountsController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        accountTable = new AccountTable();
        db.execSQL(accountTable.getCreateAccountTable());

    }

    public long addAccount(AccountModel model){
        //CHeck if the account already Exists

        //TODO Make sure there is no identical Account

            values = new ContentValues();

            values.put(accountTable.getAccountId(), model.getId());
            values.put(accountTable.getKlarnaAccountId(), model.getKlarna_id());
            values.put(accountTable.getALIAS(), model.getAlias());
            values.put(accountTable.getAccountNumber(), model.getAccount_number());
            values.put(accountTable.getIBAN(), model.getIban());
            values.put(accountTable.getBIC(), model.getBic());
            values.put(accountTable.getBankAddressCountry(), model.getBank_address_country());
            values.put(accountTable.getTransferType(), model.getTransfer_type());
            values.put(accountTable.getAccountType(), model.getAccount_type());

            long insert = db.insert(accountTable.getTableName(), null, values);
            return insert;

    }

    public AccountModel getAccount(int accountID){

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + accountTable.getTableName() + " WHERE " + accountTable.getAccountId() + " = " + accountID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        AccountModel account = new AccountModel();

        account.setId(c.getString(c.getColumnIndex(accountTable.getAccountId())));
        account.setKlarna_id(c.getString(c.getColumnIndex(accountTable.getKlarnaAccountId())));
        account.setAlias(c.getString(c.getColumnIndex(accountTable.getALIAS())));
        account.setAccount_number(c.getString(c.getColumnIndex(accountTable.getAccountNumber())));
        account.setIban(c.getString(c.getColumnIndex(accountTable.getIBAN())));
        account.setBic(c.getString(c.getColumnIndex(accountTable.getBIC())));
        account.setBank_address_country(c.getString(c.getColumnIndex(accountTable.getBankAddressCountry())));
        account.setTransfer_type(c.getString(c.getColumnIndex(accountTable.getTransferType())));
        account.setAccount_type(c.getString(c.getColumnIndex(accountTable.getAccountType())));

        return account;
    }

    public AccountModel getAccountWithIban(String Iban){

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + accountTable.getTableName() + " WHERE " + accountTable.getIBAN() + " = " + Iban;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        AccountModel account = new AccountModel();

        account.setId(c.getString(c.getColumnIndex(accountTable.getAccountId())));
        account.setKlarna_id(c.getString(c.getColumnIndex(accountTable.getKlarnaAccountId())));
        account.setAlias(c.getString(c.getColumnIndex(accountTable.getALIAS())));
        account.setAccount_number(c.getString(c.getColumnIndex(accountTable.getAccountNumber())));
        account.setIban(c.getString(c.getColumnIndex(accountTable.getIBAN())));
        account.setBic(c.getString(c.getColumnIndex(accountTable.getBIC())));
        account.setBank_address_country(c.getString(c.getColumnIndex(accountTable.getBankAddressCountry())));
        account.setTransfer_type(c.getString(c.getColumnIndex(accountTable.getTransferType())));
        account.setAccount_type(c.getString(c.getColumnIndex(accountTable.getAccountType())));

        return account;
    }



    public boolean deleteAccount(String accountID){
        boolean success;
        db = this.getWritableDatabase();

        try{
            db.delete(accountTable.getTableName(), accountTable.getAccountId() + " = ? ", new String[]{accountID});
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        return success;
    };
}
