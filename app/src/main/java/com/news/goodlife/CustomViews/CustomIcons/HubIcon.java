package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HubIcon extends androidx.appcompat.widget.AppCompatTextView {

    Paint test, innercircle;

    public HubIcon(@NonNull Context context) {
        super(context);
    }

    public HubIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        test = new Paint();
        test.setAntiAlias(true);
        test.setColor(Color.RED);
        test.setStyle(Paint.Style.STROKE);

        test.setStrokeJoin(Paint.Join.ROUND);
        test.setStrokeCap(Paint.Cap.ROUND);
        test.setPathEffect(new CornerPathEffect(20));

        innercircle = new Paint();
        innercircle.setStyle(Paint.Style.FILL);
        innercircle.setAntiAlias(true);
        innercircle.setColor(Color.parseColor("#26272B"));
        innercircle.setAlpha(255);



        setWillNotDraw(false);
    }

    int move = 0;
    int sweepAngle = 57;
    int alpha = 255;
    int stroke = 6;
    int duration = 2000;
    @Override
    protected void onDraw(Canvas canvas) {


        int size = (getHeight() - 10) - stroke;

        RectF rectF = new RectF(getWidth()/2 - size / 2, getHeight()/2 - size / 2, getWidth()/2 + size / 2, getHeight()/2 + size / 2);

        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth() / 2 - 40, innercircle);

        test.setStrokeWidth(stroke);
        test.setColor(Color.parseColor("#ffffff"));
        test.setAlpha(alpha);
        canvas.drawArc(rectF, 270 + move, sweepAngle, false, test);
        test.setColor(Color.parseColor("#f3d224"));
        test.setAlpha(alpha);
        canvas.drawArc(rectF, 342 + move, sweepAngle, false, test);
        test.setColor(Color.parseColor("#f01939"));
        test.setAlpha(alpha);
        canvas.drawArc(rectF, 54 + move, sweepAngle, false, test);
        test.setColor(Color.parseColor("#00aa3b"));
        test.setAlpha(alpha);
        canvas.drawArc(rectF, 126 + move, sweepAngle, false, test);
        test.setColor(Color.parseColor("#0043ac"));
        test.setAlpha(alpha);
        canvas.drawArc(rectF, 198 + move, sweepAngle, false, test);

        test.setColor(Color.parseColor("#606060"));
        test.setStrokeWidth(1);
        canvas.drawLine(getWidth(), getHeight() * .3f, getWidth(), getHeight(), test);

        super.onDraw(canvas);

    }

    public void animateSpin(){
        ValueAnimator va = ValueAnimator.ofInt(0, 1440);
        va.setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                move = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                alpha = 255;
                ValueAnimator va = ValueAnimator.ofInt(57, 72);
                va.setDuration(duration / 2);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        sweepAngle = (int) valueAnimator.getAnimatedValue();
                    }
                });
                va.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                        //Animate stroke Width

                        ValueAnimator va = ValueAnimator.ofInt(6, 2);
                        va.setDuration(duration/2);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                stroke = (int)valueAnimator.getAnimatedValue();
                            }
                        });
                        va.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {}
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                ValueAnimator va = ValueAnimator.ofInt(2, 6);
                                va.setDuration(duration / 2);
                                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        stroke = (int)valueAnimator.getAnimatedValue();
                                    }
                                });
                                va.start();
                            }
                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }
                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        va.start();

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ValueAnimator va = ValueAnimator.ofInt(72, 47);
                        va.setDuration(duration / 2);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                sweepAngle = 5 + (int) valueAnimator.getAnimatedValue();
                                //move = (int) valueAnimator.getAnimatedValue() / 2;
                                //alpha = 255 - ((int) valueAnimator.getAnimatedValue() * 3);
                            }
                        });
                        va.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                va.start();
            }

            @Override
            public void onAnimationEnd(Animator animator) {



            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        va.start();
    }
}
