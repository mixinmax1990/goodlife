package com.news.goodlife.Fragments.PopFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.R;

public class IncomingCashPopFragment extends Fragment {

    String frameTransitionName, incomeTitleTransitinonName, incomeValueTransitionName;

    View frame, incometitle, incomevalue;
    public IncomingCashPopFragment(String frameTransitionName, String incomeTitleTransitinonName, String incomeValueTransitionName) {
        this.frameTransitionName = frameTransitionName;
        this.incomeTitleTransitinonName = incomeTitleTransitinonName;
        this.incomeValueTransitionName = incomeValueTransitionName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pop_income_fragment, container, false);

        frame = root.findViewById(R.id.frame);
        frame.setTransitionName(frameTransitionName);
        incometitle = root.findViewById(R.id.pop_incometitle);
        incometitle.setTransitionName(incomeTitleTransitinonName);
        incomevalue = root.findViewById(R.id.pop_incomevalue);
        incomevalue.setTransitionName(incomeValueTransitionName);

        return root;
    }
}
