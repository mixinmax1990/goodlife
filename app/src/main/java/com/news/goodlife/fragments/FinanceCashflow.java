package com.news.goodlife.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Adapters.Recycler.CashflowMainAdapter;
import com.news.goodlife.CustomViews.CustomIcons.SubSectionIcon;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import static androidx.recyclerview.widget.RecyclerView.*;

public class FinanceCashflow extends Fragment {

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


    public int menuTop;
    public FinanceCashflow(int menuTop) {
        this.menuTop = menuTop;

        Log.i("Menu Top", ""+menuTop);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.finance_cashflow, container, false);

        //Tab Sections
        cashflow_input_frame = root.findViewById(R.id.cashflow_input_frame);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cashflow_input_frame.getLayoutParams();
        lp.setMargins(0,0,0, menuTop);
        cashflow_input_frame.setLayoutParams(lp);
        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext());
        llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(false);
        cashflow_recycler.setLayoutManager(llm);
        cashflow_recycler.setAdapter(cashflowMainAdapter);
        elasticEdgeView = root.findViewById(R.id.elasticEdge);
        cashflow_display_menu = root.findViewById(R.id.cashflow_display_menu);
        elasticContentSkeleton = root.findViewById(R.id.elasticContentSkeleton);
        skeletonLP = (ConstraintLayout.LayoutParams) elasticContentSkeleton.getLayoutParams();
        //skeletonLP.width = 0;
        //elasticContentSkeleton.setLayoutParams(skeletonLP);


        listeners();


        return root;
    }

    //Elastic Parameters
    float elTD, elDist, menDist;
    int elasticContWidth;
    boolean menuVisible = false;
    boolean openingMenu = false;

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
                        //elasticEdgeView.setTouchDown((int)e.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        elasticEdgeView.animateCloseEdge();
                        openingMenu = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        menDist = (elTD - e.getX())/4;
                        if(menDist >= 0){
                            scrollingDist = 0;
                            //Opening Menu
                            openingMenu = true;

                            if(!menuVisible){
                                if(menDist > 100){
                                    menDist = 100;
                                    showDisplayMenu(true);
                                }
                                elasticEdgeView.setPull((int)menDist);
                            }
                        }

                        break;
                }
                Log.i("Touching X", "Horizontal" + e.getX());
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
                elasticContWidth = cashflow_display_menu.getWidth() + 30;
            }
        });





    }
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
                Log.i("Alpha", "--"+alpha);
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

    boolean pauseAnim = false;

    int scrollingDist = 0;
    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    System.out.println("The RecyclerView is not scrolling");
                    Log.i("FirstVisible =", "View "+llm.findFirstVisibleItemPosition());
                    loopVisibleItems(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());
                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    System.out.println("Scrolling now");
                    //showDisplayMenu(false);
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    System.out.println("Scroll Settling");
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

            } else if (dy < 0) {
                //System.out.println("Scrolled Upwards - " +dy);

            } else {
                //System.out.println("No Vertical Scrolled");
            }


        }

        private void loopVisibleItems(RecyclerView recyclerView, int first, int last){
            for(int i = first; i <= last; i++){
                try {
                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(i);
                    SpectrumBar bar = vh.itemView.findViewById(R.id.spectrumBar);
                    LiquidView liquid = vh.itemView.findViewById(R.id.budget_liquid);
                    bar.animateCashflow();
                    liquid.animateWave();
                }
                catch (Exception e){

                }
            }
        }
    }
    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

