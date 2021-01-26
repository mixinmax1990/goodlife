package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.Adapters.Recycler.CashflowMainAdapter;
import com.news.goodlife.CustomViews.BubbleChartCategories;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.MarkedConstraintLayout;
import com.news.goodlife.CustomViews.MonthCashflowBezier;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Data.Local.Models.WalletEventDayOrderModel;
import com.news.goodlife.Functions.InflateDayDetails;
import com.news.goodlife.Functions.InflateSideMonthDay;
import com.news.goodlife.Interfaces.MonthCashflowBezierCallback;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.LayoutManagers.MultiDaysLinearLayoutManager;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Models.CalendarLayoutMonth;
import com.news.goodlife.Models.DayCashflowModel;
import com.news.goodlife.Models.MonthCashflowModel;
import com.news.goodlife.Models.toCalendarViewTransition;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;
import com.news.goodlife.StartActivity;
import com.news.goodlife.Transitions.DetailsTransition;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import static androidx.recyclerview.widget.RecyclerView.*;

public class WalletMultiDaysFragment extends Fragment{


    //RecylcerView
    RecyclerView cashflow_recycler;
    MultiDaysLinearLayoutManager llm;
    CashflowMainAdapter cashflowMainAdapter;
    ConstraintLayout.LayoutParams skeletonLP;
    FrameLayout popContainer;
    FrameLayout menu_container;
    public StartActivity activity;
    BlurView blurTopGraph;

    BorderRoundView slideIndicator;
    CalendarLayoutDay selectedDay;
    Date firstDayInRange;
    DatabaseController myDB;
    SingletonClass singletonClass;



    View wallet_side_container;

    public DatabaseController getMyDB() {
        return myDB;
    }

    public Date getFirstDayInRange() {
        return firstDayInRange;
    }

    public void setFirstDayInRange(Date firstDayInRange) {
        this.firstDayInRange = firstDayInRange;
    }

