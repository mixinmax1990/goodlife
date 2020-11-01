package com.news.goodlife.Fragments.PopFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BulletPointTextView;
import com.news.goodlife.PopWindowData.CashCategoryData;
import com.news.goodlife.R;

import java.util.List;

public class CostCategoriesChart extends Fragment {

    View frame, graph;
    FlexboxLayout cashcat_detail_flex;
    String frameTransName, graphTransName, flexTransName, daynameTransitionName;
    List<CashCategoryData> allCategories;
    ConstraintLayout graph_detail_container;

    public CostCategoriesChart(String graphTransName, String frameTransName, String flexTransName, String daynameTransitionName,  List<CashCategoryData> allCategories){

        this.graphTransName = graphTransName;
        this.frameTransName = frameTransName;
        this.flexTransName = flexTransName;
        this.allCategories = allCategories;
        this.daynameTransitionName = daynameTransitionName;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pop_costcat_fragment, container, false);

        graph = root.findViewById(R.id.graph);
        graph.setTransitionName(graphTransName);
        frame = root.findViewById(R.id.frame);
        frame.setTransitionName(frameTransName);
        cashcat_detail_flex = root.findViewById(R.id.cashcat_detail_flex);
        cashcat_detail_flex.setTransitionName(flexTransName);
        graph_detail_container = root.findViewById(R.id.graph_detail_container);

        for(CashCategoryData cashCategoryData: allCategories){

            BulletPointTextView tempText = new BulletPointTextView(getContext(),null, cashCategoryData.getBulletPointColor(), 6);
            tempText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            tempText.setText(cashCategoryData.getCategoryName());
            tempText.setId(View.generateViewId());
            tempText.setTransitionName(cashCategoryData.getTransitionName());
            tempText.setPadding(40,0,0,0);
            tempText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            //tempText.setScaleX(1.3f);
            //tempText.setScaleY(1.3f);
            cashcat_detail_flex.addView(tempText);
        }
        return root;
    }


}
