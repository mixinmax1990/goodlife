package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class LabeledTextView extends androidx.appcompat.widget.AppCompatTextView {

    Paint roundedStrokePaint, backgroundBox;
    String label;
    boolean darkMode;

    @ColorInt
    int strokeColorSelected, strokeColorUnselected, backgroundColor;

    public LabeledTextView(@NonNull Context context) {
        super(context);
    }

    public LabeledTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        darkMode = getResources().getBoolean(R.bool.dark);
        label = getTag().toString();

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.entryBorder, typedValue, true);
        strokeColorSelected = typedValue.data;

        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        strokeColorUnselected = typedValue.data;

        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            backgroundColor = ((ColorDrawable) background).getColor();



        setWillNotDraw(false);
        setPaints();
    }

    int text_height = 0;
    int text_width = 0;
    Rect bounds = new Rect();

    int strokeWidth ;
    int padding = 20;

    private void setPaints() {
        strokeWidth = (int) (2 * getResources().getDisplayMetrics().scaledDensity);
        roundedStrokePaint = new Paint();
        roundedStrokePaint.setAntiAlias(true);
        roundedStrokePaint.setStyle(Paint.Style.STROKE);
        roundedStrokePaint.setStrokeWidth(strokeWidth);
        roundedStrokePaint.setColor(strokeColorUnselected);
        roundedStrokePaint.setAlpha(150);
        roundedStrokePaint.setTextSize(12 * getResources().getDisplayMetrics().scaledDensity);

        //GetText Bounds
        roundedStrokePaint.getTextBounds(label, 0, label.length(), bounds);
        text_width = bounds.width();

        backgroundBox = new Paint();
        backgroundBox.setStyle(Paint.Style.FILL);
        backgroundBox.setAntiAlias(true);
        backgroundBox.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        roundedStrokePaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(0 + padding,0 + padding, getWidth() - padding, getHeight() - padding), 20,20, roundedStrokePaint);

        backgroundBox.setColor(backgroundColor);
        backgroundBox.setAlpha(255);
        canvas.drawRect(new RectF(50, 0, 90 + text_width , 50), backgroundBox);

        roundedStrokePaint.setStyle(Paint.Style.FILL);
        canvas.drawText(label, 70, padding + (int)(padding/2), roundedStrokePaint);
    }

    int height, width;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = getHeight();
        width = getWidth();

    }

    public void focus(boolean focus){

        if(focus){
            roundedStrokePaint.setColor(strokeColorSelected);
            roundedStrokePaint.setAlpha(255);
        }
        else{
            roundedStrokePaint.setColor(strokeColorUnselected);
            roundedStrokePaint.setAlpha(150);
        }

        invalidate();
    }

    public void setBackgroundColor(String color){
        backgroundBox.setColor(Color.parseColor(color));
        invalidate();
    }
}
