package com.news.goodlife.CustomViews.CustomIcons;

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

public class DayViewIcon extends FrameLayout {

    int width, height;
    String lineColor;

    Paint linePaint, textPaint;
    RectF box1, box2, box3, box4, box5;

    int dp, radius;

    public DayViewIcon(@NonNull Context context) {
        super(context);
    }

    public DayViewIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        lineColor = "#393A3E";
        listeners();
        setPaints();

        box1 = new RectF();
        box2 = new RectF();
        box3 = new RectF();

        radius = 30;

        setRect();

    }

    private void setRect() {

        box1.left = width * 0.2f;
        box1.top = - 100;
        box1.bottom = width * 0.15f;
        box1.right = width * .8f;

        box2.left = width * 0.2f;
        box2.top = width * 0.22f;
        box2.bottom = width * 0.78f;
        box2.right = width * .8f;

        box3.left = width * 0.2f;
        box3.top = width * 0.85f;
        box3.bottom = width + 100;
        box3.right = width * .8f;

    }

    private void setPaints() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.parseColor(lineColor));
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
