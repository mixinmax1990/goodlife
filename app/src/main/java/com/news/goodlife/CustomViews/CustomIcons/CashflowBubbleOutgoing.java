package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.service.autofill.FillCallback;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CashflowBubbleOutgoing extends androidx.appcompat.widget.AppCompatTextView {
    Paint paint, dottedLine, namePaint;
    String name;

    public CashflowBubbleOutgoing(@NonNull Context context) {
        super(context);
    }

    public CashflowBubbleOutgoing(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        setPadding(100,15,70,65);
        listener();
    }
    int animLineX = 0;

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

        canvas.drawLine(0, getHeight() / 2, 30, getHeight() / 2, dottedLine);
        canvas.drawRoundRect(new RectF(31, 1, getWidth(), getHeight() - 1), 30, 30, paint);
        canvas.drawText(name, (getWidth() / 2) + 30, getHeight() - 30, namePaint);
        super.onDraw(canvas);
    }


    private void animateLine(){

        ValueAnimator va = ValueAnimator.ofInt(0, 200);
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
