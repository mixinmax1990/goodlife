package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.RectEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.service.autofill.FillCallback;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class WalletIcon extends FrameLayout {

    boolean Night = true;

    Paint menuText, menuBackground, cardBackground, pocketColor, pocketCutThickPaint, pocketCutSlimPaint;
    Path pocketCutout, bgPath;
    Bitmap cardOneLogo, cardTwoLogo, ardThreeLogo;
    String menuName = "Wallet";


    Paint cardColor;
    RectF rectFBG = new RectF(0,0,0,0);
    RectF cardRect = new RectF(0,0,0,0);


    SingletonClass singletonClass = SingletonClass.getInstance();

    RectF rectFrame;

    @ColorInt int baseBG, gradStart, gradEnd;
    int walletCutStroke = 25;


    public WalletIcon(@NonNull Context context) {
        super(context);
    }

    public WalletIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);



        setPaints();
    }

    private void setPaints() {
        menuText = new Paint();
        menuText.setStyle(Paint.Style.FILL);
        menuText.setColor(Color.WHITE);
        menuText.setAntiAlias(true);

        menuBackground = new Paint();
        menuBackground.setStyle(Paint.Style.FILL);

        cardColor = new Paint();
        cardColor.setColor(Color.WHITE);
        cardColor.setAlpha(255);
        cardColor.setAntiAlias(true);
        cardColor.setShadowLayer(12,0,0,Color.parseColor("#66000000"));


        pocketColor = new Paint();
        pocketColor.setStyle(Paint.Style.FILL);
        pocketColor.setColor(Color.RED);
        pocketColor.setAntiAlias(true);
        pocketColor.setShadowLayer(12,0,0,Color.parseColor("#66000000"));

        pocketCutThickPaint = new Paint();
        pocketCutThickPaint.setStyle(Paint.Style.STROKE);
        pocketCutThickPaint.setStrokeWidth(3);
        pocketCutThickPaint.setPathEffect(new DashPathEffect(new float[]{8, 8, 8, 8}, 0));
        pocketCutThickPaint.setColor(Color.BLACK);
        pocketCutThickPaint.setAlpha(100);
        pocketCutThickPaint.setAntiAlias(true);



        bgPath = new Path();

        setWillNotDraw(false);

    }

    int padding = 10;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

    }

    float margin = .32f;
    int textMargin;

    List<String> banks = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {

        rectFrame = new RectF(padding,padding, getWidth() - padding,getHeight() - padding);

        bgPath.reset();
        bgPath.addRoundRect(rectFrame, 40, 40, Path.Direction.CW);
        bgPath.close();

        menuBackground.setColor(Color.parseColor("#444951"));
        menuBackground.setAntiAlias(true);

        canvas.drawRoundRect(rectFrame, 40, 40, menuBackground);
        canvas.clipPath(bgPath);

        //Draw Cards
        banks.add("Volksbank");
        banks.add("Volksbank");


        setCardBanks(banks, canvas);

        //drawCard(canvas, 1);

        //drawCard(canvas, "Three");

        drawCutout(canvas);

        textMargin = (int)(getWidth() * margin);

        menuText.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);
        menuText.setTextAlign(Paint.Align.CENTER);

        //canvas.drawText("Wallet", (int)(getWidth()/2), (int)(getHeight()) - (textMargin / 2), menuText);




        super.onDraw(canvas);
    }

    public void setCardBanks(List<String> banks, Canvas canvas){

        int count = 0;
        for(String bank: banks){
            count ++;

            Bitmap icon = getBankIcon(bank);


            drawCard(canvas, count, icon);


        }

    }

    private Bitmap getBankIcon(String bankname){
        Bitmap bankicon;

        switch(bankname){
            case "Sparkasse":
                bankicon = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("sparkasse_logo", "drawable", getContext().getPackageName()));
                //bankicon = getResources().getDrawable(getResources().getIdentifier("sparkasse_logo", "drawable", getContext().getPackageName()), null);
                break;
            case "Volksbank":
                bankicon = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("volksbank_logo", "drawable", getContext().getPackageName()));

                break;
            default:
                bankicon = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("sparkasse_logo", "drawable", getContext().getPackageName()));
                break;
        }

        return bankicon;
    }

    float cutoutTopRatio = 0.5f;

    int centerX;
    int cutoutWidth = singletonClass.dpToPx(40);
    int cutoutHeight = singletonClass.dpToPx(10);

    int x0,y0,x1,y1,x2,y2;

    private void drawCutout(Canvas canvas) {

        int cutMargin = walletCutStroke / 2;

        Path cutThick = new Path();
        Path cutSlim = new Path();

        centerX = getWidth() / 2;
        float spread;


        int cutoutTop = (int)(getHeight() * cutoutTopRatio);

        pocketCutout = new Path();
        pocketCutout.moveTo(0, cutoutTop);
        cutThick.moveTo(0, cutoutTop + cutMargin);

        pocketCutout.lineTo(centerX - (int)(cutoutWidth/2), cutoutTop);
        cutThick.lineTo(centerX - (int)(cutoutWidth/2), cutoutTop + cutMargin);

        spread = (int)(cutoutWidth / 4);

        //First Bezier
        x2 = centerX;
        y2 = cutoutTop + cutoutHeight;

        x0 = (int)((centerX - (cutoutWidth/2)) + spread);
        y0 = cutoutTop;

        x1 = (int)(x2 - spread);
        y1 = y2;

        pocketCutout.cubicTo(x0,y0,x1,y1,x2,y2);
        cutThick.cubicTo(x0,y0 + cutMargin,x1,y1 + cutMargin,x2,y2 + cutMargin);

        //Second Bezier

        x2 = centerX + (int)(cutoutWidth/2);
        y2 = cutoutTop;

        x0 = (int)(centerX + spread);
        y0 = cutoutTop + cutoutHeight;

        x1 = (int)(x2 - spread);
        y1 = y2;

        pocketCutout.cubicTo(x0,y0,x1,y1,x2,y2);
        cutThick.cubicTo(x0,y0 + cutMargin,x1,y1 + cutMargin,x2,y2 + cutMargin);


        pocketCutout.lineTo(getWidth(), cutoutTop);
        cutThick.lineTo(getWidth(), cutoutTop + cutMargin);

        pocketCutout.lineTo(getWidth(), getHeight());
        pocketCutout.lineTo(0, getHeight());
        pocketCutout.close();

        pocketColor.setColor(Color.parseColor("#444951"));
        canvas.drawPath(pocketCutout, pocketColor);
        canvas.drawPath(cutThick, pocketCutThickPaint);

    }



    int cardPadding = singletonClass.dpToPx(8);
    float cardSizeRatio = 0.666f;
    int apart = singletonClass.dpToPx(10);

    int topMargin = singletonClass.dpToPx(15);
    int cardNo = 3;
    int cardHeight, cardWidth;
    int left, top, bottom, right;

    private void drawCard(Canvas canvas, int Card, Bitmap icon) {
        cardWidth = getWidth() - (cardPadding * 2);
        cardHeight = (int)(cardWidth * cardSizeRatio);

        left = cardPadding;
        right = cardPadding + cardWidth;
        switch(Card){
            case 1:
                top = topMargin;
                bottom = topMargin + cardHeight;
                break;
            case 2:
                top = topMargin + apart;
                bottom = top + cardHeight;
                break;
            case 3:
                break;
            default:
                break;
        }

        cardRect.top = top;
        cardRect.left = left;
        cardRect.right = right;
        cardRect.bottom = bottom;

        canvas.drawRoundRect(cardRect, 20, 20, cardColor );
        int topGravity = 20;
        int iconWidth = singletonClass.dpToPx(10);
        int iconCenterX = left + (cardWidth/2);
        int iconCenterY = top + (cardHeight/2);
        //canvas.drawBitmap(icon, null, new Rect(iconCenterX - iconWidth,(iconCenterY - iconWidth) - topGravity,iconCenterX + iconWidth,(iconCenterY + iconWidth) - topGravity), null);

    }
}
