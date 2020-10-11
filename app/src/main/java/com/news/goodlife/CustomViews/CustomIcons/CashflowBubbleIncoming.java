package com.news.goodlife.CustomViews.CustomIcons;

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

public class CashflowBubbleIncoming extends androidx.appcompat.widget.AppCompatTextView {
    Paint paint, dottedLine, namePaint;
    String name;

    public CashflowBubbleIncoming(@NonNull Context context) {
        super(context);
    }

    public CashflowBubbleIncoming(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setAntiAlias(true);
        paint.setAlpha(10);
        paint.setStyle(Paint.Style.FILL);

        dottedLine = new Paint();
        dottedLine.setStyle(Paint.Style.STROKE);
        dottedLine.setColor(Color.WHITE);
        dottedLine.setAlpha(50);

        namePaint = new Paint();
        namePaint.setColor(Color.parseColor("#FFFFFF"));
        namePaint.setAntiAlias(true);
        namePaint.setAlpha(130);
        namePaint.setTextAlign(Paint.Align.CENTER);
        namePaint.setTextSize(13 * getResources().getDisplayMetrics().scaledDensity);

        name = getTag().toString();

        setWillNotDraw(false);
        setPadding(70,15,100,65);
        listener();

    }

    int animLineX = 0;

    @Override
    protected void onDraw(Canvas canvas) {

        dottedLine.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, animLineX));

        canvas.drawRoundRect(new RectF(1, 1, getWidth()- 31, getHeight() - 1), 30, 30, paint);
        canvas.drawLine(getWidth() - 30, getHeight() / 2, getWidth(), getHeight() / 2, dottedLine);
        canvas.drawText(name, (getWidth() - 30)/2, getHeight() - 30, namePaint);

        super.onDraw(canvas);
    }

    private void listener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                animateLine();
            }
        });
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
        //va.start();

    }
}
