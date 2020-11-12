package com.news.goodlife.Adapters.Recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BulletPointTextView;
import com.news.goodlife.CustomViews.CustomEntries.LabeledEntryView;
import com.news.goodlife.CustomViews.CustomEntries.PopUpFrame;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.Fragments.WalletMultiDaysFragment;
import com.news.goodlife.Fragments.PopFragments.CostCategoriesChart;
import com.news.goodlife.Fragments.PopFragments.IncomingCashPopFragment;
import com.news.goodlife.Fragments.PopFragments.OutgoingCashPopFragment;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.PopWindowData.CashCategoryData;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CashflowMainAdapter extends RecyclerView.Adapter<CashflowMainAdapter.ViewHolder>{

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Context context;
    int position;
    FrameLayout popWindow;
    View root, rootView;
    int cat_count = 4;
    @ColorInt int selectedStroke, unselectedStroke;

    List<CalendarLayoutDay> dateRange;

    //PopFragment
    WalletMultiDaysFragment parentFragmentClass;

    public CashflowMainAdapter(Context context, FrameLayout popWindow, WalletMultiDaysFragment parentFragmentClass, View root, View rootView, List<CalendarLayoutDay> dateRange) {

        this.context = context;
        this.popWindow = popWindow;
        this.parentFragmentClass = parentFragmentClass;
        this.root = root;
        this.rootView = rootView;
        this.dateRange = dateRange;



        Log.i("DateRanges", ""+dateRange.size());

        listeners();
        loadPopFragments();

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.entryBorder, typedValue, true);
        selectedStroke = typedValue.data;

        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        unselectedStroke = typedValue.data;


    }
    int fullDisplay = 0;
    private void listeners() {
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

/*
                if(heightDiff != 0){
                    //KeyBoard is visible

                    int diff = fullDisplay - (cashflow_pop_container.getTop() + new_cashflow_container.getHeight());
                    //diff = diff - (root.getHeight() - fullDisplay);

                    Log.i("Math", ""+ diff);



                    //animateSoftkeyOpend((int) root.getY(),- (heightDiff - diff), false);
                    softKeyVisible = true;
                }
                else{
                    if(softKeyUp){
                        animateSoftkeyOpend((int) root.getY(), 0, true);
                    }

                    softKeyVisible = false;
                    //hideAddCashflowEntry();
                }
                Log.d("Keyboard Size Day", "Size: " + heightDiff);
*/
            }
        });
    }

    private void loadPopFragments() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {


        CalendarLayoutDay calDay = dateRange.get(pos);

        switch(calDay.getType()){
            case "weekend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_weekend_item, parent, false), pos, calDay);
            case "day":
                if(calDay.isToday()){
                    return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_today_item, parent, false), pos, calDay);
                }
                else{
                    return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_day_item, parent, false), pos, calDay);
                }
            case "monthend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_monthend_item, parent, false), pos, calDay);

            case "yearend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_yearend, parent, false), pos, calDay);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return dateRange.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        PopUpFrame costcat, cashout, cashin,budgetliquid, cashflow, weekendcontainer;
        FlexboxLayout cost_cat_flex;
        LiquidView liquidView;
        TextView itemday, itemDate;
        View graph, frame;
        View add_cashflow;
        ImageView add_plus, add_minus;
        TextView addcashflowBTN;
        View new_cashflow_container;
        boolean isweekend = false;
        String[] colors = {"#ff6859","#59a7ff", "#b7ff59", "#FFFFFF", "#FFEB3B"};


        //CostCat Variable
        List<CashCategoryData> allCashCategoryData = new ArrayList<>();
        List<BulletPointTextView> allCategoriesTextView = new ArrayList<>();

        //CashOut Variable
        TextView costTitle, costValue, incomeTitle, incomeValue;




        public ViewHolder(@NonNull View itemView, int pos, CalendarLayoutDay dayData) {
            super(itemView);

            switch(dayData.getType()){
                case "day":
                    graph = itemView.findViewById(R.id.graph);
                    frame = itemView.findViewById(R.id.frame);
                    liquidView = itemView.findViewById(R.id.budget_liquid);
                    liquidView.setBaseline(randomValue(10, 300));
                    itemday = itemView.findViewById(R.id.item_day);
                    itemDate = itemView.findViewById(R.id.item_date);
                    costcat = itemView.findViewById(R.id.cashcat_frame);
                    cashout = itemView.findViewById(R.id.events_outgoing);
                    cashin = itemView.findViewById(R.id.events_incoming);
                    cost_cat_flex = itemView.findViewById(R.id.cashcat_flex);


                    itemday.setText(dayData.getDAY_OF_WEEK_NAME());
                    itemDate.setText(dayData.getMONTH_DAY_NUMBER()+" "+dayData.getMONTH_NAME()+" "+dayData.getYEAR());



                    // Entry Field Views
                    add_plus = itemView.findViewById(R.id.day_newentry_plus);
                    add_minus = itemView.findViewById(R.id.day_newentry_minus);
                    new_cashflow_container = itemView.findViewById(R.id.add_cashflow_entry_container);
                    add_cashflow = itemView.findViewById(R.id.day_item_add_cashflow);
                    final LabeledEntryView amount, description;
                    amount = itemView.findViewById(R.id.day_newentry_amount);
                    description = itemView.findViewById(R.id.day_newentry_description);
                    addcashflowBTN = itemView.findViewById(R.id.add_cashflow_button);


                    setCostCats(cat_count);

                    //Cashcat Variables
                    costcat.setTransitionName(position+"costcat_frame");
                    itemday.setTransitionName(position+"_costcat_dayname");
                    graph.setTransitionName(position+"costcat_graph");
                    cost_cat_flex.setTransitionName(position+"costcat_flex");

                    //Outgoing Variables
                    cashout.setTransitionName(position+"_cashout");
                    costTitle = itemView.findViewById(R.id.outgoint_title);
                    costValue = itemView.findViewById(R.id.outgoing_value);
                    costTitle.setTransitionName(position+"_costname");
                    costValue.setTransitionName(position+"_costvalue");

                    //Incoming Variable
                    cashin.setTransitionName(position+"_cashin");
                    incomeTitle = itemView.findViewById(R.id.incoming_title);
                    incomeValue = itemView.findViewById(R.id.incoming_value);
                    incomeTitle.setTransitionName(position+"_incomename");
                    incomeValue.setTransitionName(position+"_incomingval");
                    costcat.setBackgroundColor("#101315");
                    costcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            v.setElevation(100f);
                            v.setOutlineProvider(null);
                            transitionPopWindow("CostCategories");

                        }
                    });

                    cashin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            transitionPopWindow("CashIn");
                        }
                    });

                    cashout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            transitionPopWindow("CashOut");
                        }
                    });
                    liquidView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //liquidView.resetWaveEnergy();
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
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    break;
                case "weekend":
                    break;
                case "monthend":
                    TextView monthname = itemView.findViewById(R.id.wallet_balance_card_new_month);
                    monthname.setText(dayData.getMONTH_NAME());
                    break;
                case "yearend":
                    TextView yearname = itemView.findViewById(R.id.wallet_balance_card_new_year);
                    yearname.setText(dayData.getYEAR());
                    break;
            }

        }

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

        int fullDisplay = 0;

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


        private void transitionPopWindow(String module){

            // Set up the transaction.
            FragmentTransaction ft = parentFragmentClass.activity.getSupportFragmentManager().beginTransaction();

            switch (module){
                case "CostCategories":
                    CostCategoriesChart costCategoriesChart;

                    graph = costcat.findViewById(R.id.graph);
                    costCategoriesChart = new CostCategoriesChart(graph.getTransitionName(), costcat.getTransitionName(), cost_cat_flex.getTransitionName(), itemday.getTransitionName(), allCashCategoryData);

                    // Define the shared element transition.
                    costCategoriesChart.setSharedElementEnterTransition(new DetailsTransition(400));
                    costCategoriesChart.setSharedElementReturnTransition(new DetailsTransition(400));

                    // Now use the image's view and the target transitionName to define the shared element.
                    ft.addSharedElement(graph,graph.getTransitionName());
                    ft.addSharedElement(costcat, costcat.getTransitionName());
                    //ft.addSharedElement(cost_cat_flex, cost_cat_flex.getTransitionName());
                    for(BulletPointTextView cat: allCategoriesTextView){
                        ft.addSharedElement(cat, cat.getTransitionName());
                    }

                    // Replace the fragment.
                    ft.replace(popWindow.getId(), costCategoriesChart);

                    // Enable back navigation with shared element transitions.
                    ft.addToBackStack(costCategoriesChart.getClass().getSimpleName());

                    break;
                case "CashIn":
                    IncomingCashPopFragment incomingCashPopFragment;

                    incomingCashPopFragment = new IncomingCashPopFragment(cashin.getTransitionName(), incomeTitle.getTransitionName(), incomeValue.getTransitionName());

                    incomingCashPopFragment.setSharedElementEnterTransition(new DetailsTransition(400));
                    incomingCashPopFragment.setSharedElementReturnTransition(new DetailsTransition(400));

                    ft.addSharedElement(cashin,cashin.getTransitionName());
                    ft.addSharedElement(incomeTitle, incomeTitle.getTransitionName());
                    ft.addSharedElement(incomeValue, incomeValue.getTransitionName());

                    ft.replace(popWindow.getId(), incomingCashPopFragment);

                    ft.addToBackStack(incomingCashPopFragment.getClass().getSimpleName());
                    break;
                case "CashOut":
                    OutgoingCashPopFragment outgoingCashPopFragment;

                    outgoingCashPopFragment = new OutgoingCashPopFragment(cashout.getTransitionName(), costTitle.getTransitionName(), costValue.getTransitionName());

                    outgoingCashPopFragment.setSharedElementEnterTransition(new DetailsTransition(400));
                    outgoingCashPopFragment.setSharedElementReturnTransition(new DetailsTransition(400));

                    ft.addSharedElement(cashout,cashout.getTransitionName());
                    ft.addSharedElement(costTitle, costTitle.getTransitionName());
                    ft.addSharedElement(costValue, costValue.getTransitionName());

                    ft.replace(popWindow.getId(), outgoingCashPopFragment);

                    ft.addToBackStack(outgoingCashPopFragment.getClass().getSimpleName());
                    break;
                default:
                    break;
            }
            //TODO GET EXIT TRANSITION ERROR FREE
            //parentFragmentClass.setExitTransition(new Explode().setDuration(350));

            // Finally press play.
            ft.commit();
            parentFragmentClass.activity.checkBackstack();

            //Toggle MainMenu
            parentFragmentClass.activity.toggleMainMenuContainer();
        }

        private void setCostCats(int cat_count) {
            int currentID;

            for(int i=1;i <= cat_count; i++){
                String catname = days[i];
                String bulletPointColor = colors[i-1];
                String transitionName = position+"_cat"+i;
                CashCategoryData cashCategoryData = new CashCategoryData(catname, bulletPointColor, transitionName);
                //create Costs Category TextView
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                BulletPointTextView tempText = new BulletPointTextView(context,null,bulletPointColor, 4);
                currentID = View.generateViewId();

                tempText.setText(catname);
                tempText.setId(currentID);
                tempText.setTransitionName(transitionName);
                tempText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                tempText.setPadding(30,0,0,0);
                tempText.setLayoutParams(lp);
                cost_cat_flex.addView(tempText);

                allCashCategoryData.add(cashCategoryData);
                allCategoriesTextView.add(tempText);
            }

        }

    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

