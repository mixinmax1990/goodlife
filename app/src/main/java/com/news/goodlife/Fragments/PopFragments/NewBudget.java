package com.news.goodlife.Fragments.PopFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Adapters.Recycler.BudgetPeriodAdapter;
import com.news.goodlife.CustomViews.MarkedConstraintLayout;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Functions.InflateDayDetails;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class NewBudget extends Fragment {

    View root;
    SuccessCallback callback;

    ProgressBar progressBar;
    View close;
    ViewGroup content;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public NewBudget(SuccessCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.budget_fragment_newbudget, container, false);

        progressBar = root.findViewById(R.id.progressbar);
        close = root.findViewById(R.id.close);
        content =  root.findViewById(R.id.content);

        asyncContent();

        listeners();


        return root;
    }

    RecyclerView budgetperiod_recycler;
    BudgetPeriodAdapter budgetPeriodAdapter;
    LinearLayoutManager llm;
    private void asyncContent() {

        AsyncLayoutInflater inflater = new AsyncLayoutInflater(getContext());

        inflater.inflate(R.layout.budget_fragment_newbudget_content, content, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                budgetperiod_recycler = view.findViewById(R.id.budget_period_recycler);


                budgetperiod_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int recyclerHeight = budgetperiod_recycler.getHeight();
                        int padding = (singletonClass.getDisplayWidth() / 2) - (recyclerHeight / 2);

                        budgetperiod_recycler.addOnScrollListener(new CustomScrollListner());

                        budgetPeriodAdapter = new BudgetPeriodAdapter(getContext(), 5, padding);
                        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        budgetperiod_recycler.setAdapter(budgetPeriodAdapter);
                        budgetperiod_recycler.setLayoutManager(llm);


                        budgetperiod_recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


                progressBar.setVisibility(View.GONE);
                content.addView(view);
            }
        });
    }


    private void listeners() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.error();
            }
        });


    }

    private class CustomScrollListner extends RecyclerView.OnScrollListener {
        public CustomScrollListner() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:

                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //System.out.println("Scrolling now");
                    RecyclerView.ViewHolder first = budgetperiod_recycler.findViewHolderForAdapterPosition(llm.findFirstVisibleItemPosition());
                    RecyclerView.ViewHolder last= budgetperiod_recycler.findViewHolderForAdapterPosition(llm.findLastVisibleItemPosition());
                    RecyclerView.ViewHolder middle = budgetperiod_recycler.findViewHolderForAdapterPosition(llm.findFirstCompletelyVisibleItemPosition());

                    try{
                        first.itemView.setAlpha(.2f);
                        last.itemView.setAlpha(.2f);
                        middle.itemView.setAlpha(1f);

                    }
                    catch(Exception e){

                    }

                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    // System.out.println("Scroll Settling");
                    //animateVisibleWaves(recyclerView, llm.findFirstVisibleItemPosition(), llm.findLastVisibleItemPosition());

                    break;

            }

        }


        /*public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx > 0) {
                //System.out.println("Scrolled Right");
            } else if (dx < 0) {
                //System.out.println("Scrolled Left");
            } else {
                //System.out.println("No Horizontal Scrolled");
            }

            if (dy > 0) {


                // Log.i("Scrolling", "Downward");
                int firstItem = llm.findFirstVisibleItemPosition() + 1;


                try{
                    if(currentFirst != firstItem){
                        currentFirst = firstItem;
                        RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(firstItem);

                        if(firstVH.itemView instanceof MarkedConstraintLayout){
                            markDay((MarkedConstraintLayout)firstVH.itemView);
                            markedWeekday = true;
                            setSideCalendar(currentFirst);
                        }
                        else{
                            //Not a weekday
                            unmarkDay();
                            markedWeekday = false;

                        }

                    }
                }
                catch(Exception e){

                }

            } else if (dy < 0) {

                if(!sideMenuOpened){
                    toggleSideMonth(false);
                }
                // Log.i("Scrolling", "Upward");
                int firstItem = llm.findFirstVisibleItemPosition() + 1;


                try{
                    if(currentFirst != firstItem){
                        currentFirst = firstItem;
                        RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(firstItem);

                        if(firstVH.itemView instanceof MarkedConstraintLayout){
                            markDay((MarkedConstraintLayout)firstVH.itemView);
                            markedWeekday = true;
                            setSideCalendar(currentFirst);
                        }
                        else{
                            unmarkDay();
                            markedWeekday = false;
                        }

                    }
                }
                catch(Exception e){

                }


            } else {
                //System.out.println("No Vertical Scrolled");
            }


        }*/

    }
}
