package com.news.goodlife.Data.Local.Statements.Financial;

public class BudgetTable {

    public static final String TABLE_NAME = "budget";

    private static final String BUDGET_ID = "budget_id";
    private static final String BUDGETCAT_ID = "budget_category_id";
    private static final String AMOUNT = "amount";
    private static final String FREQUENCY = "frequency";
    private static final String STARTDATE = "startdate";
    private static final String ENDDATE = "enddate";

    private static final String CREATE_BUDGET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BUDGETCAT_ID + " TEXT,"
            + AMOUNT + "TEXT,"
            + FREQUENCY + "TEXT,"
            + STARTDATE + "TEXT,"
            + ENDDATE + " TEXT"+
            ")";

    public static String getCreateBudgetTable() {
        return CREATE_BUDGET_TABLE;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getBudgetId() {
        return BUDGET_ID;
    }

    public static String getBudgetcatId() {
        return BUDGETCAT_ID;
    }

    public static String getAMOUNT() {
        return AMOUNT;
    }

    public static String getFREQUENCY() {
        return FREQUENCY;
    }

    public static String getSTARTDATE() {
        return STARTDATE;
    }

    public static String getENDDATE() {
        return ENDDATE;
    }
}
