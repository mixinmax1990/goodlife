package com.news.goodlife.Functions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;

import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class InflateDayFixedCosts {

    SingletonClass singletonClass = SingletonClass.getInstance();

    public InflateDayFixedCosts(AsyncLayoutInflater inflater, ViewGroup parent) {
        inflater.inflate(R.layout.budget_list_item, parent, callback);
    }

    private AsyncLayoutInflater.OnInflateFinishedListener callback = new AsyncLayoutInflater.OnInflateFinishedListener() {
        @Override
        public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
            //TODO Set Parameters to identify fixed cost

            IconDoughnutView dv = view.findViewById(R.id.icondoughnut);
            dv.setCategory("#FFFFFF", "icn_regular");

            CardView catcard = view.findViewById(R.id.category_item);

            ViewGroup.LayoutParams lp = catcard.getLayoutParams();


            //Populate Fixed onBoth YEar and Month

            parent.addView(view);
            //yearlyBudgetCont.addView(fixeditem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open Fixed COst Progress
                    singletonClass.changeFragment.setValue("FixedModule");
                }
            });
        }
    };
}
