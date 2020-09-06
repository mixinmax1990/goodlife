package com.news.goodlife.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.news.goodlife.Adapters.CashFlowPagerAdapter;
import com.news.goodlife.CustomViews.BezierView;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.Financial.FinancialEventModel;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class FinancialFragment extends Fragment implements OnClickedCashflowItemListener {

    TabLayout cashflowTabs;
    TabItem timeline_tab;
    TabItem once_tab;
    TabItem regular_tab;
    ViewPager cashflowViewPager;
    CashFlowPagerAdapter adapter;
    DatabaseController db;
    ConstraintLayout cashflowPopContainer;
    ConstraintLayout financialContainer;
    ConstraintLayout popCashflowContainer;
    TextView timelineCashAmount;
    TextView timelineCashDate;

    //Constraints Animations;
    ConstraintSet baseConstraint = new ConstraintSet();
    ConstraintSet cashExpanded = new ConstraintSet();
    ConstraintSet basePopCashflow = new ConstraintSet();
    ConstraintSet showPopCashflow = new ConstraintSet();

    boolean cashIsExpanded = false;
    boolean blurOn = false;

    //Charts

    HorizontalScrollView chartscrollWindow;
    BezierView mainCashflowGraph;
    TextView maxAmount, minAmount;

    //BlurView
    BlurView blurView;

    //PopUp
    EditText cashflowAmount;
    EditText cashflowDescription;
    FrameLayout popAmountClickArea;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.financial_fragment, container, false);

        db = new DatabaseController(getContext());


        financialContainer = root.findViewById(R.id.financial_fragment_constrainlayout);
        popCashflowContainer = root.findViewById(R.id.cashflow_pop_container);
        cashflowTabs = root.findViewById(R.id.cashflow_tab);
        timeline_tab = root.findViewById(R.id.cashflow_upcoming_tab);
        once_tab = root.findViewById(R.id.cashflow_once_tab);
        regular_tab = root.findViewById(R.id.cashflow_regular_tab);
        cashflowViewPager = root.findViewById(R.id.cashflow_viewpager);
        cashflowPopContainer = root.findViewById(R.id.cashflow_pop_container);
        cashflowAmount = root.findViewById(R.id.popCashAmount);
        cashflowDescription = root.findViewById(R.id.popCashDesc);
        mainCashflowGraph = root.findViewById(R.id.bezier);
        maxAmount = root.findViewById(R.id.frameMaxAmount);
        minAmount = root.findViewById(R.id.frameMinAmount);
        mainCashflowGraph.setMaxAmountTV(maxAmount);
        mainCashflowGraph.setMinAmountTV(minAmount);
        timelineCashAmount = root.findViewById(R.id.chartresult_amount);
        timelineCashDate = root.findViewById(R.id.chartresult_date);

        //Cashflow Popup
        costSelector = root.findViewById(R.id.selectorCost);
        incomeSelector = root.findViewById(R.id.selectorIncome);

        cashflowPopNotifyOn = root.findViewById(R.id.cashflowPopNotifyON);
        cashflowPopNotifyOff = root.findViewById(R.id.cashflowPopNotifyOff);
        popCardSave = root.findViewById(R.id.popCardSave);
        popCardDelete = root.findViewById(R.id.popCardDelete);

        calendarView = root.findViewById(R.id.popCalendarView);
        monthlySwitch = root.findViewById(R.id.monthlySwitch);
        cashflowPopCloseField = root.findViewById(R.id.cashflowPopCloseField);
        addCashFloatingButton = root.findViewById(R.id.add_cash);
        expandCashFloatingButton = root.findViewById(R.id.expand_cash);

        baseConstraint.clone(financialContainer);
        cashExpanded.clone(getContext(), R.layout.finacial_fragment_cash_expanded);
        basePopCashflow.clone(popCashflowContainer);
        showPopCashflow.clone(getContext(), R.layout.popup_cashflow);

        blurView = root.findViewById(R.id.finance_blurview);
        startBlurring(5);

        //Charts
        chartscrollWindow = root.findViewById(R.id.chart_scroll_view);

        chartscrollWindow.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                //Log.i("Scroll", ""+scrollX);
                String cashAmount = ""+mainCashflowGraph.updateScroll(scrollX)+"€";



                //Log.i("Scroll", mainCashflowGraph.ScrollDay);
                timelineCashDate.setText(mainCashflowGraph.ScrollDay);
                timelineCashAmount.setText(cashAmount);

            }
        });





        instantiateViewPager();
        cashflowTabs.setupWithViewPager(cashflowViewPager);
        cashflowTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cashflowViewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        listeners();
        return root;
    }

    private void instantiateViewPager() {
        testDatabase();
        adapter = new CashFlowPagerAdapter(getFragmentManager(), cashflowTabs.getTabCount(), allCashflow);
        cashflowViewPager.setAdapter(adapter);

        showsCharts(allCashflow);
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private void listeners(){
        ViewTreeObserver vto = mainCashflowGraph.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainCashflowGraph.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                } else {
                    mainCashflowGraph.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
                mainCashflowGraph.growGrafAnimation();

            }
        });
        incomeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIncome();
                toggleRemoveButtonScale(true);
            }
        });

        costSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCost();
                toggleRemoveButtonScale(true);
            }
        });
        cashflowPopNotifyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyPopOn();
                toggleRemoveButtonScale(true);
            }
        });
        cashflowPopNotifyOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyPopOff();
                toggleRemoveButtonScale(true);
            }
        });
        popCardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCashflow();
            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                Date date = new Date(year-1900, month, dayOfMonth);



                cashflow_date = ""+simpleDateFormat.format(date);

                toggleRemoveButtonScale(true);

            }
        });

        monthlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                isMonthlyChecked = isChecked;
                toggleRemoveButtonScale(true);

            }
        });

        cashflowPopCloseField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleAnimateBlur(false);

            }
        });

        addCashFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCashflowInPopup();
                toogleAnimateBlur(true);


            }
        });
        expandCashFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Transition between Layout Constraint Sets
                TransitionManager.beginDelayedTransition(financialContainer);

                if(cashIsExpanded){
                    //Collapse Cash
                    baseConstraint.applyTo(financialContainer);
                    cashIsExpanded = false;
                    expandCashFloatingButton.setImageResource(R.drawable.ic_baseline_unfold_more_24);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 10 seconds
                            mainCashflowGraph.growGrafAnimation();
                        }
                    }, 500);

                }
                else{
                    //Expand Cash
                    cashExpanded.applyTo(financialContainer);
                    cashIsExpanded = true;
                    expandCashFloatingButton.setImageResource(R.drawable.ic_baseline_unfold_less_24);
                    mainCashflowGraph.flattenGrafAnimation();
                }
            }
        });

        popCardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete cashflow
                deleteCashflow();
            }
        });

        TextWatcher tt = new TextWatcher() {
            public void afterTextChanged(Editable s){

                if(s.length() > 0){
                    cashValueEditTextIsNotEmpty = true;
                }
                else{
                    cashValueEditTextIsNotEmpty = false;
                }

                selecableCashflowSave();
            }
            public void beforeTextChanged(CharSequence s,int start,int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };
        cashflowDescription.addTextChangedListener(tt);

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s){

                if(s.length() > 0){
                    cashDescEditTextIsNotEmpty = true;
                }
                else{
                    cashDescEditTextIsNotEmpty = false;
                }

                selecableCashflowSave();
            }
            public void beforeTextChanged(CharSequence s,int start,int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };
        cashflowDescription.addTextChangedListener(tw);


    }
    private boolean cashValueEditTextIsNotEmpty = false;
    private boolean cashDescEditTextIsNotEmpty = false;

    boolean isMonthlyChecked = false;
    SimpleDateFormat sdf = new SimpleDateFormat("d MMM YYYY");
    String cashflow_date;
    String cashflow_value;
    String cashflow_desc;
    String cashflow_repeat;
    String cashflow_positiv;
    String cashflow_created;



    private boolean notifyOn;
    private void notifyPopOff() {

        cashflowPopNotifyOff.setVisibility(View.VISIBLE);
        cashflowPopNotifyOn.setVisibility(View.GONE);
        notifyOn = true;
    }

    private void notifyPopOn() {

        cashflowPopNotifyOff.setVisibility(View.GONE);
        cashflowPopNotifyOn.setVisibility(View.VISIBLE);
        notifyOn = false;
    }

    private void noPopNotifcation(){
        cashflowPopNotifyOff.setVisibility(View.GONE);
        cashflowPopNotifyOn.setVisibility(View.GONE);
        notifyOn = false;
    }

    // is it Income of Expense ?
    private boolean positivCashflow = true;
    private void setIncome() {
        incomeSelector.setAlpha(1f);
        costSelector.setAlpha(.2f);
        positivCashflow = true;
    }

    private void setCost() {
        incomeSelector.setAlpha(.2f);
        costSelector.setAlpha(1f);
        positivCashflow = false;
    }

    View decorView;
    ViewGroup rootView;
    Drawable windowBackground;

    public void startBlurring(float radius) {

        radius = Math.round(radius);

        //Max 25f
        View decorView = getActivity().getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }


    public void toogleAnimateBlur(final boolean blur){

        TransitionManager.beginDelayedTransition(popCashflowContainer);
        float from, to;
        if(blur){
            //Collapse Cash
            blurView.setVisibility(View.VISIBLE);
            from = 0f;
            to = 1f;
            showPopCashflow.applyTo(popCashflowContainer);
            blurOn = true;
            mainCashflowGraph.flattenGrafAnimation();
        }
        else{
            //Expand Cash
            from = 1f;
            to = 0f;
            basePopCashflow.applyTo(popCashflowContainer);
            blurOn = false;
            mainCashflowGraph.growGrafAnimation();
        }

        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        int mDuration = 200; //in millis
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {

                float animValue = (float) animation.getAnimatedValue();

                blurView.setAlpha(animValue);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!blur){
                    blurView.setVisibility(View.GONE);
                    //popCardDelete.setScaleX(1f);
                    //popCardDelete.setScaleY(1f);
                    toggleRemoveShow(false);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        va.start();
    }

    public void toggleRemoveButtonScale(boolean small){
        float from, to;
        if(small){
            from = 1f;
            to = .4f;
        }
        else{
            from = .4f;
            to = 1f;
        }

        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        int mDuration = 200; //in millis
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {

                float animValue = (float) animation.getAnimatedValue();
                //popCardDelete.setScaleX(animValue);
                //popCardDelete.setScaleY(animValue);
            }
        });
        //va.setRepeatCount(5);
        va.start();

    }

    public void toggleRemoveShow(boolean show){
        if(show){
            //popCardDelete.setVisibility(View.VISIBLE);
        }
        else{
            //popCardDelete.setVisibility(View.GONE);
        }

    }

    private int saveCashflow() {

        if(cashflow_date == null){
            //GEts todays Date
            Date date = new Date();
            cashflow_date = ""+simpleDateFormat.format(date);
        }

        cashflow_value = cashflowAmount.getText().toString();
        if(cashflow_value.isEmpty()){
            return 1;
        }
        cashflow_desc = cashflowDescription.getText().toString();
        if(cashflow_desc.isEmpty()){
            return 2;
        }

        if(isMonthlyChecked){
            cashflow_repeat = "true";
        }
        else{
            cashflow_repeat = "false";
        }

        if(positivCashflow){
            cashflow_positiv = "true";
        }
        else{
            cashflow_positiv = "false";
        }

        cashflow_created = DateFormat.getTimeInstance().toString();


        // Model the DATA

        FinancialEventModel model = new FinancialEventModel();

        model.setValue(cashflow_value);
        model.setPositive(cashflow_positiv);
        model.setDescription(cashflow_desc);
        model.setRepeat(cashflow_repeat);
        model.setDate(cashflow_date);
        model.setCreated(cashflow_created);

        db.FinancialEvent.newCashflow(model);

        toogleAnimateBlur(false);

        instantiateViewPager();

        return 0;
    }

    public int cashflowID = 0;
    public void deleteCashflow(){
        if(cashflowID != 0){
            db.FinancialEvent.deleteCashflow(""+cashflowID);
            instantiateViewPager();
            toogleAnimateBlur(false);
            //popCardDelete.setScaleX(1f);
            //popCardDelete.setScaleX(1f);
        }

    }
    private boolean cashflowSavable;

    public void selecableCashflowSave(){
        if(cashValueEditTextIsNotEmpty){
            if(cashDescEditTextIsNotEmpty){

                popCardSave.setAlpha(.8f);
            }
        }
        else{
            popCardSave.setAlpha(.2f);
        }

    }

    public void inputCashflowInPopup(int CashflowID){
        FinancialEventModel model = new FinancialEventModel();
        model = db.FinancialEvent.getCashflow(CashflowID);

        cashflowAmount.setText(model.getValue());
        cashflowDescription.setText(model.getDescription());
        if(model.getPositive() == "true"){
            setIncome();
        }
        else{
            setCost();
        }
        if(model.getRepeat().equals("true")){
            monthlySwitch.setChecked(true);
            Log.i("SwitchState", ""+model.getRepeat());
        }
        else{
            Log.i("SwitchState", ""+model.getRepeat());
            monthlySwitch.setChecked(false);
        }

        try {
            calendarView.setDate(new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy").parse(model.getDate()).getTime(), true, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void resetCashflowInPopup(){

        cashflowAmount.setText("");
        cashflowDescription.setText("");
        setIncome();
        monthlySwitch.setChecked(false);
    }
    @Override
    public void onResume() {

        //cashflowAmount.requestFocus();
        super.onResume();

    }

    List<FinancialEventModel> allCashflow;

    private void testDatabase(){

        //Log.i("Success", ""+db.Cashflow.newCashflow(model));

        allCashflow = db.FinancialEvent.getAllCashflow();

        //Log.d("list", list.get(0).value.toString());
        //Log.d("size", ""+list.size());

        for(FinancialEventModel cashflow: allCashflow){
            Log.i("CashflowID", ""+cashflow.getId());

        }

        //db.Cashflow.deleteCashflow("2");

        //Log.d("size after", ""+list.size());

    }


    public void onItemClickListener(@NonNull Fragment fragment) {
        if (fragment instanceof CashflowTimelineFragment) {
            CashflowTimelineFragment cashflowTimelineFragment = (CashflowTimelineFragment) fragment;
            cashflowTimelineFragment.setCallback(this);
        }
    }

    @Override
    public void onCashflowItemClicked(int position) {


            //toogleAnimateBlur(true);

    }


    // Cashflow Popup

    TextView costSelector;
    TextView incomeSelector;


    AppCompatImageButton cashflowPopNotifyOn;
    AppCompatImageButton cashflowPopNotifyOff;

    CardView popCardSave;
    CardView popCardDelete;

    CalendarView calendarView;
    Switch monthlySwitch;
    TextView cashflowPopCloseField;

    FloatingActionButton addCashFloatingButton;
    FloatingActionButton expandCashFloatingButton;




    //Financial  Charts



    private void showsCharts(List<FinancialEventModel> allCashflow) {




    }


}
