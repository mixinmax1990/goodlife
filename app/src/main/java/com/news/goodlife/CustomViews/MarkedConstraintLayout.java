package com.news.goodlife.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.news.goodlife.Singletons.SingletonClass;

public class MarkedConstraintLayout extends ConstraintLayout {

    Paint selectionBorderPaint;
    SingletonClass singletonClass = SingletonClass.getInstance();
    RectF rectF = new RectF(0,0,0,0);
    public MarkedConstraintLayout(@NonNull Context context) {
        super(context);
    }

    public MarkedConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();

        setWillNotDraw(false);
    }

    int strokeWidth = singletonClass.dpToPx(10);
    private void setPaints() {

        selectionBorderPaint = new Paint();
        selectionBorderPaint.setStyle(Paint.Style.STROKE);
        selectionBorderPaint.setStrokeWidth(strokeWidth);
        selectionBorderPaint.setColor(Color.parseColor("#FFFFFF"));
        selectionBorderPaint.setAntiAlias(true);
        selectionBorderPaint.setAlpha(100);

    }

    float radius = 50;
    float margin = (float)strokeWidth / 2;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(selected){
            rectF.top = margin;
            rectF.left = margin;
            rectF.bottom = getHeight() - margin;
            rectF.right = getWidth() - margin;

            //setAlpha

            selectionBorderPaint.setAlpha(alpha);

            canvas.drawRoundRect(rectF, radius, radius, selectionBorderPaint);
        }
    }

    boolean selected = false;
    public void selectView(Boolean select){
        selected = select;
        alpha = 155;
        invalidate();
    }

    int alpha = 255;
    public void fadeOutBorder(){
        ValueAnimator va = ValueAnimator.ofInt(255,0);
        va.setDuration(255);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animVal = (int)animation.getAnimatedValue();
                alpha = animVal;
                invalidate();
            }
        });

        va.start();
    }
}
