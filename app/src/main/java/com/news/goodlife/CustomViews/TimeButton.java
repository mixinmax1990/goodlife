package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public class TimeButton extends androidx.appcompat.widget.AppCompatTextView {
    Paint roundedBoarder;
    int dist;
    RectF rectF;

    public TimeButton(Context context) {
        super(context);
    }

    public TimeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        roundedBoarder = new Paint();
        roundedBoarder.setStyle(Paint.Style.STROKE);
        roundedBoarder.setStrokeWidth(1);
        roundedBoarder.setAntiAlias(true);
        roundedBoarder.setColor(Color.parseColor("#FABC04"));
        roundedBoarder.setAlpha(150);

        dist = 2;



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF = new RectF(dist, dist, this.getWidth() - dist, this.getHeight() - dist);

        canvas.drawRoundRect(rectF, convertDpToPixel(10), convertDpToPixel(10), roundedBoarder);

    }

    public float convertDpToPixel(float dp) {
        return dp * ((float) this.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
