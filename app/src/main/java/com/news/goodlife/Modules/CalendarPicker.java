package com.news.goodlife.Modules;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.R;

public class CalendarPicker extends Fragment {
    //Base Objects


    String title;
    TextView titleTXT;
    public CalendarPicker(String title) {
        this.title = title;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.calendar_picker_layout, container, false);
        titleTXT = root.findViewById(R.id.calendar_module_title);
        titleTXT.setText(title);

        listeners();

        return root;
    }

    public void listeners(){

    }

}
