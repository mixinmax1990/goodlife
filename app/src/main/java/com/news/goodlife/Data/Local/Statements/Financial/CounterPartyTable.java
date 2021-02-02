package com.news.goodlife.Data.Local.Statements.Financial;

public class CounterPartyTable {

    public static final String TABLE_NAME = "counterparty";
    private static final String ID = "id";
    private static final String COUNTERPARTY_ID = "counterparty_id";
    private static final String HOLDER_NAME = "holder_name";
    private static final String ALIAS = "alias";
    private static final String ACCOUNT_NUMBER = "account_number";
    private static final String IBAN = "iban";
    private static final String STREET_ADDRESS = "street_address";
    private static final String POSTALCODE = "postalcode";
    private static final String CITY = "city";
    private static final String REGION = "region";
    private static final String COUNTRY = "country";

    private static final String CREATE_COUNTERPARTY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COUNTERPARTY_ID + " TEXT,"
            + HOLDER_NAME + " TEXT,"
            + ALIAS + " TEXT,"
            + ACCOUNT_NUMBER + " TEXT,"
            + IBAN + " TEXT,"
            + STREET_ADDRESS + " TEXT,"
            + POSTALCODE + " TEXT,"
            + CITY + " TEXT,"
            + REGION + " TEXT,"
            + COUNTRY + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getID() {
        return ID;
    }

    public static String getCounterpartyId() {
        return COUNTERPARTY_ID;
    }

    public static String getHolderName() {
        return HOLDER_NAME;
    }

    public static String getALIAS() {
        return ALIAS;
    }

    public static String getAccountNumber() {
        return ACCOUNT_NUMBER;
    }

    public static String getIBAN() {
        return IBAN;
    }

    public static String getStreetAddress() {
        return STREET_ADDRESS;
    }

    public static String getPOSTALCODE() {
        return POSTALCODE;
    }

    public static String getCITY() {
        return CITY;
    }

    public static String getREGION() {
        return REGION;
    }

    public static String getCOUNTRY() {
        return COUNTRY;
    }

    public static String getCreateCounterpartyTable() {
        return CREATE_COUNTERPARTY_TABLE;
    }
}
