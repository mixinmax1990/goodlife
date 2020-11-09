package com.news.goodlife.CustomViews;

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
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.news.goodlife.Models.BubbleSurround;
import com.news.goodlife.Models.CostCatData;
import com.news.goodlife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleChartCategories extends FrameLayout {
    Paint bubbleFill, bubbleFillStroke, squarePaint;
    List<CostCatData> percData;
    Boolean darkMode;
    static int count = 0;
    @ColorInt int iconColor;

    float sizeSquared;
    float marginPerc = .35f;

    public BubbleChartCategories(@NonNull Context context) {
        super(context);
    }

    public BubbleChartCategories(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setPaints();
        setWillNotDraw(false);
        count++;
        Log.i("Count", ""+count);
        darkMode = getResources().getBoolean(R.bool.dark);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frameWidth = getWidth();
                frameHeight = getHeight();
                sizeSquared = frameWidth * frameHeight;
                sizeRatio = frameWidth / frameHeight;

                percData = calculatePercentage(testData(), 500);

                bubbleDistribution();
                drawn = true;
                invalidate();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

        });

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        iconColor = typedValue.data;


    }

    private void bubbleDistribution() {

        //Get the biggest Position

        //Log.i("BiggestBubble", ""+biggestBubble.getPercentage());
        getSquareSizeOfBiggestBubble();

        positionSquares();

    }

    List<BubbleSurround> allBubblesSurround = new ArrayList<>();
    private void positionSquares() {

        int bubbleHolderLength;
        //Get All The Data with Percentages
        for(CostCatData costCatData: percData){

            bubbleHolderLength = squareLengthFromPerc(costCatData.getPercentage());
            findFreeSpace(bubbleHolderLength, costCatData);
        }
    }

    boolean horizontal;
    private void findFreeSpace(int length, CostCatData data) {
        if(sizeRatio >= 1){
            horizontal = false;
        }
        else{
            horizontal = true;
        }

        BubbleSurround bubbleSurround;
        bubbleSurround = new BubbleSurround();
        //Set The Length
        bubbleSurround.setLength(length);
        if(allBubblesSurround.isEmpty()){
            bubbleSurround.setPosition(0,length,0,length);
            bubbleSurround.setSurroundingSpace(0,frameHeight - length, 0, frameWidth - length);
            bubbleSurround.setData(data);

            //Set The center point of Square to Draw Bubble later
            bubbleSurround.setCenterX(length/2);
            bubbleSurround.setCenterY(length/2);
        }
        else{

            iteratePlacedbubbles(bubbleSurround, length, data);

        }

        allBubblesSurround.add(bubbleSurround);

    }


    private boolean iteratePlacedbubbles(BubbleSurround bubbleSurround, int length, CostCatData data){
        boolean ignore, isBigger;
        int overflow;

        for(BubbleSurround placedBubbleSurround: allBubblesSurround){
                overflow = length - placedBubbleSurround.getLength();
                if(placedBubbleSurround.getLength() < length)
                { isBigger = true;}
                else{ isBigger = false; }

                //Check at the End for Space
                if(placedBubbleSurround.getSpaceEnd() > length){
                    ignore = false;

                    //Check OverflowBottom
                    if(placedBubbleSurround.getBiggerOverflowEnd() > 0){
                        //There is no Space so ignore if bubble is bigger
                        if(isBigger){
                            ignore = true;
                        }
                    }

                    if(!ignore){

                        //We Found Space at the End of Itarated Bubble to place the new Bubble into
                        bubbleSurround.setPosition(
                                placedBubbleSurround.getTop(),
                                placedBubbleSurround.getTop() + length,
                                placedBubbleSurround.getEnd(),
                                placedBubbleSurround.getEnd() + length
                        );

                        //Set the Sorrounding Space of the New bubble
                        bubbleSurround.setSurroundingSpace(
                                0,
                                frameHeight - bubbleSurround.getBottom(),
                                0,
                                frameWidth - bubbleSurround.getEnd()
                        );
                        //Set Data
                        bubbleSurround.setData(data);

                        //Set The center point of Square to Draw Bubble later
                        bubbleSurround.setCenterX(placedBubbleSurround.getEnd()+(length/2));
                        bubbleSurround.setCenterY(placedBubbleSurround.getTop()+(length/2));

                        //Get Information from Placed Button if a Bigger Bubble is Underneath it
                        placedBubbleSurround.setBiggerOverflowEnd(overflow);

                        bubbleSurround.setBiggerOverflowBottom(placedBubbleSurround.getBiggerOverflowBottom());

                        //Remove the Free Space from the iterated bubble
                        placedBubbleSurround.setSpaceEnd(0);

                        return true;
                    }

                }

                //Check at the Bottom for Space
                if(placedBubbleSurround.getSpaceBottom() > length){
                    ignore = false;
                    // found space At the Bottom of Iterated Bubble to place the new Bubble into

                    //Check OverflowBottom
                    if(placedBubbleSurround.getBiggerOverflowBottom() > 0){
                        //There is no Space so ignore if bubble is bigger
                        if(isBigger){
                            ignore = true;
                        }

                    }

                    if(!ignore){
                        bubbleSurround.setPosition(
                                placedBubbleSurround.getBottom(),
                                placedBubbleSurround.getBottom() + length,
                                placedBubbleSurround.getEnd() - length,
                                placedBubbleSurround.getEnd()
                        );
                        // Set Surrounding space of new Button
                        bubbleSurround.setSurroundingSpace(
                                0,
                                frameHeight - bubbleSurround.getBottom(),
                                0,
                                frameWidth - bubbleSurround.getEnd()
                        );
                        //Set Data
                        bubbleSurround.setData(data);

                        //Set The center point of Square to Draw Bubble later
                        bubbleSurround.setCenterX(placedBubbleSurround.getEnd() - (length/2));
                        bubbleSurround.setCenterY(placedBubbleSurround.getBottom() + (length/2));

                        //Get Information from Placed Button if a Bigger Bubble is Underneath it
                        placedBubbleSurround.setBiggerOverflowBottom(overflow);
                        bubbleSurround.setBiggerOverflowEnd(placedBubbleSurround.getBiggerOverflowEnd());


                        placedBubbleSurround.setSpaceBottom(0);

                        return true;
                    }

                }
        }

        return false;
    }

    private void getSquareSizeOfBiggestBubble(){
        float biggestBubbleSquareSize = sizeSquared * biggestBubble.getPercentage();
        double biggestBubbleLength = Math.sqrt(biggestBubbleSquareSize);
        // Log.i("Percentage", ""+biggestBubble.getPercentage());
        checkSizeBounds(biggestBubbleLength);



    }

    private int squareLengthFromPerc(float perc){
        int squareLength = (int)Math.round(Math.sqrt((sizeSquared * perc)* fitRatio));

        return squareLength;
    }

    float fitRatio = 1f;
    private int checkSizeBounds(double length){
        int finalBubbleLength;
        if(length > frameHeight){
            fitRatio = (float)(frameHeight / length);
            return Math.round(frameHeight);
        }
        if(length > frameWidth){
            fitRatio = (float)(frameWidth / length);
            return Math.round(frameWidth);
        }
        finalBubbleLength = (int)Math.round(length);
        return finalBubbleLength;
    }

    private void setPaints() {
        bubbleFill = new Paint();
        bubbleFill.setStyle(Paint.Style.FILL);
        bubbleFill.setAntiAlias(true);


        bubbleFillStroke = new Paint();
        bubbleFillStroke.setStyle(Paint.Style.STROKE);

        bubbleFillStroke.setAntiAlias(true);
        bubbleFillStroke.setAlpha(150);

        squarePaint = new Paint();
        squarePaint.setColor(Color.WHITE);
        squarePaint.setAntiAlias(true);
        squarePaint.setStyle(Paint.Style.STROKE);
        squarePaint.setStrokeWidth(2);
    }

    boolean drawn = false;
    boolean canvasdrawn = false;
    boolean develop = false;
    int margin;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
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
        bubbleIcon.draw(canvas);*/

        float padding = 0.8f;

        count = allBubblesSurround.size();
        Log.i("Size of Data", ""+count);

        if(drawn){
            try{
                for(BubbleSurround bubbleSurround: allBubblesSurround){

                    if(develop) {
                        RectF rect = new RectF(
                                bubbleSurround.getStart(),
                                bubbleSurround.getTop(),
                                bubbleSurround.getEnd(),
                                bubbleSurround.getBottom());

                        canvas.drawRect(rect, squarePaint);
                    }
                    else{



                        getCatColorDrawable(bubbleSurround.getData().getCatName());
                        bubbleFill.setColor(Color.parseColor(bubbleColor));
                        if(darkMode){
                            bubbleFillStroke.setColor(Color.parseColor(bubbleColor));
                        }
                        else{
                            bubbleFillStroke.setColor(Color.TRANSPARENT);
                        }

                        bubbleFillStroke.setStrokeWidth(4 * anim);
                        if(darkMode){
                            bubbleFill.setAlpha(255);
                        }
                        else{
                            bubbleFill.setAlpha(255);
                        }

                        canvas.drawCircle(bubbleSurround.getCenterX(),bubbleSurround.getCenterY(),((bubbleSurround.getLength() * padding)/2) * anim, bubbleFill);
                        canvas.drawCircle(bubbleSurround.getCenterX(),bubbleSurround.getCenterY(),((bubbleSurround.getLength() * padding)/2) * anim, bubbleFillStroke);
                        bubbleIcon.setBounds(
                                (int)(bubbleSurround.getCenterX() - ((bubbleSurround.getLength()/2) * marginPerc * anim)),
                                (int)(bubbleSurround.getCenterY() - ((bubbleSurround.getLength()/2) * marginPerc * anim)),
                                (int)(bubbleSurround.getCenterX() + ((bubbleSurround.getLength()/2) * marginPerc * anim)),
                                (int)(bubbleSurround.getCenterY() + ((bubbleSurround.getLength()/2) * marginPerc * anim))
                        );
                        bubbleIcon.setAlpha(200);
                        bubbleIcon.draw(canvas);
                    }
                }
            }
            catch(Exception e){
invalidate();
            }

        }


    }


    String bubbleColor;
    Drawable bubbleIcon;

    float anim = 1;
    boolean animated = false;
    public void animateBubbles(){
        if(!animated){
            ValueAnimator va = ValueAnimator.ofFloat(0, 1);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    anim = ((float) valueAnimator.getAnimatedValue());
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

                }
            });

            va.start();
        }

    }

    private void getCatColorDrawable(String catName){
        //Set Drawable and COlor of bubble;

        switch(catName){
            case "Food":
                bubbleColor = "#0899a1";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_grocery, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Entertainment":
                bubbleColor = "#fa425a";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_grocery, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Car":
                bubbleColor = "#f6e02e";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_car, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Transport":
                bubbleColor = "#d01c17";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_transportation, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "House":
                bubbleColor = "#fa8825";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_home, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Sports":
                bubbleColor = "#b4f237";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                break;
            case "Restaurant":
                bubbleColor = "#1aadf8";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Health":
                bubbleColor = "#9453bb";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Clothes":
                if(darkMode){
                    bubbleColor = "#FFFFFF";
                }
                else{
                    bubbleColor = "#000000";
                }
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            case "Communication":
                bubbleColor = "#f6e02e";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
            default:
                bubbleColor = "#000000";
                bubbleIcon = getResources().getDrawable(R.drawable.cat_ic_sport, null);
                DrawableCompat.setTint(bubbleIcon, iconColor);
                break;
        }
    }

    int frameWidth, frameHeight, sizeRatio;
    private List<CostCatData> calculatePercentage(List<CostCatData> allCostCatRaw, int sum){
        List<CostCatData> percentCostCat = new ArrayList<>();
        float curPerc;
        int pos = 0;
        for(CostCatData costcat: allCostCatRaw){
            curPerc = (float)costcat.getValAmount() / sum;
            costcat.setPercentage(curPerc);
            percentCostCat.add(costcat);
            checkIfBiggest(curPerc, pos);
            pos++;
        }
        return percentCostCat;
    }

    BiggestBubble biggestBubble = new BiggestBubble();
    private void checkIfBiggest(float perc, int pos) {

        if(perc > biggestBubble.getPercentage()){
            biggestBubble.setPercentage(perc);
            biggestBubble.setPosition(pos);
        }

    }

    String[] categoriesArray = {"Food", "Entertainment", "Car", "Transport", "House", "Sports", "Restaurant", "Health", "Clothes", "Communication"};

    private List<CostCatData> testData(){
        List<CostCatData> testdata = new ArrayList<>();

        CostCatData ccd;

        ccd = new CostCatData();
        ccd.setCatName(categoriesArray[randomNumb(1, 9)]);
        ccd.setValAmount(100);
        testdata.add(ccd);

        ccd = new CostCatData();
        ccd.setCatName(categoriesArray[randomNumb(1, 9)]);
        ccd.setValAmount(300);
        testdata.add(ccd);

        ccd = new CostCatData();
        ccd.setCatName(categoriesArray[randomNumb(1, 9)]);
        ccd.setValAmount(50);
        testdata.add(ccd);

        ccd = new CostCatData();
        ccd.setCatName(categoriesArray[randomNumb(1, 9)]);
        ccd.setValAmount(50);
        testdata.add(ccd);

        return testdata;
    }

    private int randomNumb(int min, int max){

        return new Random().nextInt((max - min) + 1) + min;
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    private class bubble{
        int perc;
        String catName;
    }

    class BiggestBubble{
        int position;
        float percentage;

        public BiggestBubble() {

            setPercentage(0);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }
    }
}
