package com.news.goodlife.Data.Local.Statements.Financial;

public class BalanceTable {

    public static final String TABLE_NAME = "balances";
    private static final String ID = "id";
    private static final String ACCOUNT_ID = "account_id";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String TIMESTAMP = "timestamp";


    private static final String CREATE_BUDGET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCOUNT_ID + " TEXT,"
            + AMOUNT + " TEXT,"
            + CURRENCY + " TEXT,"
            + TIMESTAMP + " TEXT"+
            ")";


    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getID() {
        return ID;
    }

    public static String getAccountId() {
        return ACCOUNT_ID;
    }

    public static String getAMOUNT() {
        return AMOUNT;
    }

    public static String getCURRENCY() {
        return CURRENCY;
    }

    public static String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public static String getCreateBudgetTable() {
        return CREATE_BUDGET_TABLE;
    }
}
