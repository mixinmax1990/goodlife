package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.BubbleChartCategories;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.CustomEntries.LabeledEntryView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.R;
import com.news.goodlife.StartActivity;

public class WalletTodayFragment extends Fragment {

    //Todo garbage collect this on destroy
    public StartActivity activity;
    ScrollView todayScroll;
    BorderRoundView slideIndicator;
    LiquidView liquidView;
    BubbleChartCategories bubbleChartCategories;

    View add_cashflow;
    View new_cashflow_container, cashflow_pop_container;
    ImageView add_plus, add_minus;
    LabeledEntryView amount, description;
    TextView addcashflowBTN;

    @ColorInt int selectedStroke, unselectedStroke;


    public WalletTodayFragment() {

    }

    private View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.wallet_main, container, false);
        todayScroll = root.findViewById(R.id.wallet_today_scroll);
        slideIndicator = root.findViewById(R.id.slide_indicator);
        liquidView = root.findViewById(R.id.budget_liquid_day);
        bubbleChartCategories = root.findViewById(R.id.item_day_bubble_chart);
        add_cashflow = root.findViewById(R.id.day_item_add_cashflow);
        new_cashflow_container = root.findViewById(R.id.add_cashflow_entry_container);
        add_plus = root.findViewById(R.id.day_newentry_plus);
        add_minus = root.findViewById(R.id.day_newentry_minus);
        amount = root.findViewById(R.id.day_newentry_amount);
        description = root.findViewById(R.id.day_newentry_description);
        cashflow_pop_container = root.findViewById(R.id.cashflow_activities_container);
        rootView = getActivity().getWindow().getDecorView();
        addcashflowBTN = root.findViewById(R.id.add_cashflow_button);

        liquidView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                liquidView.animateWave();
                bubbleChartCategories.animateBubbles();

                liquidView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



        listeners();
        activity = (StartActivity) getActivity();

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.entryBorder, typedValue, true);
        selectedStroke = typedValue.data;

        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        unselectedStroke = typedValue.data;


        return root;
    }

    View rootView;
    float elTD, menDist;
    boolean slidingFragment;
    long slidevelocoty = 0;
    private void listeners() {
        todayScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch(motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:

                            elTD = motionEvent.getX();
                            slidevelocoty = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:

                            if(slidingFragment){
                                slidevelocoty = System.currentTimeMillis() - slidevelocoty;
                                activity.autoFinishSlide((int)menDist, true, slidevelocoty);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //Log.i("Today", "Today");
                            menDist = (elTD - motionEvent.getX());
                            if(menDist >= 0){
                                slidingFragment = true;

                                activity.slideMechanism((int)menDist, true);

                            }

                            break;
                    }
                return false;
            }
        });

        add_cashflow.findViewById(R.id.add_cash_inday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add entry fields
                new_cashflow_container.setVisibility(View.VISIBLE);
                new_cashflow_container.animate().alpha(1f).scaleX(1).scaleY(1).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        amount.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                add_cashflow.setVisibility(View.GONE);

                toggleMinusPlus(true);

                addcashflowBTN.setTextColor(selectedStroke);
                addcashflowBTN.setAlpha(1f);
           }
        });

        add_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMinusPlus(true);
            }
        });

        add_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMinusPlus(false);
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                if(fullDisplay == 0)
                {
                    fullDisplay = r.height();
                }


                int heightDiff = fullDisplay - r.height();


                if(heightDiff != 0){
                    //KeyBoard is visible

                    int diff = fullDisplay - (cashflow_pop_container.getTop() + new_cashflow_container.getHeight());
                    //diff = diff - (root.getHeight() - fullDisplay);





                    animateSoftkeyOpend((int) root.getY(),- (heightDiff - diff), false);
                    softKeyVisible = true;
                }
                else{
                    if(softKeyUp){
                        animateSoftkeyOpend((int) root.getY(), 0, true);
                    }

                    softKeyVisible = false;
                    //hideAddCashflowEntry();
                }


            }
        });
        addcashflowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    int fullDisplay = 0;
    public boolean softKeyVisible = false;

    public void hideAddCashflowEntry(){

        new_cashflow_container.animate().alpha(0f).scaleX(.7f).scaleY(.7f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                new_cashflow_container.setVisibility(View.GONE);
                add_cashflow.setVisibility(View.VISIBLE);
                add_cashflow.setAlpha(0);
                add_cashflow.setScaleX(.7f);
                add_cashflow.setScaleY(.7f);
                add_cashflow.animate().alpha(1f).scaleY(1).scaleX(1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private boolean softKeyUp = false;

    private void animateSoftkeyOpend(int start, int end, final boolean close){
        ValueAnimator vaLeave = ValueAnimator.ofInt(start, end);
        vaLeave.setDuration(300);
        vaLeave.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animval = (int) valueAnimator.getAnimatedValue();
                 root.setY(animval);
            }
        });
        vaLeave.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


            }

            @Override
            public void onAnimationEnd(Animator animator) {



                if(close){

                    hideAddCashflowEntry();
                    softKeyUp = false;
                }
                else{
                    softKeyUp = true;
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


         vaLeave.start();



    }



    private void toggleMinusPlus(boolean add) {

        if(!add){
            add_plus.setColorFilter(selectedStroke);
            add_minus.setColorFilter(unselectedStroke);
            //DrawableCompat.setTint(add_plus.getDrawable(), selectedStroke);
        }
        else{
            add_minus.setColorFilter(selectedStroke);
            add_plus.setColorFilter(unselectedStroke);
        }

    }



    float visibleScreen;
    float scaleIndicator;
    public void moveIndicator(int move, int displayWidth){
        slideIndicator.setX(move);

        visibleScreen = (float)move / displayWidth;
        slideIndicator.setAlpha(visibleScreen);

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
