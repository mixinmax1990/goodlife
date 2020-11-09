package com.news.goodlife.CustomViews.CustomIcons;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.news.goodlife.R;

public class MenuIcons extends FrameLayout {

    boolean Night = true;
    Paint menuText, menuBackground, shadowLayerPaint, pageIndicatorPaint;
    Drawable menuIcon;
    String menuName;
    Boolean darkMode;

    @ColorInt int selectedIcon, unselectedIcon, menuBackgroundColor;


    MenuIconColors nightColors;
    MenuIconColors colors;

    float margin = .32f;
    float iconMargin;
    float textMargin;


    public MenuIcons(@NonNull Context context) {
        super(context);
    }

    public MenuIcons(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.menuIconSelected, typedValue, true);
        selectedIcon = typedValue.data;

        theme.resolveAttribute(R.attr.menuIconsUnselected, typedValue, true);
        unselectedIcon = typedValue.data;

        theme.resolveAttribute(R.attr.menuBackground, typedValue, true);
        menuBackgroundColor = typedValue.data;

        nightColors = new MenuIconColors(selectedIcon, unselectedIcon);

        //TODO Check for Theme than set
        colors = nightColors;
        darkMode = getResources().getBoolean(R.bool.dark);
        setPaints();

        setWillNotDraw(false);


    }

    private void setPaints() {

        setMenu(getTag().toString());

        menuText = new Paint();
        menuText.setStyle(Paint.Style.FILL);
        menuText.setColor(colors.unselected);
        menuText.setAntiAlias(true);


        menuBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        menuBackground.setStyle(Paint.Style.FILL);
        menuBackground.setColor(menuBackgroundColor);
        //menuBackground.setShadowLayer(2, 0, 0, Color.parseColor("#9F000000"));
        setLayerType(LAYER_TYPE_SOFTWARE, menuBackground);

        pageIndicatorPaint = new Paint();
        pageIndicatorPaint.setStyle(Paint.Style.FILL);
        pageIndicatorPaint.setAntiAlias(true);
        pageIndicatorPaint.setColor(Color.WHITE);


        unSelectMenu();
    }

    private int subMenus = 0;
    public void setSubMenu(int n){
        this.subMenus = n;
    }

    int height, width;
    float anim = 1f;
    int padding = 10;
    int move = 50;
    int start, end;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw Rounded Rect First
        width = getWidth();
        canvas.drawRoundRect(new RectF(0 + padding,0 + padding,getWidth() - padding, getHeight() - padding), 40,40, menuBackground);

        if(subMenus == 1){
            start = 34;
            end = width/2 + 10;
            if(darkMode){
                pageIndicatorPaint.setColor(Color.parseColor("#9FBABABA"));
            }
            else{
                pageIndicatorPaint.setColor(Color.parseColor("#585858"));
            }

            canvas.drawRoundRect(new RectF(start,padding + 20,width/2 - 10, padding + 34), 50,50, pageIndicatorPaint);
            canvas.drawRoundRect(new RectF(end,padding + 20,width -34, padding + 34), 50,50, pageIndicatorPaint);

            if(darkMode){
                pageIndicatorPaint.setColor(Color.parseColor("#000000"));
            }
            else{
                pageIndicatorPaint.setColor(Color.parseColor("#FFFFFF"));
            }

            canvas.drawRoundRect(new RectF(34 + move,padding + 20,(width/2 - 10) + move, padding + 34), 50,50, pageIndicatorPaint);

        }

        menuIcon.setAlpha(255);

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

        menuText.setTextSize(11 * getResources().getDisplayMetrics().scaledDensity);
        menuText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(menuName, (int)(width/2), (int)(width) - (textMargin / 2), menuText);

    }

    int distance;
    public void moveIndicator(float perc){
        Log.i("PErcentage", ""+perc);
        distance = end - start;
        float revPerc = 1 - perc;
        move = (int)(distance * revPerc);
        invalidate();
    }

    private void setMenu(String name){

        switch(name){
            case "Wallet":
                menuName = "Wallet";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_spa_24, null);
                setSubMenu(1);
                break;
            case "Goals":
                menuName = "Goals";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_spa_24, null);
                setSubMenu(0);
                break;
            case "Analysis":
                menuName = "Analysis";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_data_usage_24, null);
                setSubMenu(1);
                break;
            case "Hub":
                menuName = "Hub";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_apps_24, null);
                setSubMenu(0);
                break;
            case "Add":
                menuName = "";
                menuIcon = getResources().getDrawable(R.drawable.ic_baseline_add_24, null);
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

        DrawableCompat.setTint(menuIcon, colors.unselected);
        menuText.setColor(colors.unselected);
        alpha = .5f;
        invalidate();

    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    private void selectMenu(){

        if(darkMode){
            DrawableCompat.setTint(menuIcon, Color.BLACK);
            menuText.setColor(Color.BLACK);
            menuBackground.setColor(colors.selected);
        }
        else{
            DrawableCompat.setTint(menuIcon, Color.WHITE);
            menuText.setColor(Color.WHITE);
            menuBackground.setColor(colors.selected);
        }


    }


    class MenuIconColors{
        @ColorInt int selected, unselected;

        public MenuIconColors(@ColorInt int selected, @ColorInt int unselected) {
            this.selected = selected;
            this.unselected = unselected;
            alpha = 0.5f;
            invalidate();
        }
    }

}
