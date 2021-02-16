package com.news.goodlife.CustomViews.CustomIcons;

import android.accessibilityservice.GestureDescription;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.news.goodlife.R;

public class FunctionsIcon extends FrameLayout {


    Paint menuBackground;
    Path buttonPath;
    RectF rectFrame;


    // Var
    int height, width;
    int padding = 10;
    float[] radius = {40f,40f};
    float cornerRadius = 40f;


    MenuIcons.MenuIconColors nightColors;
    MenuIcons.MenuIconColors colors;

    float margin = .32f;
    float iconMargin;
    float textMargin;


    public FunctionsIcon(@NonNull Context context) {
        super(context);
    }

    public FunctionsIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();

    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    private void setPaints() {

        buttonPath = new Path();


        menuBackground = new Paint();
        menuBackground.setStyle(Paint.Style.FILL);
        menuBackground.setAntiAlias(true);
        menuBackground.setColor(Color.parseColor("#444951"));

        //unSelectMenu();
    }

    @Override
    public void setOutlineProvider(ViewOutlineProvider provider) {
        super.setOutlineProvider(provider);
    }

    private int subMenus = 0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        rectFrame = new RectF(padding,padding,w - padding,h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        buttonPath.reset();
        buttonPath.addRoundRect(rectFrame, cornerRadius, cornerRadius,Path.Direction.CW);
        buttonPath.close();
        /*
        menuBackground.setShader(new RadialGradient((float)getWidth(), (float)getHeight(),
                (float)getHeight()*1.5f, Color.parseColor("#17181A"), Color.parseColor("#373C41"),  Shader.TileMode.REPEAT));
        */
        canvas.drawRoundRect(rectFrame, radius[0],radius[1], menuBackground);
        canvas.clipPath(buttonPath);


        switch(FUNCTION){

            case "cashflow":
                canvas.drawCircle(c1X, c1Y, circlewWidth, cashflowPaint);
                canvas.drawCircle(c2X, c2Y, circlewWidth, cashflowPaint);

                //Draw Line
                cashflowPaint.setPathEffect(new DashPathEffect(new float[]{12, 12, 12, 12}, 0));

                canvas.drawLine(c1X - circlewWidth/2, c1Y -circlewWidth/2, c2X - circlewWidth/2, c2Y - circlewWidth/2, cashflowPaint);
                cashflowPaint.setPathEffect(null);
                break;
            default:
                break;

        }
    }

    String FUNCTION = "none";
    public void noFunction(){
        menuBackground.setStyle(Paint.Style.STROKE);
        menuBackground.setStrokeWidth(3);
        menuBackground.setPathEffect(new DashPathEffect(new float[]{8, 8, 8, 8}, 0));

    }

    Paint cashflowPaint;
    int circlewWidth, c1X,c1Y, c2X, c2Y, initc1X, initc1Y, initc2X, initc2Y;
    public void cashflowMagic(){
        menuBackground.setStyle(Paint.Style.FILL);
        FUNCTION = "cashflow";

        cashflowPaint = new Paint();
        cashflowPaint.setStyle(Paint.Style.STROKE);
        cashflowPaint.setColor(Color.BLACK);
        cashflowPaint.setAntiAlias(true);
        cashflowPaint.setStrokeWidth(8);

        circlewWidth = width / 10;
        initc1X = width / 2;
        initc1Y = height / 2;

        initc2X = width / 2;
        initc2Y = height / 2;

        c1X = initc1X;
        c1Y = initc1Y;

        c2X = initc2X;
        c2Y = initc2Y;

        animateInCashflow();
    }

    private void animateInCashflow() {
        int move = width / 5;

        ValueAnimator va = ValueAnimator.ofFloat(0,1);

        va.setDuration(350);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animValue = (float)animation.getAnimatedValue();

                c1X = (int)(initc1X - (move * animValue));
                c1Y = (int)(initc1Y + (move * animValue));

                c2X = (int)(initc2X + (move * animValue));
                c2Y = (int)(initc2Y - (move * animValue));

                invalidate();
            }
        });


        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

}
