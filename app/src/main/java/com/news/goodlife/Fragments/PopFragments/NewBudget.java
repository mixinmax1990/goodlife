package com.news.goodlife.Fragments.PopFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Adapters.Recycler.BudgetPeriodAdapter;
import com.news.goodlife.CustomViews.BudgetCircle;
import com.news.goodlife.CustomViews.MarkedConstraintLayout;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.Functions.InflateDayDetails;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.Interfaces.SuccessNewBudgetCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class NewBudget extends Fragment {

    View root;
    SuccessNewBudgetCallback successNewBudgetCallback;
    String categoryID;

    ProgressBar progressBar;
    View close;
    ViewGroup content;
    SingletonClass singletonClass = SingletonClass.getInstance();
    EditText enterName;


    SeekBar seekAmount, seekAmountFine;
    TextView finemin, finemax, availableAmount, allocatedAmount;

    SeekBar coverageSeekbar;


    //Saving Budget Parameters
    boolean savable = false;
    View enterNameSignal;
    int monthsSelected;
    int budgetName;
    View saveButton;
    RadioGroup typeRB;


    public NewBudget(SuccessNewBudgetCallback successNewBudgetCallback, String categoryID) {

        this.successNewBudgetCallback = successNewBudgetCallback;
        this.categoryID = categoryID;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.budget_fragment_newbudget, container, false);

        progressBar = root.findViewById(R.id.progressbar);
        close = root.findViewById(R.id.close);
        content =  root.findViewById(R.id.content);
        saveButton = root.findViewById(R.id.save_button);
        saveButton.setAlpha(.3f);

        asyncContent();

        listeners();


        return root;
    }

    private void setSavable(){
        boolean MonthSelected, NameSet, AmountAllocated;
        MonthSelected = false;
        NameSet = false;
        AmountAllocated = false;

        //Check if Parameters are set
        if(selectedBudgetCircle != null){
            MonthSelected = true;
        }

        if(!enterName.getText().toString().equals("")){
            NameSet = true;
            if(enterNameSignal.getAlpha() == 1){
                enterNameSignal.animate().alpha(0).setDuration(400);
            }
        }
        else{
            if(enterNameSignal.getAlpha() != 1){
                enterNameSignal.animate().alpha(1).setDuration(400);
            }
        }

        if(allocated > 0){
            AmountAllocated = true;
        }


        if(MonthSelected & NameSet & AmountAllocated){
            //All Set Make Button Clickable
            savable = true;
            saveButton.setAlpha(1f);

        }
        else{
            savable = false;
            saveButton.setAlpha(.3f);
        }

    }

    RecyclerView budgetperiod_recycler;
    BudgetPeriodAdapter budgetPeriodAdapter;
    LinearLayoutManager llm;
    private void asyncContent() {

        AsyncLayoutInflater inflater = new AsyncLayoutInflater(getContext());

        inflater.inflate(R.layout.budget_fragment_newbudget_content, content, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                budgetperiod_recycler = view.findViewById(R.id.budget_period_recycler);

                enterName = view.findViewById(R.id.entername);
                enterNameSignal = view.findViewById(R.id.entername_focus);

                seekAmount = view.findViewById(R.id.seekamount);
                seekAmountFine = view.findViewById(R.id.seekamountfine);
                finemin = view.findViewById(R.id.finemin);
                finemax = view.findViewById(R.id.finemax);

                availableAmount = view.findViewById(R.id.available_amount);
                allocatedAmount = view.findViewById(R.id.allocated_amount);

                coverageSeekbar = view.findViewById(R.id.coverage_seekbar);
                coverageSeekbar.setMax(4);

                typeRB = view.findViewById(R.id.period_radiogroup);

                asyncListeners();

                budgetperiod_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int recyclerHeight = budgetperiod_recycler.getHeight();
                        int padding = ((singletonClass.getDisplayWidth() / 2) - (recyclerHeight / 2)) * 2;

                        budgetperiod_recycler.addOnScrollListener(new CustomScrollListner());

                        budgetPeriodAdapter = new BudgetPeriodAdapter(getContext(), 5, padding);
                        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        budgetperiod_recycler.setAdapter(budgetPeriodAdapter);
                        budgetperiod_recycler.setLayoutManager(llm);

                        requestEnterName();


                        budgetperiod_recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


                progressBar.setVisibility(View.GONE);
                content.addView(view);
            }
        });
    }

    String selectedcatID = "";
    String selectedStartDate = "";
    String selectedEndDate = "";
    String selectedPeriod = "";
    String selectedAmount = "";

    float allocated;
    int finerange = 50;
    int remainingBudgetVal = 1300;
    int remainingBudgetYearVal = remainingBudgetVal * 12;
    float fineminval = 0;
    float finemaxval = 50;


    boolean justAllocated = false;

    private void asyncListeners() {

        seekAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                allocated = (remainingBudgetVal / 100) * progress;
                float remaining = remainingBudgetVal - allocated;

                availableAmount.setText(castToMoney(remaining));
                allocatedAmount.setText(castToMoney(allocated));

                finerange = getRange(allocated);
                fineminval = allocated - finerange;

                finemaxval = allocated + finerange;
                finemin.setText(castToMoney(fineminval));
                finemax.setText(castToMoney(finemaxval));

                selectedAmount = ""+(allocated);

                if(selectedBudgetCircle != null){

                    selectedBudgetCircle.setAllocatedMoney((int)allocated);

                }


                justAllocated = true;
                if(allocated > 0){
                    seekAmountFine.setProgress(50);
                }
                else{
                    seekAmountFine.setProgress(0);
                    finemaxval = 50;
                    finemax.setText(castToMoney(finemaxval));

                }

                setSavable();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekAmountFine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(justAllocated){

                    justAllocated = false;
                }
                else{
                    if(fineminval > 0){
                        int val = ((finerange * 2) / 100) * i;

                        float fineallocated = (allocated - finerange) + val;
                        allocated = fineallocated;

                        float remaining = remainingBudgetVal - fineallocated;


                        availableAmount.setText(castToMoney(remaining));
                        allocatedAmount.setText(castToMoney(fineallocated));

                        selectedAmount = ""+(fineallocated);

                        if(selectedBudgetCircle != null){

                            selectedBudgetCircle.setAllocatedMoney((int)fineallocated);

                        }


                    }
                    else{
                        int val = Math.round(.5f * i);

                        float fineallocated = allocated + val;
                        float remaining = remainingBudgetVal - fineallocated;

                        allocated = fineallocated;

                        availableAmount.setText(castToMoney(remaining));
                        allocatedAmount.setText(castToMoney(fineallocated));

                        selectedAmount = ""+(fineallocated);

                    }

                }

                setSavable();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String castToMoney(float amount){

        //Two Decimal Places

        double amountDec = twoDec(amount);

        StringBuilder cash = new StringBuilder();
        String string = amountDec + "€";
        cash.append(string);

        return cash.toString();

    }

    private double twoDec(float amount) {
        double x = amount;
        double y = Math.floor(x * 100) / 100;

        return y;
    }

    private int getRange(float allocated) {
        int range;

        float rangeFrac = allocated / 2;

        if(rangeFrac > 50){
            range = 50;
        }
        else{
            range = (int)rangeFrac;
        }

        return range;
    }

    public void requestEnterName(){

        enterName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(enterName, InputMethodManager.SHOW_IMPLICIT);


        enterName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press

                    if(event.getAction() == KeyEvent.ACTION_UP){
                        //Save the new Category
                        enterName.clearFocus();
                        setSavable();
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private void listeners() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successNewBudgetCallback.error();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savable){
                    //Create Budget Model to save into DB

                    BudgetModel budget = new BudgetModel();
                    budget.setName(enterName.getText().toString());
                    budget.setMonths(""+selectedBudgetCircle.getMonths());
                    budget.setRepeat(getRepeat());
                    budget.setCoverage("null");
                    budget.setAmount(""+allocated);
                    budget.setCategoryid(categoryID);
                    budget.setEnddate("null");
                    budget.setStartdate("null");
                    budget.setFrequency("null");

                    singletonClass.getDatabaseController().BudgetController.addBudget(budget);

                    successNewBudgetCallback.success(budget);
                }
            }
        });
    }

    private String getRepeat(){


        String tag = typeRB.findViewById(typeRB.getCheckedRadioButtonId()).getTag().toString();

        return tag;


    }
    BudgetCircle selectedBudgetCircle = null;
    private class CustomScrollListner extends RecyclerView.OnScrollListener {
        public CustomScrollListner() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:


                    if(!smoothScroll){
                        centersnapSelectedChild(recyclerView);
                    }
                    else{
                        smoothScroll = false;
                    }

                    //recyclerView.smoothScrollBy();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //System.out.println("Scrolling now");

                    View child = recyclerView.getChildAt(0);
                    //Log.i("ChildCOuntREc", "test"+child.getX());

                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    // System.out.println("Scroll Settling");
                    //animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());

                    break;

            }

        }

        boolean smoothScroll = false;
        private void centersnapSelectedChild(RecyclerView recyclerView) {

            int start = selectedPeriodChild.getLeft();
            int width = selectedPeriodChild.getWidth();

            int center = start + (int)(width / 2);
            int centerScreen = singletonClass.getDisplayWidth()/2;

            int move = center - centerScreen;

            smoothScroll = true;
            recyclerView.smoothScrollBy(move, 0);
            selectedBudgetCircle = selectedPeriodChild.findViewById(R.id.budget_circle);
            selectedBudgetCircle.unimateOut();

            //selectedPeriodChild.setScaleX(.4f);
            //selectedPeriodChild.setScaleY(.4f);

            setSavable();

            Log.i("Selected Child Months", " = " +selectedBudgetCircle.getMonths());

        }


        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx > 0) {
                //System.out.println("Scrolled Right");
                conditionSelectChild(recyclerView, recyclerView.getChildAt(0), recyclerView.getChildAt(1));

            } else if (dx < 0) {
                //System.out.println("Scrolled Left");
                conditionSelectChild(recyclerView, recyclerView.getChildAt(0), recyclerView.getChildAt(1));
            } else {
                //System.out.println("No Horizontal Scrolled");
            }

            if (dy > 0) {

                // Log.i("Scrolling", "Downward");

            } else if (dy < 0) {
                // Log.i("Scrolling", "Upward");

            } else {
                //System.out.println("No Vertical Scrolled");
            }

        }


        private void conditionSelectChild(RecyclerView recyclerView, View childZero, View childOne){

            if(childZero.getX() < 0){
                dimmAll(recyclerView);
                try {
                    selectChildOne(recyclerView);
                }
                catch (Exception e){
                    selectChildZero(recyclerView);
                }

            }
            else{
                dimmAll(recyclerView);
                selectChildZero(recyclerView);
            }

        }

        private void dimmAll(RecyclerView recyclerView){
            int count = recyclerView.getChildCount();

            for(int i = 0; i < count; i++){

                recyclerView.getChildAt(i).setAlpha(0.2f);

            }
        }

        private void selectChildZero(RecyclerView recyclerView) {

            selectedPeriodChild =  recyclerView.getChildAt(0);
            selectedPeriodChild.setAlpha(1);
        }

        private void selectChildOne(RecyclerView recyclerView){

            selectedPeriodChild =  recyclerView.getChildAt(1);
            selectedPeriodChild.setAlpha(1);
        }
        View selectedPeriodChild;
    }


}
