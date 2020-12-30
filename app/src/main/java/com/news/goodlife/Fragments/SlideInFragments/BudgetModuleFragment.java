package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class BudgetModuleFragment extends Fragment {
    View root;
    int budgetID;
    SingletonClass singletonMain = SingletonClass.getInstance();
    BudgetModel budgetData;
    BudgetCategoryModel categoryData;
    View budget_cont;


    LiquidView topFrameLiquid;
    ImageView iconView;
    Drawable iconDrawable;
    TextView budgetNameView;
    public BudgetModuleFragment(String budgetID) {

        this.budgetID = Integer.parseInt(budgetID);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_module_budget, container, false);

        budgetData =  singletonMain.getDatabaseController().BudgetController.getBudget(budgetID);
        categoryData = singletonMain.getDatabaseController().BudgetCategoryController.getBudgetCategory(Integer.parseInt(budgetData.getCategoryid()));

        topFrameLiquid = root.findViewById(R.id.budgetTopLiquid);
        topFrameLiquid.setHideOil(true);
        topFrameLiquid.setLiquidColor(categoryData.getCatcolor());
        topFrameLiquid.animateWave();

        iconView = root.findViewById(R.id.budget_iconview);
        budgetNameView = root.findViewById(R.id.budgetname);

        budgetNameView.setText(categoryData.catname);

        iconDrawable = getResources().getDrawable(getResources().getIdentifier(categoryData.getCaticon(), "drawable", getContext().getPackageName()), null);
        iconView.setImageDrawable(iconDrawable);

        budget_cont = root.findViewById(R.id.budget_modfrag_container);
        subtleSlide();
        return root;
    }

    int startDist = 200;
    private void subtleSlide() {
        final int marginstart = singletonMain.dpToPx(10);
        budget_cont.setX(startDist + marginstart);

        ValueAnimator va = ValueAnimator.ofInt(startDist, 0);

        va.setDuration(450);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animval = (int)valueAnimator.getAnimatedValue();

                budget_cont.setX(animval + marginstart);
            }
        });

        va.start();
    }
}
