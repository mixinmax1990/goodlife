package com.news.goodlife.Data.Local.Models.Financial;

public class AccountModel {
    String id, klarna_id, alias, account_number, iban, bic, bank_address_country, transfer_type, account_type;

    public AccountModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKlarna_id() {
        return klarna_id;
    }

    public void setKlarna_id(String klarna_id) {
        this.klarna_id = klarna_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBank_address_country() {
        return bank_address_country;
    }

    public void setBank_address_country(String bank_address_country) {
        this.bank_address_country = bank_address_country;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
