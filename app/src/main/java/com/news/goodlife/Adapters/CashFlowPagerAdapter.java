package com.news.goodlife.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.news.goodlife.Data.Local.Models.Financial.FinancialEventModel;
import com.news.goodlife.fragments.CashflowOnceFragment;
import com.news.goodlife.fragments.CashflowRegularFragment;
import com.news.goodlife.fragments.CashflowTimelineFragment;

import java.util.ArrayList;
import java.util.List;

public class CashFlowPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment mCurrentFragment;
    private List<FinancialEventModel> allCashFlowData;
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
    private int numbOfTabs;
    // tab titles
    private String[] tabTitles = new String[]{"All", "Pay", "Income"};

    public CashFlowPagerAdapter(@NonNull FragmentManager fm, int numbOfTabs, List<FinancialEventModel> allCashflowData) {
        super(fm);
        this.numbOfTabs = numbOfTabs;
        this.allCashFlowData = allCashflowData;

        incomingCashflow = new ArrayList<>();
        outgoingCashflow = new ArrayList<>();
        separateCashflows();
    }
    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                mCurrentFragment = new CashflowTimelineFragment(allCashFlowData);
                return mCurrentFragment;
            case 1:
                mCurrentFragment = new CashflowOnceFragment(outgoingCashflow);
                return mCurrentFragment;
            case 2:
                mCurrentFragment = new CashflowRegularFragment(incomingCashflow);
                return mCurrentFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }

    List<FinancialEventModel> incomingCashflow;
    List<FinancialEventModel> outgoingCashflow;

    private void separateCashflows(){

        for(FinancialEventModel cashflow : allCashFlowData){

            if(cashflow.getPositive().equals("true")){
                incomingCashflow.add(cashflow);
            }
            else{
                outgoingCashflow.add(cashflow);
            }
        }
    }
}
