package com.news.goodlife.CustomViews;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.news.goodlife.R;


public class GoalDrawView  extends ConstraintLayout {

    View parent;
    Paint backgroundPaint, progressPaint, eventCirclePaint, pastEventCirclePaint, eventTextPaint;

    int eventRadius;


    float goalProgress, animatedGoal;
    String showDetails;
    String startMoney, endMoney;
    int progressViewWidth;
    Context context;
    AttributeSet attrs;
    boolean animated = false;





    public GoalDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setAttributes(attrs);
        initiatePaints();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ValueAnimator va = ValueAnimator.ofInt(0, (int)goalProgress);
                va.setDuration(500);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        animatedGoal = (int) valueAnimator.getAnimatedValue();
                        invalidate();
                    }
                });
                va.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animated = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                if(!animated){
                    va.start();
                }

            }
        });
    }
    public GoalDrawView(Context context) {
        super(context);
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(startMoney.equals("minus")){
            progressPaint.setColor(Color.parseColor("#26282C"));
        }
        else{
            progressPaint.setColor(Color.parseColor("#57D679"));
        }
        eventRadius = 15;
        progressViewWidth = (int)(((float)this.getWidth()/100) * animatedGoal);

        canvas.drawRect(0, 0,progressViewWidth , this.getHeight(), progressPaint);

        eventTextPaint.setTextSize(35);
        eventTextPaint.setAlpha(255);
        eventTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(endMoney+"€",this.getWidth()/2, this.getHeight()/2 + 15, eventTextPaint);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = (View) getParent();
    }


    private void setAttributes(AttributeSet attrs) {
        this.attrs = attrs;
        /* set custom attributes with TypedArray */
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.GoalDrawView);
        CharSequence goalProgress_cs = arr.getString(R.styleable.GoalDrawView_goalProgress);
        if (goalProgress_cs != null) {
            // Do something with foo_cs.toString()
            setGoalProgress(Float.parseFloat(goalProgress_cs.toString()));

        }
        arr.recycle();  // Do this when done.

        /* set custom attributes with TypedArray */
        TypedArray arrStartMoney = context.obtainStyledAttributes(attrs, R.styleable.GoalDrawView);
        CharSequence startMoney_cs = arrStartMoney.getString(R.styleable.GoalDrawView_startMoney);
        if (startMoney_cs != null) {
            // Do something with foo_cs.toString()
            setStartMoney(startMoney_cs.toString());

        }
        arr.recycle();  // Do this when done.

        /* set custom attributes with TypedArray */
        TypedArray arrEndMoney = context.obtainStyledAttributes(attrs, R.styleable.GoalDrawView);
        CharSequence endMoney_cs = arrEndMoney.getString(R.styleable.GoalDrawView_endMoney);
        if (endMoney_cs != null) {
            // Do something with foo_cs.toString()
            setEndMoney(endMoney_cs.toString());

        }
        arr.recycle();  // Do this when done.

    }
    private void initiatePaints(){
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);


        //progressPaint.setAlpha(150);
        eventCirclePaint = new Paint();
        eventCirclePaint.setStyle(Paint.Style.FILL);
        eventCirclePaint.setStrokeWidth(6);
        eventCirclePaint.setAntiAlias(true);
        eventCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        eventCirclePaint.setAlpha(50);

        pastEventCirclePaint = new Paint();
        pastEventCirclePaint.setStyle(Paint.Style.FILL);
        pastEventCirclePaint.setStrokeWidth(4);
        pastEventCirclePaint.setAntiAlias(true);
        pastEventCirclePaint.setColor(Color.WHITE);
        pastEventCirclePaint.setAlpha(200);

        eventTextPaint = new Paint();
        eventTextPaint.setColor(Color.WHITE);
        eventTextPaint.setAntiAlias(true);
        eventTextPaint.setTextSize(10);
    }



    public float getGoalProgress() {
        return goalProgress;
    }

    public void setGoalProgress(float goalProgress) {
        this.goalProgress = goalProgress;
    }

    public String getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(String startMoney) {
        this.startMoney = startMoney;
    }

    public String getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(String endMoney) {
        this.endMoney = endMoney;
    }

    public String getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(String showDetails) {
        this.showDetails = showDetails;
    }

}