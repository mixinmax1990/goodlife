package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import com.news.goodlife.CustomViews.CustomEntries.LabeledTextView;
import com.news.goodlife.CustomViews.CustomEntries.SelectLinePointView;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.CustomViews.ProgressFrameView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class BudgetNew extends Fragment implements TextWatcher {
    View root, back;
    SingletonClass singletonMain = SingletonClass.getInstance();
    FlexboxLayout categories_flex;
    LabeledEntryView budget_amount_entry;
    SeekBar seekAmount, seekAmountFine;
    TextView finemin, finemax;
    float fineminval = 0;
    float finemaxval = 50;

    TextView remainingPrimary, remainingSecondary;
    TextView remainingPrimaryTitle, remainingSecondaryTitle;

    int remainingBudgetVal = 1300;
    int remainingBudgetYearVal = remainingBudgetVal * 12;

    ProgressFrameView progressOne, progressTwo, progressThree;

    //Date Etry Objects
    LabeledTextView startDateEntry, endDateEntry;
    View calendarPop;
    TextView calendarTitle, calendarSet;
    CalendarView calendarView;


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
        radioGroup = root.findViewById(R.id.frequency_radiogroup);
        seekAmount = root.findViewById(R.id.seekamount);
        seekAmountFine = root.findViewById(R.id.seekamountfine);
        finemin = root.findViewById(R.id.finemin);
        finemax = root.findViewById(R.id.finemax);

        finemin.setText(castToMoney(fineminval));
        finemax.setText(castToMoney(finemaxval));

        remainingPrimary = root.findViewById(R.id.remaining_primary);
        remainingSecondary = root.findViewById(R.id.remaining_secondary);
        remainingPrimaryTitle = root.findViewById(R.id.remaining_primary_title);
        remainingSecondaryTitle = root.findViewById(R.id.remaining_secondary_title);

        progressOne = root.findViewById(R.id.progressOne);
        progressTwo = root.findViewById(R.id.progressTwo);
        progressThree = root.findViewById(R.id.progressThree);

        startDateEntry = root.findViewById(R.id.startdate_entry);
        endDateEntry = root.findViewById(R.id.enddate_entry);
        calendarPop = root.findViewById(R.id.pop_calendar);
        calendarTitle = root.findViewById(R.id.calendar_title);
        calendarSet = root.findViewById(R.id.set_calendar);
        calendarView = root.findViewById(R.id.calendar);


        selectedStartDate = ""+ Calendar.getInstance().getTime().getTime();
        selectedEndDate = "never";
        selectedPeriod = "Month";

        Log.i("HasCode Singleton", ""+ singletonMain.hashCode());

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

            //TODO ASYNC Inflate Following
            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            View catitem = inflater.inflate(R.layout.budget_category_item, null);

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
                    progressOne.animateProgress(true);

                    Log.i("SelectedCategory", ""+selectedcatID);
                }
            });

            IconDoughnutView dv = catitem.findViewById(R.id.icondoughnut);
            dv.setCategory(category.getCatcolor(), category.getCaticon());
            categories_flex.addView(catitem);
        };

        LayoutInflater inflater = LayoutInflater.from(root.getContext());
        View catitem = inflater.inflate(R.layout.budget_category_item, null);
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

                RadioButton selected = group.findViewById(checkedId);
                selectedPeriod = selected.getText().toString();

                changePeriod();
            }
        });

        seekAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                allocated = (remainingBudgetVal / 100) * progress;
                float remaining = remainingBudgetVal - allocated;
                if(booleanMonth){
                    remainingPrimary.setText(castToMoney(remaining));
                    remainingSecondary.setText(castToMoney(remaining * 12));
                    budget_amount_entry.setText(castToMoney(allocated));

                    finerange = getRange(allocated);
                    fineminval = allocated - finerange;

                    finemaxval = allocated + finerange;
                    finemin.setText(castToMoney(fineminval));
                    finemax.setText(castToMoney(finemaxval));

                    selectedAmount = ""+(allocated);
                }
                else{
                    remainingPrimary.setText(castToMoney(remaining * 12));
                    remainingSecondary.setText(castToMoney(remaining));
                    budget_amount_entry.setText(castToMoney(allocated * 12));

                    selectedAmount = ""+(allocated * 12);
                }

                if(allocated > 0){
                    pointTwo.select();
                    pointThree.select();
                    progressTwo.animateProgress(true);
                    seekAmountFine.setProgress(50);
                }
                else{
                    progressTwo.animateProgress(false);
                    seekAmountFine.setProgress(0);
                    finemaxval = 50;
                    finemax.setText(castToMoney(finemaxval));

                }




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekAmountFine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(fineminval > 0){
                    int val = ((finerange * 2) / 100) * i;

                    float fineallocated = (allocated - finerange) + val;

                    float remaining = remainingBudgetVal - fineallocated;

                    if(booleanMonth){
                        remainingPrimary.setText(castToMoney(remaining));
                        remainingSecondary.setText(castToMoney(remaining * 12));
                        budget_amount_entry.setText(castToMoney(fineallocated));

                        selectedAmount = ""+(fineallocated);
                    }
                    else{



                    }


                }
                else{
                    int val = Math.round(.5f * i);

                    Log.i("VAl", ""+val);

                    float fineallocated = allocated + val;

                    float remaining = remainingBudgetVal - fineallocated;

                    if(booleanMonth){
                        remainingPrimary.setText(castToMoney(remaining));
                        remainingSecondary.setText(castToMoney(remaining * 12));
                        budget_amount_entry.setText(castToMoney(fineallocated));

                        selectedAmount = ""+(fineallocated);
                    }
                    else{


                    }

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        startDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarTitle.setText("Start Date");
                calendarPop.setVisibility(View.VISIBLE);
                startDate = true;
            }
        });

        endDateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarTitle.setText("End Date");
                calendarPop.setVisibility(View.VISIBLE);
                startDate = false;
            }
        });

        calendarSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarPop.setVisibility(View.GONE);

                setDatePeriod();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.DAY_OF_MONTH, i2);
                cal.set(Calendar.MONTH, i1);
                cal.set(Calendar.YEAR, i);

                if(startDate){
                    selectedStartCalendar.setTime(cal.getTime());
                }
                else{
                    selectedEndCalendar.setTime(cal.getTime());
                }

                Log.i("Date Selected Month", ""+selectedStartCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });
    }

    float allocated;
    int finerange = 50;
    private int getRange(float allocated) {
        int range;

        float rangeFrac = allocated / 2;

        if(rangeFrac > 50){
            range = 50;
        }
        else{
            range = (int)rangeFrac;
        }

        return range;
    }

    Calendar selectedStartCalendar = Calendar.getInstance();
    Calendar selectedEndCalendar = Calendar.getInstance();

    private void setDatePeriod() {

        //Log.i("Date Selected Month", ""+selectedCalendar.get(Calendar.MONTH));
        if(startDate){
            startDateEntry.focus(true);
            String dateString =
                    selectedStartCalendar.get(Calendar.DAY_OF_MONTH) + "/" +
                    selectedStartCalendar.get(Calendar.MONTH) + "/" +
                    selectedStartCalendar.get(Calendar.YEAR);
            startDateEntry.setText(dateString);

            selectedStartDate = ""+selectedStartCalendar.getTime().getTime();

        }
        else{
            endDateEntry.focus(true);

            String dateString =
                    selectedEndCalendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            selectedEndCalendar.get(Calendar.MONTH) + "/" +
                            selectedEndCalendar.get(Calendar.YEAR);
            endDateEntry.setText(dateString);

            selectedEndDate = ""+selectedEndCalendar.getTime().getTime();

        }
    }

    private boolean startDate = true;

    private boolean booleanMonth = true;
    private void changePeriod() {

        switch(selectedPeriod){
            case "Month":
                seekAmount.setProgress(0);
                remainingPrimary.setText(castToMoney(remainingBudgetVal));
                remainingSecondary.setText(castToMoney(remainingBudgetYearVal));
                remainingPrimaryTitle.setText("Month");
                remainingSecondaryTitle.setText("Year");
                booleanMonth = true;

                break;
            case "Year":
                seekAmount.setProgress(0);
                remainingPrimary.setText(castToMoney(remainingBudgetYearVal));
                remainingSecondary.setText(castToMoney(remainingBudgetVal));
                remainingPrimaryTitle.setText("Year");
                remainingSecondaryTitle.setText("Month");

                booleanMonth = false;
                break;
            default:
                break;
        }

    }

    private String castToMoney(float amount){

        //Two Decimal Places

        double amountDec = twoDec(amount);

        StringBuilder cash = new StringBuilder();
        String string = amountDec + "€";
        cash.append(string);

        return cash.toString();

    }

    private double twoDec(float amount) {
        double x = amount;
        double y = Math.floor(x * 100) / 100;

        return y;
    }

    RadioGroup radioGroup;

    RadioButton monthRadio;
    RadioButton yearRadio;

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

        if(error == null){
            //Save the Data
            BudgetModel budget = new BudgetModel();
            budget.setCategoryid(selectedcatID);
            budget.setAmount(selectedAmount);
            budget.setFrequency(selectedPeriod);
            budget.setStartdate(selectedStartDate);
            budget.setEnddate(selectedEndDate);

            Log.i("BudgetID SELCTED", ""+budget.getCategoryid());

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
    String selectedEndDate = "";
    String selectedPeriod = "";
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

    public String getSelectedEndDate() {
        return selectedEndDate;
    }

    public void setSelectedEndDate(String selectedEndDate) {
        this.selectedEndDate = selectedEndDate;
    }

    public String getSelectedPeriod() {
        return selectedPeriod;
    }

    public void setSelectedPeriod(String selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
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

        double parsedCash = Double.parseDouble(removeCurrency(s.toString()));

        int newPerc, remainingMonth;
        newPerc = (int)((100/remainingBudgetVal) * parsedCash);
        remainingMonth = remainingBudgetVal - (int)Double.parseDouble(removeCurrency(s.toString()));

        if(booleanMonth){

            remainingPrimary.setText(castToMoney(remainingMonth));
            remainingSecondary.setText(castToMoney(remainingMonth * 12));
        }
        else{
            remainingPrimary.setText(castToMoney(remainingMonth * 12));
            remainingSecondary.setText(castToMoney(remainingMonth));
        }

        //seekAmount.setProgress(newPerc);

        selectedAmount = s.toString();
        pointTwo.select();
        progressTwo.animateProgress(true);
    }

    public String removeCurrency(String str) {
        //TODO make sure that all Letters at the end are removed
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
