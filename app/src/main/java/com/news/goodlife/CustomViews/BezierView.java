package com.news.goodlife.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.CashflowModel;
import com.news.goodlife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BezierView extends View {

    Paint paint, outerpoints, innerpoints, scrollPointLine, scrollPoint;
    Path path, scrollPath;
    Paint lineZeroPaint;
    Paint linesAmountPaint;
    int trendLineColorStart, trendLineColorEnd;
    DatabaseController db;

    int anc0X, anc0Y, anc1X, anc1Y;

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedValue value = new TypedValue ();
        context.getTheme().resolveAttribute (R.attr.financialGraphTrendline, value, true);
        trendLineColorStart = value.data;

        final TypedValue value2 = new TypedValue ();
        context.getTheme().resolveAttribute (R.attr.backgroundThirdly, value2, true);
        trendLineColorEnd = value2.data;

        scrollPointLine = new Paint();
        scrollPointLine.setStyle(Paint.Style.STROKE);
        scrollPointLine.setColor(Color.WHITE);
        scrollPointLine.setStrokeWidth(4);
        scrollPointLine.setAlpha(155);
        scrollPointLine.setStrokeCap(Paint.Cap.ROUND);
        scrollPointLine.setPathEffect(new DashPathEffect(new float[] {0, 10}, 5));
        scrollPointLine.setAntiAlias(true);

        scrollPoint = new Paint();
        scrollPoint.setStyle(Paint.Style.STROKE);
        scrollPoint.setStrokeWidth(3);
        scrollPoint.setColor(Color.WHITE);
        scrollPoint.setAntiAlias(true);
        scrollPoint.setAlpha(255);

        scrollPath = new Path();

        db = new DatabaseController(context);

    }

    public String ScrollDay;
    boolean hasAnim = false;

    public void growGrafAnimation(){
        ValueAnimator va = ValueAnimator.ofFloat(.0f, .1f);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                factorAmount = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.start();
    }

    public void flattenGrafAnimation(){
        ValueAnimator va = ValueAnimator.ofFloat(.1f, .0f);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                factorAmount = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.start();
    }

    boolean scrollDraw = false;

    public float updateScroll(int ScrollPos){
        //scrollPosData;
        //Checks if we have reached the last Point
        if(ScrollPoint != numOfPoints - 1){
            if(ScrollPos > scrollPosData.get(ScrollPoint + 1).getTime()){
                ScrollPoint = ScrollPoint + 1;
                scrollDraw = true;
                invalidate();

            }

        }
        //Checks if we are at the First Point
        if(ScrollPoint != 0){
            if(ScrollPos < scrollPosData.get(ScrollPoint).getTime()){
                ScrollPoint = ScrollPoint - 1;

                scrollDraw = true;
                invalidate();

            }
        }


        firstCalendarEntry.setTime(firstEntryDate);
        firstCalendarEntry.add(Calendar.DATE, ScrollPos / factorTime);
        SimpleDateFormat sdf1 = new SimpleDateFormat("d MMM YY");
        ScrollDay = sdf1.format(firstCalendarEntry.getTime());

        return scrollPosData.get(ScrollPoint).getAmount();

    }

    ConstraintLayout parent;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        parent = (ConstraintLayout) getParent();


    }

    public BezierView(Context context) {
        super(context);
    }
    float x0,y0,x1,y1,x2,y2;
    CashflowBezierPoint lastPoint;
    private List<CashflowBezierPoint> scrollPosData = new ArrayList<>();
    int ScrollPoint = 0;
    int numOfPoints;
    int factorTime = 30;
    float factorAmount = 0.1f;
    float zeroMark;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        // TODO Make sure that unesesarry loads happen

        paint = new Paint();
        innerpoints = new Paint();
        outerpoints = new Paint();
        path = new Path();
        lineZeroPaint = new Paint();
        linesAmountPaint = new Paint();

        //paint.setShader(new LinearGradient(0, 0, 0, 400, trendLineColorStart, trendLineColorEnd, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(trendLineColorStart);
        paint.setStrokeWidth(8);
        paint.setAlpha(200);
        paint.setAntiAlias(true);

        innerpoints.setStyle(Paint.Style.FILL);
        innerpoints.setColor(Color.parseColor("#202125"));
        innerpoints.setAntiAlias(true);
        innerpoints.setAlpha(255);

        outerpoints.setStyle(Paint.Style.STROKE);
        outerpoints.setColor(trendLineColorStart);
        outerpoints.setStrokeWidth(3);
        outerpoints.setAntiAlias(true);
        outerpoints.setAlpha(200);

        lineZeroPaint.setColor(Color.WHITE);
        lineZeroPaint.setAlpha(40);

        linesAmountPaint.setColor(Color.WHITE);
        linesAmountPaint.setAlpha(10);



        //x is Time
        //y is Amount
        List<CashflowBezierPoint> cashflowPath = calculatedData(getData());
        scrollPosData.clear();


        //max points 3
        int countPoints = 0;


        zeroMark = this.getHeight()/2;
        int lines = 0;



        float spread;
        lastPoint = new CashflowBezierPoint(zeroMark,0f);
        path.moveTo(lastPoint.getTime()*factorTime, zeroMark - lastPoint.getAmount() * factorAmount);
        scrollPosData.add(addScrollData(lastPoint.getTime()*factorTime, 0));
        numOfPoints++;

        for(CashflowBezierPoint point: cashflowPath){
            //get each individual point
            x2 = point.getTime() * factorTime;
            y2 = zeroMark - (point.getAmount() * factorAmount);

            spread = (x2 - lastPoint.getTime() * factorTime)/2;

            x0 = (lastPoint.getTime() * factorTime) + spread;
            y0 = zeroMark - (lastPoint.getAmount() * factorAmount);

            x1 = x2 - spread;
            y1 = y2;

            // Draw Bezier Lines
            path.cubicTo(x0, y0, x1, y1, x2, y2);
            scrollPosData.add(addScrollData(x2, point.getAmount()));
            numOfPoints++;
            // Draw Actual Lines
            // canvas.drawLine(lastPoint.getTime() + factorTime, lastPoint.getAmount() * factorAmount, point.time * factorTime, lastPoint.amount * factorAmount, lineZeroPaint);
            // canvas.drawLine(point.getTime() + factorTime, lastPoint.getAmount() * factorAmount, point.time * factorTime, point.amount * factorAmount, lineZeroPaint);

            lastPoint = point;

            if(lines == 5){
                canvas.drawLine(point.getTime() * factorTime, zeroMark -30, point.getTime() * factorTime, zeroMark + 30, lineZeroPaint);
                lines = 0;
            }
            else{
                canvas.drawLine(point.getTime() * factorTime, zeroMark -20, point.getTime() * factorTime, zeroMark + 20, lineZeroPaint);
                lines++;
            }


        }


        //path.cubicTo(100, 0, 400, 0, 500, 400);
        //path.cubicTo(600, 1000, 900, 1000, 1000, 400);
        //path.cubicTo(anc0X, anc0Y, anc1X, anc1Y, this.getWidth(), 500);
        canvas.drawPath(path, paint);


        for(CashflowBezierPoint point: cashflowPath){
            //get each individual point
            x2 = point.getTime() * factorTime;
            y2 = zeroMark - (point.getAmount() * factorAmount);

            canvas.drawCircle(x2, y2, 12, innerpoints);
            canvas.drawCircle(x2, y2, 12, outerpoints);

        }
        paint.setStrokeWidth(3);
        //Drawing Lines
        canvas.drawLine(0, 0, 0, this.getHeight(), paint);
        canvas.drawLine(0, zeroMark, this.getWidth(), zeroMark, lineZeroPaint);
        canvas.drawLine(0, (zeroMark/10)*8, this.getWidth(), (zeroMark/10)*8, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*6, this.getWidth(), (zeroMark/10)*6, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*4, this.getWidth(), (zeroMark/10)*4, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*12, this.getWidth(), (zeroMark/10)*12, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*14, this.getWidth(), (zeroMark/10)*14, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*16, this.getWidth(), (zeroMark/10)*16, linesAmountPaint);

        if(scrollDraw){
            float cx = scrollPosData.get(ScrollPoint).getTime();
            float cy = zeroMark - (scrollPosData.get(ScrollPoint).getAmount() * factorAmount);
            canvas.drawLine(scrollPosData.get(ScrollPoint).getTime() + 5, zeroMark - (scrollPosData.get(ScrollPoint).getAmount() * factorAmount), scrollPosData.get(ScrollPoint + 1).getTime(), zeroMark - (scrollPosData.get(ScrollPoint).getAmount() * factorAmount), scrollPointLine);
            canvas.drawCircle(cx, cy , 12, scrollPoint);
        }



    }



    private void drawBezier(Path path, Paint paint,Canvas canvas){
        canvas.drawPath(path, paint);

    }

    private CashflowBezierPoint addScrollData(float position, float amount){

        CashflowBezierPoint point = new CashflowBezierPoint(amount, position);

        return point;
    }

    int sizeInDays = 365;
    int daysSinceFirstEntry, daysSinceFirstEntryToday;
    public long firstEntry;
    Calendar firstCalendarEntry = Calendar.getInstance();
    Date firstEntryDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private JSONObject getData(){

        JSONObject dataDays = new JSONObject();
        List<CashflowModel> data = db.Cashflow.getAllCashflow();

        boolean first = true;
        for(CashflowModel cashflow: data){

            if(first){
                String dateStr = cashflow.getDate();

                try {
                    firstEntryDate = sdf.parse(dateStr);
                    Date today = new Date();

                    long diff = today.getTime() - firstEntryDate.getTime();
                    daysSinceFirstEntryToday = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    firstEntry = firstEntryDate.getTime();



                } catch (ParseException e) {
                    e.printStackTrace();
                }

                first = false;
            }

            String dateStr = cashflow.getDate();

            try {

                Date date = sdf.parse(dateStr);
                long dayMinusfirst = date.getTime() - firstEntry;
                daysSinceFirstEntry = (int)TimeUnit.DAYS.convert(dayMinusfirst, TimeUnit.MILLISECONDS);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Log.i("Days since first entry", ""+daysSinceFirstEntry);


            if(dataDays.has(""+daysSinceFirstEntry)){
                //hast an array with that identifier
                try {
                    long DayCashflow = (int)Double.parseDouble(dataDays.get(""+daysSinceFirstEntry).toString());
                    if(cashflow.getPositive().equals("true")){
                        //add the amount
                        DayCashflow = DayCashflow + (int)Double.parseDouble(cashflow.getValue());
                    }
                    else{
                        //substract the amount

                        DayCashflow = DayCashflow + (-(int)Double.parseDouble(cashflow.getValue()));
                        /*
                        if(DayCashflow < 0){
                            DayCashflow = DayCashflow - (int)Double.parseDouble(cashflow.getValue());
                        }
                        else{
                            DayCashflow = DayCashflow + (int)Double.parseDouble(cashflow.getValue());
                        }*/

                    }
                    dataDays.put(""+daysSinceFirstEntry, DayCashflow);

                    if(cashflow.getRepeat().equals("true")){
                        int times = (daysSinceFirstEntryToday + sizeInDays)/30;
                        int futureday = 0;
                        for(int i = 1; i < times; i++){
                            futureday = daysSinceFirstEntry + (i * 30);

                            if(cashflow.getPositive().equals("true")){
                                DayCashflow = (int)Double.parseDouble(cashflow.getValue());
                            }
                            else{
                                DayCashflow = -(int)Double.parseDouble(cashflow.getValue());
                            }
                            dataDays.put(""+futureday, DayCashflow);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                //has no array with that Identifier
                long dayCashflow;

                if(cashflow.getPositive().equals("true")){
                    dayCashflow = (int)Double.parseDouble(cashflow.getValue());
                }
                else{
                    dayCashflow = -(int)Double.parseDouble(cashflow.getValue());
                }

                try {
                    dataDays.put(""+daysSinceFirstEntry, dayCashflow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(cashflow.getRepeat().equals("true")){
                    int times = (daysSinceFirstEntryToday + sizeInDays)/30;
                    int futureday = 0;
                    for(int i = 1; i < times; i++){
                        futureday = daysSinceFirstEntry + (i * 30);

                        if(cashflow.getPositive().equals("true")){
                            dayCashflow = (int)Double.parseDouble(cashflow.getValue());
                        }
                        else{
                            dayCashflow = -(int)Double.parseDouble(cashflow.getValue());
                        }
                        try {
                            dataDays.put(""+futureday, dayCashflow);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }


        }

        //Log.i("JSONOBJ LENGTH", ""+dataDays.toString());

        return dataDays;

    }

    private List<CashflowBezierPoint> calculatedData(JSONObject cashflowData) {

        List<CashflowBezierPoint> cashflowBezierPath = new ArrayList<>();
        List<CashflowBezierPoint> cashflowLinePath = new ArrayList<>();

        int graphSize = sizeInDays + daysSinceFirstEntry;
        CashflowBezierPoint cashflowBezierPoint;
        CashflowBezierPoint cashflowLinePoint;
        int cash = 0;
        for(int i = 0; i < graphSize; i++){

            if(cashflowData.has(""+i)){
                try {
                    cash = cash + Integer.parseInt(cashflowData.get(""+i).toString());
                    //Log.i("CashData Look for -", ""+cashflowData.get(""+i).toString());
                    //Log.i("CashData A Look for -", ""+Integer.parseInt(cashflowData.get(""+i).toString()));
                    cashflowBezierPoint = new CashflowBezierPoint((float)cash,(float)i);
                    cashflowBezierPath.add(cashflowBezierPoint);

                    cashflowLinePoint = new CashflowBezierPoint((float)cash,(float)i);
                    cashflowLinePath.add(cashflowLinePoint);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return cashflowLinePath;
    }

    private List<CashflowBezierPoint> testData(){
        List<CashflowBezierPoint> cashflowBezierPath = new ArrayList<>();

        CashflowBezierPoint cashflowBezierPoint;
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(2000f,1f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point Two
        cashflowBezierPoint = new CashflowBezierPoint(1600f,2f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(1656f,3f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(1436f,4f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point Two
        cashflowBezierPoint = new CashflowBezierPoint(1900f,5f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(-1900f,6f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(40f,7f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point Two
        cashflowBezierPoint = new CashflowBezierPoint(360f,8f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(266f,9f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(80f,10f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point Two
        cashflowBezierPoint = new CashflowBezierPoint(80f,11f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(200f,12f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(200f,13f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point Two
        cashflowBezierPoint = new CashflowBezierPoint(250f,14f);
        cashflowBezierPath.add(cashflowBezierPoint);
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(200f,15f);
        cashflowBezierPath.add(cashflowBezierPoint);

        return cashflowBezierPath;
    }

    public class CashflowBezierPoint {
        Float amount, time;
        public CashflowBezierPoint(Float amount, Float time){
            setAmount(amount);
            setTime(time);
        }

        public Float getAmount() {
            return amount;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }

        public Float getTime() {
            return time;
        }

        public void setTime(Float time) {
            this.time = time;
        }
    }
}

