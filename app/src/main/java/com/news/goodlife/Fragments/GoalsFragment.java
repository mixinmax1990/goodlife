package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.BezierView;
import com.news.goodlife.CustomViews.DoughnutChartView;
import com.news.goodlife.CustomViews.GoalDrawView;
import com.news.goodlife.Modules.AddGoal;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class GoalsFragment extends Fragment {

    LinearLayout personalProfileView;
    BezierView bezierView;
    DoughnutChartView doughnutChartView;
    FinancialFragment financialFragment;
    ViewGroup financeContainer;
    FragmentTransaction ft;

    //TODO Add this View Dynamic
    LinearLayout goalContainer;
    LinearLayout addGoalBtn;

    //BlurView
    BlurView blurView;
    ViewGroup blurContainer;

    Bundle savedInstanceState;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.goals_fragment, container, false);
        personalProfileView = root.findViewById(R.id.financial_profiles);
        bezierView = root.findViewById(R.id.bezier_overview);
        financialFragment = new FinancialFragment();
        financeContainer = root.findViewById(R.id.finance_container);
        goalContainer = root.findViewById(R.id.goal1container);
        addGoalBtn = root.findViewById(R.id.addGoal);
        doughnutChartView = root.findViewById(R.id.goal_progress_doughnut);
        blurView = root.findViewById(R.id.finance_main_blurview);
        startBlurring(20);
        this.savedInstanceState = savedInstanceState;





// Note that we need the API version check here because the actual transition classes (e.g. Fade)
// are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
// ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            financialFragment.setSharedElementEnterTransition(new DetailsTransition(400));
            financialFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            financialFragment.setSharedElementReturnTransition(new DetailsTransition(400));
        }

        listeners();


        return root;
    }

    Context context;
    public GoalsFragment(Context context) {
        this.context = context;
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

    public void toggleAnimateBlur(final boolean blur){

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

    String blurChild = "";
    private void clearBlurView(){
        switch(blurChild){
            case "blur_module":
                doughnutChartView.animateDoughnutShrink();
                break;
            case "add_goal_module":
                break;
            default:
                break;
        }


    }

    private void listeners(){
        goalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final GoalDrawView clickedV = v.findViewWithTag("goalSimple");

                AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());

                asyncLayoutInflater.inflate(R.layout.goal_detail_layout, blurView, new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                        // Layout Asynchronously Inflated
                        assert parent != null;
                        parent.removeAllViews();
                        parent.addView(view);
                        //add Blur Child
                        blurChild = "goal_module";

                        doughnutChartView = parent.findViewById(R.id.goal_progress_doughnut);
                        doughnutChartView.setProgress((int)clickedV.getGoalProgress());
                        doughnutChartView.animateDoughnutGrow();

                        onViewCreated(view, savedInstanceState);
                    }
                });

                toggleAnimateBlur(true);
            }
        });

        addGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddGoal addGoalFragment = new AddGoal();

                ft = getChildFragmentManager().beginTransaction();
                ft.replace(blurView.getId(), addGoalFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                blurChild = "add_goal_module";
                ft.commit();

                toggleAnimateBlur(true);
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
                //toggleAnimateBlur(false);
                //clearBlurView();
            }

        });
    }


}
