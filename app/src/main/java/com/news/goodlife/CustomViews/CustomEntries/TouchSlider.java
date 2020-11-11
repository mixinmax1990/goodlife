package com.news.goodlife.CustomViews.CustomEntries;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class TouchSlider extends FrameLayout {
    Paint backgroundPaint, selectedPaint,beginingTextPaint, currentTextPaint, todayLine;
    Boolean darkMode;
    @ColorInt int touchsliderBackground, touchsliderSelectBG, textColor;
    public TouchSlider(@NonNull Context context) {
        super(context);
    }


    public TouchSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        darkMode = getResources().getBoolean(R.bool.dark);

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        textColor = typedValue.data;

        theme.resolveAttribute(R.attr.touchsliderBackground, typedValue, true);
        touchsliderBackground = typedValue.data;

        theme.resolveAttribute(R.attr.touchsliderSelectedBackground, typedValue, true);
        touchsliderSelectBG = typedValue.data;


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                animateGrowSlider();

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        setWillNotDraw(false);
        setPaints();
    }

    private void setPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(touchsliderBackground);

        selectedPaint = new Paint();
        selectedPaint.setStyle(Paint.Style.FILL);
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(touchsliderSelectBG);

        beginingTextPaint = new Paint();
        beginingTextPaint.setStyle(Paint.Style.FILL);
        beginingTextPaint.setAntiAlias(true);
        beginingTextPaint.setColor(textColor);

        currentTextPaint = new Paint();
        currentTextPaint.setStyle(Paint.Style.FILL);
        currentTextPaint.setAntiAlias(true);
        currentTextPaint.setColor(textColor);

        todayLine = new Paint();
        todayLine.setStyle(Paint.Style.FILL);
        todayLine.setColor(Color.parseColor("#FFFFFF"));

    }

    int position = 600;
    int paddingTop;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paddingTop = (int) (getHeight()/2 * paddingTopRatio);


        canvas.drawRect(new RectF(0,(0 + paddingTop), getWidth(), (getHeight()) - paddingTop), backgroundPaint);

        canvas.drawRect(new RectF(0,(0 + paddingTop),position + touchMoveDist, (getHeight() - paddingTop)), selectedPaint);

        canvas.drawLine(position,0, position, getHeight(), todayLine);
    }

    float paddingTopRatio;

    private void animateGrowSlider(){
        ValueAnimator va = ValueAnimator.ofFloat(1, 0);

        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                paddingTopRatio = (float) valueAnimator.getAnimatedValue();
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
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    int TD, touchMoveDist = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


            switch(event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:

                    TD = (int)event.getX();
                    //Log.i("Touched", "Down");
                    break;
                case MotionEvent.ACTION_UP:
                    //Log.i("Touched", "Up");
                    //position = position + touchMoveDist;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMoveDist =  (int) (event.getX() - TD);
                    //Log.i("Touched", "Move"+touchMoveDist);
                    invalidate();
                    break;
                default:
                    break;
            }


        return true;
    }
}
