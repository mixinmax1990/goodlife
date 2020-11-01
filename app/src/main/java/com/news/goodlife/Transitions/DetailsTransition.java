package com.news.goodlife.Transitions;

import android.view.animation.DecelerateInterpolator;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

public class DetailsTransition extends TransitionSet {
    public DetailsTransition(int duration) {
        setOrdering(ORDERING_TOGETHER);
        setDuration(duration);
        setInterpolator(new DecelerateInterpolator());
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }
}

