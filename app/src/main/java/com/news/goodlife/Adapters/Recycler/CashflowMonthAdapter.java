package com.news.goodlife.Adapters.Recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;
import com.news.goodlife.Models.CalendarLayout;
import com.news.goodlife.R;

import java.util.List;
import java.util.Random;

public class CashflowMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<CalendarLayout> allCalendarLayouts;

    CalendarSelectDayListener callback;
    //List<toCalendarViewTransition> allTransitionNames;

    public CashflowMonthAdapter(Context context, List<CalendarLayout> allCalendarLayouts) {
        this.context = context;
        this.allCalendarLayouts = allCalendarLayouts;
        callback = (CalendarSelectDayListener) context;
        //this.allTransitionNames = allTransitionNames;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case 1:
                //inflate Day
                View v1 = inflater.inflate(R.layout.month_wallet_day_item, parent, false);
                viewHolder = new ViewHolderData(v1);
                break;
            case 2:
                //Inflate Monthend
                View v2 = inflater.inflate(R.layout.month_wallet_monthend_item, parent, false);
                viewHolder = new ViewHolderMonthEnd(v2);
                break;
            case 3:
                //Inflate Weekend
                View v3 = inflater.inflate(R.layout.month_wallet_weekened_item, parent, false);
                viewHolder = new ViewHolderWeekEnd(v3);
                break;
            default:
                //Inflate Yearend
                View v4 = inflater.inflate(R.layout.month_wallet_yearend_item, parent, false);
                viewHolder = new ViewHolderYearEnd(v4);
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

        String type = allCalendarLayouts.get(position).getType();

        switch(type){
            case "day":
                return 1;
            case "monthend":
                return 2;
            case "weekend":
                return 3;
            default:
                return 4;

        }
    }



    boolean weekend = false;
    boolean monthend = false;


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        //callback = null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        CalendarLayout dayCal = allCalendarLayouts.get(position);



        if(holder instanceof ViewHolderData){
            //Cast the Holder ((ViewHolderData)holder).bla bla bla

            if(weekend & !monthend){
                //break the line of view somehoe


                ViewGroup.LayoutParams lp = ((ViewHolderData)holder).itemView.getLayoutParams();
                if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                    FlexboxLayoutManager.LayoutParams flexboxLp =
                            (FlexboxLayoutManager.LayoutParams) ((ViewHolderData)holder).itemView.getLayoutParams();
                    flexboxLp.setWrapBefore(true);
                    ((ViewHolderData)holder).itemView.setLayoutParams(flexboxLp);

                }


                weekend = false;
            }
            else{
                ViewGroup.LayoutParams lp = ((ViewHolderData)holder).itemView.getLayoutParams();
                if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                    FlexboxLayoutManager.LayoutParams flexboxLp =
                            (FlexboxLayoutManager.LayoutParams) ((ViewHolderData)holder).itemView.getLayoutParams();
                    flexboxLp.setWrapBefore(false);
                    ((ViewHolderData)holder).itemView.setLayoutParams(flexboxLp);

                }
                monthend = false;

            }

            ((ViewHolderData)holder).dayLiquid.setBaseline(randomValue(0, 200));
            ((ViewHolderData)holder).dayMonthTextView.setText(dayCal.getMONTH_DAY_NUMBER());
            ((ViewHolderData)holder).dayNameTextView.setText(dayCal.getDAY_OF_WEEK_NAME());
            ((ViewHolderData)holder).dayLiquid.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {

                   ((ViewHolderData)holder).dayLiquid.animateWave();

                }
            });

            if(position == 13 || position == 4){
                ((ViewHolderData)holder).dayLiquid.setNegative(true);
            }
        }

        if(holder instanceof ViewHolderMonthEnd){
            //Log.i("ViewHolder Type", "MonthStart");
            ((ViewHolderMonthEnd)holder).monthEndTextView.setText(dayCal.getMONTH_NAME());

            monthend = true;

        }
        if(holder instanceof ViewHolderWeekEnd){
            //Log.i("ViewHolder Type", "MonthStart");
            weekend = true;

        }

        if(holder instanceof ViewHolderYearEnd){
            //Log.i("ViewHolder Type", "MonthStart");
            ((ViewHolderYearEnd)holder).yearTXT.setText(dayCal.getYEAR());
        }


    }

    @Override
    public int getItemCount() {
        return allCalendarLayouts.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener{

        LiquidView dayLiquid;
        TextView dayNameTextView;
        BorderRoundView liquidContainer;
        TextView dayMonthTextView;

        public ViewHolderData(@NonNull final View itemView) {
            super(itemView);

            dayLiquid = itemView.findViewById(R.id.day_liquid);
            dayNameTextView = itemView.findViewById(R.id.day_name);
            dayMonthTextView = itemView.findViewById(R.id.monthview_day_number);
            liquidContainer = itemView.findViewById(R.id.day_item_liquidcard);
            dayLiquid.setTextSize(12);
            dayLiquid.noText(true);

            liquidContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setElevation(200);
                    animateDayClick(itemView);

                }
            });
        }

        @Override
        public void onClick(View v) {


        }

        private boolean animCenter = false;
        private void animateDayClick(final View view){
            ValueAnimator va = ValueAnimator.ofFloat(1, 2);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    float animVal  = ((float) valueAnimator.getAnimatedValue());
                    if(animVal > 1.5f){
                        animVal = 3 - animVal;

                        if(animCenter){
                            //Open Window One
                        }
                    }


                    itemView.setScaleX(animVal);
                    itemView.setScaleY(animVal);

                }
            });

            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                    //Load Multidays Fragment inisde Window One

                    callback.calendarDaySelected(true);

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    view.setElevation(1);
                    //Open Window One
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

    public class ViewHolderMonthEnd extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView monthEndTextView;


        public ViewHolderMonthEnd(@NonNull View itemView) {
            super(itemView);
            monthEndTextView = itemView.findViewById(R.id.newmonth_text);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public class ViewHolderYearEnd extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView yearTXT;


        public ViewHolderYearEnd(@NonNull View itemView) {
            super(itemView);
            yearTXT = itemView.findViewById(R.id.year_text);

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
