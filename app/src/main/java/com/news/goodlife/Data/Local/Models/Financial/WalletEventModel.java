package com.news.goodlife.Data.Local.Models.Financial;

import java.util.Date;

public class WalletEventModel {

    public String id;
    public String value;
    public String description;
    public String positive;
    public String repeat;
    public String date;
    public String created;
    public Date DateOBJ;

    public WalletEventModel() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String add) {
        this.positive = add;
    }

    public String getDate() {
        return date;
    }

    public String getCreated() {
        return created;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Date getDateOBJ() {
        return DateOBJ;
    }

    public void setDateOBJ(Date dateOBJ) {
        DateOBJ = dateOBJ;
    }
}
