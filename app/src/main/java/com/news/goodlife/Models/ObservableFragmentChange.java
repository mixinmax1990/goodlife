package com.news.goodlife.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ObservableFragmentChange extends Observable implements Serializable {
    private String value;
    public List<String> data = new ArrayList<>();

    public ObservableFragmentChange(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.setChanged();
        this.notifyObservers(value);
    }

    public List<String> getData() {
        return data;
    }
}
