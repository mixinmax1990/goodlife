package com.news.goodlife.CustomViews;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.news.goodlife.Fragments.WalletMultiDaysFragment;
import com.news.goodlife.Interfaces.MonthCashflowBezierCallback;
import com.news.goodlife.Models.BezierCurvePoint;
import com.news.goodlife.Models.DayCashflowModel;
import com.news.goodlife.Models.MonthCashflowModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.VIBRATOR_SERVICE;

public class MonthCashflowBezier extends View {

    Paint line, curve, curveselected, areaSelected, point, pointselected, textPaint;
    Path curvePath, selectedCurvePath, areaSelectedPath;

    public List<MonthCashflowModel> bezierData;
    private float smallestAmmount;
    private float largestAmount;
    private Vibrator myVib;

    WalletMultiDaysFragment parent;


    private int moveMonth;


    public MonthCashflowBezier(Context context) {
        super(context);
    }

    public MonthCashflowBezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        setWillNotDraw(false);
        setPaints();
    }

    public void setParent(WalletMultiDaysFragment parent) {
        this.parent = parent;
    }

    private void setPaints() {
        line = new Paint();
        line.setStyle(Paint.Style.STROKE);
        line.setColor(Color.WHITE);
        line.setAntiAlias(true);
        line.setAlpha(50);
        line.setStrokeWidth(1);

        point = new Paint();
        point.setStyle(Paint.Style.FILL);
        point.setColor(Color.WHITE);
        point.setAntiAlias(true);

        curve = new Paint();
        curve.setStyle(Paint.Style.STROKE);
        curve.setColor(Color.WHITE);
        curve.setAntiAlias(true);
        curve.setStrokeWidth(12);
        curve.setAlpha(100);

        curveselected = new Paint();
        curveselected.setStyle(Paint.Style.STROKE);
        curveselected.setColor(Color.parseColor("#FFFFFF"));
        curveselected.setAntiAlias(true);
        curveselected.setStrokeWidth(12);
        curveselected.setAlpha(250);
        
        areaSelected = new Paint();
        areaSelected.setStyle(Paint.Style.FILL);
        //areaSelected.setColor(Color.parseColor("#6AA8EC0A"));
        areaSelected.setAntiAlias(true);

        pointselected = new Paint();
        pointselected.setStyle(Paint.Style.FILL);
        pointselected.setColor(Color.parseColor("#FFFFFF"));
        pointselected.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

    }

    boolean curveSet = false;
    int baseLine;
    int width , height;
    boolean once = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        setMatrix();
        
        moveMonth = - width * 2;

        dayWidth = getWidth()/visibleDays;

        final float zero = getAmountPos(30);
        final float middle = getAmountPos(largestAmount / 2);
        final float top = getAmountPos(largestAmount);

            canvas.drawLine(0, zero, width, zero, line);
            canvas.drawLine(0, middle, width, middle, line);
            canvas.drawLine(0, top, width, top, line);


        drawGraph(canvas);



    }

    boolean getsNegative = false;
    float valToHeightRatio;

    //TODO figure out how to handle Largest Negative
    private void setMatrix() {
        if(smallestAmmount < 0){
            //Add the
            getsNegative = true;
            float valSpectrum = Math.abs(smallestAmmount) + largestAmount;
            valToHeightRatio = (height - 15)/ valSpectrum;
        }
        else{
            valToHeightRatio = (height - 15)/ largestAmount;
        }

    }

    private float getAmountPos(float Amount){
        //if its Negative add the smallest Ammount to the Amount
        if(getsNegative){
            Amount += Math.abs(smallestAmmount);
        }

        float position = getHeight() - (int)(Amount * valToHeightRatio);
        return position;
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    int visibleDays = 35;
    int dayWidth;
    public void setBezierData(List<MonthCashflowModel> bezierData) {
        this.bezierData = bezierData;
        //Set First Day
        setFirstAndLastDays();
        // Invalidate a draw the the Curve


    }

    String scrolledDate = "0";


    public void setScrolledDate(String scrolledDate) {
        this.scrolledDate = scrolledDate;
        Log.i("ScrolledPosition", ""+scrolledDate);
        isPastCurve = true;
        invalidate();
    }

    LinearGradient arealg;
    //List<int>
    //Curve Parameters
    float x0,y0,x1,y1,x2,y2;
    BezierCurvePoint lastPoint;
    int currentX = 0;
    int circlePosition = 0;
    private void drawGraph(Canvas canvas) {
        //Log.i("DrawGraph", "Runs");
        curvePath = new Path();
        selectedCurvePath = new Path();
        areaSelectedPath = new Path();
        float selectedPX = -100;
        float selectedPY = -100;
        Date selectedDay = new Date();
        String selectedAmount = "";

        //first iterate thrue the first empty days
        currentX = moveMonth + dayWidth/2;
        lastPoint = new BezierCurvePoint((float)getAmountPos(0),(float)currentX);
        curvePath.moveTo(lastPoint.getTime(), lastPoint.getAmount());
        selectedCurvePath.moveTo(lastPoint.getTime(), lastPoint.getAmount());
        areaSelectedPath.moveTo(lastPoint.getTime(), lastPoint.getAmount());

        for(int i = 0; i < emptyFirstDays; i++){
            float amountPos = getAmountPos(0);
            canvas.drawCircle(currentX, amountPos, 6, point);
            //Set The Curve Path While iterating
            setCurvePoint(new BezierCurvePoint(amountPos, (float)currentX), false);
            currentX += dayWidth;
        }
        //then Iterate thru the Data

        for(MonthCashflowModel month: bezierData){
            for(DayCashflowModel day : month.getMonthCashflow()){
                float amountPos = getAmountPos(day.getAmount());
                //Log.i("BezierDataDays", ""+circlePosition);
                if(scrolledDate.equals(""+day.getDate().getTime())){
                    Log.i("DayMatched", "MatchFound at"+circlePosition);


                    selectedPY = amountPos;
                    selectedPX = currentX;
                    selectedAmount = ""+day.getAmount();
                    selectedDay.setTime(day.getDate().getTime());
                    canvas.drawCircle(currentX, amountPos, 6, point);
                    setCurvePoint(new BezierCurvePoint(amountPos, (float)currentX), true);
                }
                else{
                    canvas.drawCircle(currentX, amountPos, 6, point);
                    setCurvePoint(new BezierCurvePoint(amountPos, (float)currentX), false);
                }


                currentX += dayWidth;
                circlePosition++;
            }
        }

        //than iterate thrue the last empty days

        for(int i = 0; i < emptyLastDays; i++){
            float amountPos = getAmountPos(0);
            canvas.drawCircle(currentX, amountPos, 6, point);

            setCurvePoint(new BezierCurvePoint(amountPos, (float)currentX), true);

            currentX += dayWidth;
        }

        arealg = new LinearGradient(0,0,0,getHeight(), Color.parseColor("#82D7E1"),Color.parseColor("#82D7E1"), Shader.TileMode.CLAMP);
        areaSelected.setShader(arealg);
        areaSelected.setAlpha(80);

        //Draw Bezier Curve
        canvas.drawPath(curvePath, curve);
        canvas.drawPath(selectedCurvePath, curveselected);
        //canvas.drawPath(areaSelectedPath, areaSelected);
        canvas.drawCircle(selectedPX, selectedPY, 30, pointselected);
        Calendar selCal = Calendar.getInstance();
        selCal.setTime(selectedDay);
        canvas.drawText(""+selCal.get(Calendar.DAY_OF_MONTH), selectedPX,selectedPY + 10, textPaint);

        parent.setNewDay(selCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()), selectedAmount);
    }
    boolean isPastCurve = true;
    private void setCurvePoint(BezierCurvePoint point, boolean lastSelected){
            float spread;

            //get each individual point
            x2 = point.getTime();
            y2 = point.getAmount();

            spread = (x2 - lastPoint.getTime())/2;

            x0 = (lastPoint.getTime()) + spread;
            y0 = lastPoint.getAmount();

            x1 = x2 - spread;
            y1 = y2;

            // Draw Bezier Lines
            curvePath.cubicTo(x0, y0, x1, y1, x2, y2);
            if(!lastSelected){
                if(isPastCurve){
                    selectedCurvePath.cubicTo(x0, y0, x1, y1, x2, y2);
                    areaSelectedPath.cubicTo(x0, y0, x1, y1, x2, y2);
                }
            }
            else{
                if(isPastCurve){
                    selectedCurvePath.cubicTo(x0, y0, x1, y1, x2, y2);
                    areaSelectedPath.cubicTo(x0, y0, x1, y1, x2, y2);
                    areaSelectedPath.lineTo(x2, getHeight());
                    areaSelectedPath.lineTo(0, getHeight());
                    areaSelectedPath.close();
                    isPastCurve = false;
                }

            }

            lastPoint = point;
    }

    Calendar firstDayOfFirstMonthCal = Calendar.getInstance();
    Calendar lastDayOfLastMonthCal = Calendar.getInstance();
    int emptyFirstDays, emptyLastDays;
    private void setFirstAndLastDays() {
        Date firstDayWithData = bezierData.get(0).getMonthCashflow().get(0).getDate();
        firstDayOfFirstMonthCal.setTime(firstDayWithData);
        firstDayOfFirstMonthCal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfFirstMonth = firstDayOfFirstMonthCal.getTime();

        //Get Last Day
        Date lastDayWithData = bezierData.get(bezierData.size() - 1).getMonthCashflow().get(bezierData.get(bezierData.size() - 1).getMonthCashflow().size() - 1).getDate();
        lastDayOfLastMonthCal.setTime(lastDayWithData);
        int lastDay = lastDayOfLastMonthCal.getActualMaximum(Calendar.DATE);
        lastDayOfLastMonthCal.set(Calendar.DAY_OF_MONTH, lastDay);
        Date lastDayOfLastMonthDate = lastDayOfLastMonthCal.getTime();

        long diff;
        diff = firstDayWithData.getTime() - firstDayOfFirstMonth.getTime();
        emptyFirstDays = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        diff = lastDayOfLastMonthDate.getTime() - lastDayWithData.getTime();
        emptyLastDays = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        Log.i("Empty Last Days", "start:"+emptyFirstDays + " , end:"+emptyLastDays);
    }


    public float getSmallestAmmount() {
        return smallestAmmount;
    }

    public void setSmallestAmmount(float smallestAmmount) {
        this.smallestAmmount = smallestAmmount;
    }

    public float getLargestAmount() {
        return largestAmount;
    }

    public void setLargestAmount(float largestAmount) {
        this.largestAmount = largestAmount;
    }

}
