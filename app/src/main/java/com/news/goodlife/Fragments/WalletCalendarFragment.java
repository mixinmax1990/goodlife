package com.news.goodlife.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.Adapters.Recycler.CashflowMonthAdapter;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;

import com.news.goodlife.LayoutManagers.WalletCalendarLinearLayoutManager;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Models.CalendarLayoutMonth;
import com.news.goodlife.R;
import com.news.goodlife.StartActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WalletCalendarFragment extends Fragment {

    RecyclerView monthRecycler;
    WalletCalendarLinearLayoutManager LM;
    //List<toCalendarViewTransition> allTransitionNames;
    BorderRoundView slideIndicator;
    public StartActivity activity;
    private View scroll_today;

    CalendarSelectDayListener callbackCalendarSelectDayListener;

    public WalletCalendarFragment() {

        //this.allTransitionNames = allTransitionNames;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_calendar, container, false);

        //setExitTransition(new Fade());
        activity = (StartActivity) getActivity();

        monthRecycler = root.findViewById(R.id.month_recycler);
        LM = new WalletCalendarLinearLayoutManager(getContext());
        slideIndicator = root.findViewById(R.id.slide_indicator);
        scroll_today = root.findViewById(R.id.scroll_today);


        //Todo Get Data From DataBase and get First Entry Date

        getCalendarRange(1);


        //TODO ADD Dynamic Data to Adapter
        CashflowMonthAdapter cashflowMonthAdapter = new CashflowMonthAdapter(getContext(), allMonths);
        monthRecycler.setAdapter(cashflowMonthAdapter);
        monthRecycler.setLayoutManager(LM);

        //monthRecycler.scrollToPosition(1000);
        monthRecycler.scrollToPosition(todayItemPosition);

        monthRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setEnterTransition(null);
                //monthRecycler.scrollBy(0, 80000);

                monthRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        listeners();



        return root;
    }
    float elTDX, elTDY, elDist, menDist;
    int elasticContWidth;
    boolean menuVisible = false;
    boolean openingMenu = false;

    long slideVelocity = 0;
    float dir_barrier = 10f;
    boolean scrolling_horizontally = false;
    boolean direction_identified = false;
    float distX, distY;

    private void listeners() {
        monthRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                switch(e.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        elTDX = e.getX();
                        elTDY = e.getY();
                        if(menuVisible) {
                            //showDisplayMenu(false);
                        }

                        slideVelocity = System.currentTimeMillis();
                        //elasticEdgeView.setTouchDown((int)e.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        activity.resetScrollDist();
                        if(openingMenu){
                            //Check how far We are and animate the rest
                            slideVelocity = System.currentTimeMillis() - slideVelocity;
                            activity.autoFinishSlide((int)menDist, false, slideVelocity);
                        }
                        LM.setScrollEnabled(true);
                        direction_identified = false;
                        scrolling_horizontally = false;
                        openingMenu = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!direction_identified){
                            distX = Math.abs(elTDX - e.getX());
                            distY = Math.abs(elTDY - e.getY());

                            if(distX > dir_barrier){
                                //Scrolling HOrizontally Disable recycler Scroll
                                direction_identified = true;
                                scrolling_horizontally = true;
                                elTDX = e.getX();
                                LM.setScrollEnabled(false);
                            }

                            if(distY > dir_barrier){
                                scrolling_horizontally = false;
                                direction_identified = true;
                            }

                        }
                        else{
                            if(scrolling_horizontally){
                                menDist = (elTDX - e.getRawX());

                                if(menDist < 0){
                                    activity.slideCalendar = true;
                                    //Opening Menu
                                    openingMenu = true;
                                    //Log.i("Calendar", "Calendar");
                                    activity.slideMechanism((int)Math.abs(menDist), false);
                                }
                            }
                            else{
                                LM.setScrollEnabled(true);
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

        scroll_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthRecycler.smoothScrollToPosition(todayItemPosition);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        callbackCalendarSelectDayListener = (CalendarSelectDayListener) context;
    }

    @Override
    public void onDetach() {

        //ResetCallback
        callbackCalendarSelectDayListener = null;
        super.onDetach();
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

    int forecastYears = 10;
    //List of all Days
    public List<CalendarLayoutDay> allCalendarDays = new ArrayList<>();
    //List of CaldarMoths
    List<CalendarLayoutMonth> allMonths = new ArrayList<>();

    private int todayItemPosition;
    private void getCalendarRange(long Date){

        //getToday
        Calendar today = Calendar.getInstance();

        //SetTime To Zero
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        //clear TimeZone
        today.clear(Calendar.ZONE_OFFSET);

        //Todo make first day a Date - for now go 60 days back

        int forecastDays = forecastYears * 365;// * 365

        //Go X years back
        Calendar loopDay = Calendar.getInstance();
        loopDay.add(Calendar.DATE, - forecastDays);

        //Calendar Obj of first day 10 Years back

        // Add PastDays
        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);

            //Set The Day Object
            CalendarLayoutDay calendarLayoutDay = new CalendarLayoutDay("day", loopDay);


            //Check if WEEK,MONTH,YEAR ends
            allCalendarDays.add(calendarLayoutDay);
            Log.i("InLoop Calendadays", ""+allCalendarDays.size());
            analysisPoints(loopDay);

        }

        Log.i("CalRangeSize", ""+allCalendarDays.size());

        //TODO GET TODAY TO WORK IN THE CALENDAR
        //allCalendarDays.get(allCalendarDays.size() - 1).setToday(true);

        todayItemPosition = allMonths.size();
        
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

            allMonths.add(month);

            allCalendarDays = new ArrayList<>();

        }
        day.add(Calendar.DATE, -1);

    }


}
