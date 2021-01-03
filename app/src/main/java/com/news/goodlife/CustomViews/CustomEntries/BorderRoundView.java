package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.news.goodlife.R;

public class BorderRoundView extends ConstraintLayout {
    String strokeColor, backgroundColor;
    int strokeSize, borderRadius;
    @ColorInt int expandDotColor;

    boolean stroke, background, radius, strokeCol;
    int radiusSize = 0;

    Paint paintFill, paintStroke, expandableArcPaint;
    RectF roundedRectangle, expandEdgeRectangle;

    AttributeSet attrs;
    Context context;
    private boolean expandable = true;

    public BorderRoundView(@NonNull Context context) {
        super(context);
    }
    public BorderRoundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setAttributes(attrs);

        setWillNotDraw(false);

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        expandDotColor = typedValue.data;
        configurePaints();
    }


    int expandMargin = 25;

    @Override
    protected void onDraw(Canvas canvas) {
        roundedRectangle = new RectF(0 + strokeSize, 0 + strokeSize, getWidth() - strokeSize, getHeight() - strokeSize);
        expandEdgeRectangle = new RectF((int)(getWidth() - radiusSize * 1) - expandMargin, (int)(getHeight() - radiusSize * 1) - expandMargin, getWidth() - expandMargin, getHeight() - expandMargin);
        if(background){

            canvas.drawRoundRect(roundedRectangle, radiusSize, radiusSize, paintFill);
        }
        if(stroke){

            canvas.drawRoundRect(roundedRectangle, radiusSize, radiusSize, paintStroke);
        }

        if(expandable){
            //canvas.drawCircle(getWidth() - 35, getHeight() -35, 5, expandableArcPaint);
        }
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

        if(background){
            paintFill = new Paint();
            paintFill.setStyle(Paint.Style.FILL);
            paintFill.setColor(Color.parseColor(getBackgroundColor()));
            paintFill.setAntiAlias(true);

        }
        if(radius){
            radiusSize = getBorderRadius();
        }

        expandableArcPaint = new Paint();
        expandableArcPaint.setStyle(Paint.Style.FILL);
        expandableArcPaint.setStrokeWidth(3);
        expandableArcPaint.setColor(expandDotColor);
        expandableArcPaint.setAlpha(100);
        expandableArcPaint.setAntiAlias(true);
        expandableArcPaint.setStrokeCap(Paint.Cap.ROUND);

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
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }
}
