package com.news.goodlife.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.news.goodlife.R;

public class BubbleChartCategories extends FrameLayout {
    Paint bubbleFill;

    public BubbleChartCategories(@NonNull Context context) {
        super(context);
    }

    public BubbleChartCategories(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();
        setWillNotDraw(false);
    }

    private void setPaints() {
        bubbleFill = new Paint();
        bubbleFill.setStyle(Paint.Style.FILL);
        bubbleFill.setAntiAlias(true);
        bubbleFill.setAlpha(150);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getCatColorDrawable("House");
        bubbleFill.setColor(Color.parseColor(bubbleColor));
        canvas.drawCircle(70,70,50, bubbleFill);
        bubbleIcon.setBounds(40,40,100,100);
        bubbleIcon.draw(canvas);

        getCatColorDrawable("Entertainment");
        bubbleFill.setColor(Color.parseColor(bubbleColor));
        canvas.drawCircle(210,120,80, bubbleFill);
        bubbleIcon.setBounds(160,70,260,170);
        bubbleIcon.draw(canvas);

        getCatColorDrawable("Health");
        bubbleFill.setColor(Color.parseColor(bubbleColor));
        canvas.drawCircle(70,170,30, bubbleFill);
        bubbleIcon.setBounds(50,150,90,190);
        bubbleIcon.draw(canvas);

    }

    String bubbleColor;
    Drawable bubbleIcon;

    private void getCatColorDrawable(String catName){
        //Set Drawable and COlor of bubble;
        switch(catName){
            case "Food":
                bubbleColor = "#0899a1";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_grocery, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#55EFEC"));
                break;
            case "Entertainment":
                bubbleColor = "#fa425a";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_grocery, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#FFFFFF"));
                break;
            case "Car":
                bubbleColor = "#f6e02e";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_car, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#000000"));
                break;
            case "Transport":
                bubbleColor = "#d01c17";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_transportation, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#EA6553"));
                break;
            case "House":
                bubbleColor = "#fa8825";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_home, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#FE5F10"));
                break;
            case "Sports":
                bubbleColor = "#b4f237";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, Color.parseColor("#000000"));
                break;
            case "Restaurant":
                bubbleColor = "#1aadf8";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                break;
            case "Health":
                bubbleColor = "#9453bb";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                break;
            case "Clothes":
                bubbleColor = "#FFFFFF";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                break;
            case "Communication":
                break;
            default:
                break;
        }
    }

    private class bubble{
        int perc;
        String catName;
    }
}
