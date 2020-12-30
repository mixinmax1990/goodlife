package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
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
import com.news.goodlife.CustomViews.MonthCashflowBezier;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Data.Local.Models.WalletEventDayOrderModel;
import com.news.goodlife.Interfaces.MonthCashflowBezierCallback;
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

    //Account Tabs
    //TODO For testing Purposes (these will have to be created dynamically)

    FrameLayout cashflow_input_frame;


    //RecylcerView
    RecyclerView cashflow_recycler;
    MultiDaysLinearLayoutManager llm;
    CashflowMainAdapter cashflowMainAdapter;
    ElasticEdgeView elasticEdgeView;
    ConstraintLayout cashflow_display_menu;
    FrameLayout elasticContentSkeleton;
    ConstraintLayout.LayoutParams skeletonLP;
    FrameLayout popContainer;
    FrameLayout menu_container;
    View monthviewIcon;
    public StartActivity activity;
    TextView overflowDate;
    BlurView blurTopGraph;

    BorderRoundView slideIndicator;
    CalendarLayoutDay selectedDay;
    Date firstDayInRange;
    DatabaseController myDB;
    SingletonClass singletonClass;

    //Bezier Graph
    MonthCashflowBezier bezier_curve_cont;
    View monthGraph;

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



        for(MonthCashflowModel month: bezierData){
            Log.i("Month", ""+month.getMonthName());
            for(DayCashflowModel day: month.getMonthCashflow()){
                Calendar cal = Calendar.getInstance();
                cal.setTime(day.getDate());
                Log.i("Day", ""+cal.get(Calendar.DAY_OF_MONTH)+ " Amount:"+day.getAmount());
            }
        }



        Log.i("JSONOBject", ""+orderedData.toString());

        //bezier curve
        bezier_curve_cont = root.findViewById(R.id.graph_container);
        bezier_curve_cont.setBezierData(bezierData);
        bezier_curve_cont.setSmallestAmmount(smallesAmount);
        bezier_curve_cont.setLargestAmount(largestAmount);
        bezier_curve_cont.setParent(this);

        bezierMonth = root.findViewById(R.id.monthbezier_month);
        bezierBalance = root.findViewById(R.id.monthbezier_balance);
        monthGraph = root.findViewById(R.id.multidaysmonthgraph);
        //blurTopGraph = root.findViewById(R.id.blurtopgraph);
        //blur(20, blurTopGraph);

        //Tab Sections
        cashflow_input_frame = root.findViewById(R.id.cashflow_input_frame);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cashflow_input_frame.getLayoutParams();
        lp.setMargins(0,0,0, menuTop);
        cashflow_input_frame.setLayoutParams(lp);
        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext(), popContainer, this, root, getActivity().getWindow().getDecorView(), allCalendarDays, orderedData);
        llm = new MultiDaysLinearLayoutManager(getContext());
        llm.setStackFromEnd(false);




        cashflow_recycler.setLayoutManager(llm);
        cashflow_recycler.setNestedScrollingEnabled(true);
        cashflow_recycler.setAdapter(cashflowMainAdapter);
        elasticEdgeView = root.findViewById(R.id.elasticEdge);
        cashflow_display_menu = root.findViewById(R.id.cashflow_display_menu);
        elasticContentSkeleton = root.findViewById(R.id.elasticContentSkeleton);
        skeletonLP = (ConstraintLayout.LayoutParams) elasticContentSkeleton.getLayoutParams();
        //skeletonLP.width = 0;
        //elasticContentSkeleton.setLayoutParams(skeletonLP);
        monthviewIcon = root.findViewById(R.id.monthview_icon);
        overflowDate = root.findViewById(R.id.overflow_date);
        slideIndicator = root.findViewById(R.id.slide_indicator);

        cashflow_recycler.scrollToPosition(todayItemPosition);

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

        int forecastDays = 100;// * 365


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
                        if(menuVisible) {
                            showDisplayMenu(false);
                        }

                        slidevelocoty = System.currentTimeMillis();
                        //elasticEdgeView.setTouchDown((int)e.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        elasticEdgeView.animateCloseEdge();
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


        cashflow_display_menu.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //showDisplayMenu(true);
                if(!cashflow_menu_rendered){
                    elasticContWidth = cashflow_display_menu.getWidth() + 30;
                    cashflow_menu_rendered = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                                //showDisplayMenu(true);
                            }
                    }, 200);   //5 seconds
                }
            }
        });


        monthviewIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openMonthFragment();
            }
        });



    }

    private void openMonthFragment() {

        List<LiquidView> allVisibleLiquids = new ArrayList<>();
        List<TextView> allVisibleDayNames = new ArrayList<>();
        List<toCalendarViewTransition> allTransitionNames = new ArrayList<>();
        LiquidView countVisibleLiquid;
        TextView dayName;
        String liquidTransitionName, daynameTransitionName;
        toCalendarViewTransition transDataInstance;


        for(int i = llm.findFirstVisibleItemPosition(); i <= llm.findLastVisibleItemPosition(); i++){
            try {

                ViewHolder vh = cashflow_recycler.findViewHolderForAdapterPosition(i);
                liquidTransitionName = i+"_trans";
                daynameTransitionName = i+"_dayname";
                transDataInstance = new toCalendarViewTransition(liquidTransitionName, daynameTransitionName, "test");
                countVisibleLiquid = vh.itemView.findViewById(R.id.budget_liquid);
                dayName = vh.itemView.findViewById(R.id.item_day);
                dayName.setTransitionName(daynameTransitionName);
                countVisibleLiquid.setTransitionName(liquidTransitionName);
                allVisibleDayNames.add(dayName);
                allVisibleLiquids.add(countVisibleLiquid);
                allTransitionNames.add(transDataInstance);
            }
            catch (Exception e){

                e.printStackTrace();
            }
        }


        WalletCalendarFragment walletCalendarFragment = new WalletCalendarFragment();
        FragmentTransaction ft = ((StartActivity)getContext()).getSupportFragmentManager().beginTransaction();

        //TODO Soffisticate Transition to MonthView

        walletCalendarFragment.setSharedElementEnterTransition(new DetailsTransition(350));
        walletCalendarFragment.setSharedElementReturnTransition(new DetailsTransition(350));
        walletCalendarFragment.setEnterTransition(new Explode().setDuration(350));
        //TODO GET EXIT TRANSITION ERROR FREE
        //setExitTransition(new Fade().setDuration(450));

        for(LiquidView visibleLiquid: allVisibleLiquids){
            ft.addSharedElement(visibleLiquid, visibleLiquid.getTransitionName());
        }

        for(TextView dayname : allVisibleDayNames){
            ft.addSharedElement(dayname, dayname.getTransitionName());
        }

        // Replace the fragment.
        ft.replace(popContainer.getId(), walletCalendarFragment);

        // Enable back navigation with shared element transitions.
        ft.addToBackStack(walletCalendarFragment.getClass().getSimpleName());

        // Finally press play.
        ft.commit();

        activity.checkBackstack();
    }

    boolean cashflow_menu_rendered = false;

    float alpha = 0;
    private void showDisplayMenu(boolean show) {
        ValueAnimator va;

        if(show){
            va = ValueAnimator.ofInt(0, elasticContWidth);
            menuVisible = true;
        }
        else{
            va = ValueAnimator.ofInt(elasticContWidth, 0);
            menuVisible = false;
        }

        va.setDuration(150);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animval = (int)valueAnimator.getAnimatedValue();
                skeletonLP.width = animval;
                elasticContentSkeleton.setLayoutParams(skeletonLP);
                alpha = 1f/elasticContWidth * animval;
                //Log.i("Alpha", "--"+alpha);
                cashflow_display_menu.setAlpha(alpha);
            }

        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //waveEnergy = waveEnergy - 0.2f;
                //if(waveEnergy < 0){
                // waveEnergy = 0;
                //}
                //Log.i("Energy", ""+waveEnergy);

            }
        });


            va.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    boolean pauseAnim = false;

    int scrollingDist = 0;

    TextView bezierMonth, bezierBalance;
    public void setNewDay(String monthName, String balance) {
        //Change The Data On BezierView
        try {
            bezierMonth.setText(monthName);
            String rounded = round(Float.parseFloat(balance), 2) + "€";
            bezierBalance.setText(rounded);
            Log.i("Data Returned", "" + monthName + "--" + balance);
        }
        catch (Exception e){

        }
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    //System.out.println("The RecyclerView is not scrolling");
                    //Log.i("FirstVisible =", "View "+llm.findFirstVisibleItemPosition());
                    monthGraph.setVisibility(GONE);

                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());
                    if(!activity.mainMenuVisible) {
                        //activity.toggleMainMenuContainer();
                    }
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

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx > 0) {
                //System.out.println("Scrolled Right");
            } else if (dx < 0) {
                //System.out.println("Scrolled Left");
            } else {
                //System.out.println("No Horizontal Scrolled");
            }

            if (dy > 0) {
               // System.out.println("Scrolled Downwards - "+dy);
               // Log.i("Scrolling", "Downward");

                try{
                    getTopDay();
                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());
                    bezier_curve_cont.setScrolledDate(vh.itemView.findViewById(R.id.item_date).getTag().toString());

                    Log.i("DaysString" ,""+vh.itemView.findViewById(R.id.item_date).getTag().toString());
                }
                catch(Exception e){

                };
                activity.scrollHeightMenu(dy, false);

            } else if (dy < 0) {
                //System.out.println("Scrolled Upwards - " +dy);
                //Log.i("Scrolling", "Upward");
                try{
                    getTopDay();

                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());
                    bezier_curve_cont.setScrolledDate(vh.itemView.findViewById(R.id.item_date).getTag().toString());

                    Log.i("DaysString" ,""+vh.itemView.findViewById(R.id.item_date).getTag().toString());
                }
                catch(Exception e){

                };

                activity.scrollHeightMenu(dy, true);

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

    //SOme Test Stuff
    TextView topdate;
    TextView topday;
    ViewHolder scrollingTopViewHolder;
    TextView hiddenDay, hiddenDate;
    private void getTopDay() {
        //Log.i("Function","is Running");
        //llm.findFirstVisibleItemPosition();
        scrollingTopViewHolder = cashflow_recycler.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());
        topdate = scrollingTopViewHolder.itemView.findViewById(R.id.item_date);
        topday = scrollingTopViewHolder.itemView.findViewById(R.id.item_day);
        //Log.i("TopDay",""+topdate.getText());
        if(overflowDate.getText() != topdate.getText()){


            overflowDate.setText(topdate.getText());
            hiddenDate = topdate;

        }


    }


    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

