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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Type;

public class DayDetailViewIcon extends FrameLayout {

    int width, height;
    String lineColor;

    Paint linePaint, textPaint;
    RectF box1, box2, box3, box4, box5;

    int dp, radius;

    public DayDetailViewIcon(@NonNull Context context) {
        super(context);
    }

    public DayDetailViewIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        lineColor = "#000000";
        listeners();
        setPaints();

        box1 = new RectF();
        box2 = new RectF();
        box3 = new RectF();

        radius = 30;

        setRect();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //animateIcons();
            }
        });

    }

    int anim;

    private void setRect() {

        box1.left = width * 0.2f;
        box1.top = - 100;
        box1.bottom = width * 0.15f - anim;
        box1.right = width * .8f;

        box2.left = width * 0.2f;
        box2.top = width * 0.22f - anim;
        box2.bottom = width * 0.78f + anim;
        box2.right = width * .8f;

        box3.left = width * 0.2f;
        box3.top = width * 0.85f + anim;
        box3.bottom = width + 100;
        box3.right = width * .8f;

    }

    private void animateIcons(){
        ValueAnimator va = ValueAnimator.ofInt(0, 20);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setRect();
        canvas.drawRoundRect(box1, radius, radius, linePaint);
        canvas.drawRoundRect(box2, radius, radius, linePaint);
        canvas.drawRoundRect(box3, radius, radius, linePaint);

        //canvas.drawText("Month", width/2, height - 20, textPaint);

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }
}
