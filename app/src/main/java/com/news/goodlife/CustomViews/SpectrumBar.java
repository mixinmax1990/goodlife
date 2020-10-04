package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.service.autofill.FillCallback;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class SpectrumBar extends FrameLayout {

    int pos, neg, budget;
    Paint backgroundPaint, posPaint, negPaint, budgetPaint, reachedBudget, zeroLine;
    RectF rectangleBG, rectanglePos, rectangleNeg, rectangleReachedBudget, rectangleBudget;
    int zeroMark;
    int radiusSize = 100;
    boolean drawn = false;

    AttributeSet attrs;
    Context context;
    public SpectrumBar(@NonNull Context context) {
        super(context);
    }

    public SpectrumBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });


        setWillNotDraw(false);
        this.context = context;
    }

    int growthPercentage = 1;

    public void animateCashflow() {
        ValueAnimator va = ValueAnimator.ofInt(0, 100);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                growthPercentage = (int) valueAnimator.getAnimatedValue();
                invalidate();
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

            }
        });

        va.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!drawn){
            zeroMark = getWidth()/2;
            rectangleBG = new RectF(0, (int)(getHeight() * 0.66) , getWidth(), getHeight());
            rectanglePos = new RectF(0, (int)(getHeight() * 0.66) , 0, getHeight());
            rectangleNeg = new RectF(zeroMark, (int)(getHeight() * 0.66) , zeroMark, getHeight());
            rectangleReachedBudget = new RectF(zeroMark, (int)(getHeight() * 0.66) , zeroMark, getHeight());
            rectangleBudget = new RectF(zeroMark, (int)(getHeight() * 0.66) , zeroMark, getHeight());
            drawn = true;
        }
        else{
            rectanglePos.right = ((int)(getWidth() * 0.7)/100) * growthPercentage;
            //rectangleNeg.left = zeroMark - (300/100) * growthPercentage;
            //rectangleBudget.left = zeroMark - (400/100) * growthPercentage;
            //rectangleReachedBudget.left = zeroMark - (400/100) * growthPercentage;
        }

        canvas.drawRoundRect(rectangleBG, radiusSize, radiusSize, backgroundPaint);
        canvas.drawRoundRect(rectanglePos, radiusSize, radiusSize, posPaint);
        //canvas.drawRoundRect(rectangleBudget, radiusSize, radiusSize, budgetPaint);
        //canvas.drawRoundRect(rectangleNeg, radiusSize, radiusSize, negPaint);
        //canvas.drawRoundRect(rectangleReachedBudget, radiusSize, radiusSize, reachedBudget);


    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    private void setPaints(){


        backgroundPaint = new Paint();
        posPaint = new Paint();
        reachedBudget = new Paint();
        negPaint = new Paint();
        budgetPaint = new Paint();
        zeroLine = new Paint();

        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setAlpha(5);

        reachedBudget.setStyle(Paint.Style.FILL);
        reachedBudget.setColor(Color.WHITE);
        reachedBudget.setAlpha(20);
        reachedBudget.setAntiAlias(true);

        zeroLine.setStyle(Paint.Style.STROKE);
        zeroLine.setColor(Color.WHITE);
        zeroLine.setAntiAlias(true);
        zeroLine.setAlpha(200);

        posPaint.setStyle(Paint.Style.FILL);
        posPaint.setColor(Color.parseColor("#FFFFFF"));
        posPaint.setAntiAlias(true);

        negPaint.setStyle(Paint.Style.FILL);
        negPaint.setColor(Color.parseColor("#961e37"));
        negPaint.setAntiAlias(true);

        budgetPaint.setStyle(Paint.Style.FILL);
        budgetPaint.setColor(Color.parseColor("#212E36"));
        //budgetPaint.setAlpha(50);
        budgetPaint.setAntiAlias(true);

    }


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
