package com.news.goodlife.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.Models.ModuleCoords;
import com.news.goodlife.Models.RelationshipMap;

import java.util.List;

public class RelationshipMapView extends FrameLayout {
    //TODO change to Relationship Map
    List<RelationshipMap> mapData;

    Paint dotsPaint, linePaint;

    public RelationshipMapView(@NonNull Context context) {
        super(context);
    }

    public RelationshipMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPaints();

        setWillNotDraw(false);
    }

    private void setPaints() {

        dotsPaint = new Paint();
        dotsPaint.setStyle(Paint.Style.FILL);
        dotsPaint.setColor(Color.WHITE);
        dotsPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);


        linePaint.setColor(Color.WHITE);
        linePaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        linePaint.setPathEffect(new DashPathEffect(new float[]{40, 20, 20, 20}, animLinePhase));

        if(mapSet){
            for(RelationshipMap mc: mapData){
                canvas.drawCircle(mc.getFromX(),mc.getFromY(), 15, dotsPaint);
                canvas.drawCircle(mc.getToX(),mc.getToY(), 15, dotsPaint);

                canvas.drawLine(mc.getFromX(), mc.getFromY(), mc.getToX(), mc.getToY(), linePaint);

            }

        }
    }

    boolean mapSet = false;

    public void setMapData(List<RelationshipMap> mapData){
        this.mapData = mapData;
        mapSet = true;
        animateFlow();
        invalidate();
        //TODO Animate Map
    }


    public void removeMap(){
        mapSet = false;
        //TODO Stop the Animation
        invalidate();
        stopanimatingFlow();
    }
    ValueAnimator va;
    int animLinePhase;

    private void animateFlow() {
        va = ValueAnimator.ofInt(200, 0);
        va.setDuration(3000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animVal = (int)valueAnimator.getAnimatedValue();
                animLinePhase = animVal;
                invalidate();
            }
        });

        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new LinearInterpolator());
        va.start();
    }

    private void stopanimatingFlow() {

        va.end();
    }


}
