package com.news.goodlife.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.news.goodlife.Adapters.Recycler.CashflowMonthAdapter;
import com.news.goodlife.MainActivity;
import com.news.goodlife.Models.toCalendarViewTransition;
import com.news.goodlife.R;

import java.util.List;

public class FinanceCashflowMonth extends Fragment {

    RecyclerView monthRecycler;
    FlexboxLayoutManager flexLM;
    List<toCalendarViewTransition> allTransitionNames;

    public FinanceCashflowMonth(List<toCalendarViewTransition> allTransitionNames) {

        this.allTransitionNames = allTransitionNames;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.finance_cashflow_month, container, false);

        //setExitTransition(new Fade());

        monthRecycler = root.findViewById(R.id.month_recycler);
        flexLM = new FlexboxLayoutManager(getContext());
        flexLM.setFlexWrap(FlexWrap.WRAP);
        flexLM.setFlexDirection(FlexDirection.ROW);
        flexLM.setJustifyContent(JustifyContent.FLEX_START);
        flexLM.setAlignItems(AlignItems.FLEX_START);

        //TODO ADD Dynamic Data to Adapter
        CashflowMonthAdapter cashflowMonthAdapter = new CashflowMonthAdapter(getContext(), allTransitionNames);
        monthRecycler.setAdapter(cashflowMonthAdapter);
        monthRecycler.setLayoutManager(flexLM);

        monthRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setEnterTransition(null);
            }
        });

        return root;
    }


}
