package com.news.goodlife.Data.Local.Statements.Financial;

import java.sql.Time;

public class WalletEventTable {

    //Create Structure of Data Table

    //Table Name
    public static final String TABLE_NAME= "event";

    //Table Content
    private static final String FINANCIAL_EVENT_ID = "f_event_id";
    private static final String FINANCIAL_EVENT_PREFIX = "f_event_prefix";
    private static final String FINANCIAL_EVENT_VALUE = "f_event_value";
    private static final String FINANCIAL_EVENT_DESCRIPTION = "f_event_description";
    private static final String FINANCIAL_EVENT_REPEAT = "f_event_repeat";
    private static final String FINANCIAL_EVENT_DATE = "f_event_date";
    private static final String FINANCIAL_EVENT_STATUS = "f_event_status";
    private static final String FINANCIAL_EVENT_CREATED = "f_event_created";

    //Query to Create Table
    public static final String CREATE_FINANCIAL_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + FINANCIAL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FINANCIAL_EVENT_PREFIX + " TEXT,"
            + FINANCIAL_EVENT_VALUE + " TEXT,"
            + FINANCIAL_EVENT_DESCRIPTION + " TEXT,"
            + FINANCIAL_EVENT_REPEAT + " TEXT,"
            + FINANCIAL_EVENT_DATE + " TEXT,"
            + FINANCIAL_EVENT_CREATED + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getFinancialEventId() {
        return FINANCIAL_EVENT_ID;
    }

    public static String getFinancialEventPrefix() {
        return FINANCIAL_EVENT_PREFIX;
    }

    public static String getFinancialEventValue() {
        return FINANCIAL_EVENT_VALUE;
    }

    public static String getFinancialEventDescription() {
        return FINANCIAL_EVENT_DESCRIPTION;
    }

    public static String getFinancialEventDate() {
        return FINANCIAL_EVENT_DATE;
    }

    public static String getFinancialEventCreated() {
        return FINANCIAL_EVENT_CREATED;
    }

    public static String getCreateFinancialTableEvent() {
        return CREATE_FINANCIAL_TABLE_EVENT;
    }
    public static String getFinancialEventRepeat() {
        return FINANCIAL_EVENT_REPEAT;
    }
}
