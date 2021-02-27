package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class NofificationTextView extends androidx.appcompat.widget.AppCompatTextView {

    RectF rectF;
    Paint paint;

    public NofificationTextView(Context context) {
        super(context);
    }

    public NofificationTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

    }

    private void setPaintAndRect() {

        rectF = new RectF(0,0,width, height);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#D4D8E4"));
        paint.setAntiAlias(true);

    }

    int height, width;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        setPaintAndRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRoundRect(rectF, 100,100,paint);
        super.onDraw(canvas);
    }

}
