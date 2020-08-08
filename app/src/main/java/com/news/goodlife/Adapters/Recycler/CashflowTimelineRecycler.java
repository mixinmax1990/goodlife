package com.news.goodlife.Adapters.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.Data.Local.Models.CashflowModel;
import com.news.goodlife.Interfaces.RecyclerViewClickListener;
import com.news.goodlife.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CashflowTimelineRecycler extends RecyclerView.Adapter<CashflowTimelineRecycler.ViewHolder> {

    Context context;
    RecyclerViewClickListener mListener;
    List<CashflowModel> cashflows;
    CashflowModel cashflow;
    SimpleDateFormat sdf = new SimpleDateFormat("d MMM YY");
    String reformattedDate;

    public CashflowTimelineRecycler(Context context, List<CashflowModel> data, RecyclerViewClickListener listener) {
    this.context = context;
    this.mListener = listener;
    this.cashflows = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cashflow_list_item, parent, false), mListener, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        cashflow = cashflows.get(position);

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
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
