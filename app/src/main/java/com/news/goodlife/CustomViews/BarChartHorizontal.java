package com.news.goodlife.CustomViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.service.autofill.FillCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.news.goodlife.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BarChartHorizontal extends ConstraintLayout {
    Paint barBackgroundPos, barBackgroundNeg;
    Paint baselinePaint, sectionlinePaint;

    @ColorInt int barBackgroundPosDarkColor, barBackgroundNegDarkColor;
    @ColorInt int barBackgroundPosLightColor, barBackgroundNegLightColor;
    @ColorInt int baselineColor;

    boolean darkMode;

    int bars, sectionHeight, timelineWidth, chartCanvasHeight, largestBudget;
    int marginBottom, marginStartEnd;
    List<ChartData> allChartData = new ArrayList<>();
    RectF chartLine = new RectF(0,0,0,0);


    public BarChartHorizontal(@NonNull Context context) {
        super(context);
    }

    public BarChartHorizontal(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        darkMode = getResources().getBoolean(R.bool.dark);

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        baselineColor = typedValue.data;

        barBackgroundPosDarkColor = Color.parseColor("#59a7ff");
        barBackgroundNegDarkColor = Color.parseColor("#ff6859");
        setPaints();
        setTestData();

        marginBottom = dpToPx(10);
        marginStartEnd = dpToPx(20);
        setLargestBudget(220);



        setWillNotDraw(false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        sectionHeight = (getHeight() - marginBottom) / 5;
        timelineWidth = (getWidth() - (marginStartEnd * 2))/ allChartData.size();

        super.onDraw(canvas);

        baselinePaint.setAlpha(30);
        //Draw Section Lines
        canvas.drawLine(marginStartEnd, getHeight() - (marginBottom + sectionHeight * 2), getWidth() - marginStartEnd, getHeight() - (marginBottom + sectionHeight * 2), baselinePaint);
        canvas.drawLine(marginStartEnd, getHeight() - (marginBottom + sectionHeight * 3), getWidth() - marginStartEnd, getHeight() - (marginBottom + sectionHeight * 3), baselinePaint);
        canvas.drawLine(marginStartEnd, getHeight() - (marginBottom + sectionHeight * 4), getWidth() - marginStartEnd, getHeight() - (marginBottom + sectionHeight * 4), baselinePaint);

        //Bottom Seperator Line
        baselinePaint.setAlpha(50);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), baselinePaint);

        //Draw BaseLine
        baselinePaint.setAlpha(255);
        int baselineY = getHeight() - (marginBottom + sectionHeight);
        chartCanvasHeight = baselineY;
        canvas.drawLine(marginStartEnd, baselineY, getWidth() - marginStartEnd, baselineY, baselinePaint);

        //Draw Chart

        int count = 0;
        int lineHeight = 20;
        int currentSectionX = marginStartEnd;
        //TODO change Ratio
        float budgetHeightRatio = 1.4f;
        int spaceBetweenLines = dpToPx(3);

        Log.i("BudgetRatio", "Ratio = "+budgetHeightRatio);

        for(ChartData chartData: allChartData){
            count++;

            int xCenter = currentSectionX + (timelineWidth/2);
            baselinePaint.setAlpha(255);
            //Find the center of the section
            canvas.drawLine(xCenter, baselineY, xCenter, baselineY + lineHeight, baselinePaint);
            if(!chartData.active){
                baselinePaint.setAlpha(100);
            }
            canvas.drawText(chartData.description, xCenter,baselineY + lineHeight + 30,baselinePaint);

            int budgetHeight = (int) (budgetHeightRatio * chartData.budget);
            int amountHeight = (int) (budgetHeightRatio * chartData.value);

            //First Draw The Budget Line
            barBackgroundPos.setAlpha(50);

            chartLine.left = currentSectionX + spaceBetweenLines;
            chartLine.top = (baselineY - dpToPx(5)) - budgetHeight;
            chartLine.right = (currentSectionX + timelineWidth) - spaceBetweenLines;
            chartLine.bottom = baselineY - dpToPx(5);

            //canvas.drawRoundRect(chartLine, 100,100, barBackgroundPos);

            if(chartData.active){
                barBackgroundPos.setAlpha(255);
                if(count == 11){
                    barBackgroundPos.setColor(barBackgroundNegDarkColor);
                }
                else{
                    barBackgroundPos.setColor(barBackgroundPosDarkColor);
                }
            }
            else{
                barBackgroundPos.setAlpha(75);
            }



            chartLine.top = (baselineY - dpToPx(5)) - amountHeight;
            canvas.drawRoundRect(chartLine, 100,100, barBackgroundPos);


            currentSectionX = currentSectionX + timelineWidth;
        }

    }


    private void setPaints() {

        barBackgroundPos = new Paint();
        barBackgroundPos.setStyle(Paint.Style.FILL);
        barBackgroundPos.setAntiAlias(true);

        barBackgroundNeg = new Paint();
        barBackgroundNeg.setStyle(Paint.Style.FILL);
        barBackgroundNeg.setAntiAlias(true);

        baselinePaint = new Paint();
        baselinePaint.setStyle(Paint.Style.FILL);
        baselinePaint.setAntiAlias(true);
        baselinePaint.setColor(baselineColor);
        baselinePaint.setTextAlign(Paint.Align.CENTER);
        baselinePaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);


        if(darkMode){
            barBackgroundPos.setColor(barBackgroundPosDarkColor);
            barBackgroundNeg.setColor(barBackgroundNegDarkColor);
        }
        else{
            barBackgroundPos.setColor(barBackgroundPosLightColor);
            barBackgroundNeg.setColor(barBackgroundNegLightColor);
        }

    }

    public int getLargestBudget() {
        return largestBudget;
    }

    public void setLargestBudget(int largestBudget) {
        this.largestBudget = largestBudget;
    }

    public int getBars() {
        return bars;
    }

    public void setBars(int bars) {
        this.bars = bars;
    }

    public List<ChartData> getAllChartData() {
        return allChartData;
    }


    private void setTestData() {

        ChartData data;
        //Previous week
        data = new ChartData(170, 10, "M", false);
        allChartData.add(data);

        data = new ChartData(170, 120, "D", false);
        allChartData.add(data);

        data = new ChartData(170, 20, "M", false);
        allChartData.add(data);

        data = new ChartData(170, 100, "D",false);
        allChartData.add(data);

        data = new ChartData(170, 170, "F", false);
        allChartData.add(data);

        data = new ChartData(170, 15 ,"S",false);
        allChartData.add(data);

        data = new ChartData(170, 30 ,"S", false);
        allChartData.add(data);


        //this Week

        data = new ChartData(200, 120, "M", true);
        allChartData.add(data);

        data = new ChartData(200, 60, "D", true);
        allChartData.add(data);

        data = new ChartData(200, 40, "M", true);
        allChartData.add(data);

        data = new ChartData(200, 150, "D",true);
        allChartData.add(data);

        data = new ChartData(200, 170, "F", true);
        allChartData.add(data);

        data = new ChartData(200, 90 ,"S",true);
        allChartData.add(data);

        data = new ChartData(200, 20 ,"S", true);
        allChartData.add(data);

        // Next week

        data = new ChartData(220, 220, "M", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "D", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "M", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "D", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "F", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "S", false);
        allChartData.add(data);

        data = new ChartData(220, 220, "S", false);
        allChartData.add(data);

    }


    private class ChartData{
        int budget;
        int value;
        String description;
        boolean active;

        public ChartData(int budget, int value, String description, boolean active) {
            this.budget = budget;
            this.value = value;
            this.description = description;
            this.active = active;
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

