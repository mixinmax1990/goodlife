package com.news.goodlife.fragments;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


        listeners();


        return root;
    }



    private void listeners() {

        cashflow_recycler.addOnScrollListener(new CustomScrollListener());



    }

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
                //System.out.println("Scrolled Downwards");
            } else if (dy < 0) {
                //System.out.println("Scrolled Upwards");
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

