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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.Singletons.SingletonClass;

public class BudgetCircleMini extends FrameLayout {
    Paint barsPaint, barsBgPaint, labelPaint, iconBGPaint;
    RectF rectF, rectFinner;
    int height, width;
    int strokeSize = 30;
    int strokeSizeOuter = 0;

    int months = 3;
    boolean draw = false;
    float barMarginPerc = 0.1f;
    int allocatedMoney = 0;
    int fontSize =(int)(13 * getResources().getDisplayMetrics().scaledDensity);
    SingletonClass singletonClass = SingletonClass.getInstance();



    public BudgetCircleMini(@NonNull Context context) {
        super(context);
    }

    public BudgetCircleMini(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        setWillNotDraw(false);

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
        barsBgPaint.setStrokeWidth(strokeSizeOuter);
        barsBgPaint.setColor(Color.WHITE);
        barsBgPaint.setStrokeCap(Paint.Cap.ROUND);
        barsBgPaint.setStrokeJoin(Paint.Join.ROUND);
        barsBgPaint.setPathEffect(new CornerPathEffect(20));
        barsBgPaint.setAntiAlias(true);
        barsBgPaint.setAlpha(30);

        labelPaint = new Paint();
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setColor(Color.WHITE);
        labelPaint.setAntiAlias(true);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(fontSize);
        labelPaint.setAlpha(255);

        iconBGPaint = new Paint();
        iconBGPaint.setStyle(Paint.Style.FILL);
        iconBGPaint.setColor(Color.WHITE);
        iconBGPaint.setAlpha(30);
        iconBGPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        height = h;
        width = w;

        setRectFs(0);
        setPaints();
        draw = true;
        invalidate();
    }

    private void setRectFs(int padding){
        strokeSize = (int)(width * 0.07f);
        strokeSizeOuter = (int)(strokeSize * 0.5f);
        int innerMargin = (int)(strokeSize * 2.5);

        rectF = new RectF(padding + strokeSize, padding + strokeSize,(width - strokeSize) - padding,(height - strokeSize) - padding);
        rectFinner = new RectF(padding + (strokeSizeOuter + innerMargin),padding + (strokeSizeOuter + innerMargin),(width - strokeSizeOuter - innerMargin) - padding,(height - strokeSizeOuter - innerMargin) - padding);
        iconRectF = new RectF(0,0,width,height);
    }

    int geom_centerX, geom_centerY;
    int geom_center_angle, geom_radius;
    boolean icon = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBarParams();
        if(draw){
            //draw

            setColor();
            int radiusCircle = (int)(360 * percentageAnim);
            //Log.i("PercentageRadius", ""+radiusCircle);
            canvas.drawArc(rectF, 270, radiusCircle, false, barsPaint);
            for(int i = 0; i < months; i++){

                if(i == 0){
                    barsBgPaint.setAlpha(255);
                }
                else{
                    barsBgPaint.setAlpha(30);
                }
                //Month Line
                //Log.i("DRAWING", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
                canvas.drawArc(rectFinner, startAngle, sweepAngle, false, barsBgPaint);
                startAngle = startAngle + sweepAngle + barMargin;

            }

            //canvas.drawText("356€", width/2, height/2 + (fontSize/3), labelPaint);
        }

        if(icon){
            barsBgPaint.setAlpha(200);
            barsPaint.setAlpha(200);
            canvas.drawArc(rectF, 270, 300, false, barsPaint);
            for(int i = 0; i < months; i++){
                //Month Line
                //Log.i("DRAWING", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
                canvas.drawArc(rectFinner, startAngle, sweepAngle, false, barsBgPaint);
                startAngle = startAngle + sweepAngle + barMargin;

            }

        }
    }

    RectF iconRectF;
    public void setIcon(){

        this.months = 3;
        barsPaint.setColor(Color.WHITE);
        barsPaint.setAlpha(150);
        barsBgPaint.setColor(Color.WHITE);
        barsBgPaint.setAlpha(150);

        icon = true;
        draw = false;

        invalidate();

    }

    public void setActiveIcon(){
        this.months = 3;
        barsPaint.setColor(Color.WHITE);
        barsPaint.setAlpha(100);
        barsBgPaint.setColor(Color.WHITE);
        barsBgPaint.setAlpha(100);
        icon = true;
        draw = false;
        invalidate();
    }


    private void setColor() {

        if(percentageAnim < .25){
            //Make it Red
            barsPaint.setColor(Color.parseColor("#F18B81"));
            //barsPaint.setColor(Color.parseColor("#ff6859"));
            return;
        }

        if(percentageAnim < .50f){
            //MAke it Yellow
            barsPaint.setColor(Color.parseColor("#FED664"));
            return;
        }
        if(percentageAnim < .75f){
            //Make it Green
            barsPaint.setColor(Color.parseColor("#ADD581"));
            //barsPaint.setColor(Color.parseColor("#12BC6C"));
            return;
        }

        //Make it Blue
        barsPaint.setColor(Color.parseColor("#89B3F6"));
        //barsPaint.setColor(Color.parseColor("#59B2FF"));

    }

    public void setPercentage(int perc){
        percentage = perc;
        animatePercentage(true);
    }

    public void unimateOut(){
        percentage = 100;
        animatePercentage(false);
    }
    int percentage = 80;
    float percentageAnim = 100;
    private void animatePercentage(boolean in){

        ValueAnimator va = ValueAnimator.ofFloat(0,1);

        va.setDuration(1000);

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
