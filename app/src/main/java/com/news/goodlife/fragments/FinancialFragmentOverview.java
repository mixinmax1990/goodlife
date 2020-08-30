package com.news.goodlife.fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.news.goodlife.CustomViews.BezierView;
import com.news.goodlife.R;
import com.news.goodlife.Transitions.DetailsTransition;

public class FinancialFragmentOverview extends Fragment {

    LinearLayout personalProfileView;
    BezierView bezierView;
    FinancialFragment financialFragment;
    ViewGroup financeContainer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.financial_fragment_main, container, false);
        personalProfileView = root.findViewById(R.id.personal_profile);
        bezierView = root.findViewById(R.id.bezier_overview);
        financialFragment = new FinancialFragment();
        financeContainer = root.findViewById(R.id.finance_container);


// Note that we need the API version check here because the actual transition classes (e.g. Fade)
// are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
// ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            financialFragment.setSharedElementEnterTransition(new DetailsTransition());
            financialFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            financialFragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        personalProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                financeContainer.removeAllViews();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(bezierView, "personal")
                        .replace(R.id.finance_container, financialFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
