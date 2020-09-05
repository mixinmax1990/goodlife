package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class GoalEventFrameView extends LinearLayout {
    Paint roundedBoarder;
    int dist;
    public GoalEventFrameView(Context context) {
        super(context);
    }

    public GoalEventFrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        dist = 30;
        roundedBoarder = new Paint();
        roundedBoarder.setStyle(Paint.Style.STROKE);
        roundedBoarder.setStrokeWidth(26);
        roundedBoarder.setAntiAlias(true);
        roundedBoarder.setColor(Color.parseColor("#008DFD"));
        roundedBoarder.setAlpha(150);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRoundRect(new RectF(dist, dist, getWidth() - dist, 246), 50, 50, roundedBoarder);
    }
}
