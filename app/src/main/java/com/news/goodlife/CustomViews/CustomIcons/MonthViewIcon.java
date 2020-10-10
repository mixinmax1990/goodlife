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
        lineColor = "#FFFFFF";
        listeners();
        setPaints();

        box1 = new RectF();
        box2 = new RectF();
        box3 = new RectF();
        box4 = new RectF();

        radius = 20;

        setRect();

    }

    private void setRect() {

        box1.left = width * 0.25f;
        box1.top = width * 0.25f;
        box1.bottom = width * 0.48f;
        box1.right = width * .48f;

        box2.left = width * 0.02f + width / 2;
        box2.top = width * 0.25f;
        box2.bottom = width * 0.48f;
        box2.right = width * .25f + width / 2;

        box3.left = width * 0.25f;
        box3.top = width * 0.02f + width / 2;
        box3.bottom = width * .25f + width / 2;
        box3.right = width * .48f;

        box4.left = width * 0.02f + width / 2;
        box4.top =  width * 0.02f + width / 2;
        box4.bottom = width * .25f + width / 2;
        box4.right = width * .25f + width / 2;

    }

    private void setPaints() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.parseColor(lineColor));
        linePaint.setAlpha(50);
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
        canvas.drawRoundRect(box4, radius, radius, linePaint);
        //canvas.drawText("Month", width/2, height - 20, textPaint);

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }
}
