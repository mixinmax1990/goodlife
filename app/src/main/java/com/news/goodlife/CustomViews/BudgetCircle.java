package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BudgetCircle extends FrameLayout {
    Paint barsPaint, barsBgPaint;
    RectF rectF;
    int height, width;
    int strokeSize = 30;

    int months = 3;
    boolean draw = false;
    float barMarginPerc = 0.1f;


    public BudgetCircle(@NonNull Context context) {
        super(context);
    }

    public BudgetCircle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setBarParams();

        setWillNotDraw(false);
        setPaints();
    }

    float barMargin;
    float sweepAngle;
    float startAngle;
    private void setBarParams() {
        barMargin = 14;
        sweepAngle = (float)(360 / months) - barMargin;
        startAngle = -90;

        Log.i("DRAWING STart", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
    }

    public void setMonths(int months) {
        this.months = months;
        setBarParams();
        invalidate();
    }

    private void setPaints(){
        barsPaint = new Paint();
        barsPaint.setColor(Color.WHITE);
        barsPaint.setAntiAlias(true);
        barsPaint.setStyle(Paint.Style.STROKE);
        barsPaint.setStrokeWidth(strokeSize);
        barsPaint.setStrokeCap(Paint.Cap.ROUND);
        barsPaint.setStrokeJoin(Paint.Join.ROUND);
        barsPaint.setPathEffect(new CornerPathEffect(20));

        barsBgPaint = new Paint();
        barsBgPaint.setStyle(Paint.Style.STROKE);
        barsBgPaint.setStrokeWidth(strokeSize);
        barsBgPaint.setColor(Color.WHITE);
        barsBgPaint.setStrokeCap(Paint.Cap.ROUND);
        barsBgPaint.setStrokeJoin(Paint.Join.ROUND);
        barsBgPaint.setPathEffect(new CornerPathEffect(20));
        barsBgPaint.setAntiAlias(true);
        barsBgPaint.setAlpha(50);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0 + strokeSize,0 + strokeSize,w - strokeSize,h - strokeSize);
        height = h;
        width = w;
        draw = true;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(draw){
            //draw

            for(int i = 0; i < months; i++){

                Log.i("DRAWING", "barMargin = "+ barMargin +"-- sweepAngle = "+ sweepAngle + " startAngle = "+ startAngle);
                canvas.drawArc(rectF, startAngle, sweepAngle, false, barsPaint);
                startAngle = startAngle + sweepAngle + barMargin;

            }
        }
    }
}
