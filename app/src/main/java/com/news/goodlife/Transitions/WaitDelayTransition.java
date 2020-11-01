package com.news.goodlife.Transitions;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.TransitionSet;

public class WaitDelayTransition extends TransitionSet {
    public WaitDelayTransition() {
        setOrdering(ORDERING_TOGETHER);
        setDuration(2000);
        //setStartDelay(1000);
        ////addTransition(new Fade());
    }
}