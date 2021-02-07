package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
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
        menuBackground.setColor(Color.WHITE);

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
    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

}
