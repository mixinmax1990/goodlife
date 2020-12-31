package com.news.goodlife.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressBarLabeld extends ConstraintLayout {

    Paint bgPaint, progressPaint, textPaint;
    RectF rectF = new RectF(0,0,0,0);
    RectF rectFprogress = new RectF(0,0,0,0);

    String label = "";
    int progress = 70;

    public ProgressBarLabeld(@NonNull Context context) {
        super(context);
    }

    public ProgressBarLabeld(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();
        label = getTag().toString();
        try {

        }
        catch(Exception e){

        }

        animatedProgress = progress;

        setWillNotDraw(false);
    }

    public void setLabel(String label){

        this.label = label;
        invalidate();

    }

    public void setProgress(int prog){

        this.progress = prog;
        animatedProgress = prog;
        invalidate();
    }

    private void setPaints() {
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.parseColor("#59a7ff"));
        bgPaint.setAntiAlias(true);
        bgPaint.setAlpha(30);

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(Color.parseColor("#59a7ff"));
        progressPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setAlpha(200);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(12 * getResources().getDisplayMetrics().scaledDensity);

    }


    int centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF.top = 0;
        rectF.left = 0;
        rectF.bottom = getHeight();
        rectF.right = getWidth();

        rectFprogress.top = 0;
        rectFprogress.left = 0;
        rectFprogress.bottom = getHeight();

        centerY = getHeight()/2 + 10;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectFprogress.right = (int)(getWidth() / 100) * animatedProgress;
        centerX = (int)(getWidth() / 100) * animatedProgress - 20;

        canvas.drawRoundRect(rectF, 200, 200, bgPaint);
        canvas.drawRoundRect(rectFprogress, 200,200, progressPaint);

        canvas.drawText(label, centerX,centerY,textPaint);


    }

    int animatedProgress;
    public void animateProgress(){
        ValueAnimator va = ValueAnimator.ofInt(0, progress);

        va.setDuration(600);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animVal = (int)valueAnimator.getAnimatedValue();
                Log.i("Animating", ""+animVal);

                animatedProgress = animVal;
                invalidate();

            }
        });

        va.start();
    }
}
