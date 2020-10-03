package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CashflowGoalBubble extends androidx.appcompat.widget.AppCompatTextView {

    Paint paint, dottedLine, namePaint;
    String name;
    int animLineX = 0;

    public CashflowGoalBubble(@NonNull Context context) {
        super(context);
    }

    public CashflowGoalBubble(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.parseColor("#212E36"));
        paint.setAntiAlias(true);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.FILL);

        dottedLine = new Paint();
        dottedLine.setStyle(Paint.Style.STROKE);
        dottedLine.setColor(Color.WHITE);
        dottedLine.setAlpha(50);

        namePaint = new Paint();
        namePaint.setColor(Color.parseColor("#2F9EC7"));
        namePaint.setAntiAlias(true);
        namePaint.setTextAlign(Paint.Align.CENTER);
        namePaint.setTextSize(30);

        animateLine();

        name = getTag().toString();

        setWillNotDraw(false);
        setPadding(160,15,100,65);
        listener();
    }

    private void listener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                animateLine();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        dottedLine.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, animLineX));
        canvas.drawLine(0, getHeight() / 2, 60, getHeight() / 2, dottedLine);
        canvas.drawRoundRect(new RectF(61, 1, getWidth(), getHeight() - 1), 30, 30, paint);
        canvas.drawText(name, (getWidth() / 2) + 60, getHeight() - 30, namePaint);
        super.onDraw(canvas);
    }


    private void animateLine(){

        ValueAnimator va = ValueAnimator.ofInt(200, 0);
        va.setDuration(6000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animVal = (int)valueAnimator.getAnimatedValue();
                animLineX = animVal;
                invalidate();
            }
        });

        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new LinearInterpolator());
        va.start();

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }
}
