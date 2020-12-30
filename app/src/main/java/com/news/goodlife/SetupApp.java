package com.news.goodlife;

import android.util.Log;

import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class SetupApp {

    SingletonClass singletonClass = SingletonClass.getInstance();

    public SetupApp() {

        //checkBudgetCategories();
    }

    List<BudgetCategoryModel> budgetCatData;
    private void checkBudgetCategories() {
        budgetCatData = singletonClass.getDatabaseController().BudgetCategoryController.getAllBudgetCategories();

        Log.i("DataSize BudgetCat", ""+budgetCatData.size());
        resetBaseCategories();
        if(budgetCatData.size() == 0){

            resetBaseCategories();
        }

        Log.i("After BudgetCat", ""+budgetCatData.size());

    }

    public void resetBaseCategories() {

        //Delete all Categories
        singletonClass.getDatabaseController().BudgetCategoryController.deleteAllBudgetCategories();

        for(BudgetCategoryModel category: getBaseCategories()){

            singletonClass.getDatabaseController().BudgetCategoryController.addBudgetCategory(category);

        }

    }
    BudgetCategoryModel category;
    private List<BudgetCategoryModel> getBaseCategories(){
        List<BudgetCategoryModel> allCategories = new ArrayList<>();

        category = new BudgetCategoryModel();
        category.setCatname("Food");
        category.setCatcolor("#5affac");
        category.setCaticon("budget_icon_kitchen");

        allCategories.add(category);

        category = new BudgetCategoryModel();

        category.setCatname("Birthday");
        category.setCatcolor("#ff5a7d");
        category.setCaticon("budget_icon_cake");

        allCategories.add(category);

        category = new BudgetCategoryModel();

        category.setCatname("Clothes");
        category.setCatcolor("#ffee5a");
        category.setCaticon("budget_icon_clothes");

        allCategories.add(category);

        return allCategories;
    }




}
