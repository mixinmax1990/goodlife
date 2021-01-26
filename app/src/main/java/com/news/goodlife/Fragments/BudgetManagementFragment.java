package com.news.goodlife.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.Fragments.PopFragments.NewBudget;
import com.news.goodlife.Fragments.SlideInFragments.BudgetBreakdown;
import com.news.goodlife.Fragments.SlideInFragments.BudgetNew;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.List;

public class BudgetManagementFragment extends Fragment {

    CardView slideinFragmentView;
    View rootContent, newcat;
    SingletonClass singletonMain;
    FlexboxLayout budgetFlexCont;
    View root;
    View new_budget_cat, new_budget_cat_darkframe, addCategorySign, addCategoryTitle;
    EditText enterCatName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.budget_fragment, container, false);

        singletonMain = SingletonClass.getInstance();

        Log.i("HasCode Singleton", ""+ singletonMain.hashCode());
        slideinFragmentView = root.findViewById(R.id.slidein_fragment);
        rootContent = root.findViewById(R.id.budget_manage_content);
        newcat = root.findViewById(R.id.new_cat);
        budgetFlexCont = root.findViewById(R.id.budget_frag_budget_cat_flexcont);

        //Get the Categories
        AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
        inflateCategories(asyncLayoutInflater);

        //New Category Item


        listeners();

        //listSetBudgets();
        return root;
    }

    int inflatedCategories = 0;
    int catNo;
    boolean startItem = true;
    private void inflateCategories(AsyncLayoutInflater inflater) {
        //SET this as Category size

        List<BudgetCategoryModel> allCategories = singletonMain.getDatabaseController().BudgetCategoryController.getAllBudgetCategories();
        catNo = allCategories.size();

        Log.i("CatSIze" , "="+catNo);
        if(catNo == 0){
            inflateAddCategory(inflater);
            return;
        }

        for(int i = 0; i  < catNo; i++){

            int layout;
            if(startItem){
                layout = R.layout.budget_fragment_flexitem_start;
                startItem = false;
            }
            else{
                layout = R.layout.budget_fragment_flexitem_end;
                startItem = true;
            }
            final String catname = allCategories.get(i).getCatname();

            inflater.inflate(layout, budgetFlexCont, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                    inflatedCategories++;
                    ViewGroup flexParent = view.findViewById(R.id.budgetitems_flex_container);
                    TextView catnameTV = view.findViewById(R.id.category_name);
                    catnameTV.setText(catname);

                    inflateAddBudget(flexParent, inflater);
                    budgetFlexCont.addView(view);
                    inflateAddCategory(inflater);
                }
            });

        }
    }

    View addCategoryView = null;
    private void inflateAddCategory(AsyncLayoutInflater inflater){
        if(addCategoryView != null){
            budgetFlexCont.removeView(addCategoryView);
        }
        if(inflatedCategories == catNo){
            //Inflate the addNewCat

            int layout;
            if(startItem){
                layout = R.layout.budget_fragment_flexitem_newcat_start;
            }
            else{
                layout = R.layout.budget_fragment_flexitem_newcat_end;
            }
            inflater.inflate(layout, budgetFlexCont, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                    new_budget_cat = view.findViewById(R.id.new_budget_cat);
                    new_budget_cat_darkframe = view.findViewById(R.id.add_budget_cat_darkcont);
                    addCategorySign = view.findViewById(R.id.add_category_sign);
                    enterCatName = view.findViewById(R.id.new_category_name);
                    addCategoryTitle = view.findViewById(R.id.add_category_title);
                    addCategoryView = view;

                    new_budget_cat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            expandNewCat();
                        }
                    });

                    enterCatName.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                // Perform action on key press

                                if(event.getAction() == KeyEvent.ACTION_UP){
                                    //Save the new Category
                                    BudgetCategoryModel budgetCategory = new BudgetCategoryModel();
                                    budgetCategory.setCatname(enterCatName.getText().toString());
                                    budgetCategory.setChildcount("0");
                                    singletonMain.getDatabaseController().BudgetCategoryController.addBudgetCategory(budgetCategory);

                                    //Now Place New Category Container
                                    singletonMain.toggleFadeView(false, view, new SuccessCallback() {
                                        @Override
                                        public void success() {

                                            inflateNewCategory(inflater);
                                        }

                                        @Override
                                        public void error() {

                                        }
                                    });

                                }
                                return false;
                            }
                            return false;
                        }
                    });



                    budgetFlexCont.addView(view);
                    singletonMain.toggleFadeView(true, view, new SuccessCallback() {
                        @Override
                        public void success() {

                        }

                        @Override
                        public void error() {

                        }
                    });
                }
            });

        }
    }

    private void inflateNewCategory(AsyncLayoutInflater inflater) {
        BudgetCategoryModel newCategory = singletonMain.getDatabaseController().BudgetCategoryController.getLatestBudget();

        int layout;
        if(startItem){
            layout = R.layout.budget_fragment_flexitem_start;
            startItem = false;
        }
        else{
            layout = R.layout.budget_fragment_flexitem_end;
            startItem = true;
        }

        inflater.inflate(layout, budgetFlexCont, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                ViewGroup flexParent = view.findViewById(R.id.budgetitems_flex_container);
                TextView catnameTV = view.findViewById(R.id.category_name);

                catnameTV.setText(newCategory.getCatname());

                inflateAddBudget(flexParent, inflater);
                budgetFlexCont.addView(view);
                inflateAddCategory(inflater);

                singletonMain.toggleFadeView(true ,view, new SuccessCallback() {
                    @Override
                    public void success() {
                        //Finished Animating

                    }

                    @Override
                    public void error() {

                    }
                });
                //FadeIn the View
            }
        });

    }


    private void inflateAddBudget(ViewGroup parent, AsyncLayoutInflater inflater){

        inflater.inflate(R.layout.budget_fragment_category_flexitem_newbudget, parent, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.replace(slideinFragmentView.getId(), new NewBudget(new SuccessCallback() {
                            @Override
                            public void success() {
                                //Successfully stored new Budget
                            }

                            @Override
                            public void error() {
                                //Closed the Budget Without Saving
                                singletonMain.toggleFadeView(false, slideinFragmentView, new SuccessCallback() {
                                    @Override
                                    public void success() {
                                        slideinFragmentView.removeAllViews();

                                    }

                                    @Override
                                    public void error() {

                                    }
                                });

                            }
                        }));

                        ft.commit();

                        singletonMain.toggleFadeView(true, slideinFragmentView, new SuccessCallback() {
                            @Override
                            public void success() {


                            }

                            @Override
                            public void error() {

                            }
                        });
                    }
                });

                parent.addView(view);
            }
        });

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
            //dv.setCategory(category.getCatcolor(), category.getCaticon());

            //budgetFlexCont.addView(catitem);
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
       /*openBreakdownTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideIn("Breakdown");
            }
        });*/
        /*
       */

    }


    ViewGroup.LayoutParams newCatLP;
    private void expandNewCat() {
        newCatLP = (ViewGroup.LayoutParams) new_budget_cat_darkframe.getLayoutParams();
        getDimensions();

        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        va.setDuration(350);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animVal = (float)animation.getAnimatedValue();

                int catGrowth = (int)(newCategoryBackgroundWidth + (newCatGrowthValue * animVal));

                newCatLP.width = catGrowth;
                newCatLP.height = catGrowth;

                new_budget_cat_darkframe.setLayoutParams(newCatLP);

                addCategorySign.setRotationY(90 * animVal);

                enterCatName.setAlpha(animVal);
                addCategoryTitle.setAlpha(1- animVal);
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                enterCatName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                enterCatName.setHint("Category Name");
                //addCategoryTitle.setVisibility(View.GONE);
                enterCatName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(enterCatName, InputMethodManager.SHOW_IMPLICIT);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }

    int categoryBackgroundMaxWidth, newCategoryBackgroundWidth;
    int newCatGrowthValue;
    private void getDimensions() {
        categoryBackgroundMaxWidth = budgetFlexCont.getWidth()/2 - (singletonMain.dpToPx(15));
        newCategoryBackgroundWidth = singletonMain.dpToPx(50);

        newCatGrowthValue = categoryBackgroundMaxWidth - newCategoryBackgroundWidth;

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
