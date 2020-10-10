package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.SpectrumBar;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import java.util.Random;

public class CashflowMainAdapter extends RecyclerView.Adapter<CashflowMainAdapter.ViewHolder>{

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Context context;
    public CashflowMainAdapter(Context context) {

        this.context = context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(weekend){
            return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_recycler_item_week_analysis, parent, false), viewType);
        }
        else{
            return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_recycler_item, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int day;
        if(position > 7){
            day = position - 8;
        }

        else{
            day = position;
        }

        if(position != 7){
            holder.itemday.setText( days[day]);
        }

        if(position == 4){
            holder.liquidView.setNegative(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 7){
            weekend = true;
        }
        else{
            weekend = false;
        }
        return position;
    }

    boolean weekend, monthend, yearend;

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SpectrumBar spectrumBar;
        LiquidView liquidView;
        TextView itemday;
        boolean isweekend = false;


        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(weekend){
                isweekend = true;

            }
            else {
                spectrumBar = itemView.findViewById(R.id.spectrumBar);
                liquidView = itemView.findViewById(R.id.budget_liquid);
                liquidView.setBaseline(randomValue(10, 600));
                itemday = itemView.findViewById(R.id.item_day);

            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("Clicked Cash Day", "True");

            if(weekend){

            }
            else{
                liquidView.animateWave();
            }
        }
    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }
}
