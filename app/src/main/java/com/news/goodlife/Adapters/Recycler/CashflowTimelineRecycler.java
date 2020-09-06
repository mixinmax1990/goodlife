package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Data.Local.Models.Financial.FinancialEventModel;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CashflowTimelineRecycler extends RecyclerView.Adapter<CashflowTimelineRecycler.ViewHolder> {

    Context context;
    RecyclerViewClickListener mListener;
    List<FinancialEventModel> cashflows;
    FinancialEventModel cashflow;
    SimpleDateFormat sdf = new SimpleDateFormat("d MMM YY");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String reformattedDate;
    String last_itemDate = "";
    String current_itemDate = "";
    String next_itemDate = "";
    int tillNextDate = 0;

    public CashflowTimelineRecycler(Context context, List<FinancialEventModel> data, RecyclerViewClickListener listener) {
    this.context = context;
    this.mListener = listener;
    this.cashflows = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i("less than 4","Desc:"+cashflow.getDescription()+" -- Days till Next"+tillNextDate);

        //Check if last item hast the same Date
        if(last_itemDate.equals(current_itemDate)){
            //Check if next item has the same Date
            if(next_itemDate.equals(current_itemDate)){
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_center, parent, false), mListener, viewType);
            }
            else{
                //Last item of the Day
                if(tillNextDate > 1){
                    if(tillNextDate < 4){

                        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_bottom_long, parent, false), mListener, viewType);
                    }
                    else{
                        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_bottom_verylong, parent, false), mListener, viewType);
                    }

                }
                else{
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_bottom, parent, false), mListener, viewType);
                }

            }

        }
        else{
            if(next_itemDate.equals(current_itemDate)){
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_top, parent, false), mListener, viewType);
            }
            else{
                //SIngle entry on the Day
                if(tillNextDate > 1){
                    if(tillNextDate < 4){

                        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_single_long, parent, false), mListener, viewType);
                    }
                    else{
                        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_single_verylong, parent, false), mListener, viewType);
                    }

                }
                else{
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item, parent, false), mListener, viewType);
                }

            }
        }
        //return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item_top, parent, false), mListener, viewType);

    }

    Date CurrentDate, NextDate;

    @Override
    public int getItemViewType(int position) {

        // Gt Dates of next and Previous Cashflows

        cashflow = cashflows.get(position);

        Date d;

        //Current Item Date
        try {
            d = dateFormat.parse(cashflow.getDate());
            CurrentDate = d;
            current_itemDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Last Item Date
        if(position != 0){
            try {
                d = dateFormat.parse(cashflows.get(position - 1).getDate());
                last_itemDate = sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            last_itemDate = "";
        }

        //Next Item Date
        if(position != cashflows.size() - 1){
            try {
                d = dateFormat.parse(cashflows.get(position + 1).getDate());
                NextDate = d;
                next_itemDate = sdf.format(d);

                long diff = NextDate.getTime() - CurrentDate.getTime();
                tillNextDate = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            next_itemDate = "";
        }

        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.parent.setTag(""+cashflow.id);
        String ident = "-";
        if(cashflow.getPositive().equals("true")){
            ident = "+";
        }

        String value = ident+" "+cashflow.value+" €";
        holder.cashflowValueTW.setText(value);


        Date d;
        try {
            d = dateFormat.parse(cashflow.getDate());
            reformattedDate = sdf.format(d);
            holder.cashflowDateTW.setText(reformattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.cashflowDateTW.setText("-");
        }
        holder.cashflowDescTW.setText(cashflow.description);

        if(position == cashflows.size() - 1){
            //Give it a margin
            holder.parent.setPadding(0,0,0,200);
        }
    }
//"d MMM YYYY"
    @Override
    public int getItemCount() {

        return cashflows.size();

    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView cashflowValueTW;
        TextView cashflowDateTW;
        TextView cashflowDescTW;
        View parent;

        private RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener, int i) {
            super(itemView);


            parent = itemView;
            parent.setId(i);
            cashflowValueTW = parent.findViewById(R.id.cashflow_item_value);
            cashflowDateTW = parent.findViewById(R.id.cashflow_item_date);
            cashflowDescTW = parent.findViewById(R.id.cashflow_item_desc);

            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, Integer.parseInt(view.getTag().toString()), "cashflow");

        }
    }
}
