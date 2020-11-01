package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
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
import androidx.transition.Fade;

import com.news.goodlife.Adapters.Recycler.CashflowMainAdapter;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.MainActivity;
import com.news.goodlife.Models.toCalendarViewTransition;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    FrameLayout popContainer;
    FrameLayout menu_container;
    View monthviewIcon;
    public MainActivity activity;
    TextView overflowDay;



    public int menuTop;
    public FinanceCashflow(int menuTop, FrameLayout popContainer, FrameLayout menu_container) {
        this.menuTop = menuTop;
        this.popContainer = popContainer;
        this.menu_container = menu_container;


        Log.i("Menu Top", ""+menuTop);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.finance_cashflow, container, false);
        activity = (MainActivity) getActivity();

        //Tab Sections
        cashflow_input_frame = root.findViewById(R.id.cashflow_input_frame);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cashflow_input_frame.getLayoutParams();
        lp.setMargins(0,0,0, menuTop);
        cashflow_input_frame.setLayoutParams(lp);
        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext(), popContainer, this);
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

        final LiquidView[] countVisibleLiquid = new LiquidView[1];
        final TextView[] countVisibleDayNames = new TextView[1];



        cashflow_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for(int i = llm.findFirstVisibleItemPosition(); i <= llm.findLastVisibleItemPosition(); i++){
                    try {
                        ViewHolder vh = cashflow_recycler.findViewHolderForAdapterPosition(i);
                        countVisibleLiquid[0] = vh.itemView.findViewById(R.id.budget_liquid);
                        countVisibleDayNames[0] = vh.itemView.findViewById(R.id.item_day);
                        countVisibleDayNames[0].setTransitionName(i+"_dayname");
                        countVisibleLiquid[0].setTransitionName(i+"_trans");
                        countVisibleLiquid[0].getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                try {
                                   // countVisibleLiquid[0].animateWave();
                                }catch(Exception e){

                                };
                            }
                        });
                    }
                    catch (Exception e){

                    }
                }
            }
        });

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
                Log.i("AllTransitionNamesCount", "Crashes");
                e.printStackTrace();
            }
        }

        Log.i("AllTransitionNamesCount", ""+allTransitionNames.size());

        FinanceCashflowMonth financeCashflowMonth = new FinanceCashflowMonth(allTransitionNames);
        FragmentTransaction ft = ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction();

        //TODO Soffisticate Transition to MonthView

        financeCashflowMonth.setSharedElementEnterTransition(new DetailsTransition(350));
        financeCashflowMonth.setSharedElementReturnTransition(new DetailsTransition(350));
        financeCashflowMonth.setEnterTransition(new Explode().setDuration(350));
        //TODO GET EXIT TRANSITION ERROR FREE
        //setExitTransition(new Fade().setDuration(450));

        for(LiquidView visibleLiquid: allVisibleLiquids){
            ft.addSharedElement(visibleLiquid, visibleLiquid.getTransitionName());
        }

        for(TextView dayname : allVisibleDayNames){
            ft.addSharedElement(dayname, dayname.getTransitionName());
        }

        // Replace the fragment.
        ft.replace(popContainer.getId(), financeCashflowMonth);

        // Enable back navigation with shared element transitions.
        ft.addToBackStack(financeCashflowMonth.getClass().getSimpleName());

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
                    animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());
                    ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());
                    if(!activity.mainMenuVisible) {
                        activity.toggleMainMenuContainer();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //System.out.println("Scrolling now");
                    if(activity.mainMenuVisible) {
                        activity.toggleMainMenuContainer();
                    }
                    //showDisplayMenu(false);


                    //getTopDay();
                    //llm.findFirstVisibleItemPosition();
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                   // System.out.println("Scroll Settling");

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

            } else if (dy < 0) {
                //System.out.println("Scrolled Upwards - " +dy);
                //Log.i("Scrolling", "Upward");
                try{
                    getTopDay();
                }
                catch(Exception e){

                };

            } else {
                //System.out.println("No Vertical Scrolled");
            }


        }

        private void animateVisibleWaves(RecyclerView recyclerView, int first, int last){
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

