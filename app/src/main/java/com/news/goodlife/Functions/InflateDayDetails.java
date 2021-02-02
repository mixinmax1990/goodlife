package com.news.goodlife.Functions;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.Calendar;
import java.util.Locale;


public class InflateDayDetails {

    View root;
    //Budget Containers
    ViewGroup monthFlex;
    TextView day_weekday, day_date, day_available_cash, day_remaining_budget, day_expenses, day_income, day_savings_reserved, day_savings_available;
    LiquidView available_liquid, reserved_liquid;
    //Load a Data object holding the Information for the day

    ViewGroup parentCont;
    CalendarLayoutDay dayData;
    View cover;

    int parent_height;

    SuccessCallback successCallback;
    AsyncLayoutInflater inflater;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public InflateDayDetails(AsyncLayoutInflater inflator, ViewGroup parent, View cover,@Nullable CalendarLayoutDay dayData, SuccessCallback successCallback) {

        this.inflater = inflator;
        this.parentCont = parent;
        this.successCallback = successCallback;
        inflator.inflate(R.layout.wallet_recycler_day_item, parent, callback);

        this.dayData = dayData;
        this.parent_height = parent.getHeight();
        this.cover = cover;
    }

    int rootheight;
    final AsyncLayoutInflater.OnInflateFinishedListener callback = new AsyncLayoutInflater.OnInflateFinishedListener() {
        @Override
        public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

            root = view;
            view.setAlpha(0);

            Log.i("AsyncInflation", "Done");
            monthFlex = view.findViewById(R.id.month_flex);

            day_weekday = view.findViewById(R.id.item_day);
            day_date = view.findViewById(R.id.item_date);
            setDate();

            day_available_cash = view.findViewById(R.id.available_amount);

            day_expenses = view.findViewById(R.id.outgoing_value);
            day_income = view.findViewById(R.id.incoming_value);

            day_savings_reserved = view.findViewById(R.id.goal_saving_module_amount);
            day_savings_available = view.findViewById(R.id.saving_module_amount);

            available_liquid = view.findViewById(R.id.revenue_liquid_module);
            reserved_liquid = view.findViewById(R.id.goals_liquid_module);

            //Inflate Budgets Async separately after data is Loaded

            loadData();
            loadFixed(view);


            module_rev = view.findViewById(R.id.module_status_overview);
            module_exp = view.findViewById(R.id.fixed_costs_module);
            module_inc = view.findViewById(R.id.events_incoming);
            module_burn = view.findViewById(R.id.cashcat_frame);
            module_budg = view.findViewById(R.id.budget_main_container);
            module_trans = view.findViewById(R.id.cashflow_activities_container);
            module_goal = view.findViewById(R.id.goals_module);
            module_savings = view.findViewById(R.id.savings_module);
            module_date = view.findViewById(R.id.date_container);

            rootheight = view.getHeight();
            parentCont.addView(view);

            listeners();

        }
    };


    View module_date, module_rev, module_exp, module_inc, module_burn, module_budg, module_trans, module_goal, module_savings;
    float mdate_top, mrev_top, mexp_top, minc_top, mburn_top, mbudg_top, mtrans_top, mgoal_top, msavings_top;
    private void baseCollapseDay() {

        //mrev_top = module_rev.getY();
        //mright_top = module_right_up.getY();
        //mbudg_top = module_budg.getTop();
        //mtrans_top = module_trans.getTop();
        //mgoal_top = module_goal.getTop();
        msavings_top = module_savings.getY();
        mgoal_top = module_goal.getY();
        mtrans_top = module_trans.getY();
        mbudg_top = module_budg.getY();
        mexp_top = module_exp.getY();
        minc_top = module_inc.getY();
        mburn_top = module_burn.getY();
        mrev_top = module_rev.getY();
        mdate_top = module_date.getY();

        module_savings.setY(0);
        module_goal.setY(0);
        module_trans.setY(0);
        module_budg.setY(0);
        module_exp.setY(0);
        module_inc.setY(0);
        module_burn.setY(0);
        module_rev.setY(0);
        module_date.setY(0);

    }

    private void setDate() {

        if(dayData == null)
        {
            return;
        }
        Calendar dayCal = Calendar.getInstance();
        dayCal.setTime(dayData.getDate());

        String dayName = dayCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        day_weekday.setText(dayName);

        String dayDate = dayData.getMONTH_DAY_NUMBER()+" "+dayData.getMONTH_NAME()+" "+dayData.getYEAR();
        day_date.setText(dayDate);

    }

    IconDoughnutView fixedCost, fixedIncome;
    private void loadFixed(View root) {

        //InflateDayFixedCosts AsyncFixed = new InflateDayFixedCosts(inflater, monthFlex);
        fixedCost = root.findViewById(R.id.fixed_cost_doughnut);
        fixedCost.setCategory("#FFFFFF", null);

        fixedIncome = root.findViewById(R.id.fixed_income_doughnut);
        fixedIncome.setCategory("#FFFFFF", null);


    }

    public void loadBudgets(){

        new InflateDayBudgets(inflater, monthFlex);

    }

    private void loadData() {

        //Load the Data from the Object of the Day

    }

    int inflated = 0;
    public void listeners(){

        layoutListener(module_date);
        layoutListener(module_budg);
        layoutListener(module_rev);
        layoutListener(module_exp);
        layoutListener(module_inc);
        layoutListener(module_burn);
        layoutListener(module_trans);
        layoutListener(module_savings);
        layoutListener(module_goal);


        module_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singletonClass.changeFragment.setValue("FixedModule");
            }
        });




    }

    private void layoutListener(View v){

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inflated++;
                setUpPositions();
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpPositions(){

        if(inflated == 9){
            baseCollapseDay();
            expandDay();
        }


    }
    public void expandDay(){

        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        va.setDuration(250);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animVal = (float)animation.getAnimatedValue();

                module_savings.setY(msavings_top * animVal);
                module_goal.setY(mgoal_top * animVal);
                module_trans.setY(mtrans_top * animVal);
                module_budg.setY(mbudg_top * animVal);
                module_exp.setY(mexp_top * animVal);
                module_inc.setY(minc_top * animVal);
                module_burn.setY(mburn_top * animVal);
                module_rev.setY(mrev_top * animVal);
                module_date.setY(mdate_top * animVal);

                root.setAlpha(animVal);
                cover.setAlpha(1f - animVal);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                available_liquid.animateWave();
                reserved_liquid.animateWave();
                cover.setVisibility(View.GONE);
                successCallback.success();


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.setInterpolator(new DecelerateInterpolator());

        va.start();

    }

    public void collapseDay(SuccessCallback callback){
        ValueAnimator va = ValueAnimator.ofFloat(1,0);
        va.setDuration(150);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animVal = (float)animation.getAnimatedValue();
                float animValMove;
                if(animVal < 0.3) animValMove = 0.3f;
                else animValMove = animVal;

                module_savings.setY(msavings_top * animValMove);
                module_goal.setY(mgoal_top * animValMove);
                module_trans.setY(mtrans_top * animValMove);
                module_budg.setY(mbudg_top * animValMove);
                module_exp.setY(mexp_top * animValMove);
                module_inc.setY(minc_top * animValMove);
                module_burn.setY(mburn_top * animValMove);
                module_rev.setY(mrev_top * animValMove);
                module_date.setY(mdate_top * animValMove);

                root.setAlpha(animVal);
                cover.setAlpha(1f - animVal);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                cover.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                callback.success();
                parentCont.removeAllViews();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.setInterpolator(new DecelerateInterpolator());

        va.start();

    }
}
