package com.news.goodlife.Fragments.SlideInFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.news.goodlife.Data.Local.Models.Financial.FixedCostModel;
import com.news.goodlife.Fragments.PopFragments.ManageFixedFragment;
import com.news.goodlife.Interfaces.SuccessCallback;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import java.util.List;

public class FixedCostsFragment extends Fragment {

    View root;
    View popup, editBtn;
    SingletonClass singletonClass = SingletonClass.getInstance();
    ViewGroup fixedFlex;
    TextView total;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.slidein_fixed_costs_layout, container, false);

        popup = root.findViewById(R.id.popup);
        editBtn = root.findViewById(R.id.edit_fixed_btn);
        fixedFlex = root.findViewById(R.id.fixed_flex);
        total = root.findViewById(R.id.total_costs);

        getAllFixedCosts();

        listeners();

        return root;
    }

    List<FixedCostModel> allFixedCosts;
    int TotalFixedCosts;
    private void getAllFixedCosts() {

        fixedFlex.removeAllViews();
        TotalFixedCosts = 0;

        AsyncLayoutInflater inflater = new AsyncLayoutInflater(getContext());

        allFixedCosts = singletonClass.getDatabaseController().FixedCostsController.getAllFixedCosts();

        for(FixedCostModel fixedCost: allFixedCosts){

            TotalFixedCosts = TotalFixedCosts + Integer.parseInt(fixedCost.getAmount());
            inflater.inflate(R.layout.slidein_fixed_costs_listitem, fixedFlex, new AsyncLayoutInflater.OnInflateFinishedListener() {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                    TextView amountTV = view.findViewById(R.id.transaction_amount);
                    TextView desc = view.findViewById(R.id.desc_cont);

                    amountTV.setText(singletonClass.monefy(fixedCost.getAmount()));
                    desc.setText(fixedCost.getName());

                    fixedFlex.addView(view);
                }
            });


        }


        total.setText(singletonClass.monefy(""+TotalFixedCosts));


    }

    private void listeners() {

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEdit();
            }
        });
    }

    private void openEdit() {

        ManageFixedFragment editFixedFragment = new ManageFixedFragment(false, popup, new SuccessCallback() {
            @Override
            public void success() {
                getAllFixedCosts();
            }

            @Override
            public void error() {

            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();



        singletonClass.toggleFadeView(true, popup, new SuccessCallback() {
            @Override
            public void success() {
                ft.replace(popup.getId(), editFixedFragment);
                ft.commit();
            }

            @Override
            public void error() {

            }
        });
    }
}
