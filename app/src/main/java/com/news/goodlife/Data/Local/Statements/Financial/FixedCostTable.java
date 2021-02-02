package com.news.goodlife.Data.Local.Statements.Financial;

public class FixedCostTable {

    public static final String TABLE_NAME = "fixed_costs";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String IBAN = "iban";
    private static final String EXPECTED = "expected";
    private static final String AMOUNT = "amount";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";


    private static final String CREATE_FIXED_COSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IBAN + " TEXT,"
            + NAME + " TEXT,"
            + TRANSACTION_ID + " TEXT,"
            + EXPECTED + " TEXT,"
            + AMOUNT + " TEXT,"
            + START_DATE + " TEXT,"
            + END_DATE + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getID() {
        return ID;
    }

    public static String getIBAN() {
        return IBAN;
    }

    public static String getNAME() {
        return NAME;
    }

    public static String getTransactionId() {
        return TRANSACTION_ID;
    }

    public static String getEXPECTED() {
        return EXPECTED;
    }

    public static String getAMOUNT() {
        return AMOUNT;
    }

    public static String getStartDate() {
        return START_DATE;
    }

    public static String getEndDate() {
        return END_DATE;
    }

    public static String getCreateFixedCostsTable() {
        return CREATE_FIXED_COSTS_TABLE;
    }
}
