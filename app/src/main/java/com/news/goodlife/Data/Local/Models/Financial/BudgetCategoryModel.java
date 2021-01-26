package com.news.goodlife.Data.Local.Models.Financial;

public class BudgetCategoryModel {
    public String id, catname, childcount;

    public BudgetCategoryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getChildcount() {
        return childcount;
    }

    public void setChildcount(String childcount) {
        this.childcount = childcount;
    }
}
