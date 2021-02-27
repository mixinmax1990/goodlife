package com.news.goodlife.CustomViews.CustomEntries;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;

public class PopUpFrame extends ConstraintLayout {
    String strokeColor, backgroundColor;
    @ColorInt int color;
    @ColorInt int expandDotColor;
    int backgroundColorInt;

    int strokeSize, borderRadius;

    boolean stroke, background, radius, strokeCol;
    int radiusSize = 0;

    Paint paintFill, paintStroke, expandableArcPaint, ripplePaint;
    RectF roundedRectangle, expandEdgeRectangle;
    Path roundPath;

    AttributeSet attrs;
    Context context;
    private boolean expandable = true;

    public PopUpFrame(@NonNull Context context) {
        super(context);
    }
    public PopUpFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;



        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.moduleBackground, typedValue, true);
        color = typedValue.data;

        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        expandDotColor = typedValue.data;


        backgroundColorInt = Color.parseColor("#1f2024");

        setAttributes(attrs);
        configurePaints();
        setWillNotDraw(false);

        setBackgroundColor("#212226");
        //#1DFFFFFF
    }


    int expandMargin = 25;
    int width, height, centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


    int rippleDiameter = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        roundedRectangle = new RectF((0 + strokeSize) + paddingLeft, (0 + strokeSize) + paddingTop, (getWidth() - strokeSize) - paddingRight, (getHeight() - strokeSize) - paddingBottom);
        expandEdgeRectangle = new RectF((int)(getWidth() - radiusSize * 1) - expandMargin, (int)(getHeight() - radiusSize * 1) - expandMargin, getWidth() - expandMargin, getHeight() - expandMargin);

//#1C2125
            roundPath = new Path();

            paintFill.setColor(color);
            //canvas.drawRoundRect(roundedRectangle, radiusSize, radiusSize, paintFill);
            paintFill.setColor(backgroundColorInt);
            //paintFill.setAlpha(10);

            roundPath.reset();
            roundPath.addRoundRect(roundedRectangle, radiusSize, radiusSize, Path.Direction.CW);
            roundPath.close();

            canvas.clipPath(roundPath);

            canvas.drawPath(roundPath, paintFill);
            //canvas.drawRoundRect(roundedRectangle, radiusSize, radiusSize, paintFill);
            //canvas


        if(stroke){

            canvas.drawRoundRect(roundedRectangle, radiusSize, radiusSize, paintStroke);
        }

        if(expandable){
           // canvas.drawCircle(getWidth() - 35, getHeight() -35, 5, expandableArcPaint);
        }


        if(drawRipple){

            canvas.drawCircle(touchX, touchY,rippleDiameter, ripplePaint);

        };
        super.onDraw(canvas);
    }
    private void configurePaints(){


        if(stroke){
            paintStroke = new Paint();
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setStrokeWidth((float) getStrokeSize());
            paintStroke.setAntiAlias(true);

            if(strokeCol){
                paintStroke.setColor(Color.parseColor(getStrokeColor()));

            }
            else{
                paintStroke.setColor(Color.parseColor("#FFFFFF"));
            }


            // All Set Draw Border
        }


            paintFill = new Paint();
            paintFill.setStyle(Paint.Style.FILL);
            paintFill.setAntiAlias(true);


        if(radius){
            radiusSize = getBorderRadius();
        }

        expandableArcPaint = new Paint();
        expandableArcPaint.setStyle(Paint.Style.FILL);
        expandableArcPaint.setStrokeWidth(3);
        expandableArcPaint.setColor(expandDotColor);
        if(expandDotColor == -16777216){
            expandableArcPaint.setAlpha(255);
        }
        else{
            expandableArcPaint.setAlpha(100);
        }

        expandableArcPaint.setAntiAlias(true);
        expandableArcPaint.setStrokeCap(Paint.Cap.ROUND);

        ripplePaint = new Paint();
        ripplePaint.setStyle(Paint.Style.FILL);
        ripplePaint.setColor(Color.WHITE);
        ripplePaint.setAlpha(20);
        ripplePaint.setAntiAlias(true);
    }

    public void setAttributes(AttributeSet attrs){
        this.attrs = attrs;
        TypedArray strokeSizeArray = context.obtainStyledAttributes(attrs, R.styleable.BorderRoundView);
        CharSequence strokeSize_cs = strokeSizeArray.getString(R.styleable.BorderRoundView_strokeCustomSize);
        if(strokeSize_cs != null){
            setStrokeSize(Integer.parseInt(strokeSize_cs.toString()));
            stroke = true;
        }
        else{
            strokeSize = 0;
        }

        TypedArray borderRadiusArray = context.obtainStyledAttributes(attrs, R.styleable.BorderRoundView);
        CharSequence borderRadius_cs = borderRadiusArray.getString(R.styleable.BorderRoundView_borderCustomRadius);
        if(borderRadius_cs != null){
            setBorderRadius(Integer.parseInt(borderRadius_cs.toString()));
            radius = true;
        }


        TypedArray strokeColorArray = context.obtainStyledAttributes(attrs, R.styleable.BorderRoundView);
        CharSequence strokeColor_cs = strokeColorArray.getString(R.styleable.BorderRoundView_strokeCustomColor);
        if(strokeColor_cs != null){
            setStrokeColor(strokeColor_cs.toString());
            strokeCol = true;
        }

        TypedArray backgroundColorArray = context.obtainStyledAttributes(attrs, R.styleable.BorderRoundView);
        CharSequence backgroundColor_cs = backgroundColorArray.getString(R.styleable.BorderRoundView_backgroundCustomColor);
        if(backgroundColor_cs != null){
            setBackgroundColor(backgroundColor_cs.toString());
            background = true;
        }

        borderRadiusArray.recycle();
        strokeColorArray.recycle();
        strokeSizeArray.recycle();
        backgroundColorArray.recycle();

    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }


    public void highlight(int type){

        switch(type){
            case 1:
                backgroundColorInt = Color.parseColor("#2A3240");
                break;
            case 2:
                backgroundColorInt = Color.parseColor("#FFFFFF");
                break;
            default:
                break;
        }

        invalidate();
    }
    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    boolean drawRipple = false;
    float touchX, touchY, fluct;
    float scale = 1;
    public void ripple(float x, float y, SuccessCallback callback){
        drawRipple = true;
        touchX = x;
        touchY = y;

        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        va.setDuration(400);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animVal = (float) animation.getAnimatedValue();
                rippleDiameter = (int)(animVal * (width));

                fluct = animVal;
                if(animVal > .5f){
                    fluct = 1 - animVal;
                }
                scale = 1 + fluct * 0.5f;
                setScaleX(scale);
                setScaleY(scale);

                invalidate();

            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                setElevation(100);
                setOutlineProvider(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                callback.success();
                drawRipple = false;
                setElevation(0);
                invalidate();
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

    int paddingLeft = 0;
    int paddingTop = 0;
    int paddingRight = 0;
    int paddingBottom = 0;
    public void bgPadding(int left, int top, int right, int bottom){

        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
        invalidate();

    }
}

