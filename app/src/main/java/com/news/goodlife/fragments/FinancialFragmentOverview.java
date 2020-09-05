package com.news.goodlife.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Fade;

import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.BezierView;
import com.news.goodlife.CustomViews.DoughnutChartView;
import com.news.goodlife.CustomViews.GoalChartView;
import com.news.goodlife.CustomViews.GoalDrawView;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class FinancialFragmentOverview extends Fragment {

    LinearLayout personalProfileView;
    BezierView bezierView;
    DoughnutChartView doughnutChartView;
    FinancialFragment financialFragment;
    ViewGroup financeContainer;

    //TODO Add this View Dynamic
    LinearLayout goalContainer;

    //BlurView
    BlurView blurView;
    ViewGroup blurContainer;
    ConstraintLayout blurConstraintContainer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.financial_fragment_main, container, false);
        personalProfileView = root.findViewById(R.id.financial_profiles);
        bezierView = root.findViewById(R.id.bezier_overview);
        financialFragment = new FinancialFragment();
        financeContainer = root.findViewById(R.id.finance_container);
        goalContainer = root.findViewById(R.id.goal1container);
        doughnutChartView = root.findViewById(R.id.goal_progress_doughnut);

        blurView = root.findViewById(R.id.finance_main_blurview);
        startBlurring(20);
        blurContainer = root.findViewById(R.id.blurContent);
        blurConstraintContainer = (ConstraintLayout) blurContainer;





// Note that we need the API version check here because the actual transition classes (e.g. Fade)
// are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
// ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            financialFragment.setSharedElementEnterTransition(new DetailsTransition());
            financialFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            financialFragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        listeners();


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void startBlurring(float radius) {

        radius = Math.round(radius);

        //Max 25f
        View decorView = getActivity().getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }

    public void toggleAnimateBlur(final boolean blur, final float clickedProgress){


        float from, to;
        if(blur){
            //Collapse Cash
            blurView.setVisibility(View.VISIBLE);
            from = 0f;
            to = 1f;
        }
        else{
            //Expand Cash
            from = 1f;
            to = 0f;
        }

        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        int mDuration = 300; //in millis
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {

                float animValue = (float) animation.getAnimatedValue();


                blurView.setAlpha(animValue);

            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(!blur){
                    doughnutChartView.animateDoughnutShrink();
                }
                {
                    doughnutChartView.setProgress((int)clickedProgress);
                    doughnutChartView.animateDoughnutGrow();
                }

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!blur){
                    blurView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        va.start();
    }

    private void listeners(){
        goalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalDrawView clickedV = v.findViewWithTag("goalSimple");
                toggleAnimateBlur(true, clickedV.getGoalProgress());
            }
        });

        personalProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                financeContainer.removeAllViews();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(bezierView, "personal")
                        .replace(R.id.finance_container, financialFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        blurView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnimateBlur(false, 0f);
            }

        });
    }


}
