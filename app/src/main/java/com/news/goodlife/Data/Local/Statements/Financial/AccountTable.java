package com.news.goodlife.Data.Local.Statements.Financial;

public class AccountTable {

    public static final String TABLE_NAME = "account";

    private static final String ACCOUNT_ID = "account_id";
    private static final String KLARNA_ACCOUNT_ID = "klarna_account_id";
    private static final String ALIAS = "alias";
    private static final String ACCOUNT_NUMBER = "account_number";
    private static final String IBAN = "iban";
    private static final String BIC = "bic";
    private static final String BANK_ADDRESS_COUNTRY = "bank_adress_country";
    private static final String TRANSFER_TYPE = "transfer_type";
    private static final String ACCOUNT_TYPE = "account_type";

    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KLARNA_ACCOUNT_ID + " TEXT,"
            + ALIAS + " TEXT,"
            + ACCOUNT_NUMBER + " TEXT,"
            + IBAN + " TEXT,"
            + BIC + " TEXT,"
            + BANK_ADDRESS_COUNTRY + " TEXT,"
            + TRANSFER_TYPE + " TEXT,"
            + ACCOUNT_TYPE + " TEXT"+
            ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getAccountId() {
        return ACCOUNT_ID;
    }

    public static String getKlarnaAccountId() {
        return KLARNA_ACCOUNT_ID;
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

    public static String getBIC() {
        return BIC;
    }

    public static String getBankAddressCountry() {
        return BANK_ADDRESS_COUNTRY;
    }

    public static String getTransferType() {
        return TRANSFER_TYPE;
    }

    public static String getAccountType() {
        return ACCOUNT_TYPE;
    }

    public static String getCreateAccountTable() {
        return CREATE_ACCOUNT_TABLE;
    }
}
