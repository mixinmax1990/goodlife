package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CashflowBubble extends ConstraintLayout {

    RectF rectF, tinyRectF;
    Paint paint;
    Path path;


    public CashflowBubble(Context context) {
        super(context);
    }

    public CashflowBubble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();
    }

    int strokePadding = 30;
    private void setPaints() {
        setPadding(strokePadding,0,strokePadding,strokePadding);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setShadowLayer(10, 0, 10, Color.parseColor("#31000000"));
        paint.setPathEffect(new CornerPathEffect(40));
    }

    int width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = h;
        width = w;

        rectF = new RectF(0,0,w,h);
    }


    boolean drawTiny = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRoundRect(rectF, 50,50,paint);

        if(drawTiny){
            canvas.drawPath(path, paint);
            //canvas.drawRoundRect(tinyRectF, 10,10,paint);
        }
    }

    public void setIn(boolean in){

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if(in){
                    Log.i("WidthOf", ""+ width);
                    //tinyRectF = new RectF(width - 70,0,width,70);
                    path = new Path();
                    path.moveTo(width, 0);
                    path.lineTo(width, height - strokePadding);
                    path.lineTo(strokePadding, height - strokePadding);
                    path.lineTo(strokePadding, 0);
                    path.close();
                    paint.setColor(Color.parseColor("#26282C"));
                    //tinyRectF = new RectF(0,0,70,70);
                }
                else{
                    //tinyRectF = new RectF(0,0,70,70);
                    path = new Path();
                    path.moveTo(width, 0);
                    path.lineTo(width, height - strokePadding);
                    path.lineTo(0, height - strokePadding);
                    path.lineTo(0, 0);
                    path.close();
                    paint.setColor(Color.parseColor("#26282C"));

                }

                drawTiny = true;
                invalidate();

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

}
