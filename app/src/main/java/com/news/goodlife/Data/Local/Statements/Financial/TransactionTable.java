package com.news.goodlife.Data.Local.Statements.Financial;

import java.util.Currency;

public class TransactionTable {

    public static final String TABLE_NAME = "account_transaction";
    private static final String ID = "id";
    private static final String ACCOUNT_ID = "account_id";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String REFERENCE = "reference";
    private static final String COUNTERPARTY_ID = "counterparty_id";
    private static final String DATE = "date";
    private static final String VALUE_DATE = "value_date";
    private static final String BOOKING_DATE = "booking_date";
    private static final String STATE = "state";
    private static final String TYPE = "type";
    private static final String METHOD = "method";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_CURRENCY = "amount_currency";

    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCOUNT_ID + " TEXT,"
            + TRANSACTION_ID + " TEXT,"
            + REFERENCE + " TEXT,"
            + COUNTERPARTY_ID + " TEXT,"
            + DATE + " TEXT,"
            + VALUE_DATE + " TEXT,"
            + BOOKING_DATE + " TEXT,"
            + STATE + " TEXT,"
            + TYPE + " TEXT,"
            + METHOD + " TEXT,"
            + AMOUNT + " TEXT,"
            + AMOUNT_CURRENCY + " TEXT"+
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

    public static String getTransactionId() {
        return TRANSACTION_ID;
    }

    public static String getREFERENCE() {
        return REFERENCE;
    }

    public static String getCounterpartyId() {
        return COUNTERPARTY_ID;
    }

    public static String getDATE() {
        return DATE;
    }

    public static String getValueDate() {
        return VALUE_DATE;
    }

    public static String getBookingDate() {
        return BOOKING_DATE;
    }

    public static String getSTATE() {
        return STATE;
    }

    public static String getTYPE() {
        return TYPE;
    }

    public static String getMETHOD() {
        return METHOD;
    }

    public static String getAMOUNT() {
        return AMOUNT;
    }

    public static String getAmountCurrency() {
        return AMOUNT_CURRENCY;
    }

    public static String getCreateTransactionTable() {
        return CREATE_TRANSACTION_TABLE;
    }
}
