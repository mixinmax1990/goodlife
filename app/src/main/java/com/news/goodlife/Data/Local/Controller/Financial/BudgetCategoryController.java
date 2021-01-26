package com.news.goodlife.Data.Local.Controller.Financial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.news.goodlife.Data.Local.DatabaseHelper;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Statements.Financial.BudgetCategoryTable;

import java.util.ArrayList;
import java.util.List;

public class BudgetCategoryController extends DatabaseHelper {

    BudgetCategoryTable budgetCategoryTable;
    private SQLiteDatabase db;
    private ContentValues values;

    public BudgetCategoryController(@Nullable Context context) {
        super(context);

        db = this.getWritableDatabase();
        budgetCategoryTable = new BudgetCategoryTable();
        db.execSQL(budgetCategoryTable.getCreateBudgetCategoryTable());
    }

    public long addBudgetCategory(BudgetCategoryModel model){
        values = new ContentValues();

        values.put(budgetCategoryTable.getBudgetCategoryName(), model.getCatname());
        values.put(budgetCategoryTable.getBudgetCategoryChildcount(), model.getChildcount());

        long insert = db.insert(budgetCategoryTable.getTableName(), null, values);
        return insert;
    }

    public BudgetCategoryModel getBudgetCategory(int budgetCatID){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + budgetCategoryTable.getTableName() + " WHERE " + budgetCategoryTable.getBudgetCategoryId() + " = " + budgetCatID;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        BudgetCategoryModel category = new BudgetCategoryModel();

        category.setId(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryId())));
        category.setCatname(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryName())));
        category.setChildcount(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryChildcount())));

        return category;
    }

    public BudgetCategoryModel getLatestBudget(){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + budgetCategoryTable.getTableName() + " ORDER BY " + budgetCategoryTable.getBudgetCategoryId() + " DESC LIMIT 1";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            c.moveToFirst();
        }

        BudgetCategoryModel category = new BudgetCategoryModel();

        category.setId(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryId())));
        category.setCatname(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryName())));
        category.setChildcount(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryChildcount())));

        return category;
    }

    public List<BudgetCategoryModel> getAllBudgetCategories(){
        db = this.getWritableDatabase();

        List<BudgetCategoryModel> datalist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + budgetCategoryTable.TABLE_NAME;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                BudgetCategoryModel category = new BudgetCategoryModel();

                category.setId(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryId())));
                category.setCatname(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryName())));
                category.setChildcount(c.getString(c.getColumnIndex(budgetCategoryTable.getBudgetCategoryChildcount())));

                datalist.add(category);
            }
            while(c.moveToNext());
        }
        return datalist;
    }

    public boolean deleteBudgetCategory(String budgetCatID){

        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            db.delete(budgetCategoryTable.TABLE_NAME, budgetCategoryTable.getBudgetCategoryId() + " = ? ", new String[]{budgetCatID});
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        return success;
    }

    public boolean deleteAllBudgetCategories(){

        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            db.execSQL("delete from "+ budgetCategoryTable.TABLE_NAME);
            success = true;
        }
        catch(SQLException e){
            success = false;
        }

        return success;
    }
}
