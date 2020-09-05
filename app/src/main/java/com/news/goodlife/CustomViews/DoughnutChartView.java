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

import androidx.constraintlayout.widget.ConstraintLayout;

public class DoughnutChartView extends ConstraintLayout {

    Paint test;
    int progress;
    float sweepAngle;
    int animatedProgress = 0;

    public DoughnutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        test = new Paint();
        test.setStyle(Paint.Style.STROKE);
        test.setStrokeWidth(10);
        test.setColor(Color.RED);

        setWillNotDraw(false);

        progress = 30;


    }
    public DoughnutChartView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sweepAngle = ((float)360 / 100) * animatedProgress;
        int size = 300;
        RectF rectF = new RectF(getWidth()/2 - size / 2, getHeight()/2 - size / 2, getWidth()/2 + size / 2, getHeight()/2 + size / 2);


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#26282C"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);

        canvas.drawArc(rectF, 0, 360, false, paint);

        paint.setColor(Color.parseColor("#4085F3"));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(20));
        canvas.drawArc(rectF, -90, sweepAngle, false, paint);

        size = size + 80;
        rectF = new RectF(getWidth()/2 - size / 2, getHeight()/2 - size / 2, getWidth()/2 + size / 2, getHeight()/2 + size / 2);

        paint.setColor(Color.parseColor("#FABC04"));
        paint.setStrokeWidth(8);
        paint.setPathEffect(new CornerPathEffect(20));
        canvas.drawArc(rectF, -90, sweepAngle * 0.7f, false, paint);

        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        String txt = String.valueOf(animatedProgress);
        int xPos = getWidth() / 2 - (int)(paint.measureText(txt) / 2);
        int yPos = (int)(getHeight() / 2 - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(txt+"%", xPos, yPos, paint);

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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
