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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.news.goodlife.Adapters.Recycler.CashflowMonthAdapter;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;

import com.news.goodlife.Models.CalendarLayout;
import com.news.goodlife.R;
import com.news.goodlife.StartActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WalletCalendarFragment extends Fragment {

    RecyclerView monthRecycler;
    FlexboxLayoutManager flexLM;
    //List<toCalendarViewTransition> allTransitionNames;
    BorderRoundView slideIndicator;
    public StartActivity activity;

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
        flexLM = new FlexboxLayoutManager(getContext());
        flexLM.setFlexWrap(FlexWrap.WRAP);
        flexLM.setFlexDirection(FlexDirection.ROW);
        flexLM.setJustifyContent(JustifyContent.FLEX_START);
        flexLM.setAlignItems(AlignItems.FLEX_START);

        slideIndicator = root.findViewById(R.id.slide_indicator);


        //Todo Get Data From DataBase and get First Entry Date

        getCalendarRange(1);

        for(CalendarLayout cl: allCalendarDates){
            Log.i("DayCal", ""+cl.getCalendar().get(Calendar.DAY_OF_WEEK));
        }

        //TODO ADD Dynamic Data to Adapter
        CashflowMonthAdapter cashflowMonthAdapter = new CashflowMonthAdapter(getContext(), allCalendarDates);
        monthRecycler.setAdapter(cashflowMonthAdapter);
        monthRecycler.setLayoutManager(flexLM);

        //monthRecycler.scrollToPosition(1000);
        //monthRecycler.smoothScrollToPosition(todayItemPosition);

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
    float elTD, elDist, menDist;
    int elasticContWidth;
    boolean menuVisible = false;
    boolean openingMenu = false;

    long slideVelocity = 0;
    private void listeners() {
        monthRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                switch(e.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        elTD = e.getX();
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
                        openingMenu = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        menDist = (elTD - e.getRawX());
                        if(menDist < 0){
                            activity.slideCalendar = true;
                            //Opening Menu
                            openingMenu = true;
                            //Log.i("Calendar", "Calendar");
                            activity.slideMechanism((int)Math.abs(menDist), false);
                            /*

                            if(!menuVisible){
                                if(menDist > 100){
                                    menDist = 100;
                                    showDisplayMenu(true);
                                }
                                elasticEdgeView.setPull((int)menDist);
                            }*/
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

    int forecastYears = 2;
    public List<CalendarLayout> allCalendarDates = new ArrayList<>();

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

        //Todo make first da a Date - for now go 60 days back

        int forecastDays = forecastYears * 365;// * 365


        //Go X years back
        Calendar loopDay = Calendar.getInstance();
        loopDay.add(Calendar.DATE, - forecastDays);

        //Calendar Obj of first day 10 Years back

        // Add PastDays
        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);

            //Set The Day Object
            CalendarLayout calendarLayout = new CalendarLayout("day", loopDay);

            //Check if WEEK,MONTH,YEAR ends
            allCalendarDates.add(calendarLayout);
            analysisPoints(loopDay);
        }

        todayItemPosition = allCalendarDates.size();
        
        //add Future Days

        for(int i = 0; i < forecastDays; i++){

            loopDay.add(Calendar.DATE, 1);
            //Set The Day Object
            CalendarLayout calendarLayout = new CalendarLayout("day", loopDay);


            //Check if WEEK,MONTH,YEAR ends
            allCalendarDates.add(calendarLayout);
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
            CalendarLayout calendarLayout = new CalendarLayout("weekend", day);
            allCalendarDates.add(calendarLayout);

        }

        if(year != day.get(Calendar.YEAR)){
            //Add YearEnd Analaysis
            CalendarLayout calendarLayout = new CalendarLayout("yearend", day);
            allCalendarDates.add(calendarLayout);

        }

        if(month != day.get(Calendar.MONTH)){
            //Add MonthEnd Analysis
            CalendarLayout calendarLayout = new CalendarLayout("monthend", day);
            allCalendarDates.add(calendarLayout);

        }
        day.add(Calendar.DATE, -1);

    }


}
