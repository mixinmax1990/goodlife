package com.news.goodlife.Fragments.PopFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.R;

public class OutgoingCashPopFragment extends Fragment {

    String frameTransitionName, costTitleTransitionName, costValueTransitionName;

    View frame, costtitle, costsum;
    public OutgoingCashPopFragment(String frameTransitionName, String costTitleTransitionName, String costValueTransitionName) {
        this.frameTransitionName = frameTransitionName;
        this.costTitleTransitionName = costTitleTransitionName;
        this.costValueTransitionName = costValueTransitionName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pop_cost_fragment, container, false);

        frame = root.findViewById(R.id.frame);
        frame.setTransitionName(frameTransitionName);
        costtitle = root.findViewById(R.id.pop_costtitle);
        costsum = root.findViewById(R.id.pop_costvalue);
        costsum.setTransitionName(costValueTransitionName);
        costtitle.setTransitionName(costTitleTransitionName);
        return root;
    }
}
