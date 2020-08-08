package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.CashflowModel;
import com.news.goodlife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BezierView extends View {

    Paint paint;
    Path path;
    Paint lineZeroPaint;
    Paint linesAmountPaint;
    int trendLineColorStart, trendLineColorEnd;
    DatabaseController db;

    int anc0X, anc0Y, anc1X, anc1Y;

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (R.attr.financialGraphTrendline, value, true);
        trendLineColorStart = value.data;

        final TypedValue value2 = new TypedValue ();
        context.getTheme ().resolveAttribute (R.attr.colorAccent, value, true);
        trendLineColorEnd = value2.data;

        db = new DatabaseController(context);




    }

    public BezierView(Context context) {
        super(context);
    }
    float x0,y0,x1,y1,x2,y2;
    CashflowBezierPoint lastPoint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO Make sure that unesesarry loads happen

        paint = new Paint();
        path = new Path();
        lineZeroPaint = new Paint();
        linesAmountPaint = new Paint();

        //paint.setShader(new LinearGradient(0, 0, 0, 400, trendLineColorStart, trendLineColorEnd, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(trendLineColorStart);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);

        lineZeroPaint.setColor(Color.WHITE);
        lineZeroPaint.setAlpha(40);

        linesAmountPaint.setColor(Color.WHITE);
        linesAmountPaint.setAlpha(10);



        //x is Time
        //y is Amount
        List<CashflowBezierPoint> cashflowPath = calculatedData(getData());

        //max points 3
        int countPoints = 0;
        int factorTime = 30;
        float factorAmount = 0.1f;
        float zeroMark = this.getHeight()/2;
        int lines = 0;



        float spread;
        lastPoint = new CashflowBezierPoint(2000f,0f);
        path.moveTo(lastPoint.getTime()*factorTime, zeroMark - lastPoint.getAmount() * factorAmount);

        for(CashflowBezierPoint point: cashflowPath){
            //get each individual point
            x2 = point.getTime() * factorTime;
            y2 = zeroMark - (point.getAmount() * factorAmount);

            spread = (x2 - lastPoint.getTime() * factorTime)/2;

            x0 = (lastPoint.getTime() * factorTime) + spread;
            y0 = zeroMark - (lastPoint.getAmount() * factorAmount);

            x1 = x2 - spread;
            y1 = y2;

            path.cubicTo(x0, y0, x1, y1, x2, y2);
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

        //Drawing Lines
        canvas.drawLine(0, zeroMark, this.getWidth(), zeroMark, lineZeroPaint);
        canvas.drawLine(0, (zeroMark/10)*8, this.getWidth(), (zeroMark/10)*8, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*6, this.getWidth(), (zeroMark/10)*6, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*4, this.getWidth(), (zeroMark/10)*4, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*12, this.getWidth(), (zeroMark/10)*12, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*14, this.getWidth(), (zeroMark/10)*14, linesAmountPaint);
        canvas.drawLine(0, (zeroMark/10)*16, this.getWidth(), (zeroMark/10)*16, linesAmountPaint);





    }



    private void drawBezier(Path path, Paint paint,Canvas canvas){
        canvas.drawPath(path, paint);

    }

    int sizeInDays = 365;
    int daysSinceFirstEntry, daysSinceFirstEntryToday;
    long firstEntry;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private JSONObject getData(){

        JSONObject dataDays = new JSONObject();
        List<CashflowModel> data = db.Cashflow.getAllCashflow();

        boolean first = true;
        for(CashflowModel cashflow: data){

            Log.i("CashData Look for -", ""+cashflow.getValue());


            if(first){
                String dateStr = cashflow.getDate();

                try {
                    Date date = sdf.parse(dateStr);
                    Date today = new Date();

                    long diff = today.getTime() - date.getTime();
                    daysSinceFirstEntryToday = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    firstEntry = date.getTime();


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
                        DayCashflow = DayCashflow - (int)Double.parseDouble(cashflow.getValue());
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

        int graphSize = sizeInDays + daysSinceFirstEntry;
        CashflowBezierPoint cashflowBezierPoint;
        int cash = 0;
        for(int i = 0; i < graphSize; i++){

            if(cashflowData.has(""+i)){
                try {
                    cash = cash + Integer.parseInt(cashflowData.get(""+i).toString());
                    //Log.i("CashData Look for -", ""+cashflowData.get(""+i).toString());
                    //Log.i("CashData A Look for -", ""+Integer.parseInt(cashflowData.get(""+i).toString()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            cashflowBezierPoint = new CashflowBezierPoint((float)cash,(float)i);
            cashflowBezierPath.add(cashflowBezierPoint);
        }

        return cashflowBezierPath;
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

