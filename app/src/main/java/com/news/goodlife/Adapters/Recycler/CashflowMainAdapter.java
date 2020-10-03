package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

public class CashflowMainAdapter extends RecyclerView.Adapter<CashflowMainAdapter.ViewHolder> {

    Context context;
    public CashflowMainAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_recycler_item, parent, false), viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.i("RecyclerRUns", "True");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SpectrumBar spectrumBar;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            spectrumBar = itemView.findViewById(R.id.spectrumBar);
        }

    }
}
