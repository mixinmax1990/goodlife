package com.news.goodlife.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.MainActivity;
import com.news.goodlife.R;

public class WalletTodayFragment extends Fragment {

    //Todo garbage collect this on destroy
    public MainActivity activity;
    ScrollView todayScroll;
    BorderRoundView slideIndicator;


    public WalletTodayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_main, container, false);
        todayScroll = root.findViewById(R.id.wallet_today_scroll);
        slideIndicator = root.findViewById(R.id.slide_indicator);


        listeners();
        activity = (MainActivity) getActivity();


        return root;
    }

    float elTD, menDist;
    boolean slidingFragment;
    long slidevelocoty = 0;
    private void listeners() {
        todayScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch(motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:

                            elTD = motionEvent.getX();
                            slidevelocoty = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:

                            if(slidingFragment){
                                slidevelocoty = System.currentTimeMillis() - slidevelocoty;
                                activity.autoFinishSlide((int)menDist, true, slidevelocoty);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //Log.i("Today", "Today");
                            menDist = (elTD - motionEvent.getX());
                            if(menDist >= 0){
                                slidingFragment = true;

                                activity.slideMechanism((int)menDist, true);

                            }

                            break;
                    }
                return false;
            }
        });

    }

    float visibleScreen;
    float scaleIndicator;
    public void moveIndicator(int move, int displayWidth){
        slideIndicator.setX(move);

        visibleScreen = (float)move / displayWidth;
        slideIndicator.setAlpha(visibleScreen);

        scaleIndicator = 1 + Math.abs(visibleScreen - 1)*5;
        slideIndicator.setScaleX(scaleIndicator);
        slideIndicator.setScaleY(scaleIndicator);

    }

    public void resetIndicator(int pos){
        slideIndicator.setX(pos);
        slideIndicator.setAlpha(1);
        slideIndicator.setScaleY(1);
        slideIndicator.setScaleX(1);
    }



}
