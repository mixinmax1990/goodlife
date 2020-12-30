package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PageIndicatorBar extends FrameLayout {
    Paint bg, strokePaint, pointPaint;
    int points = 3;
    RectF borderRect = new RectF(0,0,0,0);

    public PageIndicatorBar(@NonNull Context context) {
        super(context);
    }

    public PageIndicatorBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPaints();
        setWillNotDraw(false);
    }

    int stroke = 5;

    private void setPaints() {
        bg = new Paint();
        bg.setStyle(Paint.Style.FILL);
        bg.setColor(Color.BLACK);
        bg.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(stroke);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setAntiAlias(true);
        strokePaint.setAlpha(100);

        pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setAntiAlias(true);
    }
    int size;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        size = getHeight() - (stroke * 2);

        float centerspan = (float)(size * points) / 2;
        borderRect.top = 0 + stroke;
        borderRect.left = (float)(getWidth()/2) - centerspan;
        borderRect.right = (float)(getWidth()/2) + centerspan;
        borderRect.bottom = getHeight() - stroke;

        canvas.drawRoundRect(borderRect, 100,100, bg);
        //canvas.drawRoundRect(borderRect, 100,100, strokePaint);

        float cX = (float)(((getWidth()/2) - centerspan) + (size/2));
        float cY = (float)(size + stroke * 2)/2;
        pointPaint.setAlpha(255);
        canvas.drawCircle((cX + size) + (size * moveperc),cY, (float)((size - stroke)/2), pointPaint);
        pointPaint.setAlpha(60);
        for(int i = 0; i < points; i++){

            canvas.drawCircle(cX,cY, (float)((size - stroke)/2), pointPaint);
            cX = cX + size;

        }

    }

    float moveperc = 0;
    public void movePoint(float move){

        moveperc = 1 - move;
        invalidate();
        Log.i("INDIMOVE", ""+move);
    }
    public void setPoints(int points) {
        this.points = points;
        invalidate();
    }
}
