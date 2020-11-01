package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Type;

public class MonthViewIcon extends FrameLayout {

    int width, height;
    String lineColor;

    Paint linePaint, textPaint;
    RectF box1, box2, box3, box4;

    int dp, radius;

    public MonthViewIcon(@NonNull Context context) {
        super(context);
    }

    public MonthViewIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        lineColor = "#000000";
        listeners();
        setPaints();

        box1 = new RectF();
        box2 = new RectF();
        box3 = new RectF();
        box4 = new RectF();

        radius = 15;

        setRect();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //animateIcons();
            }
        });

    }

    int anim = 0;
    private void setRect() {

        box1.left = width * 0.2f;
        box1.top = width * 0.2f;
        box1.bottom = width * 0.48f + anim;
        box1.right = width * .48f + anim;

        box2.left = (width * 0.02f + width / 2) + anim;
        box2.top = width * 0.2f;
        box2.bottom = width * 0.48f + anim;
        box2.right = (width * .8f)+ anim * 2;

        box3.left = width * 0.2f;
        box3.top = (width * 0.02f + width / 2)+ anim;
        box3.bottom = width * .8f + anim * 2;
        box3.right = width * .48f + anim;

        box4.left = (width * 0.02f + width / 2) + anim;
        box4.top =  (width * 0.02f + width / 2)+ anim;
        box4.bottom = width * .8f + anim * 2;
        box4.right = width * .8f + anim * 2;

    }

    private void setPaints() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor(lineColor));
        linePaint.setAlpha(150);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(3);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void listeners() {

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Create Some Icon Aimations

                width = getWidth();
                height = getHeight();
                dp = width/100;

                invalidate();
            }
        });
    }

    int animRadius;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setRect();
        animRadius = radius + (int)(anim * 0.25);
        canvas.drawRoundRect(box1, animRadius, animRadius, linePaint);
        canvas.drawRoundRect(box2, animRadius, animRadius, linePaint);
        canvas.drawRoundRect(box3, animRadius, animRadius, linePaint);
        canvas.drawRoundRect(box4, animRadius, animRadius, linePaint);
        //canvas.drawText("Month", width/2, height - 20, textPaint);

    }

    private void animateIcons(){
        ValueAnimator va = ValueAnimator.ofInt((int)(width * 0.8 / 2), 0);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
               anim = (int) valueAnimator.getAnimatedValue();
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
                //waveEnergy = waveEnergy - 0.2f;
                //if(waveEnergy < 0){
                // waveEnergy = 0;
                //}
                //Log.i("Energy", ""+waveEnergy);

            }
        });

        va.start();
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }
}
