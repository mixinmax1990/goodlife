package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.CustomEntries.LabeledEntryView;
import com.news.goodlife.CustomViews.CustomEntries.SelectLinePointView;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class BudgetNew extends Fragment implements TextWatcher {
    View root, back;
    SingletonClass singletonMain = SingletonClass.getInstance();
    FlexboxLayout categories_flex;
    LabeledEntryView budget_amount_entry;
    SeekBar seekAmount;

    TextView remainingBudgetMonth, remainingBudgetYear;

    int remainingBudgetVal = 1300;
    int remainingBudgetYearVal = remainingBudgetVal * 12;


    int stepsAuthorized = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_budget_new_category, container, false);
        back = root.findViewById(R.id.backarrow);
        categories_flex = root.findViewById(R.id.category_flex_cont);
        budget_amount_entry = root.findViewById(R.id.budget_amount_entry);
        saveBudgetBtn = root.findViewById(R.id.saveBudgetButton);
        monthRadio = root.findViewById(R.id.month_radio);
        yearRadio = root.findViewById(R.id.year_radio);
        customRadio = root.findViewById(R.id.custom_radio);
        radioGroup = root.findViewById(R.id.frequency_radiogroup);
        seekAmount = root.findViewById(R.id.seekamount);
        remainingBudgetMonth = root.findViewById(R.id.remaining_amount);
        remainingBudgetYear = root.findViewById(R.id.remaining_year_amount);

        listeners();
        populateCategories();
        return root;
    }

    View selectedIcon = null;
    SelectLinePointView pointOne, pointTwo, pointThree, pointFour;
    private void populateCategories() {

        pointOne = root.findViewById(R.id.pointOne);
        pointTwo = root.findViewById(R.id.pointTwo);
        pointThree = root.findViewById(R.id.pointThree);
        pointFour = root.findViewById(R.id.pointFour);
        for(BudgetCategoryModel category: singletonMain.getDatabaseController().BudgetCategoryController.getAllBudgetCategories()){
            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            View catitem = inflater.inflate(R.layout.slidein_budget_new_category_catitem, null);

            //Set Category ID inside Tag

            catitem.setTag(category.getId());
            TextView name = catitem.findViewById(R.id.category_item_name);
            name.setText(category.getCatname());

            //Get Points
            catitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedIcon != null){
                        View selected = selectedIcon.findViewById(R.id.selected);
                        selected.setVisibility(View.GONE);
                    }
                    selectedIcon = v;
                    v.findViewById(R.id.selected).setVisibility(View.VISIBLE);
                    selectedcatID = selectedIcon.getTag().toString();
                    pointOne.select();

                    Log.i("SelectedCategory", ""+selectedcatID);
                }
            });

            IconDoughnutView dv = catitem.findViewById(R.id.icondoughnut);
            dv.setCategory(category.getCatcolor(), category.getCaticon());
            categories_flex.addView(catitem);
        };

        LayoutInflater inflater = LayoutInflater.from(root.getContext());
        View catitem = inflater.inflate(R.layout.slidein_budget_new_category_catitem, null);
        IconDoughnutView dv = catitem.findViewById(R.id.icondoughnut);
        dv.addCat();

        TextView name = catitem.findViewById(R.id.category_item_name);
        name.setText("Custom");
        categories_flex.addView(catitem);

    }

    private void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeThis();
            }
        });

        budget_amount_entry.addTextChangedListener(this);

        saveBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> saveCallback = saveBudget();
                Log.i("ErrorSize", ""+saveCallback.size());
                if(saveCallback.size() != 0){
                    StringBuilder allErrorsString = new StringBuilder();
                    for(String error: saveCallback){
                        allErrorsString.append(error).append("\n");
                    }
                    Toast.makeText(getContext(), allErrorsString, LENGTH_LONG).show();
                }
               // if(saveCallback.size()){};
            }
        });

        // Frequency Radios
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pointThree.select();
                pointFour.select();

                RadioButton selected = group.findViewById(checkedId);
                selectedFrequency = selected.getText().toString();
                //Todo Start Date and EndDate

                selectedStartDate = "now";
                selectedEndData = "never";
            }
        });

        seekAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Progress", ""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    RadioGroup radioGroup;

    RadioButton monthRadio;
    RadioButton yearRadio;
    RadioButton customRadio;

    View saveBudgetBtn;
    private List<String> saveBudget(){

        List<String> errorList = new ArrayList<>();

        String error = null;
        if(!pointOne.selected){
            error = "Select a Budget Category";
            errorList.add(error);
        }
        if(!pointTwo.selected){
            error = "Allocate Budget Amount";
            errorList.add(error);
        }
        if(!pointThree.selected){
            error = "Finish Step 3";
            errorList.add(error);
        }
        if(!pointFour.selected){
            error = "Finish Step 4";
            errorList.add(error);
        }

        if(error == null){
            //Save the Data
            BudgetModel budget = new BudgetModel();
            budget.setCategoryid(selectedcatID);
            budget.setAmount(selectedAmount);
            budget.setFrequency(selectedFrequency);
            budget.setStartdate(selectedStartDate);
            budget.setEnddate(selectedEndData);

            singletonMain.getDatabaseController().BudgetController.addBudget(budget);

            List<BudgetModel> allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgets();

            Log.i("AllBudgets", "-"+allBudgets.size());

        }

        return errorList;
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


    String selectedcatID = "";
    String selectedStartDate = "";
    String selectedEndData = "";
    String selectedFrequency = "";
    String selectedAmount = "";

    public String getSelectedcatID() {
        return selectedcatID;
    }

    public void setSelectedcatID(String selectedcatID) {
        this.selectedcatID = selectedcatID;
    }

    public String getSelectedStartDate() {
        return selectedStartDate;
    }

    public void setSelectedStartDate(String selectedStartDate) {
        this.selectedStartDate = selectedStartDate;
    }

    public String getSelectedEndData() {
        return selectedEndData;
    }

    public void setSelectedEndData(String selectedEndData) {
        this.selectedEndData = selectedEndData;
    }

    public String getSelectedFrequency() {
        return selectedFrequency;
    }

    public void setSelectedFrequency(String selectedFrequency) {
        this.selectedFrequency = selectedFrequency;
    }

    public String getSelectedAmount() {
        return selectedAmount;
    }

    public void setSelectedAmount(String selectedAmount) {
        this.selectedAmount = selectedAmount;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        Log.i("Text", ""+s.toString());
        selectedAmount = s.toString();
        pointTwo.select();
    }
}
