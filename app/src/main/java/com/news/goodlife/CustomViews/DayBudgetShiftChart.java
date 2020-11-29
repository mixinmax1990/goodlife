package com.news.goodlife.CustomViews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.Models.BezierCurvePoint;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayBudgetShiftChart  extends FrameLayout {
    Paint todayPaint, restPaint, daylinesPaint, bezierPaint;
    Path bezierPath;
    RectF dayrect = new RectF(0,0,0,0);

    Date day;
    String yesterday, today, tommorow;

    public DayBudgetShiftChart(@NonNull Context context) {
        super(context);
    }

    public DayBudgetShiftChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        setPaints();

        day = new Date();

        Calendar dayCal = Calendar.getInstance();
        dayCal.setTime(day);

        Calendar daybefore = Calendar.getInstance();
        daybefore.setTime(day);
        daybefore.add(Calendar.DATE, -1);

        Calendar daynext = Calendar.getInstance();
        daynext.setTime(day);
        daynext.add(Calendar.DATE, 1);

        yesterday = daybefore.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) + " " + daybefore.get(Calendar.DAY_OF_MONTH);
        today = dayCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) + " " + dayCal.get(Calendar.DAY_OF_MONTH);
        tommorow = daynext.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) + " " + daynext.get(Calendar.DAY_OF_MONTH);


        Log.i("Tommorow", ""+today);

    }

    private void setPaints() {
        todayPaint = new Paint();
        todayPaint.setStyle(Paint.Style.FILL);
        todayPaint.setAntiAlias(true);
        todayPaint.setColor(Color.parseColor("#FFFFFF"));

        restPaint = new Paint();
        restPaint.setStyle(Paint.Style.FILL);
        restPaint.setAntiAlias(true);
        restPaint.setColor(Color.parseColor("#FFFFFF"));

        daylinesPaint = new Paint();
        daylinesPaint.setStyle(Paint.Style.FILL);
        daylinesPaint.setAntiAlias(true);
        daylinesPaint.setColor(Color.parseColor("#FFFFFF"));
        daylinesPaint.setAlpha(100);
        daylinesPaint.setTextAlign(Paint.Align.CENTER);
        daylinesPaint.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);

        bezierPaint = new Paint();
        bezierPaint.setStyle(Paint.Style.STROKE);
        bezierPaint.setColor(Color.parseColor("#81A0ED"));
        bezierPaint.setStrokeWidth(14);
        bezierPaint.setAntiAlias(true);
        bezierPaint.setStrokeJoin(Paint.Join.ROUND);
        bezierPaint.setStrokeCap(Paint.Cap.ROUND);
        bezierPaint.setPathEffect(new CornerPathEffect(20));


        bezierPath = new Path();
    }

    int space;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        space = dpToPx(5);
        int daybaseline = getHeight() - (int)(getHeight() / 5 + dpToPx(5));
        int daywidth = (int)(getWidth() / 3) - space;
        daylinesPaint.setAlpha(100);
        //Yesterdayrect
        dayrect.bottom = daybaseline;
        dayrect.left = space;
        dayrect.right = (int)(getWidth() / 3) - (int)(space/2);
        dayrect.top = daybaseline + 10;
        canvas.drawRoundRect(dayrect, 50,50 , daylinesPaint);
        canvas.drawText(yesterday, (space + (int)(daywidth/2)), daybaseline + dpToPx(15), daylinesPaint);

        dayrect.left = (int)(getWidth() / 3) + (int)(space/2);
        dayrect.right = (int)(getWidth() / 3) + (int)(space/2) + daywidth;

        canvas.drawRoundRect(dayrect, 50,50 , daylinesPaint);
        canvas.drawText(today, ((int)(getWidth() / 3) + (int)(space/2) + (int)(daywidth/2)), daybaseline + dpToPx(15), daylinesPaint);


        dayrect.left = (int)(getWidth() / 3) + (int)(space/2) + daywidth + space;
        dayrect.right = (int)(getWidth() - space);

        canvas.drawRoundRect(dayrect, 50,50 , daylinesPaint);
        canvas.drawText(tommorow, (((int)(getWidth() / 3) + (int)(space/2) + daywidth + space) + (int)(space/2) + (int)(daywidth/2)), daybaseline + dpToPx(15), daylinesPaint);


        daylinesPaint.setAlpha(10);

        dayrect.left = (int)(getWidth()/3);
        dayrect.top = 0;
        dayrect.bottom = getHeight();
        dayrect.right = getWidth() - (int)(getWidth()/3);

        canvas.drawRect(dayrect, daylinesPaint);


        int margin = 20;
        //DrawBezier
        lastPoint = new BezierCurvePoint(140f,0f + margin);
        bezierPath.moveTo(lastPoint.getTime(), lastPoint.getAmount());
        setCurvePoint(new BezierCurvePoint(140f,(float)(getWidth()/3)), false);
        setCurvePoint(new BezierCurvePoint( 50f, (float)(getWidth()/3)*2), false);
        setCurvePoint(new BezierCurvePoint( 50f, (float)getWidth() - margin), false);

        canvas.drawPath(bezierPath, bezierPaint);

        canvas.drawCircle((int)(getWidth()/3), 140, dpToPx(4), todayPaint);

        canvas.drawCircle((int)(getWidth()/3) * 2, 50, dpToPx(4), todayPaint);
    }
    float x0,y0,x1,y1,x2,y2;
    BezierCurvePoint lastPoint;
    int currentX = 0;
    private void setCurvePoint(BezierCurvePoint point, boolean lastSelected){
        float spread;

        //get each individual point
        x2 = point.getTime();
        y2 = point.getAmount();

        spread = (x2 - lastPoint.getTime())/2;

        x0 = (lastPoint.getTime()) + spread;
        y0 = lastPoint.getAmount();

        x1 = x2 - spread;
        y1 = y2;

        // Draw Bezier Lines
        bezierPath.cubicTo(x0, y0, x1, y1, x2, y2);

        lastPoint = point;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
