package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.Data.Local.Statements.Financial.BudgetTable;

import java.util.ArrayList;
import java.util.List;


public class BudgetController extends DatabaseHelper {

    BudgetTable budgetTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public BudgetController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        budgetTable = new BudgetTable();
        db.execSQL(budgetTable.getCreateBudgetTable());

    }

    public long addBudget(BudgetModel model){
        values = new ContentValues();

        values.put(budgetTable.getBudgetcatId(), model.getCategoryid());
        values.put(budgetTable.getBudgetName(), model.getName());
        values.put(budgetTable.getMONTHS(), model.getMonths());
        values.put(budgetTable.getREPEAT(), model.getRepeat());
        values.put(budgetTable.getCOVERAGE(), model.getCoverage());
        values.put(budgetTable.getAMOUNT(), model.getAmount());
        values.put(budgetTable.getFREQUENCY(), model.getFrequency());
        values.put(budgetTable.getSTARTDATE(), model.getStartdate());
        values.put(budgetTable.getENDDATE(), model.getEnddate());

        long insert = db.insert(budgetTable.getTableName(), null, values);
        return insert;

    }

    public BudgetModel getBudget(int budgetID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + budgetTable.getTableName() + " WHERE " + budgetTable.getBudgetId() + " = " + budgetID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();

        }

        BudgetModel budget = new BudgetModel();

        budget.setId(c.getString(c.getColumnIndex(budgetTable.getBudgetId())));
        budget.setName(c.getString(c.getColumnIndex(budgetTable.getBudgetName())));
        budget.setMonths(c.getString(c.getColumnIndex(budgetTable.getMONTHS())));
        budget.setRepeat(c.getString(c.getColumnIndex(budgetTable.getREPEAT())));
        budget.setCoverage(c.getString(c.getColumnIndex(budgetTable.getCOVERAGE())));
        budget.setAmount(c.getString(c.getColumnIndex(budgetTable.getAMOUNT())));
        budget.setCategoryid(c.getString(c.getColumnIndex(budgetTable.getBudgetcatId())));
        budget.setFrequency(c.getString(c.getColumnIndex(budgetTable.getFREQUENCY())));
        budget.setStartdate(c.getString(c.getColumnIndex(budgetTable.getSTARTDATE())));
        budget.setEnddate(c.getString(c.getColumnIndex(budgetTable.getENDDATE())));

        return budget;

    }

    public List<BudgetModel> getAllBudgets(){
        db = this.getWritableDatabase();

        List<BudgetModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + budgetTable.TABLE_NAME;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                BudgetModel budget = new BudgetModel();

                budget.setId(c.getString(c.getColumnIndex(budgetTable.getBudgetId())));
                budget.setName(c.getString(c.getColumnIndex(budgetTable.getBudgetName())));
                budget.setMonths(c.getString(c.getColumnIndex(budgetTable.getMONTHS())));
                budget.setRepeat(c.getString(c.getColumnIndex(budgetTable.getREPEAT())));
                budget.setCoverage(c.getString(c.getColumnIndex(budgetTable.getCOVERAGE())));
                budget.setAmount(c.getString(c.getColumnIndex(budgetTable.getAMOUNT())));
                budget.setCategoryid(c.getString(c.getColumnIndex(budgetTable.getBudgetcatId())));
                budget.setFrequency(c.getString(c.getColumnIndex(budgetTable.getFREQUENCY())));
                budget.setStartdate(c.getString(c.getColumnIndex(budgetTable.getSTARTDATE())));
                budget.setEnddate(c.getString(c.getColumnIndex(budgetTable.getENDDATE())));

                datalist.add(budget);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public List<BudgetModel> getAllBudgetsByCategory(String category_id){
        db = this.getWritableDatabase();

        List<BudgetModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + budgetTable.TABLE_NAME + " WHERE " + budgetTable.getBudgetcatId()+ " = " + category_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                BudgetModel budget = new BudgetModel();

                budget.setId(c.getString(c.getColumnIndex(budgetTable.getBudgetId())));
                budget.setName(c.getString(c.getColumnIndex(budgetTable.getBudgetName())));
                budget.setMonths(c.getString(c.getColumnIndex(budgetTable.getMONTHS())));
                budget.setRepeat(c.getString(c.getColumnIndex(budgetTable.getREPEAT())));
                budget.setCoverage(c.getString(c.getColumnIndex(budgetTable.getCOVERAGE())));
                budget.setAmount(c.getString(c.getColumnIndex(budgetTable.getAMOUNT())));
                budget.setCategoryid(c.getString(c.getColumnIndex(budgetTable.getBudgetcatId())));
                budget.setFrequency(c.getString(c.getColumnIndex(budgetTable.getFREQUENCY())));
                budget.setStartdate(c.getString(c.getColumnIndex(budgetTable.getSTARTDATE())));
                budget.setEnddate(c.getString(c.getColumnIndex(budgetTable.getENDDATE())));

                datalist.add(budget);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public boolean deleteBudget(String budgetID){
        boolean success;
        db = this.getWritableDatabase();

        try{
            db.delete(budgetTable.TABLE_NAME, budgetTable.getBudgetId() + " = ? ", new String[]{budgetID});
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        return success;
    }

    public boolean deleteAllBudgets(){
        boolean success;
        db = this.getWritableDatabase();

        try{
            db.execSQL("delete from"+ budgetTable.TABLE_NAME);
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        return success;
    }
}
