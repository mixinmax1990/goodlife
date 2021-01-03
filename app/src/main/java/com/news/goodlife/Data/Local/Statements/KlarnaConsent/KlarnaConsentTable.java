package com.news.goodlife.Data.Local.Statements.KlarnaConsent;

public class KlarnaConsentTable {

    //Table Name
    public static final String TABLE_NAME = "consent";

    //Table COntent
    private static final String SQLIGHT_AUTO_ID = "auto_id";
    private static final String KLARNA_CONSENT_ID = "consent_id";
    private static final String KLARNA_CONSENT_TOKEN = "consent_token";

    //Query to create Table
    public static final String CREATE_CONSENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + SQLIGHT_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KLARNA_CONSENT_ID + " TEXT,"
            + KLARNA_CONSENT_TOKEN + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getKlarnaConsentId() {
        return KLARNA_CONSENT_ID;
    }

    public static String getSqlightAutoId() {
        return SQLIGHT_AUTO_ID;
    }

    public static String getKlarnaConsentToken() {
        return KLARNA_CONSENT_TOKEN;
    }

    public static String getCreateConsentTable() {
        return CREATE_CONSENT_TABLE;
    }
}
