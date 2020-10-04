package com.news.goodlife.CustomViews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiquidView extends CardView {

    Paint liquidPaint, bgPaint, textPaint;
    Path liquidPath;
    RectF frame;

    int wave = 0;
    int animatedValue = 0;
    String liquidColor = "#1a5a96";
    String liquidTextPaint = "#009cff";
    String daysBudget;
    boolean isNegative = false;

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
        setLiquidTextPaint("#ef335a");
        setLiquidColor("#961a34");

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

        setWillNotDraw(false);
        setPaints();

        baseline = 200;
        waveNo = 5;

        minWaveHeight = 10;
        maxWaveHeight = 50;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


            }
        });


        setDaysBudget("200,00€");
        setRemainingBudget("145,50€");
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
        bgPaint.setColor(Color.parseColor("#ffffff"));
        bgPaint.setAntiAlias(true);
        bgPaint.setAlpha(7);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setColor(Color.parseColor(getLiquidTextPaint()));
        textPaint.setAntiAlias(true);


    }

    int waveOneHeight, waveTwoHeight, waveThreeHeight;

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

        canvas.drawPath(liquidPath, liquidPaint);
        textPaint.setColor(Color.parseColor(getLiquidTextPaint()));
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAlpha(255);
        canvas.drawText(remainingBudget, 50, baseline + 50 - (int)(randomWaveHeight(0) * waveMovementUp) , textPaint);
        textPaint.setAlpha(120);
        canvas.drawText("Day's Budget", 50,height - 30 - (int)(randomWaveHeight(0) * waveMovementUp) , textPaint );
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setAlpha(70);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        //canvas.drawText("-"+daysBudget, width - 50, baseline / 2, textPaint);


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



               Log.i("enerydep", ""+movement);
                Log.i("MoveUP", ""+waveMovementUp);
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
}
