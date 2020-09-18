package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


public class VerticalBezierView extends FrameLayout {
    Paint line;

    public VerticalBezierView(Context context) {
        super(context);
    }

    public VerticalBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);

        line = new Paint();
        line.setColor(Color.WHITE);
        line.setAlpha(50);

        setWillNotDraw(false);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), line);

        super.onDraw(canvas);
    }
}
