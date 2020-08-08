package com.news.goodlife.Data.Local.Statements;

public class CashflowTable {

    //Create Structure of Data Table

    //Table Name
    public static final String TABLE_NAME= "cashflow";

    //Table Content
    private static final String CASHFLOW__ID = "cashflow_id";
    private static final String CASHFLOW_POSITIV = "positiv";
    private static final String CASHFLOW_VALUE = "value";
    private static final String CASHFLOW_DESCRIPTION = "description";
    private static final String CASHFLOW_REPEAT = "repeat";
    private static final String CASHFLOW_DATE = "date";
    private static final String CASHFLOW_CREATED = "created";

    //Query to Create Table
    public static final String CREATE_TABLE_CASHFLOW = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + CASHFLOW__ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CASHFLOW_POSITIV + " TEXT,"
            + CASHFLOW_VALUE + " TEXT,"
            + CASHFLOW_DESCRIPTION + " TEXT,"
            + CASHFLOW_REPEAT + " TEXT,"
            + CASHFLOW_DATE + " TEXT,"
            + CASHFLOW_CREATED + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getCashflow_id() {
        return CASHFLOW__ID;
    }

    public static String getCashflowPositiv() {
        return CASHFLOW_POSITIV;
    }

    public static String getCashflowValue() {
        return CASHFLOW_VALUE;
    }

    public static String getCashflowDescription() {
        return CASHFLOW_DESCRIPTION;
    }

    public static String getCashflowDate() {
        return CASHFLOW_DATE;
    }

    public static String getCashflowCreated() {
        return CASHFLOW_CREATED;
    }

    public static String getCreateTableCashflow() {
        return CREATE_TABLE_CASHFLOW;

    }

    public static String getCashflowRepeat() {
        return CASHFLOW_REPEAT;
    }
}
