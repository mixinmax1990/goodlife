package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownBubbleTextView extends androidx.appcompat.widget.AppCompatTextView {
    Paint bgPaint;
    Path arrowPath;
    RectF textBgRect;
    int width, height;
    int arrowHeight = 30;
    int padding = 30;
    public DownBubbleTextView(@NonNull Context context) {
        super(context);
    }

    public DownBubbleTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPadding(padding,padding / 2,padding, (padding / 2) + arrowHeight);
        setPaints();
    }

    private void setPaints() {
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.parseColor("#212226"));
        bgPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        textBgRect = new RectF(0,0,w,h - arrowHeight);
        width = w;
        height = h;

        setPath(h, w);

    }

    int startPoint, endPoint;
    private void setPath(int h, int w) {
        arrowPath = new Path();
        startPoint = (w / 2) - arrowHeight;
        endPoint = (w/2) + arrowHeight;
        arrowPath.moveTo(startPoint, h - arrowHeight);
        arrowPath.lineTo((int)(w / 2), h);
        arrowPath.lineTo(endPoint, h - arrowHeight);
        arrowPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(textBgRect, 20, 20, bgPaint);
        canvas.drawPath(arrowPath, bgPaint);


        super.onDraw(canvas);
    }
}
