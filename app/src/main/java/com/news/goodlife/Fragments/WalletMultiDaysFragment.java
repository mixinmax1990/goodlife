package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;

import com.news.goodlife.Adapters.Recycler.CashflowMainAdapter;
import com.news.goodlife.CustomViews.BubbleChartCategories;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Models.CalendarLayoutMonth;
import com.news.goodlife.Models.toCalendarViewTransition;
import com.news.goodlife.R;
import com.news.goodlife.StartActivity;
import com.news.goodlife.Transitions.DetailsTransition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static androidx.recyclerview.widget.RecyclerView.*;

public class WalletMultiDaysFragment extends Fragment {

    //Account Tabs
    //TODO For testing Purposes (these will have to be created dynamically)

    FrameLayout cashflow_input_frame;


    //RecylcerView
    RecyclerView cashflow_recycler;
    LinearLayoutManager llm;
    CashflowMainAdapter cashflowMainAdapter;
    ElasticEdgeView elasticEdgeView;
    ConstraintLayout cashflow_display_menu;
    FrameLayout elasticContentSkeleton;
    ConstraintLayout.LayoutParams skeletonLP;
    FrameLayout popContainer;
    FrameLayout menu_container;
    View monthviewIcon;
    public StartActivity activity;
    TextView overflowDay;

    BorderRoundView slideIndicator;
    CalendarLayoutDay selectedDay;



    public int menuTop;
    public WalletMultiDaysFragment(int menuTop, FrameLayout popContainer, FrameLayout menu_container,@Nullable CalendarLayoutDay selected) {
        this.menuTop = menuTop;
        this.popContainer = popContainer;
        this.menu_container = menu_container;
        this.selectedDay = selected;

    }
    int firstElement = 10000;
    int lastElement = 10001;
    ViewHolder visibleViewHolder;
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.wallet_multi_days, container, false);
        activity = (StartActivity) getActivity();

        //Get Dates

        if(selectedDay != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDay.getDate());
            Log.i("Selected Day",""+cal.get(Calendar.DAY_OF_MONTH));

            getCalendarRange(selectedDay.getDate());
        }
        else{
            Log.i("Selected Day", "null");
            getCalendarRange(null);
        }




        //Tab Sections
        cashflow_input_frame = root.findViewById(R.id.cashflow_input_frame);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cashflow_input_frame.getLayoutParams();
        lp.setMargins(0,0,0, menuTop);
        cashflow_input_frame.setLayoutParams(lp);
        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext(), popContainer, this, root, getActivity().getWindow().getDecorView(), allCalendarDays);
        llm = new LinearLayoutManager(getContext());
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
        overflowDay = root.findViewById(R.id.overflow_day);
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
                            bubbleChart.animateBubbles();
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

    private int todayItemPosition;
    public List<CalendarLayoutDay> allCalendarDays = new ArrayList<>();
    private void getCalendarRange(Date selectDate){

        //getToday
        Calendar today = Calendar.getInstance();

        //SetTime To Zero
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        //clear TimeZone
        today.clear(Calendar.ZONE_OFFSET);

        //Todo make first da a Date - for now go 60 days back

        int forecastDays = 100;// * 365


        //Go X years back
        Calendar loopDay = Calendar.getInstance();
        if(selectDate != null){
            loopDay.setTime(selectDate);
        }
        loopDay.add(Calendar.DATE, - forecastDays);

        //Calendar Obj of first day 10 Years back

        // Add PastDays
        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);

            //Set The Day Object
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("day", loopDay);

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


            //Check if WEEK,MONTH,YEAR ends
            allCalendarDays.add(calendarLayoutDay);
            analysisPoints(loopDay);
        }



        //allCalendarDates.get(0).getTime();

        //c.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        //SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        //String output = sdf1.format(c.getTime());
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
    float elTD, elDist, menDist;
    int elasticContWidth;
    boolean menuVisible = false;
    boolean openingMenu = false;
    boolean slidingFragment;
    long slidevelocoty = 0;

    private void listeners() {

        cashflow_recycler.addOnScrollListener(new CustomScrollListener());
        cashflow_recycler.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                switch(e.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        elTD = e.getX();
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
                        openingMenu = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        menDist = (elTD - e.getRawX());
                        if(menDist >= 0){
                            slidingFragment = true;

                            activity.slideMechanism((int)menDist, true);

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
                                showDisplayMenu(true);
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
    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    //System.out.println("The RecyclerView is not scrolling");
                    //Log.i("FirstVisible =", "View "+llm.findFirstVisibleItemPosition());

                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());
                    if(!activity.mainMenuVisible) {
                        //activity.toggleMainMenuContainer();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //System.out.println("Scrolling now");
                    if(activity.mainMenuVisible) {
                        //activity.toggleMainMenuContainer();
                    }
                    //showDisplayMenu(false);
                    //animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());

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
                }
                catch(Exception e){

                };
                activity.scrollHeightMenu(dy, false);

            } else if (dy < 0) {
                //System.out.println("Scrolled Upwards - " +dy);
                //Log.i("Scrolling", "Upward");
                try{
                    getTopDay();
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

    //SOme Test Stuff
    TextView topdate;
    ViewHolder scrollingTopViewHolder;
    TextView hiddenDay;
    private void getTopDay() {
        //Log.i("Function","is Running");
        llm.findFirstVisibleItemPosition();
        scrollingTopViewHolder = cashflow_recycler.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());
        topdate = scrollingTopViewHolder.itemView.findViewById(R.id.item_day);
        //Log.i("TopDay",""+topdate.getText());
        if(overflowDay.getText() != topdate.getText()){

            if(hiddenDay != null){
                hiddenDay.setVisibility(VISIBLE);
            }
            overflowDay.setText(topdate.getText());
            hiddenDay = topdate;
            hiddenDay.setVisibility(INVISIBLE);
        }


    }


    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

