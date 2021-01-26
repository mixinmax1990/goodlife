package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.BudgetCircle;
import com.news.goodlife.R;

import java.util.List;

public class BudgetPeriodAdapter extends RecyclerView.Adapter<BudgetPeriodAdapter.ViewHolderBudgetPeriod> {

    int periods;
    int padding;
    Context context;
    public BudgetPeriodAdapter(Context context, int periods, int padding){
        this.context = context;
        this.periods = periods;
        this.padding = padding;
    }

    @NonNull
    @Override
    public ViewHolderBudgetPeriod onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        return new ViewHolderBudgetPeriod(LayoutInflater.from(context).inflate(R.layout.budget_period_recycler_item, parent, false), position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBudgetPeriod holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount(){

        return periods;
    }

    public class ViewHolderBudgetPeriod extends RecyclerView.ViewHolder{


        public ViewHolderBudgetPeriod(@NonNull View itemView, int pos) {
            super(itemView);

            if(pos == 0){
                itemView.setPadding(padding,0,0,0);
            }
            if(pos == periods - 1){
                itemView.setPadding(0,0,padding,0);
            }

            setPeriod(itemView, pos);
        }

        private void setPeriod(View itemView, int pos){
            BudgetCircle budgetCircle = itemView.findViewById(R.id.budget_circle);
            TextView periodNo = itemView.findViewById(R.id.period_number);
            TextView periodName = itemView.findViewById(R.id.period_name);

            switch(pos){
                case 0:
                    budgetCircle.setMonths(1);
                    periodNo.setText("1");
                    periodName.setText("Month");
                    break;
                case 1:
                    budgetCircle.setMonths(2);
                    periodNo.setText("2");
                    periodName.setText("Month's");
                    break;
                case 2:
                    budgetCircle.setMonths(3);
                    periodNo.setText("3");
                    periodName.setText("Month's");
                    break;
                case 3:
                    budgetCircle.setMonths(6);
                    periodNo.setText("6");
                    periodName.setText("Month's");
                    break;
                case 4:
                    budgetCircle.setMonths(12);
                    periodNo.setText("1");
                    periodName.setText("Year");
                    break;
                default:
                    break;

            }

        }


    }
}
