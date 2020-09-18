package com.news.goodlife.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Adapters.Recycler.CashflowMainAdapter;
import com.news.goodlife.CustomViews.CustomIcons.SubSectionIcon;
import com.news.goodlife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FinanceCashflow extends Fragment {

    //Account Tabs
    //TODO For testing Purposes (these will have to be created dynamically)
    SubSectionIcon account1, account2, account3;

    List<SubSectionIcon> tabList = new ArrayList<>();
    FrameLayout cashflow_input_frame;


    //RecylcerView
    RecyclerView cashflow_recycler;
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
        account1 = root.findViewById(R.id.account1tab);
        account2 = root.findViewById(R.id.account2tab);
        account3 = root.findViewById(R.id.account3tab);
        tabList.add(account1);
        tabList.add(account2);
        tabList.add(account3);
        cashflow_input_frame = root.findViewById(R.id.cashflow_input_frame);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cashflow_input_frame.getLayoutParams();
        lp.setMargins(0,0,0, menuTop + 15);
        cashflow_input_frame.setLayoutParams(lp);

        cashflow_recycler = root.findViewById(R.id.cashflow_recycler);
        cashflowMainAdapter = new CashflowMainAdapter(getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(false);
        cashflow_recycler.setLayoutManager(llm);
        cashflow_recycler.setAdapter(cashflowMainAdapter);


        listeners();


        return root;
    }

    private void unselectAllTabs() {

        for (final SubSectionIcon tab : tabList) {
            tab.unSelectTab();
        }
    }


    private void listeners() {

        for (final SubSectionIcon tab : tabList) {
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unselectAllTabs();
                    tab.selectTab();
                }
            });
        }

    }
}
