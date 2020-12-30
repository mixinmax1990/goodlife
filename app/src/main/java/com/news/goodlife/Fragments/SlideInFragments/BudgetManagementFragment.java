package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.List;

public class BudgetManagementFragment extends Fragment {

    CardView slideinFragmentView;
    View openBreakdownTV, rootContent, newcat;
    SingletonClass singletonMain;
    FlexboxLayout budgetFlexCont;
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_budget_manage_layout, container, false);

        singletonMain = SingletonClass.getInstance();

        Log.i("HasCode Singleton", ""+ singletonMain.hashCode());
        slideinFragmentView = root.findViewById(R.id.slidein_fragment);
        openBreakdownTV = root.findViewById(R.id.openbudgetbreakdown);
        slideinFragmentView.setX(singletonMain.getDisplayWidth());
        rootContent = root.findViewById(R.id.budget_manage_content);
        newcat = root.findViewById(R.id.new_cat);
        budgetFlexCont = root.findViewById(R.id.budget_flex_cont);

        listeners();

        listSetBudgets();
        return root;
    }

    private void listSetBudgets() {

        List<BudgetModel> allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgets();

        for(BudgetModel budget: allBudgets){

            Log.i("BudgetCat ID", ""+budget.getCategoryid());
            //TODO ASYNC Inflate Following
            BudgetCategoryModel category = singletonMain.getDatabaseController().BudgetCategoryController.getBudgetCategory(Integer.parseInt(budget.getCategoryid()));

            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            View catitem = inflater.inflate(R.layout.budget_category_item, null);

            catitem.setTag(budget.id);
            TextView name = catitem.findViewById(R.id.category_item_name);
            name.setText(category.catname);

            //TODO Set Clicklistner to edit and detail view Budget

            IconDoughnutView dv = catitem.findViewById(R.id.icondoughnut);
            dv.setCategory(category.getCatcolor(), category.getCaticon());

            budgetFlexCont.addView(catitem);
        }
    }

    boolean clicked = false;
    boolean directionset = false;
    boolean directionHorizontal = false;
    float TDX,TDY, movedX, movedY;
    float barrier = 10;
    private void listeners() {
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //some code....
                        TDX = event.getX();
                        TDY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        directionset = false;
                        directionHorizontal = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!directionset){
                            movedX = Math.abs(TDX - event.getX());
                            movedY = Math.abs(TDY - event.getY());
                            if(movedX > barrier){
                                directionHorizontal = true;
                                directionset = true;
                                TDX = event.getX();
                            }
                            if(movedY > barrier){
                                directionHorizontal = false;
                                directionset = true;
                                TDY = event.getY();
                            }
                        }
                        else{
                            if(directionHorizontal){
                                movedX = TDX - event.getRawX();
                                //moving Horizontal
                                Log.i("Horizontal", ""+movedX);
                                if(movedX < 0){

                                }
                            }
                            else{
                                movedY = TDY - event.getRawY();
                                //Move Vertical
                                Log.i("Vertical", ""+movedY);
                            }
                        }

                        break;
                    default:
                        break;
                }
                return true;

            }
        });
        openBreakdownTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideIn("Breakdown");
            }
        });
        newcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideIn("NewCategory");
            }
        });

    }

    private Fragment deepFragment;
    private void slideIn(String fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch(fragment){
            case "Breakdown":
                deepFragment = new BudgetBreakdown();
                ft.replace(slideinFragmentView.getId(), deepFragment);
                break;
            case "NewCategory":
                deepFragment = new BudgetNew();
                ft.replace(slideinFragmentView.getId(), deepFragment);
            default:
                break;
        }
        ft.commit();

        ValueAnimator va;

        va = ValueAnimator.ofInt(singletonMain.getDisplayWidth(), 0);


        va.setDuration(250);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int animval = (int)valueAnimator.getAnimatedValue();
                slideinFragmentView.setX(animval);
                int move = singletonMain.getDisplayWidth() - animval;
                rootContent.setX(-(int)(move/3));
                //Set The both offset to be reset
                singletonMain.setOffsetViewChild(slideinFragmentView);
                singletonMain.setOffsetViewParent(rootContent);
                singletonMain.getUniversalBackarrow().setX(-(int)(move/3));
            }

        });

        va.start();
    }
}
