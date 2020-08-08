package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GoalsBezierView extends View {

    Paint paint;
    Path path;
    Paint lineZeroPaint;
    Paint linesAmountPaint;
    int trendLineColorStart, trendLineColorEnd;

    int anc0X, anc0Y, anc1X, anc1Y;

    public GoalsBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (R.attr.financialGraphTrendlineGradEnd, value, true);
        trendLineColorStart = value.data;

        final TypedValue value2 = new TypedValue ();
        context.getTheme ().resolveAttribute (R.attr.financialGraphTrendlineGradEnd, value, true);
        trendLineColorEnd = value2.data;




    }

    public GoalsBezierView(Context context) {
        super(context);
    }
    float x0,y0,x1,y1,x2,y2;
    CashflowBezierPoint lastPoint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        path = new Path();
        lineZeroPaint = new Paint();
        linesAmountPaint = new Paint();

        //paint.setShader(new LinearGradient(0, 0, 0, 400, trendLineColorStart, trendLineColorEnd, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(trendLineColorStart);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setAlpha(80);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new DashPathEffect(new float[] {0,20}, 5));

        lineZeroPaint.setColor(Color.WHITE);
        lineZeroPaint.setAlpha(40);

        linesAmountPaint.setColor(Color.WHITE);
        linesAmountPaint.setAlpha(10);



        //x is Time
        //y is Amount
        List<CashflowBezierPoint> cashflowPath = testData();

        //max points 3
        int countPoints = 0;
        int factorTime = 1000;
        float factorAmount = 0.1f;
        float zeroMark = this.getHeight()/2;
        int lines = 0;



        float spread;
        lastPoint = new CashflowBezierPoint(0f,0f);
        path.moveTo(lastPoint.getTime()*factorTime, zeroMark - lastPoint.getAmount() * factorAmount);

        for(CashflowBezierPoint point: cashflowPath){
            //get each individual point
            x2 = point.getTime() * factorTime;
            y2 = zeroMark - (point.getAmount() * factorAmount);

            spread = (x2 - lastPoint.getTime() * factorTime)/4;

            x0 = (lastPoint.getTime() * factorTime) + spread;
            y0 = zeroMark - (lastPoint.getAmount() * factorAmount);

            x1 = x2 - spread;
            y1 = y2;

            path.cubicTo(x0, y0, x1, y1, x2, y2);
            lastPoint = point;

        }


        //path.cubicTo(100, 0, 400, 0, 500, 400);
        //path.cubicTo(600, 1000, 900, 1000, 1000, 400);
        //path.cubicTo(anc0X, anc0Y, anc1X, anc1Y, this.getWidth(), 500);
        canvas.drawPath(path, paint);

        //Drawing Lines





    }



    private void drawBezier(Path path, Paint paint,Canvas canvas){
        canvas.drawPath(path, paint);

    }

    private List<CashflowBezierPoint> testData(){
        List<CashflowBezierPoint> cashflowBezierPath = new ArrayList<>();

        CashflowBezierPoint cashflowBezierPoint;
        // Point One
        cashflowBezierPoint = new CashflowBezierPoint(2000f,1f);
        cashflowBezierPath.add(cashflowBezierPoint);

        return cashflowBezierPath;
    }

    public class CashflowBezierPoint {
        Float amount, time;
        public CashflowBezierPoint(Float amount, Float time){
            setAmount(amount);
            setTime(time);
        }

        public Float getAmount() {
            return amount;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }

        public Float getTime() {
            return time;
        }

        public void setTime(Float time) {
            this.time = time;
        }
    }
}

