package com.news.goodlife.Data.Local.Statements.Financial;

public class BudgetCategoryTable {

    public static final String TABLE_NAME = "budgetcat";

    private static final String BUDGET_CATEGORY_ID = "budget_category_id";
    private static final String BUDGET_CATEGORY_NAME = "budget_category_name";
    private static final String BUDGET_CATEGORY_ICON = "budget_category_icon";
    private static final String BUDGET_CATEGORY_COLOR = "budget_category_color";

    private static final String CREATE_BUDGET_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + BUDGET_CATEGORY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BUDGET_CATEGORY_NAME + " TEXT,"
            + BUDGET_CATEGORY_ICON + " TEXT,"
            + BUDGET_CATEGORY_COLOR + " TEXT"+
            ")";

    public static String getCreateBudgetCategoryTable() {
        return CREATE_BUDGET_CATEGORY_TABLE;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getBudgetCategoryId() {
        return BUDGET_CATEGORY_ID;
    }

    public static String getBudgetCategoryName() {
        return BUDGET_CATEGORY_NAME;
    }

    public static String getBudgetCategoryIcon() {
        return BUDGET_CATEGORY_ICON;
    }

    public static String getBudgetCategoryColor() {
        return BUDGET_CATEGORY_COLOR;
    }
}
