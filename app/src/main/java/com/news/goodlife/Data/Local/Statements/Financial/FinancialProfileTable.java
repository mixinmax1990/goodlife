package com.news.goodlife.Data.Local.Statements.Financial;

public class FinancialProfileTable {

    //Table Name
    public static final String TABLE_NAME = "profile";

    //Table Content
    private static final String FINANCIAL_PROFILE_ID = "f_profile_id";
    private static final String FINANCIAL_PROFILE_NAME = "f_profile_name";
    private static final String FINANCIAL_PROFILE_INITIAL_VALUE = "f_profile_initial";
    private static final String FINANCIAL_PROFILE_INITIAL_DATE = "f_profile_initial_date";
    private static final String FINANCIAL_PROFILE_TARGET_VALUE = "f_profile_target";
    private static final String FINANCIAL_PROFILE_TARGET_DEADLINE = "f_profile_deadline";

    //Query to Create Table
    public static final String CREATE_FINANCIAL_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + FINANCIAL_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FINANCIAL_PROFILE_NAME + "TEXT"
            + FINANCIAL_PROFILE_INITIAL_VALUE + "TEXT"
            + FINANCIAL_PROFILE_INITIAL_DATE+ " TEXT"
            + FINANCIAL_PROFILE_TARGET_VALUE + " TEXT"
            + FINANCIAL_PROFILE_TARGET_DEADLINE + " TEXT" +
            ")";


    //Getters
    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getFinancialProfileId() {
        return FINANCIAL_PROFILE_ID;
    }

    public static String getFinancialProfileName() {
        return FINANCIAL_PROFILE_NAME;
    }

    public static String getFinancialProfileTargetValue() {
        return FINANCIAL_PROFILE_TARGET_VALUE;
    }

    public static String getFinancialProfileTargetDeadline() {
        return FINANCIAL_PROFILE_TARGET_DEADLINE;
    }

    public static String getCreateFinancialProfileTable() {
        return CREATE_FINANCIAL_PROFILE_TABLE;
    }

    public static String getFinancialProfileInitialValue() {
        return FINANCIAL_PROFILE_INITIAL_VALUE;
    }

    public static String getFinancialProfileInitialDate() {
        return FINANCIAL_PROFILE_INITIAL_DATE;
    }
}
