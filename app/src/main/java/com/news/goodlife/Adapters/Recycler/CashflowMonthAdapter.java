package com.news.goodlife.Adapters.Recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.R;

import java.util.Random;

public class CashflowMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    //List<toCalendarViewTransition> allTransitionNames;

    public CashflowMonthAdapter(Context context) {
        this.context = context;
        //this.allTransitionNames = allTransitionNames;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case 1:
                //Inflate Space
                View v1 = inflater.inflate(R.layout.month_cashflow_space_item, parent, false);
                viewHolder = new ViewHolderTopSpace(v1);
                break;
            case 2:
                //Inflate MonthName
                View v2 = inflater.inflate(R.layout.month_wallet_monthstart_item, parent, false);
                viewHolder = new ViewHolderMonthStart(v2);
                break;
            case 3:
                //Inflate Weekend
                View v3 = inflater.inflate(R.layout.month_cashflow_weekened_item, parent, false);
                viewHolder = new ViewHolderWeekEnd(v3);
                break;
            case 4:
                //Inflate Monthend
                View v4 = inflater.inflate(R.layout.month_cashflow_monthend_item, parent, false);
                viewHolder = new ViewHolderMonthEnd(v4);
                break;
            default:
                //inflate Day
                View v5 = inflater.inflate(R.layout.month_wallet_day_item, parent, false);
                viewHolder = new ViewHolderData(v5);
                break;
        }

        return viewHolder;

    }
    int daycount = 1;
    int weekcount = 0;
    int weekdaycount = 0;
    boolean newMonthStart = false;
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 1;
        }
        else if(position == 1){
            return 2;
        }
        else{
            //check if weekend
            if(weekdaycount == 7){
                weekdaycount = 0;
                weekcount++;
                return 3;
            }
            //check if monthend
            if(weekcount == 4){
              weekcount = 0;
              newMonthStart = true;
              return 4;
            }

            if(newMonthStart){
                newMonthStart = false;
                return 2;
            }

            weekdaycount++;
            return 5;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderData){
            //Cast the Holder ((ViewHolderData)holder).bla bla bla
            //Log.i("ViewHolder Type", "DayStart");
            ((ViewHolderData)holder).dayLiquid.setBaseline(randomValue(0, 200));
            ((ViewHolderData)holder).monthDayNumberTV.setText(""+position);
            ((ViewHolderData)holder).dayLiquid.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {
                   ((ViewHolderData)holder).dayLiquid.animateWave();
                }
            });

            if(position == 4){
                //((ViewHolderData)holder).dayLiquid.setTransitionName(allTransitionNames.get(0).getLiquidView());
                //((ViewHolderData)holder).dayNameTextView.setTransitionName(allTransitionNames.get(0).getDateDayName());
            }
            if(position == 5){
                //((ViewHolderData)holder).dayLiquid.setTransitionName(allTransitionNames.get(1).getLiquidView());
                //((ViewHolderData)holder).dayNameTextView.setTransitionName(allTransitionNames.get(1).getDateDayName());
            }

            /*if(allTransitionNames.size() > 2){
                if(position == 6){
                    ((ViewHolderData)holder).dayLiquid.setTransitionName(allTransitionNames.get(2).getLiquidView());
                    ((ViewHolderData)holder).dayNameTextView.setTransitionName(allTransitionNames.get(2).getDateDayName());
                }

            }*/

            if(position == 13 || position == 4){
                ((ViewHolderData)holder).dayLiquid.setNegative(true);
            }

        }

        if(holder instanceof  ViewHolderMonthStart){
            //Log.i("ViewHolder Type", "MonthStart");
        }
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener{

        LiquidView dayLiquid;
        TextView dayNameTextView;
        BorderRoundView liquidContainer;
        TextView monthDayNumberTV;

        public ViewHolderData(@NonNull final View itemView) {
            super(itemView);

            dayLiquid = itemView.findViewById(R.id.day_liquid);
            dayNameTextView = itemView.findViewById(R.id.day_name);
            monthDayNumberTV = itemView.findViewById(R.id.monthview_day_number);
            liquidContainer = itemView.findViewById(R.id.day_item_liquidcard);
            dayLiquid.setTextSize(12);
            dayLiquid.noText(true);

            liquidContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setElevation(200);
                    animateDayClick(itemView);

                    Log.i("Testing","Click Runs");
                }
            });
        }

        @Override
        public void onClick(View v) {


        }

        private void animateDayClick(final View view){
            ValueAnimator va = ValueAnimator.ofFloat(1, 2);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    float animVal  = ((float) valueAnimator.getAnimatedValue());
                    if(animVal > 1.5f){
                        animVal = 3 - animVal;
                    }
                    Log.i("ValAn", ""+animVal);

                    itemView.setScaleX(animVal);
                    itemView.setScaleY(animVal);

                }
            });

            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setElevation(1f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            va.setInterpolator(null);


            va.start();

        }
    }
    public class ViewHolderTopSpace extends RecyclerView.ViewHolder implements View.OnClickListener{



        public ViewHolderTopSpace(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public class ViewHolderMonthStart extends RecyclerView.ViewHolder implements View.OnClickListener{



        public ViewHolderMonthStart(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public class ViewHolderMonthEnd extends RecyclerView.ViewHolder implements View.OnClickListener{



        public ViewHolderMonthEnd(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public class ViewHolderWeekEnd extends RecyclerView.ViewHolder implements View.OnClickListener{



        public ViewHolderWeekEnd(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
