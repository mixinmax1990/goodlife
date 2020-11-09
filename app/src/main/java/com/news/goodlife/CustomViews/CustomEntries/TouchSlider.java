package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawRect(new RectF(0,0,getWidth(),getHeight()), backgroundPaint);

        canvas.drawRect(new RectF(0,0,position + touchMoveDist, getHeight()), selectedPaint);

        canvas.drawLine(position,0, position, getHeight(), todayLine);
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
                    Log.i("Touched", "Down");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("Touched", "Up");
                    //position = position + touchMoveDist;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMoveDist =  (int) (event.getX() - TD);
                    Log.i("Touched", "Move"+touchMoveDist);
                    invalidate();
                    break;
                default:
                    break;
            }


        return true;
    }
}
