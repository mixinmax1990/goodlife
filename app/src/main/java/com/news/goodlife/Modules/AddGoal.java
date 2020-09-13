package com.news.goodlife.Modules;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.news.goodlife.R;

public class AddGoal extends Fragment {

    //Base Objects
    FragmentTransaction ft;

    //Module Specific Objects
    TextView startCalendar, goalCalendar;
    FrameLayout extraWindow;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_goal_layout, container, false);

        startCalendar = root.findViewById(R.id.add_goal_startdate_text);
        goalCalendar = root.findViewById(R.id.add_goal_dealinedate_text);
        extraWindow = root.findViewById(R.id.extraWindow);

        listeners();

        return root;
    }

    public AddGoal() {

    }

    public void listeners(){

        startCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarPicker cp = new CalendarPicker("Start Date");
                ft = getChildFragmentManager().beginTransaction();
                ft.replace(extraWindow.getId(),cp);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                extraWindow.setVisibility(View.VISIBLE);
                ft.commit();
            }
        });

        goalCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarPicker cp = new CalendarPicker("Deadline");
                ft = getChildFragmentManager().beginTransaction();
                ft.replace(extraWindow.getId(),cp);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                extraWindow.setVisibility(View.VISIBLE);
                ft.commit();

            }
        });


    }


}
