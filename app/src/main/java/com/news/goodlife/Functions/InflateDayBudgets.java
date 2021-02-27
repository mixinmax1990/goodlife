package com.news.goodlife.Functions;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BudgetCircleMini;
import com.news.goodlife.CustomViews.CustomEntries.PopUpFrame;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class InflateDayBudgets {

    SingletonClass singletonMain = SingletonClass.getInstance();

    boolean startItem = true;

    public InflateDayBudgets(AsyncLayoutInflater inflater, ViewGroup parent) {

        List<BudgetModel> allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgets();
        if(allBudgets.size() != 0){
            int pos = 1;
            for(BudgetModel budget: allBudgets){
                inflateBudget(inflater, budget, parent, pos);
                pos++;
                if(pos == 4){
                    pos = 1;
                }
            }
        }
        else{

            inflateNoBudget(inflater, parent);

        }




        List<BudgetCategoryModel> allCategories = singletonMain.getDatabaseController().BudgetCategoryController.getAllBudgetCategories();


        /*for(BudgetCategoryModel budgetCategory: allCategories){

            final String catname = budgetCategory.getCatname();
            final String catid = budgetCategory.getId();


            List<BudgetModel> allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgetsByCategory(budgetCategory.id);

            int layout;

            if(allBudgets.size() > 3){
                layout = R.layout.budget_fragment_flexitem_full;

            }
            else{

                if(startItem){
                    layout = R.layout.budget_fragment_flexitem_start;
                    startItem = false;
                }
                else{
                    layout = R.layout.budget_fragment_flexitem_end;
                    startItem = true;
                }
            }

            inflater.inflate(layout, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                    //Category is inflated
                    ViewGroup flexParent = view.findViewById(R.id.budgetitems_flex_container);
                    //give Flex container the id of the Category so we can call it later

                    TextView catnameTV = view.findViewById(R.id.category_name);
                    catnameTV.setText(catname);
                    catnameTV.setVisibility(View.GONE);

                    //inflateAddBudget(flexParent, inflater);
                    View addBudgetButton = view.findViewById(R.id.add_budget_button);
                    addBudgetButton.setVisibility(View.GONE);



                    boolean full;
                    if(allBudgets.size() > 3){
                        full = true;
                    }
                    else{
                        full = false;
                    }

                    parent.addView(view);

                    for(BudgetModel budget: allBudgets){
                        inflateBudget(inflater, budget, flexParent, full);
                    }

                }
            });
        }*/
    }

    private void inflateNoBudget(AsyncLayoutInflater inflater, ViewGroup parent) {

        inflater.inflate(R.layout.budget_flexitem_setbudgets, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                parent.addView(view);

            }
        });
    }

    int testprogress = 110;
    int testcount = 0;
    private void inflateBudget(AsyncLayoutInflater inflater, BudgetModel budget, ViewGroup parent, int pos){



        inflater.inflate(R.layout.budget_flexitem_3rd, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent){
                testcount++;
                PopUpFrame popUpFrame = (PopUpFrame) view;
                paddView(pos, popUpFrame);

                testprogress = testprogress - 10;
                BudgetCircleMini budgetCircleMini = view.findViewById(R.id.budget_circle_mini);
                budgetCircleMini.setMonths(Integer.parseInt(budget.getMonths()));
                budgetCircleMini.setPercentage(testprogress);

                TextView budgetName = view.findViewById(R.id.budget_name);
                budgetName.setText(budget.getName());

                TextView remaining = view.findViewById(R.id.restbudget_available);
                remaining.setText(singletonMain.monefy(budget.amount));

                TextView newTransactions = view.findViewById(R.id.new_covered_tranactions);

                if(testcount == 2){
                    popUpFrame.highlight(1);
                    newTransactions.setVisibility(View.VISIBLE);
                    newTransactions.setText("+2");
                }

                if(testcount == 6){
                    popUpFrame.highlight(1);
                    newTransactions.setVisibility(View.VISIBLE);
                    newTransactions.setText("+1");
                }

                if(testcount == 7){
                    popUpFrame.highlight(1);
                    newTransactions.setVisibility(View.VISIBLE);
                    newTransactions.setText("+5");
                }


                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) view.getLayoutParams();
                lp.setFlexBasisPercent(.333f);
                view.setLayoutParams(lp);

                parent.addView(view);
                singletonMain.toggleFadeView(true, view, new SuccessCallback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void error() {

                    }
                });

                popUpFrame.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //some code....
                                break;
                            case MotionEvent.ACTION_UP:
                                popUpFrame.ripple(event.getX(), event.getY(), new SuccessCallback() {
                                    @Override
                                    public void success() {
                                        singletonMain.changeFragment.data = new ArrayList<>();
                                        singletonMain.changeFragment.getData().add(budget.getId());
                                        singletonMain.changeFragment.setValue("BudgetModule");
                                    }

                                    @Override
                                    public void error() {

                                    }
                                });
                                v.performClick();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            default:
                                break;

                        }
                        return true;
                    }
                });
            }

            private void paddView(int pos, PopUpFrame view) {

                if(pos == 1){
                    view.bgPadding(0,singletonMain.dpToPx(3),singletonMain.dpToPx(2),0);
                    view.setPadding(0,singletonMain.dpToPx(3),singletonMain.dpToPx(2),0);

                }
                else if(pos == 2){
                    view.bgPadding(singletonMain.dpToPx(1),singletonMain.dpToPx(3),singletonMain.dpToPx(1),0);
                    view.setPadding(singletonMain.dpToPx(1),singletonMain.dpToPx(3),singletonMain.dpToPx(1),0);
                }
                else{
                    view.bgPadding(singletonMain.dpToPx(2),singletonMain.dpToPx(3),0,0);
                    view.setPadding(singletonMain.dpToPx(2),singletonMain.dpToPx(3),0,0);
                }
            }
        });
    }


    private void inflateBudget(AsyncLayoutInflater inflater, BudgetModel budget, ViewGroup parent, boolean bigCategory){

        inflater.inflate(R.layout.budget_flexitem_mini, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {


                testprogress = testprogress - 10;
                BudgetCircleMini budgetCircleMini = view.findViewById(R.id.budget_circle_mini);
                budgetCircleMini.setMonths(Integer.parseInt(budget.getMonths()));
                budgetCircleMini.setPercentage(testprogress);

                TextView budgetName = view.findViewById(R.id.budget_name);
                budgetName.setText(budget.getName());

                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) view.getLayoutParams();
                if(bigCategory){
                    //Change Flex to 25%
                    lp.setFlexBasisPercent(.25f);
                }
                else{
                    lp.setFlexBasisPercent(.5f);
                }
                view.setLayoutParams(lp);

                parent.addView(view);
                singletonMain.toggleFadeView(true, view, new SuccessCallback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void error() {

                    }
                });

                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //some code....
                                break;
                            case MotionEvent.ACTION_UP:
                                ((PopUpFrame)view).ripple(event.getX(), event.getY(), new SuccessCallback() {
                                    @Override
                                    public void success() {
                                        singletonMain.changeFragment.data = new ArrayList<>();
                                        singletonMain.changeFragment.getData().add(budget.getId());
                                        singletonMain.changeFragment.setValue("BudgetModule");
                                    }

                                    @Override
                                    public void error() {

                                    }
                                });
                                v.performClick();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            default:
                                break;

                        }
                        return true;
                    }
                });
            }
        });
    }
}
