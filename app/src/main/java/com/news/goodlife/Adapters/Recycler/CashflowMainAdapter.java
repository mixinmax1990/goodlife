package com.news.goodlife.Adapters.Recycler;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.transition.Explode;
import androidx.transition.Fade;

import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BulletPointTextView;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.CustomEntries.PopUpFrame;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Fragments.FinanceCashflow;
import com.news.goodlife.Fragments.PopFragments.CostCategoriesChart;
import com.news.goodlife.Fragments.PopFragments.IncomingCashPopFragment;
import com.news.goodlife.Fragments.PopFragments.OutgoingCashPopFragment;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.MainActivity;
import com.news.goodlife.PopWindowData.CashCategoryData;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;
import com.news.goodlife.Transitions.WaitDelayTransition;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CashflowMainAdapter extends RecyclerView.Adapter<CashflowMainAdapter.ViewHolder>{

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Context context;
    int position;
    FrameLayout popWindow;
    int cat_count = 4;

    //PopFragment
    FinanceCashflow parentFragmentClass;

    public CashflowMainAdapter(Context context, FrameLayout popWindow, FinanceCashflow parentFragmentClass) {

        this.context = context;
        this.popWindow = popWindow;
        this.parentFragmentClass = parentFragmentClass;

        loadPopFragments();
    }

    private void loadPopFragments() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(weekend){
            return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_recycler_item_week_analysis, parent, false), viewType);
        }
        else{
            return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_recycler_item, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.position = position;

        int day;
        if(position > 7){
            day = position - 8;
        }

        else{
            day = position;
        }

        if(position != 7){
            holder.itemday.setText( days[day]);
        }

        if(position == 4){
            holder.liquidView.setNegative(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 7){
            weekend = true;
        }
        else{
            weekend = false;
        }
        return position;
    }

    boolean weekend, monthend, yearend;

    @Override
    public int getItemCount() {

        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        PopUpFrame costcat, cashout, cashin,budgetliquid, cashflow;
        FlexboxLayout cost_cat_flex;
        LiquidView liquidView;
        TextView itemday;
        View graph, frame;
        boolean isweekend = false;
        String[] colors = {"#ff6859","#59a7ff", "#b7ff59", "#FFFFFF", "#FFEB3B"};


        //CostCat Variable
        List<CashCategoryData> allCashCategoryData = new ArrayList<>();
        List<BulletPointTextView> allCategoriesTextView = new ArrayList<>();

        //CashOut Variable
        TextView costTitle, costValue, incomeTitle, incomeValue;




        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(weekend){
                isweekend = true;

            }
            else {
                graph = itemView.findViewById(R.id.graph);
                frame = itemView.findViewById(R.id.frame);
                liquidView = itemView.findViewById(R.id.budget_liquid);
                liquidView.setBaseline(randomValue(10, 300));
                itemday = itemView.findViewById(R.id.item_day);
                costcat = itemView.findViewById(R.id.cashcat_frame);
                cashout = itemView.findViewById(R.id.events_outgoing);
                cashin = itemView.findViewById(R.id.events_incoming);
                cost_cat_flex = itemView.findViewById(R.id.cashcat_flex);

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

            }
            //itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            Log.i("Clicked Cash Day", "True");

            if(weekend){

            }
            else{
                liquidView.animateWave();
            }
        }


    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

