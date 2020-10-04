package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import java.util.Random;

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

        if(position == 4){
            holder.liquidView.setNegative(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SpectrumBar spectrumBar;
        LiquidView liquidView;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            spectrumBar = itemView.findViewById(R.id.spectrumBar);
            liquidView = itemView.findViewById(R.id.budget_liquid);
            liquidView.setBaseline(randomValue(10, 600));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("Clicked Cash Day", "True");
            liquidView.animateWave();
        }

        private int randomValue(int rangeStart, int rangeEnd){

            int random = new Random().nextInt(rangeEnd) + rangeStart;
            return random;
        }
    }
}
