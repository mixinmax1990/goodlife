package com.news.goodlife.Data.Local.Models.Financial;

public class FinancialProfileModel {
    public String id, name, initial_value, initial_date, target_value, target_deadline;

    public FinancialProfileModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitial_value() {
        return initial_value;
    }

    public void setInitial_value(String initial_value) {
        this.initial_value = initial_value;
    }

    public String getInitial_date() {
        return initial_date;
    }

    public void setInitial_date(String initial_date) {
        this.initial_date = initial_date;
    }

    public String getTarget_value() {
        return target_value;
    }

    public void setTarget_value(String target_value) {
        this.target_value = target_value;
    }

    public String getTarget_deadline() {
        return target_deadline;
    }

    public void setTarget_deadline(String target_deadline) {
        this.target_deadline = target_deadline;
    }
}
