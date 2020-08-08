package com.news.goodlife.Data.Local.Models;

public class CashflowModel {

    public String id;
    public String value;
    public String description;
    public String positive;
    public String repeat;
    public String date;
    public String created;

    public CashflowModel() {
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
}
