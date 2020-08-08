package com.news.goodlife.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Adapters.Recycler.CashflowTimelineRecycler;
import com.news.goodlife.Data.Local.Models.CashflowModel;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import java.util.List;

public class CashflowOnceFragment extends Fragment {

    RecyclerView recyclerView;
    List<CashflowModel> cashflows;

    OnClickedCashflowItemListener callback;

    public CashflowOnceFragment(List<CashflowModel> outgoingCashflows) {
        this.cashflows = outgoingCashflows;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cashflow_once, container, false);
        recyclerView = root.findViewById(R.id.cashflow_outgoing_recyclerview);
        loadRecycler();
        return root;
    }

    public void setCallback(OnClickedCashflowItemListener callback) {
        this.callback = callback;
    }
    private void loadRecycler(){

        final RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, String type) {
                callback.onCashflowItemClicked(position);

            }
        };

        CashflowTimelineRecycler cashflow_adapter = new CashflowTimelineRecycler(getContext(), cashflows, listener);
        recyclerView.setAdapter(cashflow_adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(false);
        recyclerView.setLayoutManager(llm);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnClickedCashflowItemListener){
            callback = (OnClickedCashflowItemListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + "must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

}
