package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class RoundedTextView extends androidx.appcompat.widget.AppCompatTextView {

    String tag;
    int backgroundColor;
    Paint backgroundPaint;
    RectF rect = new RectF(0,0,0,0);

    public RoundedTextView(Context context) {
        super(context);
    }

    public RoundedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        try{
            tag = getTag().toString();
            backgroundColor = Color.parseColor(tag);
        }
        catch(Exception e){}

        setPaints();
    }

    private void setPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.bottom = getHeight();
        rect.top = 0;
        rect.right = getWidth();
        rect.left = 0;
        canvas.drawRoundRect(rect, 100,100 ,backgroundPaint);
    }
}
