package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {
    Paint underlinePaint;
    public UnderlinedTextView(@NonNull Context context) {
        super(context);
    }

    public UnderlinedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        underlinePaint = new Paint();
        underlinePaint.setStyle(Paint.Style.STROKE);
        underlinePaint.setColor(Color.parseColor("#FFFFFF"));
        underlinePaint.setAntiAlias(true);
        underlinePaint.setStrokeWidth(1);
        underlinePaint.setAlpha(0);


        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, underlinePaint);
        super.onDraw(canvas);
    }
}
