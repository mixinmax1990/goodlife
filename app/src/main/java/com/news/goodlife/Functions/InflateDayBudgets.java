package com.news.goodlife.Functions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BudgetCircleMini;
import com.news.goodlife.CustomViews.IconDoughnutView;
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

        List<BudgetCategoryModel> allCategories = singletonMain.getDatabaseController().BudgetCategoryController.getAllBudgetCategories();


        for(BudgetCategoryModel budgetCategory: allCategories){

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
        }
    }

    int testprogress = 110;
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

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        singletonMain.changeFragment.data = new ArrayList<>();
                        singletonMain.changeFragment.getData().add(budget.getId());
                        singletonMain.changeFragment.setValue("BudgetModule");
                    }
                });
            }
        });
    }
}