    public int menuTop;
    public WalletMultiDaysFragment(int menuTop, FrameLayout popContainer, FrameLayout menu_container,@Nullable CalendarLayoutDay selected, @Nullable DatabaseController DB) {
        singletonClass = SingletonClass.getInstance();
        this.menuTop = menuTop;
        this.popContainer = popContainer;
        this.menu_container = menu_container;
        this.selectedDay = selected;
        this.myDB = singletonClass.getDatabaseController();

    }
    int firstElement = 10000;
    int lastElement = 10001;
    ViewHolder visibleViewHolder;
    View root;
    JSONObject orderedData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.wallet_multi_days, container, false);
        activity = (StartActivity) getActivity();

        orderedData = myDB.WalletEvent.getAllByRange(getFirstDayInRange());
        //Get Dates
        if(selectedDay != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDay.getDate());
            //Log.i("Selected Day",""+cal.get(Calendar.DAY_OF_MONTH));

            getCalendarRange(selectedDay.getDate());
        }
        else{
            //Log.i("Selected Day", "null");
            getCalendarRange(null);
        }

        //blurTopGraph = root.findViewById(R.id.blurtopgraph);
        //blur(20, blurTopGraph);

        //Tab Sections
        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext(), popContainer, this, root, getActivity().getWindow().getDecorView(), allCalendarDays, orderedData);
        llm = new MultiDaysLinearLayoutManager(getContext());
        llm.setStackFromEnd(false);




        cashflow_recycler.setLayoutManager(llm);
        cashflow_recycler.setNestedScrollingEnabled(true);
        cashflow_recycler.setAdapter(cashflowMainAdapter);

        slideIndicator = root.findViewById(R.id.slide_indicator);

        cashflow_recycler.scrollToPosition(todayItemPosition);

        sideMonthDayHolder = root.findViewById(R.id.side_month_dayholder);
        int maxHeight = (int)(singletonClass.getDisplayHeight() * 0.6);
        sideMonthDayHolder.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sideMonthDayHolder.getLayoutParams().height = maxHeight;
                sideMonthDayHolder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        sideMonthName = root.findViewById(R.id.side_month_monthname);
        wallet_side_container = root.findViewById(R.id.wallet_side_month_container);
        wallet_side_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                wallet_side_container.setX(singletonClass.getDisplayWidth() + wallet_side_container.getWidth());
                wallet_side_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        final LiquidView[] countVisibleLiquid = new LiquidView[1];
        final TextView[] countVisibleDayNames = new TextView[1];


        cashflow_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //Log.i("FirstVisibleElement", ""+llm.findFirstVisibleItemPosition());
                if(firstElement != llm.findFirstVisibleItemPosition()){
                    firstElement = llm.findFirstVisibleItemPosition();
                    //resetWave(firstElement);

                    for(int i = llm.findFirstVisibleItemPosition(); i <= llm.findLastVisibleItemPosition(); i++){
                        //Log.i("Visible Element Loop", "Runs"+i);
                        final int x = i;
                        try {
                            visibleViewHolder = cashflow_recycler.findViewHolderForAdapterPosition(i);
                            LiquidView liquid = visibleViewHolder.itemView.findViewById(R.id.budget_liquid);
                            BubbleChartCategories bubbleChart = visibleViewHolder.itemView.findViewById(R.id.item_day_bubble_chart);
                            TextView date = visibleViewHolder.itemView.findViewById(R.id.item_day);
                            liquid.animateWave();
                            //bubbleChart.animateBubbles();
                            liquid.setTransitionName(i + "_trans");
                            date.setTransitionName(i + "_dayname");

                        }catch(Exception e){

                        }

                    }
                }
            }
        });

        listeners();

        return root;
    }


    ViewGroup sideMonthDayHolder;
    TextView sideMonthName;
    String selectedMonth = "none";
    private void setSideCalendar(int position) {

        CalendarLayoutDay selectedDay = allCalendarDays.get(position);

        if(!selectedDay.getMONTH_NUMBER().equals(selectedMonth)){
            //Draw a new Month
            Log.i("Drawing Month", "True");
            sideMonthDayHolder.removeAllViews();
            selectedMonth = selectedDay.getMONTH_NUMBER();
            drawMonthDays();
        }
        else{
            //select the Day
            Log.i("Selecting Month", "True");
            selectSideDay(selectedDay);
        }
    }

    private void drawMonthDays(){
        boolean calnameset = false;

        for(CalendarLayoutDay calDay: allCalendarDays){

            if(calDay.getMONTH_NUMBER().equals(selectedMonth) & calDay.getType().equals("day")){
                //The Day belongs to the Month
                //Asyncinflate the Day

                sideMonthName.setText(calDay.getMONTH_NAME_SHORT());

                new InflateSideMonthDay(new AsyncLayoutInflater(getContext()), sideMonthDayHolder, calDay, new SuccessCallback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void error() {

                    }
                });
            }
        }
    }

    BorderRoundView selectedDayButton = null;
    TextView selectedDayNumber = null;
    private void selectSideDay(CalendarLayoutDay selectedDay){

        if(selectedDayButton != null){
            selectedDayNumber.setTextColor(Color.WHITE);
            selectedDayButton.dynamicallySetBackgroundColor("#212226");
        }

        //Get the Date of item at that Position
        selectedDayButton = sideMonthDayHolder.findViewWithTag(selectedDay.getMONTH_DAY_NUMBER());
        selectedDayNumber = selectedDayButton.findViewById(R.id.sidemonth_daynumber);
        selectedDayButton.dynamicallySetBackgroundColor("#FFFFFF");
        selectedDayNumber.setTextColor(Color.BLACK);

    }

    public MultiDaysLinearLayoutManager getLlm() {
        return llm;
    }

    public RecyclerView getCashflow_recycler() {
        return cashflow_recycler;
    }

    private void blur(float radius, BlurView view) {
        radius = Math.round(radius);

        //Max 25f
        View decorView = activity.getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        view.setClipToOutline(true);
        view.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        view.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

    }

    public void enterNewDataEntry(WalletEventModel data, int itemPos){

        View itemRoot = root.findViewWithTag("pos_"+itemPos);
        //itemRoot.setAlpha(.3f);


        ViewGroup cashflowContainer = itemRoot.findViewById(R.id.flex_cont);

        LayoutInflater inflater = LayoutInflater.from(root.getContext());
        final View event;
        //View day = inflater.inflate(R.layout.month_wallet_day_item, root, false);

        boolean neg;
        if(data.getPositive().equals("plus")){
            neg = false;
            event = inflater.inflate(R.layout.wallet_recycler_day_cashflow_item_pos, null);
        }
        else{
            neg = true;
            event = inflater.inflate(R.layout.wallet_recycler_day_cashflow_item_neg, null);
        }

        event.setVisibility(GONE);
        event.setScaleX(.7f);
        event.setScaleY(.7f);
        event.setAlpha(0);



        String amount = (neg? "-": "+")+data.getValue();
        ((TextView)event.findViewById(R.id.amount)).setText(amount);
        ((TextView)event.findViewById(R.id.description)).setText(data.getDescription());

        //Set Index Correctly
        cashflowContainer.addView(event, 3);

        FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) event.getLayoutParams();
        lp.width = cashflowContainer.getWidth();
        event.setLayoutParams(lp);

        final ProgressBar spinner = cashflowContainer.findViewById(R.id.progress_loader);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                spinner.setVisibility(GONE);
                event.setVisibility(VISIBLE);
                event.animate().scaleY(1f).scaleX(1f).alpha(1);

            }
        }, 600);

        //Log.i("Data Received", ""+ itemPos);


    }

    static float balanceamount = 2011.11f;
    private int todayItemPosition;
    public List<CalendarLayoutDay> allCalendarDays = new ArrayList<>();
    public List<MonthCashflowModel> bezierData = new ArrayList<>();
    private void getCalendarRange(Date selectDate){

        //BezierData

        MonthCashflowModel monthCashflowModel = new MonthCashflowModel();
        bezierData.add(monthCashflowModel);

        //getThelast
        bezierData.get(bezierData.size()-1);


        //Todo make first da a Date - for now go 60 days back

        int forecastDays = 200;// * 365


        //Go X years back
        Calendar loopDay = Calendar.getInstance();
        if(selectDate != null){
            loopDay.setTime(selectDate);
        }

        //SetTime To Zero
        loopDay.set(Calendar.HOUR_OF_DAY, 0);
        loopDay.set(Calendar.MINUTE, 0);
        loopDay.set(Calendar.SECOND, 0);
        loopDay.set(Calendar.MILLISECOND, 0);
        //clear TimeZone
        loopDay.clear(Calendar.ZONE_OFFSET);

        loopDay.add(Calendar.DATE, - forecastDays);
        setFirstDayInRange(loopDay.getTime());

        //Set Name of Month for Bezier
        bezierData.get(bezierData.size()-1).setMonthName(loopDay.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        //Log.i("FirstDayInrnge",""+ getFirstDayInRange().getTime());

        //Calendar Obj of first day 10 Years back

        // Add PastDays
        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);

            //Set The Day Object
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("day", loopDay);

            //Check if there is data on the day and Change the Balance
            dayHasEvents(loopDay.getTime());
            DayCashflowModel dayCashflowModel = new DayCashflowModel(loopDay.getTime(), balanceamount);
            bezierData.get(bezierData.size()-1).addDayCashflow(dayCashflowModel);
            //Check if WEEK,MONTH,YEAR ends
            allCalendarDays.add(calendarLayoutDay);
            analysisPoints(loopDay);


        }

        allCalendarDays.get(allCalendarDays.size() - 1).setToday(true);

        todayItemPosition = allCalendarDays.size();

        //add Future Days

        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);
            //Set The Day Object
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("day", loopDay);

            dayHasEvents(loopDay.getTime());
            DayCashflowModel dayCashflowModel = new DayCashflowModel(loopDay.getTime(), balanceamount);
            bezierData.get(bezierData.size()-1).addDayCashflow(dayCashflowModel);

            //Check if WEEK,MONTH,YEAR ends
            allCalendarDays.add(calendarLayoutDay);
            analysisPoints(loopDay);

        }



        //allCalendarDates.get(0).getTime();

        //c.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        //SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        //String output = sdf1.format(c.getTime());
    }

    private void dayHasEvents(Date time) {

        WalletEventDayOrderModel dataList = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWithoutTime = null;
        try {
            dateWithoutTime = sdf.parse(sdf.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dayOnly = ""+dateWithoutTime.getTime();

        //Get The Data ordered for the Day
        if(orderedData.has(dayOnly)){
            try {
                dataList = (WalletEventDayOrderModel) orderedData.get(dayOnly);
                //Log.i("Found","entries ="+dataList.getDaysData().size());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(dataList != null){
            for(WalletEventModel daydata: dataList.getDaysData())
            {
                if(daydata.getPositive().equals("plus")){
                    //add to balance
                    balanceamount += Float.parseFloat(daydata.value);
                }
                else{
                    //remove from balance
                    balanceamount -= Float.parseFloat(daydata.value);
                }
            }
            setAmountSpectrum();
        }
    }
    float smallesAmount, largestAmount;
    private void setAmountSpectrum() {
        if(smallesAmount > balanceamount){
            smallesAmount = balanceamount;
        }

        if(largestAmount < balanceamount){
            largestAmount = balanceamount;
        }
    }

    int year, weekday, month;
    private void analysisPoints(Calendar day) {

        weekday = day.get(Calendar.DAY_OF_WEEK);
        year = day.get(Calendar.YEAR);
        month = day.get(Calendar.MONTH);



        // get NextDay

        day.add(Calendar.DATE, 1);
        if(weekday == 1){
            //ADD Weekend Analysis
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("weekend", day);
            allCalendarDays.add(calendarLayoutDay);

        }

        if(year != day.get(Calendar.YEAR)){
            //Add YearEnd Analaysis
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("yearend", day);
            allCalendarDays.add(calendarLayoutDay);

        }

        if(month != day.get(Calendar.MONTH)){

            //Add MonthEnd Analysis
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("monthend", day);
            allCalendarDays.add(calendarLayoutDay);

            CalendarLayoutMonth month = new CalendarLayoutMonth(allCalendarDays);

            //BezierData start new Month
            MonthCashflowModel monthCashflowModel = new MonthCashflowModel();
            monthCashflowModel.setMonthName(day.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            bezierData.add(monthCashflowModel);

        }
        day.add(Calendar.DATE, -1);

    }

    float visibleScreen;
    float scaleIndicator;
    public void moveIndicator(int move, int displayWidth){
        slideIndicator.setX(move);

        visibleScreen = 1 + Math.abs((float)move / displayWidth);
        slideIndicator.setAlpha(Math.abs(-1 + (float)move / displayWidth));

        scaleIndicator = 1 + Math.abs(visibleScreen - 1)*5;
        slideIndicator.setScaleX(scaleIndicator);
        slideIndicator.setScaleY(scaleIndicator);

    }
    public void resetIndicator(int pos){

        slideIndicator.setX(pos);
        slideIndicator.setAlpha(1);
        slideIndicator.setScaleY(1);
        slideIndicator.setScaleX(1);
    }

    private void resetWave(int elementPos){
        visibleViewHolder = cashflow_recycler.findViewHolderForAdapterPosition(elementPos);
        assert visibleViewHolder != null;
        LiquidView element = visibleViewHolder.itemView.findViewById(R.id.budget_liquid);
        element.resetWaveEnergy();
    }

    //Elastic Parameters
    float elTDX, elTDY, elDist, menDist;
    int elasticContWidth;
    boolean menuVisible = false;
    boolean openingMenu = false;
    boolean slidingFragment;
    long slidevelocoty = 0;
    float dir_barrier = 10f;
    boolean scrolling_horizontally = false;
    boolean direction_identified = false;
    float distX, distY;

    private void listeners() {

        cashflow_recycler.addOnScrollListener(new CustomScrollListener());
        cashflow_recycler.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                switch(e.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        slidingFragment = false;
                        elTDX = e.getX();
                        elTDY = e.getY();

                        slidevelocoty = System.currentTimeMillis();
                        //elasticEdgeView.setTouchDown((int)e.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        activity.resetScrollDist();
                        if(slidingFragment){
                            //Check how far We are and animate the rest
                            slidevelocoty = System.currentTimeMillis() - slidevelocoty;
                            activity.autoFinishSlide((int)menDist, true, slidevelocoty);
                        }
                        llm.setScrollEnabled(true);
                        distX = 0f;
                        distY = 0f;
                        direction_identified = false;
                        scrolling_horizontally = false;
                        openingMenu = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //CHeck if I have reached the directionbarrier
                        if(!direction_identified){
                            distX = Math.abs(elTDX - e.getX());
                            distY = Math.abs(elTDY - e.getY());

                            if(distX > dir_barrier){
                                //Scrolling Horizontally disable recycler scroll
                                direction_identified = true;
                                scrolling_horizontally = true;
                                elTDX = e.getX();
                                llm.setScrollEnabled(false);
                            }

                            if(distY > dir_barrier){
                                scrolling_horizontally = false;
                                direction_identified = true;
                            }
                        }
                        else{
                            if(scrolling_horizontally){
                                menDist = (elTDX - e.getRawX());
                                if(menDist >= 0){
                                    slidingFragment = true;
                                    activity.slideMechanism((int)menDist, true);
                                }
                                else{
                                    activity.slideSideMenu((int)Math.abs(menDist));
                                }
                            }
                            else{
                                llm.setScrollEnabled(true);

                                //Todo Completely remove Month Graph from Here
                                //monthGraph.setVisibility(View.VISIBLE);
                            }

                        }

                        break;
                }
                //Log.i("Touching X", "Horizontal" + e.getX());
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {



            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    boolean cashflow_menu_rendered = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    InflateDayDetails AsyncDay;
    int expandedItemNo = 0;
    boolean detailedView = false;
    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    //System.out.println("The RecyclerView is not scrolling");
                    //Log.i("FirstVisible =", "View "+llm.findFirstVisibleItemPosition());


                    if(markedWeekday){

                        if(!detailedView){
                            expandedItemNo = currentFirst;
                            detailedView = true;

                            ViewGroup detailcont = lastSelected.findViewById(R.id.day_detail_container);
                            View overview_cover = lastSelected.findViewById(R.id.overview_cover);
                            //cashflow_recycler.smoothScrollToPosition(expandedItemNo);

                            cashflow_recycler.smoothScrollBy(0, (int)lastSelected.getY());
                            //recyclerView.smooth
                            AsyncDay = new InflateDayDetails(new AsyncLayoutInflater(getContext()), detailcont, overview_cover, null, new SuccessCallback() {
                                @Override
                                public void success(){
                                    Log.i("Expand Day is ", "Inside");
                                    try{

                                        toggleSideMonth(false);
                                    }
                                    catch(Exception e){

                                    }
                                }

                                @Override
                                public void error() {

                                }
                            });
                        }
                    }


                    /*
                    monthGraph.setVisibility(GONE);


                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());
                    try{
                        View overview_cover = vh.itemView.findViewById(R.id.overview_cover);
                        //overview_cover.setVisibility(GONE);
                        ViewGroup container = vh.itemView.findViewById(R.id.day_detail_container);

                        InflateDayDetails AsyncDay;
                        AsyncDay = new InflateDayDetails(new AsyncLayoutInflater(getContext()), container, overview_cover, null);
                        if(!activity.mainMenuVisible) {
                            //activity.toggleMainMenuContainer();
                        }


                    }
                    catch(Exception e){

                    }
                    */


                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //System.out.println("Scrolling now");
                    if(activity.mainMenuVisible) {
                        //activity.toggleMainMenuContainer();
                        cashflowMainAdapter.hideEditCard();
                        //cashflowMainAdapter.hideAddCashflowEntry(false);
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    }
                    //showDisplayMenu(false);
                    animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());

                    //getTopDay();
                    //llm.findFirstVisibleItemPosition();
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                   // System.out.println("Scroll Settling");
                    //animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());

                    break;

            }

        }

        int currentFirst = 0;
        boolean markedWeekday = false;
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx > 0) {
                //System.out.println("Scrolled Right");
            } else if (dx < 0) {
                //System.out.println("Scrolled Left");
            } else {
                //System.out.println("No Horizontal Scrolled");
            }

            if (dy > 0) {

                if(!sideMenuOpened){
                    toggleSideMonth(false);
                }
               // Log.i("Scrolling", "Downward");
                int firstItem = llm.findFirstVisibleItemPosition() + 1;


                try{
                    if(currentFirst != firstItem){
                        currentFirst = firstItem;
                        ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(firstItem);

                        if(firstVH.itemView instanceof MarkedConstraintLayout){
                            markDay((MarkedConstraintLayout)firstVH.itemView);
                            markedWeekday = true;
                            setSideCalendar(currentFirst);
                        }
                        else{
                            //Not a weekday
                            unmarkDay();
                            markedWeekday = false;

                        }

                    }
                }
                catch(Exception e){

                }

            } else if (dy < 0) {

                if(!sideMenuOpened){
                    toggleSideMonth(false);
                }
                // Log.i("Scrolling", "Upward");
                int firstItem = llm.findFirstVisibleItemPosition() + 1;


                try{
                    if(currentFirst != firstItem){
                        currentFirst = firstItem;
                        ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(firstItem);

                        if(firstVH.itemView instanceof MarkedConstraintLayout){
                            markDay((MarkedConstraintLayout)firstVH.itemView);
                            markedWeekday = true;
                            setSideCalendar(currentFirst);
                        }
                        else{
                            unmarkDay();
                            markedWeekday = false;
                        }

                    }
                }
                catch(Exception e){

                }


            } else {
                //System.out.println("No Vertical Scrolled");
            }


        }

        int prevFirst;

        private void animateVisibleWaves(RecyclerView recyclerView, int first, int last){


            if(prevFirst != first){
                prevFirst = first;
                for(int i = first; i <= last; i++){
                    try {

                        ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(i);
                        SpectrumBar bar = vh.itemView.findViewById(R.id.spectrumBar);
                        LiquidView liquid = vh.itemView.findViewById(R.id.budget_liquid);
                        //bar.animateCashflow();
                        liquid.animateWave();
                    }
                    catch (Exception e){

                    }
                }
            }

        }
    }

    boolean sideMenuOpened = false;
    private void toggleSideMonth(boolean open) {
        sideMenuOpened = open;
        int start, end;
        float from, to;

        start = singletonClass.getDisplayWidth() - wallet_side_container.getWidth();
        end = singletonClass.getDisplayWidth() + wallet_side_container.getWidth();
        int dist = end - start;
        float scaleRecycler = 0.8f;
        if(open){
            from = 1;
            to = 0;
        }
        else{
            from = 0;
            to = 1;
        }

        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        va.setDuration(250);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animVal = (float)animation.getAnimatedValue();
                float moveside = start + (dist * animVal);
                float recyclerScale = 0.9f + (0.1f * animVal);
                float recyclerMove = 0 + ((singletonClass.getDisplayWidth() * 0.025f) * (1 - animVal));

                wallet_side_container.setX(moveside);

                cashflow_recycler.setScaleX(recyclerScale);
                cashflow_recycler.setScaleY(recyclerScale);
                cashflow_recycler.setX(-recyclerMove);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(markedItem){
                    lastSelected.fadeOutBorder();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }


    MarkedConstraintLayout lastSelected = null;
    boolean markedItem = false;

    private void markDay(MarkedConstraintLayout mark){
        if(detailedView){
            //Delete Expanded Details
            AsyncDay.collapseDay(new SuccessCallback() {
                @Override
                public void success() {

                }

                @Override
                public void error() {

                }
            });

            expandedItemNo = 0;
            detailedView = false;
        }

        if(lastSelected != null){
            lastSelected.selectView(false);
        }
        mark.selectView(true);
        markedItem = true;
        lastSelected = mark;

    }

    private void unmarkDay(){
        if(detailedView){
            //Delete Expanded Details

            AsyncDay.collapseDay(new SuccessCallback() {
                @Override
                public void success() {

                }

                @Override
                public void error() {

                }
            });

            expandedItemNo = 0;
            detailedView = false;
        }

        if(lastSelected != null){
            lastSelected.selectView(false);
            markedItem = false;
        }
        lastSelected = null;
    }
    private float getScreenSizeRatio(View item){

        float perc;

        float top = item.getTop();
        float bottom = item.getBottom();
        singletonClass.getDisplayHeight();

        if(top > 0){
            //The item is within the Screen
            if(bottom > singletonClass.getDisplayHeight()){
                float difference = bottom - singletonClass.getDisplayHeight();
                Log.i("difference", ""+difference);
                perc = 0f;
            }
            else{
                perc = 100f;
            }
        }
        else{
            perc = 0f;
        }
        //Determin What the screenHeight

        return perc;
    }

    public static float round(float value, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        float tmp = value * pow;
        float tmpSub = tmp - (int) tmp;

        return ( (float) ( (int) (
                value >= 0
                        ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                        : (tmpSub >= -0.5f ? tmp : tmp - 1)
        ) ) ) / pow;

        // Below will only handles +ve values
        // return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

