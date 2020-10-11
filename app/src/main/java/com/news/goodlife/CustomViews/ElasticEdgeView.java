package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ElasticEdgeView extends FrameLayout {

    Paint bodyPaint;
    Path elasticPath;

    int touchDown = 1500;
    int pull = 100;
    int width, height, elasticContainerHeight, elasticContainerWidth;

    public ElasticEdgeView(@NonNull Context context) {
        super(context);
    }

    public ElasticEdgeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        setPaints();


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                touchDown = getHeight()/2;
                animateCloseEdge();

            }
        });

    }

    private void setPaints() {
        bodyPaint = new Paint();
        bodyPaint.setColor(Color.parseColor("#000000"));
        bodyPaint.setAntiAlias(true);



    }

    int x1,y1,x2,y2,x3,y3;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        elasticContainerHeight = (int)(getHeight() * .8f);

        elasticPath = new Path();
        //Log.i("PullANim", ""+pull);

        elasticPath.moveTo(getWidth(), touchDown - (elasticContainerHeight/2));
        x1 = getWidth();
        y1 = touchDown - ((elasticContainerHeight/2)/2);
        x2 = getWidth() - pull;
        y2 = touchDown - ((elasticContainerHeight/2)/2);
        x3 = getWidth() - pull;
        y3 = touchDown;
        elasticPath.cubicTo(x1,y1,x2,y2,x3,y3);
        x1 = getWidth() - pull;
        y1 = touchDown + ((elasticContainerHeight/2)/2);
        x2 = getWidth();
        y2 = touchDown + ((elasticContainerHeight/2)/2);
        x3 = getWidth();
        y3 = touchDown + (elasticContainerHeight/2);
        elasticPath.cubicTo(x1,y1,x2,y2,x3,y3);
        elasticPath.close();

        canvas.drawPath(elasticPath,bodyPaint);


    }

    public void animateCloseEdge(){
        ValueAnimator va = ValueAnimator.ofInt(pull, 0);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setPull((int) valueAnimator.getAnimatedValue());
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //waveEnergy = waveEnergy - 0.2f;
                //if(waveEnergy < 0){
                // waveEnergy = 0;
                //}
                //Log.i("Energy", ""+waveEnergy);

            }
        });

        va.start();
    }



    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    public int getTouchDown() {
        return touchDown;
    }

    public void setTouchDown(int touchDown) {
        this.touchDown = touchDown;
    }

    public int getPull() {
        return pull;
    }

    public void setPull(int pull) {
        this.pull = pull;
        invalidate();
        //postInvalidate();
    }


}
