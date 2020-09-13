package com.news.goodlife.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.news.goodlife.R;

import java.util.ArrayList;
import java.util.List;

public class GoalChartView extends ConstraintLayout {

    View parent;
    Paint backgroundPaint, progressPaint, eventCirclePaint, pastEventCirclePaint, eventLinePaint, pastEventLinePaint, starPaint, eventTextPaint;

    int cx, cy, eventRadius, eventPastRadius;


    float goalProgress;
    String showDetails;
    int progressViewWidth;
    Context context;
    AttributeSet attrs;


    List<GoalChartView.GoalEventPoint> eventData;
    List<GoalChartView.GoalEventPoint> testPastEvents;


    public GoalChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setAttributes(attrs);
        initiatePaints();


    }
    public GoalChartView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        eventData = testData();

        eventRadius = 15;

        double ratio = (double) this.getHeight() / this.getWidth();
        float halfEventRadius = (int)(eventRadius + .05 / 2);
        progressViewWidth = (int)((this.getWidth()/100) * goalProgress);


        //canvas.drawRect(0, 0,progressViewWidth , this.getHeight(), progressPaint);

        //Show Detailed Graph
            int count = 0;
            for(GoalChartView.GoalEventPoint goalPoint : eventData){
                canvas.drawCircle(goalPoint.getTime() , goalPoint.getAmount(), eventRadius, eventCirclePaint);

                if(count > 0 & count < eventData.size()){
                    //Draw Line between Points
                    //Past Point
                    GoalChartView.GoalEventPoint pastPoint = eventData.get(count - 1);
                    canvas.drawLine(pastPoint.getTime() + halfEventRadius, pastPoint.getAmount() - (float)(halfEventRadius * ratio), goalPoint.getTime() - halfEventRadius, goalPoint.getAmount() + (float)(halfEventRadius * ratio), eventCirclePaint);
                }
                if(count == eventData.size() - 1){
                    eventTextPaint.setTextAlign(Paint.Align.RIGHT);
                    //drawStar(goalPoint.getTime(), goalPoint.getAmount(), 50, canvas);
                    canvas.drawText("16,500€",goalPoint.getTime() - eventRadius - 10, goalPoint.getAmount() + eventRadius / 2 - 20, eventTextPaint);
                }
                if(count == 0){

                    eventTextPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("0,00€",goalPoint.getTime() + eventRadius + 10, goalPoint.getAmount() + eventRadius / 2 + 20, eventTextPaint);
                }
                count++;
            }

            count = 0;
            //Past Events
            for(GoalChartView.GoalEventPoint goalPoint : testPastEvents){
                canvas.drawCircle(goalPoint.getTime() , goalPoint.getAmount(), 8, pastEventCirclePaint);
                if(count > 0 & count < testPastEvents.size()){
                    //Draw Line between Points
                    //Past Point
                    GoalChartView.GoalEventPoint pastPoint = testPastEvents.get(count - 1);
                    canvas.drawLine(pastPoint.getTime(), pastPoint.getAmount(), goalPoint.getTime(), goalPoint.getAmount(), pastEventCirclePaint);
                }
                if(count == testPastEvents.size() - 1){
                    eventTextPaint.setTextAlign(Paint.Align.LEFT);
                    //drawStar(goalPoint.getTime(), goalPoint.getAmount(), 50, canvas);
                    canvas.drawText("4,500€",goalPoint.getTime() + eventRadius + 10, goalPoint.getAmount() + eventRadius / 2 + 20, eventTextPaint);
                }
                count++;
            }
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = (View) getParent();
    }


    private void setAttributes(AttributeSet attrs) {
        this.attrs = attrs;
        /* set custom attributes with TypedArray
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.GoalDrawView);
        CharSequence goalProgress_cs = arr.getString(R.styleable.GoalDrawView_goalProgress);
        if (goalProgress_cs != null) {
            // Do something with foo_cs.toString()
            setGoalProgress(Float.parseFloat(goalProgress_cs.toString()));

        }
        arr.recycle();  // Do this when done.*/
    }
    private void initiatePaints(){
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(Color.parseColor("#4CAF50"));
        //progressPaint.setAlpha(150);
        eventCirclePaint = new Paint();
        eventCirclePaint.setStyle(Paint.Style.FILL);
        eventCirclePaint.setStrokeWidth(6);
        eventCirclePaint.setAntiAlias(true);
        eventCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        eventCirclePaint.setAlpha(50);

        pastEventCirclePaint = new Paint();
        pastEventCirclePaint.setStyle(Paint.Style.FILL);
        pastEventCirclePaint.setStrokeWidth(4);
        pastEventCirclePaint.setAntiAlias(true);
        pastEventCirclePaint.setColor(Color.WHITE);
        pastEventCirclePaint.setAlpha(200);

        starPaint = new Paint();
        starPaint.setColor(Color.BLUE);
        starPaint.setAntiAlias(true);

        eventTextPaint = new Paint();
        eventTextPaint.setColor(Color.WHITE);
        eventTextPaint.setAntiAlias(true);
        eventTextPaint.setTextSize(24);
    }

    public float getGoalProgress() {
        return goalProgress;
    }

    public void setGoalProgress(float goalProgress) {
        this.goalProgress = goalProgress;
    }

    public String getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(String showDetails) {
        this.showDetails = showDetails;
    }

    //Load Goal Event Data
    public List<GoalChartView.GoalEventPoint> testData(){
        List<GoalChartView.GoalEventPoint> eventPoints = new ArrayList<>();
        testPastEvents = new ArrayList<>();


        //TODO change with Database Goal Data
        int x = 1;
        int events = 5;
        int widthE = this.getWidth() / events;
        int heightE = this.getHeight() / events;
        while(x < events){

            GoalChartView.GoalEventPoint eventPoint = new GoalChartView.GoalEventPoint((float)this.getHeight() - (heightE * x) , (float)widthE * x);
            eventPoints.add(eventPoint);

            if(x < 3){
                testPastEvents.add(eventPoint);

            }
            x++;
        }
        return eventPoints;
    }

    public void drawStar(float posX, float posY, int size, Canvas canvas){

        Path path = new Path();
        float mid = posX;
        float half = posY;
        mid = mid - half;



        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f);

        path.close();

        canvas.drawPath(path, starPaint);
    }

    //Inner Class Model
    public class GoalEventPoint {
        Float amount, time;
        public GoalEventPoint(Float amount, Float time){
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
