package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DoughnutChartCategories extends ConstraintLayout {

    Paint test;
    int progress;
    float sweepAngle;
    int animatedProgress = 0;

    public DoughnutChartCategories(Context context, AttributeSet attrs) {
        super(context, attrs);
        test = new Paint();
        test.setStyle(Paint.Style.STROKE);
        test.setStrokeWidth(getWidth()/10);
        test.setColor(Color.RED);

        setWillNotDraw(false);

        progress = 30;

        getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                animateDoughnutGrow();
            }
        });


    }
    public DoughnutChartCategories(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sweepAngle = ((float)360 / 100) * 40;
        int size = 300;
        RectF rectF = new RectF(0 + 15, 0 + 15, getWidth() - 15, getWidth() - 15);


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setAlpha(20);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth()/12);

        canvas.drawArc(rectF, 0, 360, false, paint);

        paint.setColor(Color.parseColor("#ff6859"));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(20));
        canvas.drawArc(rectF, -90, sweepAngle, false, paint);

        size = size + 80;
        rectF = new RectF(getWidth()/2 - size / 2, getHeight()/2 - size / 2, getWidth()/2 + size / 2, getHeight()/2 + size / 2);

        paint.setColor(Color.parseColor("#ff6859"));
        paint.setStrokeWidth(8);
        paint.setPathEffect(new CornerPathEffect(20));
        //canvas.drawArc(rectF, -90, sweepAngle * 0.7f, false, paint);

        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        String txt = String.valueOf(animatedProgress);
        int xPos = getWidth() / 2 - (int)(paint.measureText(txt) / 2);
        int yPos = (int)(getHeight() / 2 - ((paint.descent() + paint.ascent()) / 2));
        //canvas.drawText(txt+"%", xPos, yPos, paint);

    }

    public void animateDoughnutGrow(){
        ValueAnimator va = ValueAnimator.ofInt(0, progress);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedProgress = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();

    }
    public void animateDoughnutShrink(){
        ValueAnimator va = ValueAnimator.ofInt(progress, 0);
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedProgress = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
