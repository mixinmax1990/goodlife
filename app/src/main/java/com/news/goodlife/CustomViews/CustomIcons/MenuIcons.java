package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.news.goodlife.R;

public class MenuIcons extends FrameLayout {

    boolean Night = true;
    Paint menuText;
    Drawable menuIcon;
    String menuName;

    MenuIconColors dayColors = new MenuIconColors("#00A6FF", "#000000");
    MenuIconColors nightColors = new MenuIconColors("#00A6FF", "#FFFFFF");
    MenuIconColors colors;

    float margin = .2f;
    float iconMargin;
    float textMargin;


    public MenuIcons(@NonNull Context context) {
        super(context);
    }

    public MenuIcons(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);



        //TODO Check for Theme than set
        colors = nightColors;

        setPaints();

        setWillNotDraw(false);


    }

    private void setPaints() {

        setMenu(getTag().toString());

        menuText = new Paint();
        menuText.setStyle(Paint.Style.FILL);
        menuText.setColor(Color.parseColor(colors.unselected));




        unSelectMenu();
    }

    int height, width;
    float anim = 1f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setAlpha(alpha);
        width = getWidth();
        textMargin = width * margin;
        iconMargin = (width * margin) * anim;
        Log.i("AnimVal", ""+ iconMargin);

        menuIcon.setBounds(
                (int)(0 + iconMargin),
                (int)(0 + iconMargin),
                (int)(width - iconMargin),
                (int)(width - iconMargin)
        );
        menuIcon.setFilterBitmap(true);
        menuIcon.draw(canvas);

        menuText.setTextSize((int)(width/5));
        menuText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(menuName, (int)(width/2), (int)(width), menuText);

    }

    private void setMenu(String name){

        switch(name){
            case "Wallet":
                menuName = "Wallet";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_add_box_24, null);
                break;
            case "Goals":
                menuName = "Goals";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_spa_24, null);
                break;
            case "Analysis":
                menuName = "Analysis";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_data_usage_24, null);
                break;
            case "Hub":
                menuName = "Hub";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_apps_24, null);
                break;
            default:
                break;

        }

    }
    float alpha = 0.5f;
    float revAnim;
    ValueAnimator vaEnter = ValueAnimator.ofFloat(1, 0);
    public void animateEnter(){


        unSelectMenu();
        vaEnter.setDuration(100);
        vaEnter.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                anim = ((float) valueAnimator.getAnimatedValue());
                revAnim = 1 - anim;
                if(revAnim > 0.5f){
                    alpha = revAnim;
                }
                invalidate();
            }
        });

        vaEnter.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if(autoleave){
                    animateLeaveAction();
                    autoleave = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        vaEnter.start();
    }
    boolean autoleave = false;

    public void animateLeave(){

        if(anim != 0f){
            //Wait till animation is done
            autoleave = true;
        }
        else{
            animateLeaveAction();
        }
    }
    public void animateLeaveAction(){

        ValueAnimator vaLeave = ValueAnimator.ofFloat(0, 1);
        vaLeave.setDuration(300);
        vaLeave.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                anim = ((float) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        vaLeave.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                selectMenu();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        vaLeave.start();
    }
    public void unSelectMenu(){

        DrawableCompat.setTint(menuIcon, Color.parseColor(colors.unselected));
        menuText.setColor(Color.parseColor(colors.unselected));
        alpha = .5f;
        invalidate();

    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    private void selectMenu(){

        DrawableCompat.setTint(menuIcon, Color.parseColor(colors.selected));
        menuText.setColor(Color.parseColor(colors.selected));

    }


    class MenuIconColors{
    String selected, unselected;

        public MenuIconColors(String selected, String unselected) {
            this.selected = selected;
            this.unselected = unselected;
            alpha = 0.5f;
            invalidate();
        }
    }

}
