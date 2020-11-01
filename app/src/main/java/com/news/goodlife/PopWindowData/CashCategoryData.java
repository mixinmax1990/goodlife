package com.news.goodlife.PopWindowData;

public class CashCategoryData {
    String categoryName, bulletPointColor, transitionName;

    public CashCategoryData(String categoryName, String bulletPointColor, String transitionName) {
        this.categoryName = categoryName;
        this.bulletPointColor = bulletPointColor;
        this.transitionName = transitionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBulletPointColor() {
        return bulletPointColor;
    }

    public void setBulletPointColor(String bulletPointColor) {
        this.bulletPointColor = bulletPointColor;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }
}
