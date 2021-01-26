package com.news.goodlife.Functions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;

import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class InflateDayBudgets {

    SingletonClass singletonMain = SingletonClass.getInstance();
    public InflateDayBudgets(AsyncLayoutInflater inflater, ViewGroup parent) {
        List<BudgetModel> allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgets();
        if(allBudgets != null){
            for(final BudgetModel budget: allBudgets){
                final BudgetCategoryModel category = singletonMain.getDatabaseController().BudgetCategoryController.getBudgetCategory(Integer.parseInt(budget.getCategoryid()));
                inflater.inflate(R.layout.budget_list_item, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                        view.setTag(budget.getId());

                        final IconDoughnutView dv = view.findViewById(R.id.icondoughnut);
                        //dv.setCategory("#64CE65", category.getCaticon());

                        CardView catcard = view.findViewById(R.id.category_item);
                        TextView budget_name = view.findViewById(R.id.budget_name);

                        budget_name.setText(category.getCatname());

                        parent.addView(view);

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
    }
}
