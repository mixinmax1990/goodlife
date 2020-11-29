package com.news.goodlife.Models;

public class BezierCurvePoint {
    Float amount, time;
    public BezierCurvePoint(Float amount, Float time){
        setAmount(amount);
        setTime(time);
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }
}