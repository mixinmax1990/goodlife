package com.news.goodlife.Fragments.SlideInFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.ProgressBarLabeld;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

public class FixedModuleFragment extends Fragment {
    View root, content_container;
    ProgressBarLabeld universalProgress;
    SingletonClass singletonMain = SingletonClass.getInstance();


    public FixedModuleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_module_fixed, container , false);

        universalProgress = root.findViewById(R.id.universal_fixed_progress);
        universalProgress.animateProgress();

        content_container = root.findViewById(R.id.fixed_module_content);
        subtleSlide();

        return root;
    }

    int startDist = 200;
    private void subtleSlide(){

        final int marginstart = singletonMain.dpToPx(10);
        content_container.setX(startDist + marginstart);

        ValueAnimator va = ValueAnimator.ofInt(startDist, 0);

        va.setDuration(450);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animval = (int)valueAnimator.getAnimatedValue();

                content_container.setX(animval + marginstart);
            }
        });

        va.start();
    }
}
