package com.news.goodlife.Functions;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.news.goodlife.CustomViews.CustomBezierGraph;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Models.DayCashflowModel;
import com.news.goodlife.Models.MultiDayCashflowModel;
import com.news.goodlife.Processing.Models.DayDataModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class InflateDayDetails {

    View root;
    //Budget Containers
    ViewGroup monthFlex;
    TextView day_weekday, day_date, day_available_cash, day_remaining_budget, day_expenses, day_income, day_savings_reserved, day_savings_available;
    //Load a Data object holding the Information for the day

    ViewGroup parentCont;
    DayDataModel dayData;
    View cover;

    int parent_height;

    SuccessCallback successCallback;
    AsyncLayoutInflater inflater;
    SingletonClass singletonClass = SingletonClass.getInstance();
    LayoutInflater inflaterNormal;

    public InflateDayDetails(LayoutInflater normalInflater, AsyncLayoutInflater inflator, ViewGroup parent, View cover, @Nullable DayDataModel dayData, SuccessCallback successCallback) {

        this.inflaterNormal = normalInflater;
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

            //Draw th Week Graph
            CustomBezierGraph weekGraph = view.findViewById(R.id.week_graph);
            weekGraph.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    weekGraph.setMultiDayData(testBezier(), new Date());
                    weekGraph.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });


            Log.i("AsyncInflation", "Done");
            monthFlex = view.findViewById(R.id.month_flex);

            day_weekday = view.findViewById(R.id.item_day);
            day_date = view.findViewById(R.id.item_date);
            setDate();

            day_available_cash = view.findViewById(R.id.available_amount);

            day_expenses = view.findViewById(R.id.outgoing_value);
            day_income = view.findViewById(R.id.incoming_value);

            day_savings_available = view.findViewById(R.id.saving_module_amount);


            //Inflate Budgets Async separately after data is Loaded

            loadData();
            loadFixed(view);


            module_rev = view.findViewById(R.id.module_status_overview);
            module_exp = view.findViewById(R.id.fixed_costs_module);
            module_inc = view.findViewById(R.id.events_incoming);
            module_burn = view.findViewById(R.id.cashcat_frame);
            module_budg = view.findViewById(R.id.budget_main_container);
            module_trans = view.findViewById(R.id.cashflow_activities_container);
            module_savings = view.findViewById(R.id.savings_module);
            module_date = view.findViewById(R.id.date_container);

            ViewGroup transactionCont = view.findViewById(R.id.flex_trans_cont);
            inflateTransactions(transactionCont, dayData);

            rootheight = view.getHeight();
            parentCont.addView(view);

            listeners();



        }
    };


    private MultiDayCashflowModel testBezier() {
        MultiDayCashflowModel  multidaysData = new MultiDayCashflowModel();
        DayCashflowModel dayData;
        Date testDate = new Date();
        //create 7 Days Test Data
        dayData = new DayCashflowModel(testDate,2000);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1400);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,600);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1200);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1700);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,2500);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1600);
        multidaysData.addDayCashflow(dayData);

        return multidaysData;

    }
    int iterationCount = 1;
    //View rootTrans;
    private void inflateTransactions(ViewGroup transactionCont, DayDataModel dayDataModel){
        int noOfTransactions = dayDataModel.getDayTransactionsModel().getDaysTransactions().size();
        Log.i("Nof", " - "+noOfTransactions);


        if(noOfTransactions == 0){

        }
        else{

            for(TransactionModel transaction: dayDataModel.getDayTransactionsModel().getDaysTransactions()){

                int layout;

                if(transaction.getType().equals("DEBIT")){
                    layout = R.layout.transaction_item_debit;
                }
                else{
                    layout = R.layout.transaction_item_credit;
                }


                View rootTrans = inflaterNormal.inflate(layout, transactionCont, true);
                rootTrans = transactionCont.getChildAt(transactionCont.getChildCount() - 1);
                TextView amount = rootTrans.findViewById(R.id.transaction_amount);
                TextView description = rootTrans.findViewById(R.id.transaction_description);
                Log.i("HashCodeView",""+rootTrans.hashCode()+" No of Children:" + transactionCont.getChildCount());
                amount.setText(singletonClass.monefy(transaction.getAmount()));
                description.setText(transaction.getReference());
                //((TextView)root.findViewById(R.id.transaction_amount)).setText(singletonClass.monefy(transaction.getAmount()));
                //((TextView)root.findViewById(R.id.transaction_description)).setText(transaction.getReference());

                /*
                inflater.inflate(layout, transactionCont, new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                        ((TextView)view.findViewById(R.id.transaction_amount)).setText(singletonClass.monefy(transaction.getAmount()));
                        ((TextView)view.findViewById(R.id.transaction_description)).setText(transaction.getReference());

                        parent.addView(view);

                        iterationCount ++;
                        if(iterationCount == noOfTransactions){

                            //enable inflation


                        }

                    }
                });*/


            }
        }

    }


    View module_date, module_rev, module_exp, module_inc, module_burn, module_budg, module_trans, module_savings;
    float mdate_top, mrev_top, mexp_top, minc_top, mburn_top, mbudg_top, mtrans_top, mgoal_top, msavings_top;
    private void baseCollapseDay() {

        //mrev_top = module_rev.getY();
        //mright_top = module_right_up.getY();
        //mbudg_top = module_budg.getTop();
        //mtrans_top = module_trans.getTop();
        //mgoal_top = module_goal.getTop();
        msavings_top = module_savings.getY();
        mtrans_top = module_trans.getY();
        mbudg_top = module_budg.getY();
        mexp_top = module_exp.getY();
        minc_top = module_inc.getY();
        mburn_top = module_burn.getY();
        mrev_top = module_rev.getY();
        mdate_top = module_date.getY();

        module_savings.setY(0);
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
        dayCal.setTime(dayData.getDayDate());

        String dayName = dayCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        day_weekday.setText(dayName);

        String dayDate = dayCal.get(Calendar.DAY_OF_MONTH)+" "+dayCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+dayCal.get(Calendar.YEAR);
        day_date.setText(dayDate);

    }

    IconDoughnutView fixedCost, fixedIncome, savings;
    private void loadFixed(View root) {

        //InflateDayFixedCosts AsyncFixed = new InflateDayFixedCosts(inflater, monthFlex);
        fixedCost = root.findViewById(R.id.fixed_cost_doughnut);
        fixedCost.setCategory("#FFFFFF", null);

        fixedIncome = root.findViewById(R.id.fixed_income_doughnut);
        fixedIncome.setCategory("#FFFFFF", null);

        savings = root.findViewById(R.id.savings_doughnut);
        savings.setCategory("#FFFFFF", null);


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

        if(inflated == 8){


                //If all Transactions are Loaded them Expand the Layout
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
                module_trans.setY(mtrans_top * animVal);
                module_budg.setY(mbudg_top * animVal);
                module_exp.setY(mexp_top * animVal);
                module_inc.setY(minc_top * animVal);
                module_burn.setY(mburn_top * animVal);
                module_rev.setY(mrev_top * animVal);
                module_date.setY(mdate_top * animVal);

                root.setAlpha(animVal);

                //cover.setAlpha(1f - animVal);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            cover.setAlpha(0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


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
