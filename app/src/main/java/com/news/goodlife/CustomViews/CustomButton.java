package com.news.goodlife.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.news.goodlife.R;

public class CustomButton extends androidx.appcompat.widget.AppCompatTextView {

    Paint roundedBoarder, textPaint;
    int dist;
    RectF rectF;
    String customRadius, customBackgroundColor;
    AttributeSet attrs;
    Context context;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setWillNotDraw(false);


        setAttributes(attrs);

        roundedBoarder = new Paint();
        roundedBoarder.setStyle(Paint.Style.FILL);
        roundedBoarder.setStrokeWidth(1);
        roundedBoarder.setAntiAlias(true);
        roundedBoarder.setColor(Color.parseColor(getCustomBackgroundColor()));
        roundedBoarder.setAlpha(255);

        dist = 2;

        textPaint = new Paint();
        textPaint.setTextSize(this.getTextSize());
        textPaint.setColor(this.getCurrentTextColor());
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);


    }

    private void setAttributes(AttributeSet attrs) {
        this.attrs = attrs;
        //set custom attributes with TypedArray
        TypedArray arrRadius = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        CharSequence customRadius_cs = arrRadius.getString(R.styleable.CustomButton_customRadius);
        if (customRadius_cs != null) {
            // Do something with foo_cs.toString()
            setCustomRadius(customRadius_cs.toString());
        }
        arrRadius.recycle();  // Do this when done.*/

        //set custom attributes with TypedArray
        TypedArray arrBG = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        CharSequence customBG_cs = arrBG.getString(R.styleable.CustomButton_customBackgroundColor);
        if (customBG_cs != null) {
            // Do something with foo_cs.toString()
            setCustomBackgroundColor(customBG_cs.toString());
        }
        arrBG.recycle();  // Do this when done.*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF = new RectF(dist, dist, this.getWidth() - dist, this.getHeight() - dist);

        canvas.drawRoundRect(rectF, convertDpToPixel(getCustomRadius()), convertDpToPixel(getCustomRadius()), roundedBoarder);
        canvas.drawText((String)this.getText(), this.getWidth()/2f,this.getHeight()/2f + 10,textPaint);
    }

    public int getCustomRadius() {
        return Integer.parseInt(customRadius);
    }

    public void setCustomRadius(String customRadius) {
        this.customRadius = customRadius;
    }

    public String getCustomBackgroundColor() {
        return customBackgroundColor;
    }

    public void setCustomBackgroundColor(String customBackgroundColor) {
        this.customBackgroundColor = customBackgroundColor;
    }

    public float convertDpToPixel(float dp) {
        return dp * ((float) this.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
