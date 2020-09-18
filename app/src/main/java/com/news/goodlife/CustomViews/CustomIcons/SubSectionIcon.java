package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SubSectionIcon extends androidx.appcompat.widget.AppCompatTextView {

    Paint linePaint = new Paint();
    Path vectorPath = new Path();
    Matrix drawMatrix = new Matrix();
    int width, height;
    int alpha = 255;
    float scaleX, scaleY;
    boolean selected = true;
    public SubSectionIcon(@NonNull Context context) {
        super(context);
    }

    public SubSectionIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        linePaint.setAntiAlias(true);
        selected = true;
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.parseColor("#202125"));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //animateView();
            }
        });
        setWillNotDraw(false);

        selectTab();



    }

    public void unSelectTab(){

        setAlpha(.2f);
    }
    public void selectTab(){
        setAlpha(1f);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        linePaint.setAlpha(alpha);
        width = getWidth();
        height = getHeight();
        scaleX = width / 100f;
        scaleY = height / 100f;

        drawMatrix.setScale(scaleX, scaleY);

        setVectorPath();

        vectorPath.transform(drawMatrix);


        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), 20, 20, linePaint);

        super.onDraw(canvas);
    }

    private void setVectorPath(){




    }

    private void animateView(){
        ValueAnimator va = ValueAnimator.ofInt(20, 50);
        va.setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                alpha = (int) valueAnimator.getAnimatedValue();
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

        //va.start();
    }


}
