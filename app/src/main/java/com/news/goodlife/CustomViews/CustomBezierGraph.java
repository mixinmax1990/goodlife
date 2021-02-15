package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.news.goodlife.Models.BezierCurvePoint;
import com.news.goodlife.Models.DayCashflowModel;
import com.news.goodlife.Models.MultiDayCashflowModel;

import java.util.Date;

public class CustomBezierGraph extends View {
    Paint realLinePaint, predictLinePaint, previousLinePaint;
    Path realPath, predictedPath, previousPath;

    int days;
    MultiDayCashflowModel multiDayData;

    public CustomBezierGraph(Context context) {
        super(context);
    }

    public CustomBezierGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();

    }

    private void setPaints() {

        realLinePaint = new Paint();
        realLinePaint.setStyle(Paint.Style.STROKE);
        realLinePaint.setStrokeWidth(15);
        realLinePaint.setAntiAlias(true);
        realLinePaint.setColor(Color.parseColor("#89B3F6"));


        predictLinePaint = new Paint();
        predictLinePaint.setStyle(Paint.Style.STROKE);
        predictLinePaint.setStrokeWidth(15);
        predictLinePaint.setAntiAlias(true);
        predictLinePaint.setColor(Color.parseColor("#FFFFFF"));
        predictLinePaint.setAlpha(70);

    }

    int width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h;
        Log.i("Width SC", ""+w);

    }

    Date today = null;
    public void setMultiDayData(MultiDayCashflowModel multiDayData, @Nullable Date today) {
        realLinePaint.setStrokeCap(Paint.Cap.ROUND);
        realLinePaint.setStrokeJoin(Paint.Join.ROUND);
        realLinePaint.setPathEffect(new CornerPathEffect(20));

        this.multiDayData = multiDayData;
        days = multiDayData.getMonthCashflow().size();
        dataSet = true;

        if(today != null){
            this.today = today;
        }

        //Get the Smallest and Largest Amounts
        getAmountSpectrum();
        //getThe Bezier Path
        createBezier();
        //Now Draw the Path
        invalidate();
    }

    //Icon Bezier Setting

    public void setAnalysisIconBezier(){

        realLinePaint.setAlpha(0);

        predictLinePaint.setColor(Color.parseColor("#000000"));
        predictLinePaint.setAlpha(100);
        predictLinePaint.setStrokeWidth(8);

        this.multiDayData = analysisIconBezierData();
        days = multiDayData.getMonthCashflow().size();
        dataSet = true;

        //Get the Smallest and Largest Amounts
        getAmountSpectrum();
        //getThe Bezier Path
        createBezier();
        //Now Draw the Path
        invalidate();

    }

    private MultiDayCashflowModel analysisIconBezierData() {
        MultiDayCashflowModel  multidaysData = new MultiDayCashflowModel();
        DayCashflowModel dayData;
        Date testDate = new Date();
        //create 7 Days Test Data
        dayData = new DayCashflowModel(testDate,1300);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,500);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,900);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1500);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1000);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,200);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,800);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1800);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,2700);
        multidaysData.addDayCashflow(dayData);


        return multidaysData;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(dataSet){
            //Then Draw

            canvas.drawPath(predictedPath, predictLinePaint);
            canvas.drawPath(realPath, realLinePaint);

        }
        else{
            //Write Some Message

        }

    }


    boolean dataSet = false;

    float x0,y0,x1,y1,x2,y2;
    BezierCurvePoint lastPoint;
    float currentX = 0;
    int dayWidth;
    boolean predictionDay = false;
    private void createBezier()  {

        //Get The Day Width
        dayWidth = width / (days-1);
        setMatrix();

        realPath = new Path();
        predictedPath = new Path();


        int iterationCount =  0;

        for(DayCashflowModel day: multiDayData.getMonthCashflow()){
            //Inside here we will calculate the bezierPoints

            //Test if day is Prediction Day
            if(iterationCount > 3){
                predictionDay = true;
            }
            //-------

            if(iterationCount == 0){
                lastPoint = new BezierCurvePoint(getAmountPos(day.getAmount()), currentX);
                if(!predictionDay){
                    realPath.moveTo(lastPoint.getTime(), lastPoint.getAmount());
                }
                predictedPath.moveTo(lastPoint.getTime(), lastPoint.getAmount());

            }
            else{
                setCurvePoint(new BezierCurvePoint(getAmountPos(day.getAmount()), currentX));
            }

            iterationCount++;
            currentX += dayWidth;
            Log.i("currentX", " uu "+width);

        }

    }

    private void setCurvePoint(BezierCurvePoint point){
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
        if(!predictionDay){
            realPath.cubicTo(x0, y0, x1, y1, x2, y2);

        }
        predictedPath.cubicTo(x0, y0, x1, y1, x2, y2);

        lastPoint = point;
    }

    boolean getsNegative = false;
    float valToHeightRatio;
    private float smallestAmount = 0;
    private float largestAmount = 0;

    private void setMatrix() {
        if(smallestAmount < 0){
            //Add the
            getsNegative = true;
            float valSpectrum = Math.abs(smallestAmount) + largestAmount;
            valToHeightRatio = (height - 15)/ valSpectrum;
        }
        else{
            valToHeightRatio = (height - 15)/ largestAmount;
        }
    }

    private void getAmountSpectrum() {

        for(DayCashflowModel day: multiDayData.getMonthCashflow()){

            if(day.getAmount() < smallestAmount){

                smallestAmount = day.getAmount();

            }
            if(day.getAmount() > largestAmount){

                largestAmount = day.getAmount();

            }

        }
    }

    private float getAmountPos(float Amount){
        //if its Negative add the smallest Amount to the Amount
        if(getsNegative){
            Amount += Math.abs(smallestAmount);
        }

        float position = height - (int)(Amount * valToHeightRatio);
        return position;
    }

    public float getSmallestAmount() {
        return smallestAmount;
    }

    public void setSmallestAmount(float smallestAmount) {
        this.smallestAmount = smallestAmount;
    }

    public float getLargestAmount() {
        return largestAmount;
    }

    public void setLargestAmount(float largestAmount) {
        this.largestAmount = largestAmount;
    }
}
