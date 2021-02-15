package com.news.goodlife.CustomViews.CustomIcons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class FlagGoalIcon extends View {
    Paint thickStroke, slimStroke;
    Path thickPath, thinPath;

    int height, width;

    public FlagGoalIcon(Context context) {
        super(context);
    }

    public FlagGoalIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h;

        setPaints();
        setPath();
    }

    int margin, section;
    float gap;
    private void setPath() {
        margin = 20;

        section = (height - (margin * 2))/3;
        gap = section * 0.5f;

        thickPath = new Path();
        thickPath.moveTo(margin, height - margin);
        thickPath.lineTo(margin, margin);
        //Make Arc

        //---
        thickPath.lineTo(margin + (section * 1.5f), margin + section);
        thickPath.lineTo( margin + gap, (margin + (section * 2)) - gap);

    }

    private void setPaints() {

        thickStroke = new Paint();
        thickStroke.setStyle(Paint.Style.STROKE);
        thickStroke.setStrokeWidth(9);
        thickStroke.setColor(Color.BLACK);
        thickStroke.setStrokeCap(Paint.Cap.ROUND);
        thickStroke.setStrokeJoin(Paint.Join.ROUND);
        thickStroke.setPathEffect(new CornerPathEffect(20));
        thickStroke.setAlpha(100);

        slimStroke = new Paint();
        slimStroke.setStyle(Paint.Style.STROKE);
        slimStroke.setStrokeWidth(2);
        slimStroke.setColor(Color.BLACK);
        slimStroke.setStrokeCap(Paint.Cap.ROUND);
        slimStroke.setStrokeJoin(Paint.Join.ROUND);
        slimStroke.setPathEffect(new CornerPathEffect(20));
        slimStroke.setAlpha(100);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(thickPath, thickStroke);
    }
}
