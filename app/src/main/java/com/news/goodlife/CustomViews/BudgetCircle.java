package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.Singletons.SingletonClass;

public class BudgetCircle extends FrameLayout {
    Paint barsPaint, barsBgPaint, labelPaint;
    RectF rectF, rectFinner;
    int height, width;
    int strokeSize = 30;

    int months = 3;
    boolean draw = false;
    float barMarginPerc = 0.1f;
    int allocatedMoney = 0;
    SingletonClass singletonClass = SingletonClass.getInstance();



    public BudgetCircle(@NonNull Context context) {
        super(context);
    }

    public BudgetCircle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        setWillNotDraw(false);
        setPaints();
    }

    float barMargin;
    float sweepAngle;
    float startAngle;
    private void setBarParams() {
        barMargin = 14;
        sweepAngle = (float)(360 / months) - barMargin;
        startAngle = 270;

        //Log.i("DRAWING STart", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
    }

    public int getMonths(){
        return this.months;
    }

    public void setMonths(int months) {
        this.months = months;
        setBarParams();
        invalidate();
    }

    private void setPaints(){
        barsPaint = new Paint();
        barsPaint.setColor(Color.WHITE);
        barsPaint.setAntiAlias(true);
        barsPaint.setTextSize(100);
        barsPaint.setStyle(Paint.Style.STROKE);
        barsPaint.setStrokeWidth(strokeSize);
        barsPaint.setStrokeCap(Paint.Cap.ROUND);
        barsPaint.setStrokeJoin(Paint.Join.ROUND);
        barsPaint.setPathEffect(new CornerPathEffect(20));

        barsBgPaint = new Paint();
        barsBgPaint.setStyle(Paint.Style.STROKE);
        barsBgPaint.setStrokeWidth((int)(strokeSize * 0.5f));
        barsBgPaint.setColor(Color.WHITE);
        barsBgPaint.setStrokeCap(Paint.Cap.ROUND);
        barsBgPaint.setStrokeJoin(Paint.Join.ROUND);
        barsBgPaint.setPathEffect(new CornerPathEffect(20));
        barsBgPaint.setAntiAlias(true);
        barsBgPaint.setAlpha(255);

        labelPaint = new Paint();
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setColor(Color.WHITE);
        labelPaint.setAntiAlias(true);
        labelPaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);

    }

    int outerMargin = singletonClass.dpToPx(40);
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int innerMargin = 40;
        rectF = new RectF(outerMargin,outerMargin,w - outerMargin,h - outerMargin);
        rectFinner = new RectF(outerMargin + innerMargin,outerMargin + innerMargin,w - outerMargin - innerMargin,h - outerMargin - innerMargin);
        height = h;
        width = w;
        draw = true;
        invalidate();
    }

    int geom_centerX, geom_centerY;
    int geom_center_angle, geom_radius;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBarParams();
        if(draw){
            //draw
            String cuts;
            if(allocatedMoney != 0){
                cuts = ""+Math.round((float)(allocatedMoney / months))+singletonClass.getCurrencySymbol();
            }
            else{

                cuts = ""+Math.round((float)(100 / months))+"%";

            }

            

            geom_center_angle = 270 + (int)(sweepAngle / 2);
            Log.i("Center Angle", ""+ geom_center_angle);
            Log.i("SweepAngle", ""+sweepAngle);
            geom_centerX = width / 2;
            geom_centerY = height / 2;
            geom_radius = (int)(geom_centerY - outerMargin + 40);
            setColor();
            int radiusCircle = (int)(360 * percentageAnim);
            //Log.i("PercentageRadius", ""+radiusCircle);
            canvas.drawArc(rectFinner, 270, radiusCircle, false, barsPaint);
            for(int i = 0; i < months; i++){
                //Month Line
                //Log.i("DRAWING", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
                canvas.drawArc(rectF, startAngle, sweepAngle, false, barsBgPaint);
                Log.i("StartAngle", ""+startAngle);
                int[] labelCoords = coord(geom_radius, geom_center_angle);

                int cx,cy;
                cx = geom_centerX + labelCoords[0];
                cy = geom_centerY + labelCoords[1];

                if(labelCoords[0] < 0)
                    labelPaint.setTextAlign(Paint.Align.RIGHT);
                else{
                    labelPaint.setTextAlign(Paint.Align.LEFT);
                }


                canvas.drawText(cuts, cx,cy, labelPaint);
                //canvas.drawCircle(cx,cy,20, labelPaint);

                //Log.i("Coord", "x = "+labelCoords[0]+"; y = "+labelCoords[1]);
                //canvas.drawArc(rectF, startAngle, sweepAngle, false, barsPaint);
                startAngle = startAngle + sweepAngle + barMargin;
                geom_center_angle = (int)(startAngle + (int)(sweepAngle / 2));
            }
        }
    }

    boolean set100 = false;
    public void setAllocatedMoney(int money){

        percentage = 100;
        if(money > 0){
            if(!set100){
                set100 = true;
                animatePercentage(true);
            }
        }
        else{
            if(set100){
                set100 = false;
                animatePercentage(false);
            }
        }

        allocatedMoney = money;
        invalidate();


    }

    private int[] coord(int radius, int angel){
        int x,y;


        //Geometry
        x = (int)(radius * Math.cos(Math.toRadians(angel)));
        y = (int)(radius * Math.sin(Math.toRadians(angel)));

        Log.i("radius", ""+radius);
        Log.i("Cosin 0", ""+x);
        Log.i("Sin 0", ""+y);
        Log.i("Centers", "centX = "+geom_centerX+" ; centY = "+geom_centerY);

        //Assign Array With Coordinates
        int[] xy = new int[2];
        xy[0]= x;
        xy[1]= y;

        return xy;
    }

    private void setColor() {

        if(percentageAnim < .25){
            //Make it Red
            barsPaint.setColor(Color.parseColor("#ff6859"));
            return;
        }

        if(percentageAnim < .50f){
            //MAke it Yellow
            barsPaint.setColor(Color.parseColor("#FED664"));
            return;
        }
        if(percentageAnim < .75f){
            //Make it Green
            barsPaint.setColor(Color.parseColor("#12BC6C"));
            return;
        }

        //Make it Blue
        barsPaint.setColor(Color.parseColor("#59B2FF"));

    }

    public void setPercentage(int perc){
        percentage = perc;
        animatePercentage(true);
    }

    public void unimateOut(){
        percentage = 100;
        animatePercentage(false);
    }
    int percentage = 100;
    float percentageAnim = 100;
    private void animatePercentage(boolean in){

        ValueAnimator va = ValueAnimator.ofFloat(0,1);

        va.setDuration(1500);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float animVal = (float)valueAnimator.getAnimatedValue();

                if(in){
                    percentageAnim = (percentage * animVal) / 100;
                }
                else{
                    percentageAnim = (percentage * (1 - animVal)) / 100;
                }


                invalidate();
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        va.start();


    }
}
