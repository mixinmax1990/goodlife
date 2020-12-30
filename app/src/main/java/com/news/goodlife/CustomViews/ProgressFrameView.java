package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgressFrameView extends FrameLayout {
    Paint background;
    @ColorInt int backgroundColor;

    RectF rectF = new RectF(0,0,0,0);

    int animProgress = 0;

    public ProgressFrameView(@NonNull Context context) {
        super(context);
    }

    public ProgressFrameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            backgroundColor = ((ColorDrawable) background).getColor();

        setPaints();

        setWillNotDraw(false);

    }

    private void setPaints() {

        background = new Paint();
        background.setStyle(Paint.Style.FILL);
        background.setColor(backgroundColor);
        background.setAntiAlias(true);
        background.setAlpha(255);

    }

    int height = 0;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF.left = 0;
        rectF.top = 0;
        rectF.right = getWidth();

        height = getHeight();
    }

    boolean animating = false;
    boolean set100 = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Make Background Transparent of On No Progress
        setBackgroundColor(Color.TRANSPARENT);

        rectF.bottom = animProgress;

        canvas.drawRect(rectF, background);
    }

    public void animateProgress(final boolean done){

        ValueAnimator va;

        if(done){
            va = ValueAnimator.ofInt(0, 100);

        }
        else{
            va = ValueAnimator.ofInt(100, 0);
            set100 = false;
        }

        va.setDuration(350);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animProgress = (height / 100) * (int)valueAnimator.getAnimatedValue();

                invalidate();
            }
        });


        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
                if(done){
                    set100 = true;
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        if(!animating){
            if(!set100){
                va.start();
            }
        }
    }
}
