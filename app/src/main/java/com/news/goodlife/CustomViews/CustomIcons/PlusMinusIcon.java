package com.news.goodlife.CustomViews.CustomIcons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

public class PlusMinusIcon extends View {

    Paint stroke;
    RectF minusRect, plusRect;

    int width, height;
    int strokeSize = 20;
    String tag;

    public PlusMinusIcon(Context context) {
        super(context);
    }

    public PlusMinusIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        colorInt = Color.parseColor("#FFFFFF");

        tag = getTag().toString();


    }


    private void setPaints() {

        stroke = new Paint();
        stroke.setColor(colorInt);
        stroke.setDither(true);
        stroke.setAlpha(alpha);
        stroke.setStyle(Paint.Style.FILL);
        stroke.setAntiAlias(true);

    }

    int centerX, centerY;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = h;
        width = w;
        centerX = width/2;
        centerY = height/2;

        strokeSize = width / 5;

        setPaints();
        setRects();
    }

    private void setRects() {

        minusRect = new RectF(0,centerY - (strokeSize/2),width,centerY + (strokeSize/2));
        plusRect = new RectF(centerX - (strokeSize / 2),0,centerX + (strokeSize / 2),height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(tag.equals("plus")){
            drawPlus(canvas);
        }
        else{
            drawMinus(canvas);
        }

    }

    private void drawMinus(Canvas canvas) {

        canvas.drawRoundRect(minusRect, 20,20,stroke);

    }

    private void drawPlus(Canvas canvas) {
        canvas.drawRoundRect(minusRect, 20,20,stroke);
        canvas.drawRoundRect(plusRect, 20,20,stroke);

    }

    int colorInt;
    int alpha = 255;
    public void selectIcon(){

        colorInt = Color.parseColor("#FFFFFF");
        alpha = 255;
        invalidate();

    }
    public void unselectIcon(){
        colorInt = Color.parseColor("#FFFFFF");
        alpha = 50;
        invalidate();
    }
}
