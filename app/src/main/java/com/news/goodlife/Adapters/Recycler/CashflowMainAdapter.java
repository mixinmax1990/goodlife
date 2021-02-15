package com.news.goodlife.Adapters.Recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.news.goodlife.CustomViews.BulletPointTextView;
import com.news.goodlife.CustomViews.CustomBezierGraph;
import com.news.goodlife.CustomViews.CustomEntries.LabeledEntryView;
import com.news.goodlife.CustomViews.CustomEntries.PopUpFrame;
import com.news.goodlife.CustomViews.IconDoughnutView;
import com.news.goodlife.CustomViews.LiquidView;
import com.news.goodlife.CustomViews.RelationshipMapView;
import com.news.goodlife.Data.Local.Models.Financial.BudgetCategoryModel;
import com.news.goodlife.Data.Local.Models.Financial.BudgetModel;
import com.news.goodlife.Data.Local.Models.Financial.TransactionModel;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Data.Local.Models.WalletEventDayOrderModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.LookupCompanyLogo;
import com.news.goodlife.Fragments.WalletMultiDaysFragment;
import com.news.goodlife.Functions.InflateDayDetails;
import com.news.goodlife.Interfaces.WalletDatabaseEvents;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Models.DayCashflowModel;
import com.news.goodlife.Models.ModuleCoords;
import com.news.goodlife.Models.MultiDayCashflowModel;
import com.news.goodlife.Models.RelationshipMap;
import com.news.goodlife.PopWindowData.CashCategoryData;
import com.news.goodlife.Processing.Models.DayDataModel;
import com.news.goodlife.Processing.Models.DayTransactionModel;
import com.news.goodlife.R;
import com.news.goodlife.Singletons.SingletonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CashflowMainAdapter extends RecyclerView.Adapter<CashflowMainAdapter.ViewHolder>{

    Context context;
    int position;
    FrameLayout popWindow;
    View root, rootView;
    int cat_count = 4;
    @ColorInt int selectedStroke, unselectedStroke;


    List<DayDataModel> dateRange;
    //List<CalendarLayoutDay> dateRange;

    WalletDatabaseEvents databaseEventsCallback;
    static int dayCount = 0;


    //editorViews
    static View delete, card;
    View new_cashflow_container_global, add_cashflow_global;
    static ProgressBar spinner;
    boolean editTextOpened = false;
    boolean editCardOpend = false;
    private SingletonClass singletonMain = SingletonClass.getInstance();


    private static int balanceamount = 0;
    //PopFragment
    WalletMultiDaysFragment parentFragmentClass;

    public CashflowMainAdapter(Context context, FrameLayout popWindow, WalletMultiDaysFragment parentFragmentClass, View root, View rootView, List<DayDataModel> dateRange) {

        this.context = context;
        this.popWindow = popWindow;
        this.parentFragmentClass = parentFragmentClass;
        this.root = root;
        this.rootView = rootView;
        this.dateRange = dateRange;

        this.databaseEventsCallback = (WalletDatabaseEvents) context;



        //Log.i("DateRanges", ""+dateRange.size());

        listeners();
        loadPopFragments();
        loadBudgets();

        testBezier();

        //Get Color Attributes
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();

        //Pick Attribute Colors
        theme.resolveAttribute(R.attr.entryBorder, typedValue, true);
        selectedStroke = typedValue.data;

        theme.resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        unselectedStroke = typedValue.data;


    }

    MultiDayCashflowModel multidaysData;
    private void testBezier() {
        multidaysData = new MultiDayCashflowModel();
        DayCashflowModel dayData;
        Date testDate = new Date();
        //create 7 Days Test Data
        dayData = new DayCashflowModel(testDate,2000);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1400);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,600);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1200);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1700);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,2500);
        multidaysData.addDayCashflow(dayData);
        dayData = new DayCashflowModel(testDate,1600);
        multidaysData.addDayCashflow(dayData);

    }

    List<BudgetModel> allBudgets;
    boolean budgetsSet = false;
    private void loadBudgets() {

        allBudgets = singletonMain.getDatabaseController().BudgetController.getAllBudgets();

        if(allBudgets.size() > 0){
            budgetsSet = true;
        }
        else{
            budgetsSet = false;
        }
    }

    int fullDisplay = 0;
    private void listeners() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                if(fullDisplay == 0)
                {
                    fullDisplay = r.height();
                }
            }
        });
    }

    private void loadPopFragments() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {


        DayDataModel calDay = dateRange.get(pos);


        return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_day_cover, parent, false), pos, calDay);

        /*
        switch(calDay.getType()){
            case "weekend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_weekend_item, parent, false), pos, calDay);
            case "day":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_day_cover, parent, false), pos, calDay);
            case "monthend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_monthend_item, parent, false), pos, calDay);

            case "yearend":
                return new CashflowMainAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_recycler_yearend, parent, false), pos, calDay);

            default:
                return null;
        }*/
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return dateRange.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        PopUpFrame costcat, cashout, cashin,budgetliquid, cashflow, weekendcontainer;
        LiquidView liquidView;
        TextView itemday, itemDate;
        View frame;

        TextView minusplus_before;

        boolean isweekend = false;
        String[] colors = {"#ff6859","#59a7ff", "#b7ff59", "#FFFFFF", "#FFEB3B"};
        ImageView add_plus, add_minus;
        LabeledEntryView amount, description;

        //CostCat Variable
        List<CashCategoryData> allCashCategoryData = new ArrayList<>();
        List<BulletPointTextView> allCategoriesTextView = new ArrayList<>();

        //CashOut Variable
        TextView costTitle, costValue, incomeTitle, incomeValue;

        InflateDayDetails AsyncDay;


        public ViewHolder(@NonNull final View itemView, final int pos, final DayDataModel dayData) {
            super(itemView);

            itemView.setTag("pos_"+pos);

           switch(dayData.getType()){
                case "day":

                    TextView dayNameTV, dayDateTV, dayBalance;
                    CustomBezierGraph bezierGraph = itemView.findViewById(R.id.cover_graph);

                    bezierGraph.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            bezierGraph.setMultiDayData(multidaysData, new Date());

                            bezierGraph.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });


                    itemView.setTag(""+pos);
                    dayNameTV = itemView.findViewById(R.id.overview_day);
                    dayDateTV = itemView.findViewById(R.id.overview_date);
                    dayBalance = itemView.findViewById(R.id.cover_balance);
                    //Get The Date Name
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dayData.getDayDate());
                    dayBalance.setText(singletonMain.monefy(""+dayData.getHypertheticalBalance()));

                    dayNameTV.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

                    String dayDateString = ""+cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " "+cal.get(Calendar.DAY_OF_MONTH)+ ", " + cal.get(Calendar.YEAR);

                    dayDateTV.setText(dayDateString);



                    //ViewGroup dayDetailsContainer = itemView.findViewById(R.id.day_detail_container);
                    //AsyncDay = new InflateDayDetails(new AsyncLayoutInflater(context), dayDetailsContainer, dayData);



                    //TODO Load Views Here

                    /*
                    final ViewGroup monthlyBudgetCont = itemView.findViewById(R.id.month_flex);
                    final ViewGroup yearyBudgetCont = itemView.findViewById(R.id.year_flex);
                    inflateFixedCost(monthlyBudgetCont, yearyBudgetCont);
                    inflateBudgets(monthlyBudgetCont, yearyBudgetCont);

                    //Views to be Mapped
                    final View overview_module = itemView.findViewById(R.id.module_status_overview);
                    final View savings_module = itemView.findViewById(R.id.savings_module);
                    cashout = itemView.findViewById(R.id.events_outgoing);
                    cashin = itemView.findViewById(R.id.events_incoming);
                    costcat = itemView.findViewById(R.id.cashcat_frame);


                    dayCount++;

                    WalletEventDayOrderModel dataList = null;



                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateWithoutTime = null;
                    try {
                        dateWithoutTime = sdf.parse(sdf.format(dayData.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String dayOnly = ""+dateWithoutTime.getTime();

                    //Get The Data ordered for the Day
                    if(orderedData.has(dayOnly)){
                        Log.i("Is Inside", "Yes at Pos"+dayCount);
                        try {
                            dataList = (WalletEventDayOrderModel) orderedData.get(dayOnly);
                            //Log.i("Found","entries ="+dataList.getDaysData().size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.i("Is Not Inside", "No at Pos"+dayCount);
                    }

                    final TextView add_save = itemView.findViewById(R.id.add_cashflow_button_save);

                    frame = itemView.findViewById(R.id.frame);
                    liquidView = itemView.findViewById(R.id.revenue_liquid_module);
                    liquidView.setBaseline(randomValue(10, 300));
                    liquidView.setSavingsBG();
                    itemday = itemView.findViewById(R.id.item_day);
                    itemDate = itemView.findViewById(R.id.item_date);


                    minusplus_before = itemView.findViewById(R.id.day_newentry_minusplus_bf);

                    amount = itemView.findViewById(R.id.day_newentry_amount);
                    description = itemView.findViewById(R.id.day_newentry_description);
                    spinner = itemView.findViewById(R.id.progress_loader);
                    spinner.setVisibility(View.GONE);
                    spinner.setAlpha(0);
                    spinner.setScaleX(.7f);
                    spinner.setScaleY(.7f);


                    Calendar dayCal = Calendar.getInstance();
                    dayCal.setTime(dayData.getDate());
                    String dayName = dayCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    itemday.setText(dayName);
                    String itemDateText = dayData.getMONTH_DAY_NUMBER()+" "+dayData.getMONTH_NAME()+" "+dayData.getYEAR();
                    itemDate.setText(itemDateText);
                    itemDate.setTag(dayOnly);



                    // Entry Field Views
                    add_plus = itemView.findViewById(R.id.day_newentry_plus);
                    add_minus = itemView.findViewById(R.id.day_newentry_minus);
                    final View new_cashflow_container = itemView.findViewById(R.id.add_cashflow_entry_container);
                    final View add_cashflow = itemView.findViewById(R.id.day_item_add_cashflow);
                    ViewGroup cashflowContainer = itemView.findViewById(R.id.flex_cont);


                    //Inflate Data if exists
                    if(dataList != null){
                        inflateData(dataList, cashflowContainer);
                    }

                    //Set the amount on

                    //setBezierDayAmount(pos);

                    //setCostCats(cat_count);

                    //Cashcat Variables
                    costcat.setTransitionName(position+"costcat_frame");
                    itemday.setTransitionName(position+"_costcat_dayname");

                    //Outgoing Variables
                    cashout.setTransitionName(position+"_cashout");
                    costTitle = itemView.findViewById(R.id.outgoint_title);
                    costValue = itemView.findViewById(R.id.outgoing_value);
                    costTitle.setTransitionName(position+"_costname");
                    costValue.setTransitionName(position+"_costvalue");

                    //Incoming Variable
                    cashin.setTransitionName(position+"_cashin");
                    incomeTitle = itemView.findViewById(R.id.incoming_title);
                    incomeValue = itemView.findViewById(R.id.incoming_value);
                    incomeTitle.setTransitionName(position+"_incomename");
                    incomeValue.setTransitionName(position+"_incomingval");
                    costcat.setBackgroundColor("#101315");
                    costcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            v.setElevation(100f);
                            v.setOutlineProvider(null);
                            transitionPopWindow("CostCategories");

                        }
                    });

                    cashin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            transitionPopWindow("CashIn");
                        }
                    });

                    cashout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            transitionPopWindow("CashOut");
                        }
                    });
                    liquidView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //liquidView.resetWaveEnergy();
                        }
                    });
                    add_cashflow.findViewById(R.id.add_cash_inday).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                            add_cashflow_global = add_cashflow;
                            new_cashflow_container_global = new_cashflow_container;
                            editTextOpened = true;
                            //Add entry fields
                            new_cashflow_container.setVisibility(View.VISIBLE);
                            new_cashflow_container.animate().alpha(1f).scaleX(1).scaleY(1).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    amount.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            add_cashflow.setVisibility(View.GONE);

                            toggleMinusPlus(true);

                            add_save.setTextColor(selectedStroke);
                            add_save.setAlpha(1f);
                        }


                    });

                    add_minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toggleMinusPlus(true);
                        }
                    });

                    add_plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toggleMinusPlus(false);
                        }
                    });

                    add_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Check if both Fields have Entries
                            String ts = ""+dayData.getDate().getTime();
                            long tslong = Long.parseLong(ts);
                            Date tsdate = new Date();
                            tsdate.setTime(tslong);

                            Calendar tsCal = Calendar.getInstance();
                            tsCal.setTime(tsdate);


                          //  Log.i("TimeSTamp", ""+""+tsCal.get(Calendar.YEAR));

                            if(amount.getText().toString().equals("") || description.getText().toString().equals("")){

                                Toast.makeText(context, "Add Description and Amount", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Data Will be saved", Toast.LENGTH_SHORT).show();
                                hideAddCashflowEntry(true);


                                WalletEventModel data = new WalletEventModel();
                                data.setValue(amount.getText().toString());
                                data.setDescription(description.getText().toString());
                                data.setCreated( ""+ new Date().getTime());
                                data.setDate(""+dayData.getDate().getTime());
                                data.setPositive(isPositiveAmount ? "plus": "minus");
                                data.setRepeat("No");

                                databaseEventsCallback.saveNewWalletEvent(data, pos);

                            }

                        }
                    });


                    //Relationship Map Algorithms
                    final RelationshipMapView mapView = itemView.findViewById(R.id.overlay_relation_map);

                    itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            //All the Modules are Loaded, start getting there coordinates

                            /*
                            moduleCenterX = (int)overview_module.getX() + (overview_module.getWidth() / 2);
                            moduleCenterY = (int)overview_module.getY() + (overview_module.getHeight() / 2);
                            ModuleCoords mc = new ModuleCoords(moduleCenterX, moduleCenterY, "overview");
                            moduleCoords.add(mc);

                            moduleCenterX = (int)savings_module.getX() + (savings_module.getWidth() / 2);
                            moduleCenterY = (int)savings_module.getY() + (savings_module.getHeight() / 2);
                            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "savings");
                            moduleCoords.add(mc);

                            moduleCenterX = (int)cashout.getX() + (cashout.getWidth() / 2);
                            moduleCenterY = (int)cashout.getY() + (cashout.getHeight() / 2);
                            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "cashout");
                            moduleCoords.add(mc);

                            moduleCenterX = (int)cashin.getX() + (cashin.getWidth() / 2);
                            moduleCenterY = (int)cashin.getY() + (cashin.getHeight() / 2);
                            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "cashin");
                            moduleCoords.add(mc);

                            moduleCenterX = (int)costcat.getX() + (costcat.getWidth() / 2);
                            moduleCenterY = (int)costcat.getY() + (costcat.getHeight() / 2);
                            mc = new ModuleCoords(moduleCenterX, moduleCenterY, "balance");
                            moduleCoords.add(mc);

                            //Iterate through Budgets


                            //Test Relationships
                            //relationshipMapData.add(connectRelationships(moduleCoords, "balance", "savings"));
                            //relationshipMapData.add(connectRelationships(moduleCoords, "savings", "overview"));
                            //mapView.setMapData(relationshipMapData);






                            //View savings_module = itemView.findViewById(R.id.savings_module);
                            //cashout = itemView.findViewById(R.id.events_outgoing);
                            //cashin = itemView.findViewById(R.id.events_incoming);
                            //costcat = itemView.findViewById(R.id.cashcat_frame);


                            itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });

                    */
                    break;
                case "weekend":
                    break;
                case "monthend":
                    //TextView monthname = itemView.findViewById(R.id.wallet_balance_card_new_month);
                    //monthname.setText(dayData.getMONTH_NAME());
                    break;
                case "yearend":
                    //TextView yearname = itemView.findViewById(R.id.wallet_balance_card_new_year);
                    //yearname.setText(dayData.getYEAR());
                    break;
            }

        }



        List<ModuleCoords> moduleCoords = new ArrayList<>();
        List<RelationshipMap> relationshipMapData = new ArrayList<>();

        private void inflateBudgets(ViewGroup monthlyBudgetCont, ViewGroup yearlyBudgetCont) {


            if(budgetsSet){
                //Has Budget Load Them
                for(final BudgetModel budget: allBudgets){

                    //TODO Check if I can make this Call more Efficient
                    final BudgetCategoryModel category = singletonMain.getDatabaseController().BudgetCategoryController.getBudgetCategory(Integer.parseInt(budget.getCategoryid()));

                    LayoutInflater inflater = LayoutInflater.from(root.getContext());
                    final View catitem = inflater.inflate(R.layout.budget_list_item, null);

                    catitem.setTag(budget.id);

                    final IconDoughnutView dv = catitem.findViewById(R.id.icondoughnut);
                    //dv.setCategory("#64CE65", category.getCaticon());

                    CardView catcard = catitem.findViewById(R.id.category_item);
                    TextView budget_name = catitem.findViewById(R.id.budget_name);

                    budget_name.setText(category.getCatname());
                    //SetWidth of the Button to match the Screen
                    ViewGroup.LayoutParams lp = catcard.getLayoutParams();

                    lp.width = budgetButtonSize;
                    lp.height = budgetButtonSize;
                    catcard.setLayoutParams(lp);

                    monthlyBudgetCont.addView(catitem);
                    //yearlyBudgetCont.addView(catitem);

                    catitem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            singletonMain.changeFragment.data = new ArrayList<>();
                            singletonMain.changeFragment.getData().add(budget.getId());
                            singletonMain.changeFragment.setValue("BudgetModule");
                        }
                    });

                }
                final float budgetFlexTop = monthlyBudgetCont.getY();
            }
            else{
                //No Budgets Encourage User to Set budget

            }

        }

        private RelationshipMap connectRelationships(List<ModuleCoords> moduleCoords, String fromID, String toID){
            RelationshipMap relationshipConnection = new RelationshipMap();

            for(ModuleCoords moduleCoord: moduleCoords){
                if(moduleCoord.getName().equals(fromID)){
                    //From Match Found set relationshipConnection
                    relationshipConnection.setFromX(moduleCoord.getX());
                    relationshipConnection.setFromY(moduleCoord.getY());
                }

                if(moduleCoord.getName().equals(toID)){
                    //To Match Found setRelationships
                    relationshipConnection.setToX(moduleCoord.getX());
                    relationshipConnection.setToY(moduleCoord.getY());
                }
            }

            //Todo make sure it is always set
            return relationshipConnection;
        }


        int moduleCenterX, moduleCenterY;
        int flexWidth, budgetButtonSize;

        private void inflateFixedCost(ViewGroup monthlyBudgetCont, ViewGroup yearlyBudgetCont){

            flexWidth = singletonMain.getDisplayWidth() - singletonMain.dpToPx(25);
            budgetButtonSize = (flexWidth / 4) - singletonMain.dpToPx(10);

            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            View fixeditem = inflater.inflate(R.layout.budget_list_item, null);

            //TODO Set Parameters to identify fixed cost

            IconDoughnutView dv = fixeditem.findViewById(R.id.icondoughnut);
            dv.setCategory("#FFFFFF", "icn_regular");

            CardView catcard = fixeditem.findViewById(R.id.category_item);

            ViewGroup.LayoutParams lp = catcard.getLayoutParams();

            lp.width = budgetButtonSize;
            lp.height = budgetButtonSize;
            catcard.setLayoutParams(lp);

            //Populate Fixed onBoth YEar and Month

            monthlyBudgetCont.addView(fixeditem);
            //yearlyBudgetCont.addView(fixeditem);

            fixeditem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Open Fixed COst Progress
                    singletonMain.changeFragment.setValue("FixedModule");
                }
            });

        }


        private void inflateData(WalletEventDayOrderModel dataList, ViewGroup cashflowContainer) {

            for(final WalletEventModel event: dataList.getDaysData()){

                LayoutInflater inflater = LayoutInflater.from(root.getContext());
                boolean neg;
                final View eventHolder;

                if(event.getPositive().equals("plus")){
                    neg = false;
                    eventHolder = inflater.inflate(R.layout.wallet_recycler_day_cashflow_item_pos, null);
                    //balanceamount = balanceamount + Integer.parseInt(event.getValue());
                }
                else{
                    neg = true;
                    eventHolder = inflater.inflate(R.layout.wallet_recycler_day_cashflow_item_neg, null);
                    //balanceamount = balanceamount - Integer.parseInt(event.getValue());
                }

                final ImageView logo = eventHolder.findViewById(R.id.company_logo);

                new LookupCompanyLogo(event.getDescription(),logo);

                String amount = (neg? "-": "+") + event.getValue();
                String description = event.getDescription();

                ((TextView)eventHolder.findViewById(R.id.amount)).setText(amount);
                ((TextView)eventHolder.findViewById(R.id.description)).setText(description);

                cashflowContainer.addView(eventHolder, 3);

                eventHolder.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.MATCH_PARENT));

                eventHolder.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        Log.i("Delete", "yes give me Options");


                        hideEditCard();


                        editCardOpend = true;

                        delete = view.findViewById(R.id.delete);
                        card = view.findViewById(R.id.background_card);

                        //card.setVisibility(View.VISIBLE);
                        //Change Background Instead card.animate().scaleY(1).scaleX(1).alpha(.2f).setDuration(200);
                        delete.setVisibility(View.VISIBLE);
                        delete.animate().scaleX(1).scaleY(1).alpha(1).setDuration(200);


                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // runn message to delete the item

                                if(parentFragmentClass.getMyDB().WalletEvent.deleteCashflow(event.getId())){
                                    //Only True if Delete was Successfull

                                    eventHolder.animate().alpha(0).scaleX(.1f).scaleY(.1f).setDuration(350).setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {

                                            eventHolder.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });

                                };
                            }
                        });

                        return true;
                    }
                });

            }

        }



        private boolean softKeyUp = false;

        private void animateSoftkeyOpend(int start, int end, final boolean close){
            ValueAnimator vaLeave = ValueAnimator.ofInt(start, end);
            vaLeave.setDuration(300);
            vaLeave.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int animval = (int) valueAnimator.getAnimatedValue();
                    root.setY(animval);
                }
            });
            vaLeave.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {


                }

                @Override
                public void onAnimationEnd(Animator animator) {



                    if(close){

                        hideAddCashflowEntry(true);
                        softKeyUp = false;
                    }
                    else{
                        softKeyUp = true;
                    }

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            vaLeave.start();

        }

        int fullDisplay = 0;
        boolean isPositiveAmount = false;

        private void toggleMinusPlus(boolean add) {

            if(!add){
                isPositiveAmount = true;
                add_plus.setColorFilter(selectedStroke);
                add_minus.setColorFilter(unselectedStroke);
                minusplus_before.setText("+");
                //DrawableCompat.setTint(add_plus.getDrawable(), selectedStroke);
            }
            else{
                isPositiveAmount = false;
                add_minus.setColorFilter(selectedStroke);
                add_plus.setColorFilter(unselectedStroke);
                minusplus_before.setText("-");
            }

        }


        private void transitionPopWindow(String module){

            // Set up the transaction.
            FragmentTransaction ft = parentFragmentClass.activity.getSupportFragmentManager().beginTransaction();

            switch (module){
                case "CostCategories":

                    break;
                case "CashIn":
                    singletonMain.changeFragment.setValue("IncomeModule");
                    break;
                case "CashOut":
                    singletonMain.changeFragment.setValue("OutgoingModule");
                    break;
                default:
                    break;
            }
            //TODO GET EXIT TRANSITION ERROR FREE
            //parentFragmentClass.setExitTransition(new Explode().setDuration(350));

            // Finally press play.
            ft.commit();
            parentFragmentClass.activity.checkBackstack();

            //Toggle MainMenu
            parentFragmentClass.activity.toggleMainMenuContainer();
        }

    }

    public void hideEditCard() {

        if(editCardOpend){
            //resetViews(card);
            resetViews(delete);
            editCardOpend = false;
        }

    }

    private void resetViews(View v){
        v.setVisibility(View.GONE);
        v.setAlpha(0);
        v.setScaleY(.7f);
        v.setScaleX(.7f);

    }


    public void hideAddCashflowEntry(final boolean fade){

        if(editTextOpened){

            editTextOpened = false;
            new_cashflow_container_global.animate().alpha(0f).scaleX(.7f).scaleY(.7f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if(!fade){
                        new_cashflow_container_global.setVisibility(View.GONE);
                        add_cashflow_global.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(fade){
                        new_cashflow_container_global.setVisibility(View.GONE);
                        add_cashflow_global.setVisibility(View.VISIBLE);
                        add_cashflow_global.setAlpha(0);
                        add_cashflow_global.setScaleX(.7f);
                        add_cashflow_global.setScaleY(.7f);
                        add_cashflow_global.animate().alpha(1f).scaleY(1).scaleX(1);

                        spinner.setVisibility(View.VISIBLE);
                        spinner.animate().scaleX(1f).scaleY(1f).alpha(1);

                    }

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }


    }



    private int randomValue(int rangeStart, int rangeEnd){

        int random = new Random().nextInt(rangeEnd) + rangeStart;
        return random;
    }



}

