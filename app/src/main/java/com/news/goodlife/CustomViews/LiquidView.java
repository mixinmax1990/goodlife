package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LiquidView extends FrameLayout {

    Paint liquidPaint, bgPaint;
    Path liquidPath;
    RectF frame;
    public LiquidView(@NonNull Context context) {
        super(context);
    }

    public LiquidView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }

    private void setPaints() {
        liquidPaint = new Paint();
        bgPaint = new Paint();

        liquidPaint.setStyle(Paint.Style.FILL);
        liquidPaint.setColor(Color.parseColor("#1a9658"));
        liquidPaint.setAntiAlias(true);

        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.parseColor("#ffffff"));
        bgPaint.setAntiAlias(true);
        bgPaint.setAlpha(7);

        liquidPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        frame = new RectF(0,0,getWidth(),getHeight());
        canvas.drawRect(frame, bgPaint);

        liquidPath.moveTo(0, wave);
        liquidPath.lineTo(0, getHeight());
        liquidPath.lineTo(getWidth(),getHeight());
        liquidPath.lineTo(getWidth(), wave);
        liquidPath.close();

        canvas.drawPath(liquidPath, liquidPaint);

        Log.i("Wave", "" + wave + " - animatedvalue "+animatedValue);

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    int wave, animatedValue;
    public void animateWave() {
        ValueAnimator va = ValueAnimator.ofInt(0, 100);
        va.setDuration(1000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                animatedValue = (int) valueAnimator.getAnimatedValue();
                if(animatedValue > 49){
                    wave = 100 - animatedValue;
                }
                else{
                    wave = animatedValue;
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
        va.setRepeatCount(5);

        va.start();
    }
}
