package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class SelectLinePointView extends androidx.appcompat.widget.AppCompatTextView {
    Paint bg, stroke, text;
    String no;
    @ColorInt int bgColor;
    public SelectLinePointView(@NonNull Context context) {
        super(context);
    }

    public SelectLinePointView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        no = getTag().toString();
        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.backgroundSecondary, typedValue, true);
        bgColor = typedValue.data;

        setPaints();
    }

    int strokewidth = 5;
    private void setPaints() {
        bg = new Paint();
        bg.setStyle(Paint.Style.FILL);
        bg.setColor(bgColor);
        bg.setAntiAlias(true);

        stroke = new Paint();
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(strokewidth);
        stroke.setColor(Color.WHITE);
        stroke.setAntiAlias(true);
        stroke.setAlpha(120);

        text = new Paint();
        text.setStyle(Paint.Style.FILL);
        text.setColor(Color.WHITE);
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(16 * getResources().getDisplayMetrics().scaledDensity);
        text.setAntiAlias(true);
    }

    float X = 0;
    int size;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(X == 0){
            X = getX();
        }

        size = getHeight();

    }

    public boolean selected = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = getHeight();
        float newX = X - (float)((size/2) + strokewidth);
        setX(newX);

        setPadding(size + 20, 20,10,20);

        if(selected){
            bg.setColor(Color.parseColor("#FFFFFF"));
        }
        else{
            bg.setColor(bgColor);
        }
        canvas.drawCircle((float)(size/2), (float)(size/2), (float)(size/2), bg);
        canvas.drawCircle((float)(size/2), (float)(size/2), (float)(size/2) - strokewidth, stroke);
        //canvas.drawText(no,(float)(size/2), (float)(size/2), text);
    }

    public void select(){
        this.selected = true;
        invalidate();
    }
}
