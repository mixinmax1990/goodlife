package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.MarkedConstraintLayout;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.ArrayList;
import java.util.List;

public class SubscribeFragment extends Fragment {
    View root;
    ViewGroup subscribeDetailContainer;
    SingletonClass singletonClass = SingletonClass.getInstance();
    View quoteCard;
    CardView membership_success_card;

    SuccessCallback callback;

    int margin;


    public SubscribeFragment(SuccessCallback successCallback) {

        this.callback = successCallback;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.slidein_subscribe_base, container, false);

        loadQuotes();

        margin = singletonClass.dpToPx(10);

        subscribeDetailContainer = root.findViewById(R.id.subscribe_detail_container);

        subscribeDetailContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                asyncSubscribeDetail();

                subscribeDetailContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        return root;
    }

    MarkedConstraintLayout markTrial, markMonth, markYear, markLifetime;
    private void asyncSubscribeDetail() {

        AsyncLayoutInflater asyncInflater = new AsyncLayoutInflater(getContext());

        asyncInflater.inflate(R.layout.slidein_subscribe_content, subscribeDetailContainer, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

                markTrial = view.findViewById(R.id.mark_trial);
                markMonth = view.findViewById(R.id.mark_month);
                markYear = view.findViewById(R.id.mark_year);
                markLifetime = view.findViewById(R.id.mark_lifetime);


                quoteCard = view.findViewById(R.id.quote_card);
                quoteBody = view.findViewById(R.id.quote_body);
                quoteAuthor = view.findViewById(R.id.quote_author);
                sliderCont = view.findViewById(R.id.quote_slider_cont);
                slider = view.findViewById(R.id.quote_slider);


                membership_success_card = view.findViewById(R.id.membership_success_card);
                continueBankButton = view.findViewById(R.id.continue_to_bank_btn);
                success_message_cont = view.findViewById(R.id.success_message_cont);

                listeners();
                parent.addView(view);

            }
        });

    }

    int drawn = 0;
    BorderRoundView childLifetime, childTrial, childMonth, childYear;
    private void listeners() {

        childTrial = (BorderRoundView) markTrial.getChildAt(0);
        childMonth = (BorderRoundView)markMonth.getChildAt(0);
        childYear = (BorderRoundView)markYear.getChildAt(0);
        childLifetime =(BorderRoundView) markLifetime.getChildAt(0);

        childTrial.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawn++;
                allDrawn();
                childTrial.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        childMonth.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawn++;
                allDrawn();
                childMonth.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        childYear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawn++;
                allDrawn();
               childYear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        childLifetime.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawn++;
                allDrawn();
                childLifetime.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        childMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMembership("month");
            }
        });

        childTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMembership("trial");
            }
        });

        childYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMembership("year");
            }
        });

        childLifetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMembership("lifetime");
            }
        });

        quoteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ReloadQuote
                /*
                if(vaQuote.isRunning()){
                    vaQuote.cancel();
                }

                startQuoteSlider();
                */
            }
        });

        continueBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToBankingConnection();
            }
        });
    }

    private void allDrawn() {

        if(drawn > 3){
            selectMembership("trial");
            //Tell Start Activity that loading is Successfull start sliding in the Fragment
            callback.success();
            //Sliding in the Fragment wait 350ms
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    animateKuddle();
                }
            }, 400);
        }
    }

    ConstraintLayout.LayoutParams trialLP, monthLP, yearLP, lifetimeLP;


    private void animateKuddle() {

        trialLP = (ConstraintLayout.LayoutParams) childTrial.getLayoutParams();
        monthLP = (ConstraintLayout.LayoutParams) childMonth.getLayoutParams();
        yearLP = (ConstraintLayout.LayoutParams) childYear.getLayoutParams();
        lifetimeLP = (ConstraintLayout.LayoutParams) childLifetime.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofInt(singletonClass.dpToPx(10), 0);

        va.setDuration(700);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animValue = (int)animation.getAnimatedValue();

                trialLP.bottomMargin = animValue;
                childTrial.setLayoutParams(trialLP);

                monthLP.bottomMargin = animValue;
                childMonth.setLayoutParams(monthLP);

                yearLP.bottomMargin = animValue;
                childYear.setLayoutParams(yearLP);

                lifetimeLP.bottomMargin = animValue;
                childLifetime.setLayoutParams(lifetimeLP);

            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                //STart Quote Slider
                startQuoteSlider();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }

    TextView quoteBody, quoteAuthor;
    View sliderCont, slider;
    int sliderWidth = 0;
    float scaleFactor = 0.2f;
    ConstraintLayout.LayoutParams sliderLP;
    float showQuoteAlpha = 0f;
    ValueAnimator vaQuote;
    private void startQuoteSlider() {

        selectQuote();

        sliderLP = (ConstraintLayout.LayoutParams) slider.getLayoutParams();
        sliderCont.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                sliderWidth = sliderCont.getWidth();
                int bottom = quoteAuthor.getTop() - quoteBody.getHeight();



                vaQuote = ValueAnimator.ofFloat(0,1);
                vaQuote.setDuration(6000);
                vaQuote.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        float animVal = (float) animation.getAnimatedValue();
                        int width = (int)(sliderWidth * animVal);
                        if(width < 20){
                            width = 20;
                        }
                        sliderLP.width = width;


                        slider.setLayoutParams(sliderLP);

                        // ScaleText

                        int scale = (int)(animVal * bottom);
                        quoteBody.setY(scale);

                        float authorAlpha = animVal * 5;

                        if(authorAlpha > 1f){
                            authorAlpha = 1f;

                            showQuoteAlpha = showQuoteAlpha + 0.01f;

                        }

                        if(animVal > 0.9){
                            float fadeout = Math.abs((animVal - 1f) * 10);
                            authorAlpha = fadeout;
                            showQuoteAlpha = fadeout;
                        }
                        quoteAuthor.setAlpha(authorAlpha);
                        quoteBody.setAlpha(showQuoteAlpha);

                    }
                });

                vaQuote.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //Restart Listener
                        startQuoteSlider();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                vaQuote.setInterpolator(new LinearInterpolator());
                vaQuote.start();

                sliderCont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    int selectedQuote = 0;
    private void selectQuote() {

        quoteAuthor.setText(allQuotes.get(selectedQuote).Author);
        quoteBody.setText(allQuotes.get(selectedQuote).Quote);

        int noOfQuotes = allQuotes.size();

        selectedQuote++;

        if(selectedQuote == noOfQuotes){
            selectedQuote = 0;
        }

    }

    List<Quote> allQuotes = new ArrayList<>();
    private void loadQuotes() {

        String[] authorArray = getResources().getStringArray(R.array.quote_author);
        String[] quoteArray = getResources().getStringArray(R.array.quote);


        for(int i = 0; i < authorArray.length; i++){
            Quote quote = new Quote(authorArray[i], quoteArray[i]);
            allQuotes.add(quote);
        }
    }

    private void selectMembership(String type){

        unselectAll();

        switch(type){
            case "trial":
                highlightSelection(childTrial);


                //If Successfull Show card and Proceed


                break;
            case "month":
                highlightSelection(childMonth);
                showSuccessCard();
                break;
            case "year":
                highlightSelection(childYear);
                break;
            case "lifetime":
                highlightSelection(childLifetime);
                break;
            default:
                break;
        }

    }

    private void showSuccessCard(){
        membership_success_card.setVisibility(View.VISIBLE);
        membership_success_card.animate().scaleX(1).scaleY(1).alpha(1).setDuration(350);


    }

    View continueBankButton;
    View success_message_cont;
    private void transitionToBankingConnection() {


        int mcardHeight = membership_success_card.getHeight();
        int mcardWidth = membership_success_card.getWidth();

        int restHeight = (singletonClass.getDisplayHeight() - mcardHeight);
        int restWidth = (singletonClass.getDisplayWidth() - mcardWidth);

        ConstraintLayout.LayoutParams cardLP = (ConstraintLayout.LayoutParams) membership_success_card.getLayoutParams();

        ValueAnimator va = ValueAnimator.ofFloat(0,1);
        va.setDuration(350);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animVal = (float)animation.getAnimatedValue();
                float revVal = 1 - animVal;

                cardLP.height = (int)(mcardHeight + (restHeight * animVal));
                cardLP.width = (int)(mcardWidth + (restWidth * animVal));

                membership_success_card.setLayoutParams(cardLP);

                continueBankButton.setAlpha(revVal);
                success_message_cont.setAlpha(revVal);

            }
        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                //Load Klarna App
                singletonClass.changeFragment.setValue("KlarnaApp");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        va.start();
    }

    private void highlightSelection(BorderRoundView member) {
        member.dynamicallySetBackgroundColor("#FFFFFF");

        ((TextView)member.findViewWithTag("title")).setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        for(int i = 0; i < member.getChildCount(); i++){

            if(member.getChildAt(i) instanceof TextView){
                ((TextView) member.getChildAt(i)).setTextColor(Color.BLACK);
            }
        }
    }

    private void removeHighlightSelection(BorderRoundView member) {

        ((TextView)member.findViewWithTag("title")).setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        for(int i = 0; i < member.getChildCount(); i++){

            if(member.getChildAt(i) instanceof TextView){
                ((TextView) member.getChildAt(i)).setTextColor(Color.WHITE);
            }
        }
    }
    private void unselectAll() {

        removeHighlightSelection(childTrial);
        removeHighlightSelection(childMonth);
        removeHighlightSelection(childYear);
        removeHighlightSelection(childLifetime);

        childLifetime.dynamicallySetBackgroundColor("#212226");
        childYear.dynamicallySetBackgroundColor("#212226");
        childTrial.dynamicallySetBackgroundColor("#212226");
        childMonth.dynamicallySetBackgroundColor("#212226");
    }

    class Quote{
        String Author;
        String Quote;

        public Quote(String author, String quote) {
            Author = author;
            Quote = quote;
        }

        public String getAuthor() {
            return Author;
        }

        public void setAuthor(String author) {
            Author = author;
        }

        public String getQuote() {
            return Quote;
        }

        public void setQuote(String quote) {
            Quote = quote;
        }
    }
}
