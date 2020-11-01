package com.news.goodlife.CustomViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BulletPointTextView extends androidx.appcompat.widget.AppCompatTextView {
    Paint bulletPointPaint;

    String bulletPointColor;
    int radius;

    public BulletPointTextView(@NonNull Context context) {
        super(context);
    }

    public BulletPointTextView(@NonNull Context context, @Nullable AttributeSet attrs, String bulletPointColor, int radius) {
        super(context, attrs);
        setBulletPointColor(bulletPointColor);
        setRadius(radius);

        setPaints();

        setWillNotDraw(false);
    }

    private void setPaints() {

            bulletPointPaint = new Paint();
            bulletPointPaint.setStyle(Paint.Style.FILL);
            bulletPointPaint.setColor(Color.parseColor(getBulletPointColor()));
            bulletPointPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getRadius()/2,(getHeight()/2), getRadius()/2, bulletPointPaint);

    }

    public String getBulletPointColor() {
        return bulletPointColor;
    }

    public void setBulletPointColor(String bulletPointColor) {
        this.bulletPointColor = bulletPointColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = dpToPx(radius);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
