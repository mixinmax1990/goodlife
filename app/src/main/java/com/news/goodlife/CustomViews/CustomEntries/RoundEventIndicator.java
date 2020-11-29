package com.news.goodlife.CustomViews.CustomEntries;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class RoundEventIndicator extends FrameLayout {
    Paint indicatorPaint;
    @ColorInt int textColor;
    float sweepAngle;
    RectF frame = new RectF(0,0,0,0);
    int eventCount = 5;

    public RoundEventIndicator(@NonNull Context context) {
        super(context);
    }

    public RoundEventIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        textColor = typedValue.data;

        setPaints();
    }

    private void setPaints() {
        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.STROKE);
        indicatorPaint.setColor(textColor);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStrokeJoin(Paint.Join.ROUND);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        indicatorPaint.setPathEffect(new CornerPathEffect(20));
        indicatorPaint.setAlpha(255);
    }

    float startAngle = 0;
    float margin = 15;
    int strokeWidth;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        strokeWidth = 8;
        indicatorPaint.setStrokeWidth(strokeWidth);
        frame.bottom = getHeight() - strokeWidth;
        frame.top = 0 + strokeWidth;
        frame.left = 0 + strokeWidth;
        frame.right = getWidth() - strokeWidth;


        sweepAngle = ((float)360 / eventCount) - margin;

        for(int i = 0; i < eventCount; i++){
            canvas.drawArc(frame, startAngle , 2, false, indicatorPaint);
            startAngle += strokeWidth + margin;
        }
    }
}
