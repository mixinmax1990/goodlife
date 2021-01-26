package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WalletDayCover extends ConstraintLayout {
    String account_balance, available_cash, remaining_budget, income, expense, goal_progress;
    int bank_activities;
    boolean alert;

    boolean dayset = false;
    public void setDay(String account_balance, String available_cash, String remaining_budget, String income, String expense, String goal_progress, int bank_activities, boolean alert){

        setAccount_balance(account_balance);
        setAvailable_cash(available_cash);
        setRemaining_budget(remaining_budget);
        setIncome(income);
        setExpense(expense);
        setGoal_progress(goal_progress);
        setBank_activities(bank_activities);
        setAlert(alert);

        dayset = true;

        invalidate();
    }

    RectF frameRect = new RectF(0,0,0,0);

    public WalletDayCover(@NonNull Context context) {
        super(context);
    }

    public WalletDayCover(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();
    }

    Paint text, title;
    private void setPaints() {
        text = new Paint();
        text.setStyle(Paint.Style.FILL);
        text.setColor(Color.WHITE);
        text.setAntiAlias(true);
        text.setTextAlign(Paint.Align.CENTER);


        int alpha = (int)(255f * 0.7f);
        title = new Paint();
        title.setStyle(Paint.Style.FILL);
        title.setColor(Color.WHITE);
        title.setAlpha(alpha);
        title.setAntiAlias(true);
        title.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(dayset){
            canvas.drawText("Day Testing", getWidth()/2, getHeight()/2,text);
        }
    }

    public String getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(String account_balance) {
        this.account_balance = account_balance;
    }

    public String getAvailable_cash() {
        return available_cash;
    }

    public void setAvailable_cash(String available_cash) {
        this.available_cash = available_cash;
    }

    public String getRemaining_budget() {
        return remaining_budget;
    }

    public void setRemaining_budget(String remaining_budget) {
        this.remaining_budget = remaining_budget;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getGoal_progress() {
        return goal_progress;
    }

    public void setGoal_progress(String goal_progress) {
        this.goal_progress = goal_progress;
    }

    public int getBank_activities() {
        return bank_activities;
    }

    public void setBank_activities(int bank_activities) {
        this.bank_activities = bank_activities;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}
