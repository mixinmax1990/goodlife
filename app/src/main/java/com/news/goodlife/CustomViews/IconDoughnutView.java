package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class IconDoughnutView extends FrameLayout {
    Paint catPaint, ring, ringbg;
    RectF rectF = new RectF(0,0,0,0);
    RectF ringRectF = new RectF(0,0,0,0);
    String category;
    Drawable icon;
    SingletonClass singletonClass = SingletonClass.getInstance();

    public IconDoughnutView(@NonNull Context context) {
        super(context);
    }

    String tag = "null";
    public IconDoughnutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        try{
            tag = getTag().toString();
        }
        catch(Exception e){

        }
        setPaints();
    }

    boolean add = false;
    Paint addPaint = new Paint();
    int addstroke = 3;
    public void addCat(){
        add = true;
        addPaint.setStyle(Paint.Style.STROKE);
        addPaint.setStrokeWidth(addstroke);
        addPaint.setColor(Color.WHITE);
        addPaint.setPathEffect(new DashPathEffect(new float[]{15, 15, 15, 15}, 0));
        addPaint.setAntiAlias(true);
        addPaint.setAlpha(100);

        invalidate();
    }

    private void setPaints() {
        catPaint = new Paint();
        catPaint.setStyle(Paint.Style.FILL);
        catPaint.setAntiAlias(true);

        ring = new Paint();
        ring.setStyle(Paint.Style.STROKE);
        ring.setAntiAlias(true);
        ring.setStrokeWidth(14);
        ring.setStrokeJoin(Paint.Join.ROUND);
        ring.setStrokeCap(Paint.Cap.ROUND);
        ring.setPathEffect(new CornerPathEffect(20));
        ring.setAlpha(200);

        ringbg = new Paint();
        ringbg.setStyle(Paint.Style.STROKE);
        ringbg.setAntiAlias(true);
        ringbg.setStrokeWidth(14);
        ringbg.setColor(Color.WHITE);
        ringbg.setAlpha(50);

    }

    int marginPerc = 20;
    int margin, iconmargin;
    float sweepAngle;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sweepAngle = ((float)360 / 100) * 80;
        margin = (int)(getWidth() / 100) * marginPerc;
        iconmargin = (int)(getWidth()/2.7f);

        if(add){

            rectF.top = 0 + addstroke;
            rectF.left = 0 + addstroke;
            rectF.bottom = getHeight() - addstroke;
            rectF.right = getWidth() - addstroke;

            canvas.drawRoundRect(rectF, 50,50, addPaint);
        }
        else{

        if(draw){
            rectF.top = 0;
            rectF.left = 0;
            rectF.bottom = getHeight();
            rectF.right = getWidth();


            ringRectF.top = margin;
            ringRectF.left = margin;
            ringRectF.right = getWidth() - margin;
            ringRectF.bottom = getHeight() - margin;

            catPaint.setAlpha(40);
            //canvas.drawRoundRect(rectF, 50,50, catPaint);
            canvas.drawArc(ringRectF, -90, 360, false, ringbg);
            ring.setAlpha(255);
            canvas.drawArc(ringRectF, -90, sweepAngle, false, ring);

            //Draw Icon
            if(icon != null){
                icon.setBounds(
                        iconmargin,
                        iconmargin,
                        getWidth() - iconmargin,
                        getHeight() - iconmargin
                );
                icon.setAlpha(180);

                icon.draw(canvas);

            }
        }
        }
    }

    boolean draw = false;
    public void setCategory(String catcolor, String caticon){
        catPaint.setColor(Color.parseColor(catcolor));
        ring.setColor(Color.parseColor(catcolor));

        if(caticon == null){
            icon = null;
        }
        else{
            icon = getResources().getDrawable(getResources().getIdentifier(caticon, "drawable", getContext().getPackageName()), null);
        }
        draw = true;
        invalidate();
    }
}
