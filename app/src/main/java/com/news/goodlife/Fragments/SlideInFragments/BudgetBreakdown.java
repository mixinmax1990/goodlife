package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class BudgetBreakdown extends Fragment {
    View root, backArrow;
    SingletonClass singletonMain = SingletonClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_budget_breakdown_layout, container, false);

        backArrow = root.findViewById(R.id.backarrow);
        listeners();
        return root;
    }

    private void listeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThis();
            }
        });
    }

    float parentOffset;
    private void closeThis() {
        parentOffset = singletonMain.getOffsetViewParent().getX();
        Log.i("Parent Offset", ""+parentOffset);
        ValueAnimator va;

        va = ValueAnimator.ofFloat(0, 1);


        va.setDuration(250);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float animval = (float)valueAnimator.getAnimatedValue();
                singletonMain.getOffsetViewChild().setX(singletonMain.getDisplayWidth() * animval);
                singletonMain.getOffsetViewParent().setX(parentOffset * (1-animval));
                singletonMain.getUniversalBackarrow().setX(parentOffset * (1-animval));
            }

        });

        va.start();
    }
}
