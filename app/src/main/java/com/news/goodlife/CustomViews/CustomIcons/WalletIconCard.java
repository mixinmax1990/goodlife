package com.news.goodlife.CustomViews.CustomIcons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.news.goodlife.Singletons.SingletonClass;

public class WalletIconCard extends CardView {

    Paint cardColor;
    RectF rectFBG = new RectF(0,0,0,0);
    RectF cardOneRect, cardTwoRect, cardThreeRect;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public WalletIconCard(@NonNull Context context) {
        super(context);
    }

    public WalletIconCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();

    }

    private void setPaints() {
        cardColor = new Paint();
        cardColor.setColor(Color.WHITE);
        cardColor.setAntiAlias(true);

    }


    int padding = singletonClass.dpToPx(8);
    float cardSizeRatio = 0.666f;
    int apart = singletonClass.dpToPx(10);

    int curtop = singletonClass.dpToPx(20);
    int cardNo = 3;
    int cardHeight, cardWidth;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectFBG.top = 0;
        rectFBG.left = 0;
        rectFBG.bottom = h;
        rectFBG.right = w;

        cardWidth = w - (padding * 2);
        cardHeight = (int)(cardWidth * cardSizeRatio);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cardOneRect = new RectF(padding,curtop,padding + cardWidth,curtop + cardHeight);
        cardTwoRect = new RectF(0,0,0,0);
        cardThreeRect = new RectF(0,0,0,0);

        canvas.drawRoundRect(cardOneRect, 20,20, cardColor);

        //canvas.drawRoundRect(rectFBG, 20,20, cardColor);
    }
}
