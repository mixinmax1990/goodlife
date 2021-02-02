package com.news.goodlife.Data.Local.Models.Financial;

public class TransactionModel {

    String id, account_id, transaction_id, reference, counterparty_id, date, value_date, booking_date, state, type, method, amount, amount_currency;

    public TransactionModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCounterparty_id() {
        return counterparty_id;
    }

    public void setCounterparty_id(String counterparty_id) {
        this.counterparty_id = counterparty_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue_date() {
        return value_date;
    }

    public void setValue_date(String value_date) {
        this.value_date = value_date;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_currency() {
        return amount_currency;
    }

    public void setAmount_currency(String amount_currency) {
        this.amount_currency = amount_currency;
    }
}
