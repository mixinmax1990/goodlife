package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.news.goodlife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiquidView extends CardView {

    Paint liquidPaint, bgPaint, textPaint;
    Path liquidPath;
    RectF frame;

    int wave = 0;
    int animatedValue = 0;
    String liquidColor = "#59a7ff";
    String liquidTextPaint = "#009cff";
    String daysBudget;
    boolean isNegative = false;
    Boolean darkMode;
    @ColorInt int textColor;

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
        setLiquidColor("#ff6859");

    }

    public String getDaysBudget() {
        return daysBudget;
    }

    public void setDaysBudget(String daysBudget) {
        this.daysBudget = daysBudget;
    }

    public String getLiquidTextPaint() {
        return liquidTextPaint;
    }

    public void setLiquidTextPaint(String liquidTextPaint) {
        this.liquidTextPaint = liquidTextPaint;
    }

    public String getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(String remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    String remainingBudget;

    public String getLiquidColor() {
        return liquidColor;
    }

    public void setLiquidColor(String liquidColor) {
        this.liquidColor = liquidColor;
        setPaints();
        invalidate();
    }

    int  waveHeight, minWaveHeight, maxWaveHeight;
    public LiquidView(@NonNull Context context) {
        super(context);
    }

    public LiquidView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        darkMode = getResources().getBoolean(R.bool.dark);
        setWillNotDraw(false);
        setPaints();

        baseline = 200;
        waveNo = 4;
        textSize = 16;

        minWaveHeight = 10;
        maxWaveHeight = 40;

        setDaysBudget("200,00€");
        setRemainingBudget("152,45€");

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        textColor = typedValue.data;

    }

    int baseline, width, height, waveLength, x1,y1,x2,y2,x3,y3;

    private void setPaints() {
        liquidPaint = new Paint();
        bgPaint = new Paint();
        textPaint = new Paint();

        liquidPaint.setStyle(Paint.Style.FILL);
        liquidPaint.setColor(Color.parseColor(getLiquidColor()));
        //1a9658   961a34  009cff
        liquidPaint.setAntiAlias(true);

        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.parseColor(getLiquidColor()));
        bgPaint.setAntiAlias(true);
        bgPaint.setAlpha(80);

        if(textEnabled){
            textPaint.setAlpha(255);
        }
        else{
            textPaint.setAlpha(0);
        }
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor(getLiquidTextPaint()));
        textPaint.setAntiAlias(true);
    }

    public boolean isTextEnabled() {
        return textEnabled;
    }

    public void setTextEnabled(boolean textEnabled) {
        this.textEnabled = textEnabled;
    }

    boolean textEnabled;
    int waveOneHeight, waveTwoHeight, waveThreeHeight;

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    int textSize;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        //waveOneHeight = waveHeight

        frame = new RectF(0,0,getWidth(),getHeight());
        //canvas.drawRect(frame, bgPaint);

        liquidPath = new Path();

        createWaves(waveNo);

        liquidPath.lineTo(getWidth(),getHeight());
        liquidPath.lineTo(0, getHeight());
        liquidPath.close();

        canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),50,50,bgPaint);

        canvas.drawPath(liquidPath, liquidPaint);
        textPaint.setColor(Color.parseColor(getLiquidTextPaint()));
        textPaint.setTextSize(textSize * getResources().getDisplayMetrics().scaledDensity);

        textPaint.setColor(textColor);
        if(!noNumber){
            canvas.drawText(remainingBudget, getWidth()/2, height / 2 - (int)(randomWaveHeight(0) * waveMovementUp) , textPaint);
        }
        textPaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);
        //canvas.drawText("Budget", getWidth()/2,height - 30 - (int)(randomWaveHeight(0) * waveMovementUp) , textPaint );
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        //canvas.drawText("-"+daysBudget, width - 50, baseline / 2, textPaint);


    }
    boolean noNumber = false;
    public void noText(boolean noNumber){
        this.noNumber = noNumber;

    }
    int waveNo;
    private void createWaves(int waveCount){
        boolean top = true;
        int x1,y1,x2,y2,x3,y3;
        int prev_x3,prev_y3;
        int newWavePeakX, newWavePeakY;

        waveLength = (width / waveCount) + (int)(waveMovementForward * (width * 0.5));
        prev_x3 = 0;
        prev_y3 = baseline - (int)(randomWaveHeight(0) * waveMovementUp);

        liquidPath.moveTo(prev_x3, prev_y3);

        for(int i = 1; i <= waveCount; i++){
            newWavePeakX = (waveLength * i);

            if(top) {
                newWavePeakY = baseline + (int)(randomWaveHeight(i) * waveMovementUp);
                top = false;
            }
            else{
                newWavePeakY = baseline - (int)(randomWaveHeight(i) * waveMovementUp);
                top = true;
            }
                x1 = newWavePeakX - (waveLength / 2);
                y1 = prev_y3;

                x2 = prev_x3 + (waveLength / 2);
                y2 = newWavePeakY;

                x3 = prev_x3 = newWavePeakX;
                y3 = prev_y3 = newWavePeakY;

            liquidPath.cubicTo(x1,y1,x2,y2,x3,y3);
            }

    }
    List<WaveHeight> generatedWaveHeights = new ArrayList<>();
    private int randomWaveHeight(int i){

        if(!generatedWaveHeights.isEmpty()){
            for(WaveHeight waveHeight : generatedWaveHeights){
                if(waveHeight.getWaveNumber() == i){
                    return waveHeight.getWaveHeight();
                }
            }
        }



        Random random = new Random();
        int rand;
        while (true){
            rand = random.nextInt(maxWaveHeight);
            if(rand !=minWaveHeight) break;
        }

        WaveHeight waveHeight = new WaveHeight();
        waveHeight.setWaveNumber(i);
        waveHeight.setWaveHeight(rand);

        generatedWaveHeights.add(waveHeight);

        return rand;
    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }

    class WaveHeight {
        int waveNumber, waveHeight;

        public void WaveHeights() {
        }

        public int getWaveNumber() {
            return waveNumber;
        }

        public void setWaveNumber(int waveNumber) {
            this.waveNumber = waveNumber;
        }

        public int getWaveHeight() {
            return waveHeight;
        }

        public void setWaveHeight(int waveHeight) {
            this.waveHeight = waveHeight;
        }
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }



    float movement;
    float waveMovementUp = -1;
    float waveMovementForward = 0;
    float energyDepreciation = 0.005f;
    float waveEnergy = 1f;

    public void setBaseline(int baseline) {
        this.baseline = baseline;
    }

    boolean animated = false;
    public void animateWave() {
        if(!animated){

        ValueAnimator va = ValueAnimator.ofFloat(-1, 3);
        va.setDuration(randomValue(1500, 2000));
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                waveEnergy = waveEnergy - energyDepreciation;
                if(waveEnergy <= 0f){
                    waveEnergy = 0f;
                }
                movement  = ((float) valueAnimator.getAnimatedValue());

                if(movement > 1){
                    waveMovementUp = 2 - movement;
                }
                else{
                    waveMovementUp = movement;
                }
                waveMovementUp *= waveEnergy;

                waveMovementForward = (1 - waveEnergy);
                        //((waveMovementUp + 1) / 2)/3;
                //Log.i("Forward", ""+waveMovementForward);



               //Log.i("enerydep", ""+movement);
                //Log.i("MoveUP", ""+waveMovementUp);
                invalidate();
            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animated = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //waveEnergy = waveEnergy - 0.2f;
                //if(waveEnergy < 0){
                   // waveEnergy = 0;
                //}
                //Log.i("Energy", ""+waveEnergy);

            }
        });
        va.setRepeatCount(4);
        va.setInterpolator(null);


        va.start();
        }
    }

    public void resetWaveEnergy() {
        this.waveMovementUp = -1;
        this.waveMovementForward = 0;
        this.energyDepreciation = 0.005f;
        this.waveEnergy = 1f;
        invalidate();
        animated = false;
    }
}
