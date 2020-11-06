package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import com.news.goodlife.MainActivity;
import com.news.goodlife.Models.toCalendarViewTransition;
import com.news.goodlife.R;

import java.util.List;

public class WalletCalendarFragment extends Fragment {

    RecyclerView monthRecycler;
    FlexboxLayoutManager flexLM;
    //List<toCalendarViewTransition> allTransitionNames;
    BorderRoundView slideIndicator;
    public MainActivity activity;

    public WalletCalendarFragment() {

        //this.allTransitionNames = allTransitionNames;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_calendar, container, false);

        //setExitTransition(new Fade());
        activity = (MainActivity) getActivity();

        monthRecycler = root.findViewById(R.id.month_recycler);
        flexLM = new FlexboxLayoutManager(getContext());
        flexLM.setFlexWrap(FlexWrap.WRAP);
        flexLM.setFlexDirection(FlexDirection.ROW);
        flexLM.setJustifyContent(JustifyContent.FLEX_START);
        flexLM.setAlignItems(AlignItems.FLEX_START);

        slideIndicator = root.findViewById(R.id.slide_indicator);

        //TODO ADD Dynamic Data to Adapter
        CashflowMonthAdapter cashflowMonthAdapter = new CashflowMonthAdapter(getContext());
        monthRecycler.setAdapter(cashflowMonthAdapter);
        monthRecycler.setLayoutManager(flexLM);

        monthRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setEnterTransition(null);
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

    float visibleScreen;
    float scaleIndicator;
    public void moveIndicator(int move, int displayWidth){
        slideIndicator.setX(move);

        visibleScreen = 1 + Math.abs((float)move / displayWidth);
        Log.i("VisibleScreen", ""+(float)move / displayWidth);
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


}
