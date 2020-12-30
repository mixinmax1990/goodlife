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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;
import com.news.goodlife.Interfaces.WalletDaysCallback;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Models.CalendarLayoutMonth;
import com.news.goodlife.R;

import java.util.List;
import java.util.Random;

public class CashflowMonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<CalendarLayoutMonth> allMonths;
    int displayWidth;

    CalendarSelectDayListener callback;
    //List<toCalendarViewTransition> allTransitionNames;

    public CashflowMonthAdapter(Context context, List<CalendarLayoutMonth> allMonths) {
        this.context = context;
        this.allMonths = allMonths;
        callback = (CalendarSelectDayListener) context;
        //this.allTransitionNames = allTransitionNames;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        displayWidth = parent.getMeasuredWidth();
        Log.i("DisplayWidth", ""+displayWidth);

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.month_main_item, parent, false);
        viewHolder = new ViewHolderMonth(v1, pos);

        /*
        switch(viewType){
            case 1:
                //inflate Day
                View v1 = inflater.inflate(R.layout.month_wallet_day_item, parent, false);
                viewHolder = new ViewHolderData(v1, position);
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
        }*/

        return viewHolder;

    }
    int daycount = 1;
    int weekcount = 0;
    int weekdaycount = 0;
    boolean newMonthStart = false;
    private int position;
    @Override
    public int getItemViewType(int position) {

        /*
        this.position = position;
        String type = allMonths.get(position).getType();

        switch(type){
            case "day":
                return 1;
            case "monthend":
                return 2;
            case "weekend":
                return 3;
            default:
                return 4;
        }*/
        return position;
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


    }

    @Override
    public int getItemCount() {
        //Log.i("AllMonths", ""+allMonths.size());
        return allMonths.size();
    }



    public class ViewHolderMonth extends RecyclerView.ViewHolder{

        private CalendarLayoutMonth month;
        private FlexboxLayout root;
        public ViewHolderMonth(@NonNull View itemView, int pos) {
            super(itemView);
            month = allMonths.get(pos);
            root = (FlexboxLayout) itemView;

            inflateDays();
        }

        boolean monday;
        private void inflateDays(){

            //Log.i("Month Count be4 Inflate", ""+ month.getDays().size());

            for(CalendarLayoutDay day: month.getDays()){
                String type = day.getType();
                //iterating thrue the Days
                Log.i("Type", ""+type);
                switch(type){
                    case "day":
                        inflateDay(day);
                        break;
                    case "monthend":
                        inflateMonthendAnalysis(day);
                        break;

                    case "weekend":
                        inflateWeekendAnalysis(day);
                        monday = true;
                        break;
                    default:
                        inflateYearendAnalysis(day);
                        break;
                }
            }
        }

        private void inflateDay(final CalendarLayoutDay dayData){
            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            final View day;
            //View day = inflater.inflate(R.layout.month_wallet_day_item, root, false);
            if(dayData.isToday()){
                 day = inflater.inflate(R.layout.month_wallet_today_item, null);
            }
            else{
                 day = inflater.inflate(R.layout.month_wallet_day_item, null);
            }

            root.addView(day);


            TextView dayNameTV = day.findViewById(R.id.day_name);
            TextView dayNumberTV = day.findViewById(R.id.monthview_day_number);
            BorderRoundView liquidContainer = day.findViewById(R.id.day_item_liquidcard);

            liquidContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    day.setElevation(200);
                    animateDayClick(day, dayData);

                }
            });

            dayNumberTV.setText(dayData.getMONTH_DAY_NUMBER());
            dayNameTV.setText(dayData.getDAY_OF_WEEK_NAME());

            if(monday){
                //its Monday so Start a new Line
                FlexboxLayout.LayoutParams flexLP =
                        (FlexboxLayout.LayoutParams) day.getLayoutParams();
                flexLP.setWrapBefore(true);
                day.setLayoutParams(flexLP);
                monday = false;
            }

        }

        private void inflateWeekendAnalysis(CalendarLayoutDay dayData){
            LayoutInflater inflater = LayoutInflater.from(root.getContext());

            //View day = inflater.inflate(R.layout.month_wallet_day_item, root, false);
            final View weekend = inflater.inflate(R.layout.month_wallet_weekened_item, null);
            root.addView(weekend);

        }

        private void inflateMonthendAnalysis(CalendarLayoutDay dayData){
            LayoutInflater inflaterMonth = LayoutInflater.from(root.getContext());

            //View day = inflater.inflate(R.layout.month_wallet_day_item, root, false);
            final View monthend = inflaterMonth.inflate(R.layout.month_wallet_monthend_item, null);
            root.addView(monthend);

            TextView monthEndTextView = monthend.findViewById(R.id.newmonth_text);
            monthEndTextView.setText(dayData.getMONTH_NAME());

            FlexboxLayout.LayoutParams flexLP =
            (FlexboxLayout.LayoutParams) monthend.getLayoutParams();
            flexLP.setWrapBefore(true);
            flexLP.setWidth(displayWidth);
            monthend.setLayoutParams(flexLP);

        }

        private void inflateYearendAnalysis(CalendarLayoutDay dayData){
            LayoutInflater inflater = LayoutInflater.from(root.getContext());

            //View day = inflater.inflate(R.layout.month_wallet_day_item, root, false);
            final View yearend = inflater.inflate(R.layout.month_wallet_yearend_item, null);
            root.addView(yearend);

            TextView yearTV = yearend.findViewById(R.id.year_text);
            yearTV.setText(dayData.getYEAR());

        }

        private boolean animCenter = false;
        private void animateDayClick(final View day, final CalendarLayoutDay data){
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


                    day.setScaleX(animVal);
                    day.setScaleY(animVal);

                }
            });

            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                    //Load Multidays Fragment inisde Window One

                    callback.calendarDaySelected(data);

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    day.setElevation(1);
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


    /*

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener{

        LiquidView dayLiquid;
        TextView dayNameTextView;
        BorderRoundView liquidContainer;
        TextView dayMonthTextView;
        int pos;

        public ViewHolderData(@NonNull final View itemView, int position) {
            super(itemView);

            this.pos = position;

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

                    callback.calendarDaySelected(allCalendarLayoutDays.get(pos));

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

    */

    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
