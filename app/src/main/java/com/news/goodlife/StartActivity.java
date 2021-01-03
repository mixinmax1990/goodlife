package com.news.goodlife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.CustomEntries.PageIndicatorBar;
import com.news.goodlife.CustomViews.CustomIcons.MenuIcons;
import com.news.goodlife.CustomViews.CustomIcons.WalletIcon;
import com.news.goodlife.CustomViews.CustomIcons.WalletIconCard;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.Data.Local.Controller.DatabaseController;
import com.news.goodlife.Data.Local.Models.Financial.WalletEventModel;
import com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks.KlarnaResponseCallback;
import com.news.goodlife.Data.Remote.Klarna.Models.Consent.POSTgetConsentDataModel;
import com.news.goodlife.Fragments.SlideInFragments.BankTransactionsFragment;
import com.news.goodlife.Fragments.SlideInFragments.BudgetManagementFragment;
import com.news.goodlife.Fragments.SlideInFragments.BudgetModuleFragment;
import com.news.goodlife.Fragments.SlideInFragments.FixedCostsFragment;
import com.news.goodlife.Fragments.SlideInFragments.FixedIncomeFragment;
import com.news.goodlife.Fragments.SlideInFragments.FixedModuleFragment;
import com.news.goodlife.Fragments.SlideInFragments.KlarnaApp;
import com.news.goodlife.Fragments.SlideInFragments.SubcriptionsFragment;
import com.news.goodlife.Fragments.WalletCalendarFragment;
import com.news.goodlife.Fragments.WalletTodayFragment;
import com.news.goodlife.Functions.RelationshipMapping;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;
import com.news.goodlife.Interfaces.WalletDatabaseEvents;
import com.news.goodlife.LayoutManagers.MultiDaysLinearLayoutManager;
import com.news.goodlife.Models.CalendarLayoutDay;
import com.news.goodlife.Singletons.SingletonClass;
import com.news.goodlife.Tools.CameraScan.CameraScanFragment;
import com.news.goodlife.Fragments.WalletMultiDaysFragment;
import com.news.goodlife.Fragments.FinancialFragment;
import com.news.goodlife.Fragments.GoalsFragment;
import com.news.goodlife.Fragments.HealthFragment;
import com.news.goodlife.Fragments.PhysicalFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class StartActivity extends AppCompatActivity implements OnClickedCashflowItemListener, CalendarSelectDayListener, WalletDatabaseEvents {



    public FrameLayout fragment_container_one;
    public FrameLayout fragment_container_two;
    public CardView fragment_container_three;
    public ImageView fragment_container_three_backarrow;
    public BorderRoundView frame_one_border, frame_two_border;
    public TextView frame_one_title, frame_two_title;

    //Authentications
    public BlurView biometric_cover;
    //private BiometricPrompt.


    private Fragment fragment;
    int selectedFragment = 0;
    boolean DarkMode = false;



    //Statusbar Replacement
    int statusBarHeight;
    FrameLayout statusbarspace;

    BlurView blurViewMenu;
    FrameLayout menu_container;
    FrameLayout menu_line;

    ElasticEdgeView elasticEdgeView;
    ConstraintLayout mainContainer;
    private Vibrator vibrator;
    //Fragment Navigation

    //Overview Frame
    TextView overviewTodayTitle, overviewCalendarTitle;


    //MenuIcons
    MenuIcons goalsBTN, analysisBTN, hubBTN, magicButton;
    WalletIcon walletBTN;
    PageIndicatorBar walletPageIndicator;

    WalletIconCard walletCardOne;

    //Menu Navigation
    View menuFloatingBtn;
    View menu_drawer;

    //Metrics
    int displayWidth, displayHeight;

    //Database
    DatabaseController myDB;

    //Callbacks


    //Connect Accounts

    View connectAccountOne;

    //Loading Data TODO make Api Call Klarna
    public String loadJSONFromAsset(){
        String json = null;

        try{
            InputStream is = this.getAssets().open("my_account.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void loadInitData(){

        //RequestCompanyWebsite requestCompanyWebsite = new RequestCompanyWebsite();
        //Log.i("Company Site", "");
        //Make sure we check new entries first and leave all existing entries
        myDB.WalletEvent.deleteAllEvents();
        // set
        try{
            JSONArray account_data = new JSONArray(loadJSONFromAsset());
            int size = account_data.length();
            for(int i = 0; i < size; i++){

                JSONObject jsondata = account_data.getJSONObject(i);
                WalletEventModel dataModel = new WalletEventModel();

                dataModel.setCreated("null");
                float amount = Float.parseFloat(jsondata.getJSONObject("amount").getString("amount")) / 100;

                dataModel.setValue(""+amount);

                try{
                    dataModel.setDescription(""+ jsondata.getJSONObject("counter_party").getString("holder_name"));
                }
                catch(JSONException e){
                    dataModel.setDescription(""+ jsondata.getString("reference"));
                }


                //create Date from string
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(""+jsondata.getString("date"));

                    dataModel.setDate(""+date.getTime());
                    dataModel.setDateOBJ(date);
                }
                catch(ParseException e){

                    dataModel.setDate("null");

                }

                if(jsondata.getString("type").contains("DEBIT")){
                    dataModel.setPositive("minus");
                }
                else{
                    dataModel.setPositive("plus");
                }

                dataModel.setRepeat("null");

                myDB.WalletEvent.newCashflow(dataModel);


                //Log.i("JSON Data", "" + account_data.getJSONObject(i).getString("reference"));
                //Log.i("JSON Data", "" + account_data.getJSONObject(i).getJSONObject("amount").getString("amount"));

            }

        }
        catch(JSONException e){
            e.printStackTrace();
        }

    }
    //Loading Icons
    SingletonClass singletonClass;
    CardView blurcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        transparentStatus();
        setStatusbarspace();

        //SIngleton Classes
        singletonClass = SingletonClass.getInstance();

        //DataBase
        myDB = new DatabaseController(this);
        //setup app
        singletonClass.setDatabaseController(myDB);

        //Check if we have Klarna Consent
        if(singletonClass.providedConsent()){
            Log.i("PROVIDED_CONSENT", ""+singletonClass.getDatabaseController().KlarnaConsentDBController.getConsent().getData().getConsent_id());
        }
        else{
            Log.i("PROVIDED_CONSENT", "No Consent");
        }


        SetupApp setup = new SetupApp();



        singletonClass.changeFragment.addObserver(changeFragment);



        walletBTN = findViewById(R.id.buttonWallet);
        goalsBTN = findViewById(R.id.buttonGoals);
        analysisBTN = findViewById(R.id.buttonAnalysis);
        hubBTN = findViewById(R.id.buttonHub);
        magicButton = findViewById(R.id.magic_button);
        walletPageIndicator = findViewById(R.id.wallet_page_indicator);

        walletCardOne = findViewById(R.id.walletCardOne);

        menu_drawer = findViewById(R.id.menu_drawer);

        fragment_container_one = findViewById(R.id.fragment_container_one);
        fragment_container_two = findViewById(R.id.fragment_container_two);
        fragment_container_three = findViewById(R.id.fragment_container_three);
        fragment_container_three_backarrow = findViewById(R.id.fragment_container_tree_backarrow);
        frame_one_border = findViewById(R.id.frame_one_border);
        frame_two_border = findViewById(R.id.frame_two_border);
        frame_one_title = findViewById(R.id.frame_one_title);
        frame_two_title = findViewById(R.id.frame_two_title);


        statusbarspace = findViewById(R.id.statusspace);


        elasticEdgeView = findViewById(R.id.elasticEdge);
        mainContainer = findViewById(R.id.main_container);
        menu_container = findViewById(R.id.menu_container);
        menu_line = findViewById(R.id.seperator_line_menu);
        menuFloatingBtn = findViewById(R.id.main_menu_float_open_btn);

        //Recognize Image
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        previewImage = findViewById(R.id.previewImage);


        //Connect Account
        connectAccountOne = findViewById(R.id.connect_account_one);

        loadInitData();

        //Test Singleton


        //callbacks
       //walletDaysCallback = (WalletDaysCallback) this.context;

        //Biometric Authentication
        biometric_cover = findViewById(R.id.biometric_cover);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricAuthentication();
            //biometric_cover.setVisibility(View.GONE);
        }
        else{
            //No Authentication Needed Hide the cover

        }

        blurViewMenu = findViewById(R.id.blurviewmenu);
        blur(10);

        //setWalletCards();

        if (getResources().getBoolean(R.bool.dark)) {

        } else {
            // Do night stuff here

        }
        //Navigation Section

        //Overview

        vibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //open Wallet First
        selectedFragment = 3;
        changeSelectedColor();


        DarkMode = true;

        listeners();
        //blur(15f);
        
        loadTools();
        menuDrawer();

        fragment_container_one.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                displayWidth = fragment_container_one.getWidth();
                displayHeight = fragment_container_one.getHeight();
                fragment_container_two.setX(displayWidth);
                fragment_container_three.setX(displayWidth);
                fragment_container_three_backarrow.setX(displayWidth);

                centerScaledWidth = (int)((int)(displayWidth * .5)/2.2);

                frame_one_border.animate().alpha(0);
                frame_two_border.animate().alpha(0);


                singletonClass.setDisplayHeight(displayHeight);
                singletonClass.setDisplayWidth(displayWidth);
                singletonClass.setUniversalBackarrow(fragment_container_three_backarrow);
                //slideMechanism(0, true);

                fragment_container_one.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        menu_drawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawerwidth = menu_drawer.getWidth();
                menu_drawer.setX(-drawerwidth);
                menu_drawer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void biometricAuthentication() {

        Log.i("BIOMETRUNS", "YES");

        //TODO Make sure all pitfalls are checked

        /*
        //Max 25f
        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();
        biometric_cover.setClipToOutline(true);

        biometric_cover.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        biometric_cover.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(6)
                .setHasFixedTransformationMatrix(true);
        */
        CancellationSignal signal = new CancellationSignal();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                .setTitle("Authenticate")
                .setSubtitle("")
                .setDescription("Remove Authentication in App Settings")
                .setNegativeButton("CANCEL", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .build();
        biometricPrompt.authenticate(signal, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.i("Authentication", "Error");
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
                Log.i("Authentication", "Help");
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.i("Authentication", "Success");
                biometric_cover.animate().alpha(0).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        biometric_cover.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.i("Authentication", "Failed");
            }
        });


    }

    private void setWalletCards() {
        int walletWidth;
        final int padding = singletonClass.dpToPx(12);

        walletBTN.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int cardWidth = walletBTN.getWidth() - (padding * 2);
                int cardHeight = (cardWidth / 6) * 4;

                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) walletCardOne.getLayoutParams();

                lp.setMargins(padding, padding, padding, padding);
                lp.width = cardWidth;
                lp.height = cardHeight;

                walletCardOne.setLayoutParams(lp);

                walletBTN.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private Observer changeFragment = new Observer() {
        @Override
        public void update(Observable o, Object newValue) {

            //Slidein Fragment Container and Load
            Log.d("Observer Main Activity", "a1 has changed, new value:"+ newValue);

            switch(newValue.toString()){
                case "IncomeModule":
                    slideInContainerThree(fragment_container_one);
                    openSideFragment("IncomingModule");
                    break;
                case "OutgoingModule":
                    slideInContainerThree(fragment_container_one);
                    openSideFragment("OutgoingModule");
                    break;
                case "BudgetModule":
                    slideInContainerThree(fragment_container_one);
                    openSideFragment("BudgetModule");
                    //Contains ID as Data
                    break;
                case "FixedModule":
                    slideInContainerThree(fragment_container_one);
                    openSideFragment("FixedModule");
                    break;
                case "AuthKlarna":
                    Log.i("Client_Token", ""+singletonClass.changeFragment.data.get(0));
                    break;
                default:
                    break;

            }

        }
    };

    View menuone, menutwo, menuthree, menufour, menufive, connectBankMenu;
    private void menuDrawer(){
        menuone = findViewById(R.id.menu_one);
        menuone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked", "MneuOne");
                slideInContainerThree(null);
                openSideFragment("BudgetManagement");
            }
        });
        menutwo = findViewById(R.id.menu_two);
        menutwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideInContainerThree(null);
                openSideFragment("FixedCosts");
            }
        });
        menuthree = findViewById(R.id.menu_three);
        menuthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideInContainerThree(null);
                openSideFragment("FixedIncome");
            }
        });
        menufour = findViewById(R.id.menu_four);
        menufour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideInContainerThree(null);
                openSideFragment("Subscriptions");
            }
        });
        menufive = findViewById(R.id.menu_five);
        menufive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideInContainerThree(null);
                openSideFragment("BankTransactions");
            }
        });

        fragment_container_three_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOutContainerThree(true);
            }
        });

    }
    int drawerwidth;
    int centerScaledWidth;
    //Slideing Fragments Mechanism

    public boolean slideToday = false;
    public boolean slideCalendar = false;
    boolean vibratedSlideIn = false;
    boolean vibratedSlideOut = false;

    public void slideMechanism(int move, boolean open){
        float moveScale;

        float slideProgess;
        if(open){
            slideProgess = (float)(displayWidth - move) / displayWidth;
            if(slideProgess > .7f){
                if(!vibratedSlideIn){
                    vibrator.vibrate(5);
                    vibratedSlideIn = true;
                }
            }
        }
        else{
            slideProgess = Math.abs((float)move / displayWidth);
            if(slideProgess > .3f){
                if(!vibratedSlideIn){
                    vibrator.vibrate(5);
                    vibratedSlideIn = true;
                }
            }
        }

        movePageIndicator(slideProgess);

        setSlideWidth(move, open);
        float visibleScreen;
        if(open){
             visibleScreen = (float)(displayWidth - move) / displayWidth;

             // Move the Indicator
            try {
                walletTodayFragment.moveIndicator((int)(displayWidth - dpToPx(8) - move * 1.5), displayWidth);
            }
            catch(Exception e){}
        }
        else{
            visibleScreen = ((float)move / displayWidth);
            walletCalendarFragment.moveIndicator((int)(dpToPx(2) + move), displayWidth);
        }


        moveScale = 0.5f + (visibleScreen/2);

        setScaleNavigation(moveScale, 1f);

        //scrollHeightMenu((move / 20)*-1, true);

    }
    public void slideSideMenu(int dist){
        float slideprogress = (float)(displayWidth - (dist*2)) / displayWidth;
        float revSlideprogress = (1f - slideprogress)*2;

        Log.i("SlideProg", ""+slideprogress);

        float scale;
        if(slideprogress >= 0.5f){
            scale = slideprogress;
            overviewWallet = true;
        }
        else{
            scale = 0.5f;
        }
        setScaleNavigation(scale, scale);

        //SLide Fragment One
        if(revSlideprogress >= 1f){
            revSlideprogress = 1f;
        }
        fragment_container_one.setX(+(centerScaledWidth * revSlideprogress));
        frame_one_border.setX(+(centerScaledWidth * revSlideprogress));
        //frame_one_border.setAlpha(revSlideprogress);
        //Slide Fragment Two
        int destination = displayWidth - (+centerScaledWidth + (int)(centerScaledWidth));
        fragment_container_two.setX(displayWidth - (destination * revSlideprogress));
        frame_two_border.setX(displayWidth - (destination * revSlideprogress));
        frame_two_border.setAlpha(revSlideprogress);
        //Slide Navigation Bar
        menu_drawer.setX(-drawerwidth * (1f - revSlideprogress));
        menu_drawer.setAlpha(revSlideprogress);

    }

    private void movePageIndicator(float perc){

        if(selectedFragment == 3 || selectedFragment == 5){
           // Wallet is selected
            //walletBTN.moveIndicator(perc);
            //TODO change move Indicator to the Right Position

            walletPageIndicator.movePoint(perc);
            //hh
        }

    }
    public void autoFinishSlide(int move,final boolean open, long velocity){


        float slideProgess;
        if(open){
             slideProgess = (float)(displayWidth - move) / displayWidth;
        }
        else{
            slideProgess = Math.abs((float)move / displayWidth);
        }

        boolean close = false;
        ValueAnimator va;
        //Opening Fragment Two
        if(open){
            if(slideProgess > .7f){
                close = true;
            }

            if(close){
                va = ValueAnimator.ofInt(move, 0);
            }
            else{
                va = ValueAnimator.ofInt(move, displayWidth);
            }
        }
        //Opening Fragment One
        else{

            //Log.i("SlideProg", ""+slideProgess);
            if(slideProgess > .3f){
                close = true;
            }

            if(close){
                va = ValueAnimator.ofInt(Math.abs(move), displayWidth);
            }
            else{
                va = ValueAnimator.ofInt(move, 0);
            }
        }



        //Determin Velocity

        if(velocity > 350){
            velocity = 350;
        }


        va.setDuration(velocity);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int animval = (int)valueAnimator.getAnimatedValue();
                //Log.i("AnimVal", ""+animval);
                slideMechanism(Math.abs(animval), open);
            }

        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                vibratedSlideIn = false;
                vibratedSlideOut = false;
                walletCalendarFragment.resetIndicator(dpToPx(2));
                walletMultiDaysFragment.resetIndicator(displayWidth - dpToPx(8));

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        va.start();

    }

    View movedView = null;
    public void slideInContainerThree(@Nullable final View frame){

        movedView = frame;

        fragment_container_three.setVisibility(View.VISIBLE);
        ValueAnimator va;
        va = ValueAnimator.ofInt(displayWidth, 0);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int animval = (int)valueAnimator.getAnimatedValue();

                float perc = 1f -(animval / (float)displayWidth);
                fragment_container_three.setX(animval);
                fragment_container_three_backarrow.setX(animval);
                menu_drawer.setX(-drawerwidth * perc);

                //Move Frame if it is not Null
                if(frame != null){
                        frame.setX(-(int)((displayWidth - animval)*0.25));
                }
            }

        });
        // long time = SystemClock.uptimeMillis();
        //float timeInterpolated = (float) time / (float)(300);
        TimeInterpolator time = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return 0;
            }
        };
        final TimeInterpolator testInterpolator = new LinearOutSlowInInterpolator();
        va.setInterpolator(testInterpolator);
        va.start();
    }
    public void slideOutContainerThree(final boolean showdrawer){
        float x = 0;
        if(movedView != null){
            x = movedView.getX();
        }

        if(opendSideFragment instanceof KlarnaApp){

            Log.i("Is Instance", "KlarnaAPP");
            //Make sure to close Session if User Goes back
            fragment_container_three_backarrow.setVisibility(View.VISIBLE);

        }
        else{
            Log.i("Is Instance", "Something else");
        }

        ValueAnimator va;
        va = ValueAnimator.ofInt(0, displayWidth);
        va.setDuration(300);
        final float finalX = x;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int animval = (int)valueAnimator.getAnimatedValue();

                float perc = 1f -(animval / (float)displayWidth);

                fragment_container_three.setX(animval);
                fragment_container_three_backarrow.setX(animval);
                if(showdrawer){
                    menu_drawer.setX(-drawerwidth * perc);
                }

                if(movedView != null){
                    movedView.setX(finalX * perc);
                }

            }

        });
        va.start();
    }

    public void xSlideOutContainerThree(float x){
        float move = Math.abs(x);
        float perc = 1f - (move/ (float)displayWidth);
        fragment_container_three.setX(move);
        fragment_container_three_backarrow.setX(move);

    }

    private void walletOverview(String from, final String to){


        if(from.equals("Overview")){

            //Get Positions of Views
            final int frag1X = (int) fragment_container_one.getX();
            final int frag2X = (int) fragment_container_two.getX();
            final float scale1Diff =  (to.equals("Calendar")? .5f : 1f) - scale;
            final float scale2Diff =  1f - scale;
            final float frag1Diff = - frag1X;
            final float frag2XDiff;
            final int heightDiff = displayHeight - displayWidth;
            final int marginBottomDiff = - marginBottom;
            final ConstraintLayout.LayoutParams LPone = (ConstraintLayout.LayoutParams) fragment_container_one.getLayoutParams();
            final ConstraintLayout.LayoutParams LPtwo = (ConstraintLayout.LayoutParams) fragment_container_two.getLayoutParams();


            Log.i("fragment X", "One = "+frag1X+" ;; Two"+frag2X);

            ValueAnimator va = ValueAnimator.ofFloat(0, 1f);
            va.setDuration(300);
            if(to.equals("Days")){
                 frag2XDiff = displayWidth - frag2X;
            }
            else{
                frag2XDiff  = - frag2X;
            }


            // Animate scale back to 1 and move Both frames to wanted position
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animVal = (float)valueAnimator.getAnimatedValue();

                    //Animate Scale
                    fragment_container_one.setScaleX(scale + (scale1Diff * animVal));
                    fragment_container_one.setScaleY(scale + (scale1Diff * animVal));
                    frame_one_border.setScaleX(scale + (scale1Diff * animVal));
                    frame_one_border.setScaleY(scale + (scale1Diff * animVal));

                    fragment_container_two.setScaleX((scale) + (scale2Diff * animVal));
                    fragment_container_two.setScaleY((scale) + (scale2Diff * animVal));
                    frame_two_border.setScaleX((scale) + (scale2Diff * animVal));
                    frame_two_border.setScaleY((scale) + (scale2Diff * animVal));

                    //Animate Position

                    fragment_container_one.setX(frag1X + (frag1Diff * animVal));
                    frame_one_border.setX(frag1X + (frag1Diff * animVal));

                    fragment_container_two.setX(frag2X + (frag2XDiff * animVal));
                    frame_two_border.setX(frag2X + (frag2XDiff * animVal));

                    // Animate LayoutParams

                }
            });

            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if(to.equals("Calendar")){
                        frame_one_border.setAlpha(0);
                    }

                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    overviewWallet = false;
                    frame_one_border.animate().alpha(0);
                    frame_two_border.animate().alpha(0);

                }
                @Override
                public void onAnimationCancel(Animator animator) {

                }
                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            va.setInterpolator(new AccelerateDecelerateInterpolator());
            va.start();

        }



    }
    float scale;

    boolean overviewWallet = false;
    int overviewFragTwoX;
    //Overview Fragments
    public void overviewFragments(){
        overviewWallet = true;

        scale = 0.5f;

        setScaleNavigation(scale,scale);

        //overviewFragTwoX = (int)(displayWidth /2.5);
        //setSlideWidth(overviewFragTwoX, true);

        fragment_container_one.setX(+centerScaledWidth);
        frame_one_border.setX(+centerScaledWidth);

        fragment_container_two.setX(+centerScaledWidth + (int)(centerScaledWidth));
        frame_two_border.setX(+centerScaledWidth  + (int)(centerScaledWidth));

    }
    int marginBottom;
    public void setScaleNavigation(float fragOneScale, float fragTwoScale){

        fragment_container_one.setScaleX(fragOneScale);
        fragment_container_one.setScaleY(fragOneScale);
        frame_one_border.setScaleX(fragOneScale);
        frame_one_border.setScaleY(fragOneScale);


        fragment_container_two.setScaleX(fragTwoScale);
        fragment_container_two.setScaleY(fragTwoScale);
        frame_two_border.setScaleX(fragTwoScale);
        frame_two_border.setScaleY(fragTwoScale);

    }

    public void setSlideWidth(int move, boolean open){
        if(open){
            fragment_container_two.setX(displayWidth - move);
            frame_two_border.setX(displayWidth - move);
        }
        else{
            fragment_container_two.setX(move);
            frame_two_border.setX(move);
        }

    }
    public void setSlideOverview(int move, boolean open){

            fragment_container_two.setX(overviewFragTwoX - move);
            //fragment_container_one.setX(-move);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!mainMenuVisible){
            toggleMainMenuContainer();
        }

    }

    private void loadTools() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void blur(float radius) {
        radius = Math.round(radius);

        //Max 25f
        View decorView = getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurViewMenu.setClipToOutline(true);

        blurViewMenu.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        blurViewMenu.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

    }

    int menuTop = 0;
    private void listeners(){

        frame_one_border.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        //walletBTN.animateLeave();

                        //Log Animate Overview Leave To Today
                        walletOverview("Overview", "Days");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                if(overviewWallet){
                    return true;
                }
                else{
                    return false;
                }

            }


        });

        frame_two_border.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        //walletBTN.animateLeave();

                        //Log Animate Overview Leave To Calendar
                        walletOverview("Overview", "Calendar");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                if(overviewWallet){
                    return true;
                }
                else{
                    return false;
                }

            }


        });



        walletBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if(selectedFragment != 3){
                            resetButtonTint();
                            //walletBTN.animateEnter();
                            selectedFragment = 3;
                            changeSelectedColor();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        //walletBTN.animateLeave();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return true;
            }
        });

        goalsBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if(selectedFragment != 2){
                            resetButtonTint();
                            goalsBTN.animateEnter();
                            selectedFragment = 2;
                            changeSelectedColor();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //goalsBTN.animateLeave();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return true;
            }
        });

        analysisBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if(selectedFragment != 3){
                            resetButtonTint();
                            analysisBTN.animateEnter();
                            selectedFragment = 3;
                            changeSelectedColor();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //analysisBTN.animateLeave();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return true;
            }
        });

        hubBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if(selectedFragment != 1){
                            resetButtonTint();
                            hubBTN.animateEnter();
                            selectedFragment = 1;
                            changeSelectedColor();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //hubBTN.animateLeave();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return true;
            }
        });

        hubBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageImportDialog();
            }
        });

        menuFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMainMenuContainer();
            }
        });


        menu_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!menuDrawn){
                    menu_containerLP = menu_container.getLayoutParams();
                    menu_containerHeight = menu_container.getHeight();
                    dynamicMenuHeight = menu_containerHeight;
                    menuDrawn = true;
                }
            }
        });


        magicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        //Run Code
                        doMagic(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        //Stop Code
                        doMagic(false);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return true;
            }
        });

        connectAccountOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideInContainerThree(fragment_container_one);
                openSideFragment("AccountOne");
            }
        });


    }

    private void doMagic(boolean action){
        if(action){
            if(selectedFragment == 3){
                //Multidays is opened get All Visible Day Views

                RelationshipMapping relationshipMapping = new RelationshipMapping();
                List<RecyclerView.ViewHolder> visibleViewHolders = new ArrayList<>();

                MultiDaysLinearLayoutManager llm = walletMultiDaysFragment.getLlm();


                for(int i = llm.findFirstVisibleItemPosition(); i <= llm.findLastVisibleItemPosition(); i++){
                    //Log.i("Visible Element Loop", "Runs"+i);

                        RecyclerView.ViewHolder visibleViewHolder = walletMultiDaysFragment.getCashflow_recycler().findViewHolderForAdapterPosition(i);
                        visibleViewHolders.add(visibleViewHolder);
                }

                relationshipMapping.setVisibleDays(visibleViewHolders);
                relationshipMapping.mapRelationships();
            }
        }
        else{

            if(selectedFragment == 3){
                //Multidays is opened get All Visible Day Views

                RelationshipMapping relationshipMapping = new RelationshipMapping();
                List<RecyclerView.ViewHolder> visibleViewHolders = new ArrayList<>();

                MultiDaysLinearLayoutManager llm = walletMultiDaysFragment.getLlm();


                for(int i = llm.findFirstVisibleItemPosition(); i <= llm.findLastVisibleItemPosition(); i++){
                    //Log.i("Visible Element Loop", "Runs"+i);

                    RecyclerView.ViewHolder visibleViewHolder = walletMultiDaysFragment.getCashflow_recycler().findViewHolderForAdapterPosition(i);
                    visibleViewHolders.add(visibleViewHolder);
                }

                relationshipMapping.setVisibleDays(visibleViewHolders);
                relationshipMapping.clearMap();
            }

        }
    }



    boolean menuDrawn = false;
    private ViewGroup.LayoutParams menu_containerLP;
    int menu_containerHeight;

    private void changeSelectedColor(){

        switch(selectedFragment){
            case 1:
                //btn.setImageTintList(getResources().getColorStateList(R.color.button_healthNight));
                Log.i("Selected", ""+selectedFragment);
                break;
            case 2:
                //btn.setImageTintList(getResources().getColorStateList(R.color.button_bodyNight));
                Log.i("Selected", ""+selectedFragment);
                break;
            case 3:
                //btn.setImageTintList(getResources().getColorStateList(R.color.button_mindNight));
                Log.i("Selected", ""+selectedFragment);
                break;
            case 4:
                //btn.setImageTintList(getResources().getColorStateList(R.color.button_moneyNight));
                Log.i("Selected", ""+selectedFragment);
                break;
        }
        openFragment();
    }

    private void resetButtonTint(){
       /* walletBTN.unSelectMenu();
        goalsBTN.unSelectMenu();
        analysisBTN.unSelectMenu();
        hubBTN.unSelectMenu();
*/
    }

    public void transparentStatus() {
        //statusbarspace = findViewById(R.id.statusspace);

        //statusbarspace.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            //ContextCompat.getColor(this,R.color.white)

        }
    }

    public Fragment opendSideFragment = null;

    public void openSideFragment(String actionName){
        FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();
        switch(actionName){
            case "BudgetManagement":
                opendSideFragment = new BudgetManagementFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "FixedCosts":
                opendSideFragment = new FixedCostsFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "FixedIncome":
                opendSideFragment = new FixedIncomeFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "Subscriptions":
                opendSideFragment = new SubcriptionsFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "BankTransactions":
                opendSideFragment = new BankTransactionsFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "IncomeModule":
                // Open Income Fragment
                // opendSideFragment = new
                break;
            case "OutgoingModule":
                break;
            case "FixedModule":
                opendSideFragment = new FixedModuleFragment();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "BudgetModule":
                opendSideFragment = new BudgetModuleFragment(singletonClass.changeFragment.getData().get(0).toString());
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                break;
            case "AccountOne":
                //opendSideFragment = new KlarnaAccountOne();
                //ft.replace(fragment_container_three.getId(), opendSideFragment);
                //Check if there is an Account

                if(singletonClass.klarnaConsent != null){

                    //Leave Break if no Session should be Started
                    //break;
                }

                //If THERE IS NO CONSENT Then start Klarna Process to get the Consent from the User
                opendSideFragment = new KlarnaApp();
                ft.replace(fragment_container_three.getId(), opendSideFragment);
                singletonClass.getKlarna().getSessionController().startSession( new KlarnaResponseCallback() {
                    @Override
                    public void success() {
                        //Succesfully Started Klarna Session
                        //Now Start a Flow
                        singletonClass.getKlarna().getFlowsController().startBalanceFlow(new KlarnaResponseCallback() {
                            @Override
                            public void success() {
                                String client_token = singletonClass.getKlarna().getFlowsController().getFlowData().getData().getClient_token();
                                //Then Launch the Klarna APP
                                ((KlarnaApp)opendSideFragment).setClient_token(client_token);
                            }

                            @Override
                            public void error() {
                                Log.i("Error", "Something went Wrongs");

                            }
                        });


                    }

                    @Override
                    public void error() {

                        Log.i("Callback Works", "error");

                    }
                });
                break;
            default:
                break;

        }

        ft.commit();

    }

    private HealthFragment healthFragment;
    private PhysicalFragment physicalFragment;
    private FinancialFragment financialFragment;
    public WalletMultiDaysFragment walletMultiDaysFragment;
    public WalletTodayFragment walletTodayFragment;
    public WalletCalendarFragment walletCalendarFragment;
    private GoalsFragment goalsFragment;
    private CameraScanFragment cameraScanFragment;

    private void openFragment() {
        fragment_container_one.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch(selectedFragment){
            case 1:
                healthFragment = new HealthFragment();
                ft.replace(fragment_container_one.getId(), healthFragment);
                ft.addToBackStack(healthFragment.getClass().getSimpleName());

                break;
            case 2:
                goalsFragment = new GoalsFragment(this.getBaseContext());
                ft.replace(fragment_container_one.getId(), goalsFragment);
                ft.addToBackStack(fragment_container_one.getClass().getSimpleName());

                break;
            case 3:
                //overviewFragments();
                //Calendar Frame


                walletCalendarFragment = new WalletCalendarFragment();
                walletMultiDaysFragment = new WalletMultiDaysFragment(menuTop, fragment_container_one, menu_container, null, myDB);
                //financeCashflow.setSharedElementReturnTransition(new DetailsTransition());
                //financeCashflow.setExitTransition(new DetailsTransition());
                ft.replace(fragment_container_one.getId(), walletMultiDaysFragment);
                //ft.addToBackStack(walletMultiDaysFragment.getClass().getSimpleName());
                //Today Frame
                ft.replace(fragment_container_two.getId(), walletCalendarFragment);

                break;
            case 4:
                physicalFragment = new PhysicalFragment();
                ft.replace(fragment_container_one.getId(), physicalFragment);
                ft.addToBackStack(physicalFragment.getClass().getSimpleName());
                break;

            case 5:
                if(dayPicked){
                    walletMultiDaysFragment = new WalletMultiDaysFragment(menuTop, fragment_container_one, menu_container, selectedDay, myDB);

                }
                else{
                    walletMultiDaysFragment = new WalletMultiDaysFragment(menuTop, fragment_container_one, menu_container, null, myDB);

                }
                ft.replace(fragment_container_one.getId(), walletMultiDaysFragment);
                break;
        }

        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    boolean dayPicked = false;
    CalendarLayoutDay selectedDay;
    int scrollDist = 0;
    boolean scrollDirUp = true;
    int dynamicMenuHeight;
    float alpha = 1;
    public void resetScrollDist(){
        scrollDist = 0;
    }
    public void scrollHeightMenu(int scroll, boolean up){

        //Log.i("Scroll", ""+scroll);
        /*
        if(scrollDirUp != up){
            resetScrollDist();
            scrollDirUp = up;
        }

        scrollDist = scrollDist + scroll;

        if(Math.abs(scrollDist) < menu_containerHeight){

            //scrolling is within the menuRange

                if(dynamicMenuHeight <= menu_containerHeight){
                    //set the dynamic height

                    dynamicMenuHeight = dynamicMenuHeight + scroll;
                    if(dynamicMenuHeight > menu_containerHeight){dynamicMenuHeight = menu_containerHeight;}
                    if(dynamicMenuHeight < 0){dynamicMenuHeight = 0;}

                    alpha = (float)dynamicMenuHeight / menu_containerHeight;
                    menu_containerLP.height = dynamicMenuHeight;
                    menu_container.setLayoutParams(menu_containerLP);
                    menuFloatingBtn.setAlpha(1-alpha);
                    menu_line.setAlpha(alpha);

                }
        }*/

    }

    public boolean mainMenuVisible = true;
    public void toggleMainMenuContainer(){

        ValueAnimator va;
        if(mainMenuVisible){
            va = ValueAnimator.ofInt(dpToPx(100), 0);

        }
        else{
            va = ValueAnimator.ofInt(0, dpToPx(100));

        }

        va.setDuration(350);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animval = (int)valueAnimator.getAnimatedValue();

                menu_containerLP.height = animval;
                menu_container.setLayoutParams(menu_containerLP);
                alpha = 1 - ((float)animval/menu_containerHeight);
                menuFloatingBtn.setAlpha(alpha);
            }

        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if(mainMenuVisible){
                    mainMenuVisible = false;
                    menu_line.animate().alpha(0).setDuration(350);

                }
                else{
                    mainMenuVisible = true;
                    menu_line.animate().alpha(1).setDuration(350);

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        //va.start();

    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setStatusbarspace() {
        statusbarspace = findViewById(R.id.statusspace);
        //statusbarspace.setVisibility(View.VISIBLE);
        statusBarHeight = getStatusBarHeight();
        ConstraintLayout.LayoutParams sbsLP = (ConstraintLayout.LayoutParams) statusbarspace.getLayoutParams();
        sbsLP.height = statusBarHeight;
        //statusbarspace.setLayoutParams(sbsLP);
    }



    @Override
    public void onCashflowItemClicked(int position) {

        financialFragment.inputCashflowInPopup(position);
        financialFragment.cashflowID = position;
        financialFragment.toggleRemoveShow(true);
        financialFragment.toogleAnimateBlur(true);
    }


    //Scan Code

    FloatingActionButton btn_addImage;
    BorderRoundView btn_scanImageSnap;

    ImageView previewImage;
    Uri image_uri;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_GALLERY_REQUEST_CODE = 1000;
    private static final int IMAGE_CAMERA_REQUEST_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];
    private void showImageImportDialog() {
        //Items to display in Dialog
        String[] items = {" Camera " , " Gallery "};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //Set Title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //First Option Clicked

                    //Check Camera Permission
                    if(!checkCameraPermission()){
                        //Not allowed request it
                        requestCameraPermission();
                    }
                    else{
                        //permission granted
                        pickCamera();
                    }
                }
                if(i == 1){
                    //Second Option Clicked

                    if(!checkStoragePermission()){
                        //Not allowed, request it
                        requestStoragePermission();
                    }
                    else{
                        //permission granted
                        pickGallery();
                    }
                }

            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        // INtent to pick Image from Gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Set intent type to Image
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean resultStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return resultStorage;
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAMERA_REQUEST_CODE);
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultStorage;
    }

    // Handle permission request Results

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(this, "Permission to use Camera Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:

                if(grantResults.length > 0){

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted){
                        pickGallery();
                    }
                    else{
                        Toast.makeText(this, "Permission to open Gallary Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    //Handle Image Result

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                //Got Image From Camera, ready to crop
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) // Enable Image Guideline
                        .start(this);
            }
            if (requestCode == IMAGE_CAMERA_REQUEST_CODE) {
                //Got Image from Gallery now Crop It
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) // Enable Image Guideline
                        .start(this);

            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); // Get Image Uri
                previewImage.setImageURI(resultUri); //Set Image Uri;


                // Get Drawable Bitmap for Text Recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) previewImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(this.getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error with Text  Recognizer", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    int prevLineTop = 0;
                    List<RecognizedData> recognizedDataList = new ArrayList<>();
                    List<RecognizedCashflow> recognizedCashflowList = new ArrayList<>();


                    StringBuilder sb = new StringBuilder();
                    // get Text From Sb until there is no Text
                    for (int i = 0; i < items.size(); i++) {

                        TextBlock myItem = items.valueAt(i);

                        for(Text line: myItem.getComponents()){


                            int type = getTextType(line.getValue());

                            if(type == 1){
                                //TODO is Money Out code
                                RecognizedCashflow recognizedCashflow = new RecognizedCashflow(line, true);
                                recognizedCashflowList.add(recognizedCashflow);

                            }
                            else{
                                RecognizedData recognizedData = new RecognizedData(line, type);
                                recognizedDataList.add(recognizedData);
                            }





                            for(Text word: line.getComponents()){

                               // Log.i("Word", ""+word.getValue());
                            }

                        }
                        //Log.i("Line", " - " + myItem.getValue() + "-- get Position" + myItem.getBoundingBox().top);

                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }

                    sortDataByCashflows(recognizedCashflowList, recognizedDataList);

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // SHow if there is any Error
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

            }
        }
    }

    String currency = "empty";
    boolean currencyIdentified = false;

    private int getTextType(String text){
        int type = 0;
        // 1 = Money, 0 = Name, 2 = Date, 3 = Time, 4 = IBAN, 5 = BIC
        findCurrency(text);
        if(isMoney(text)){
            isMoneyOut(text);
            return 1;
        }
        if(isDate(text)){
            return 2;
        }
        if(isTime(text)){
            return 3;
        }
        if(isIBAN(text)){
            return 4;
        }
        if(isBIC(text)){
            return 5;
        }

        return 0;
    }

    private boolean isTime(String text) {
        String[] timeFormats = {"HH:mm"};
        SimpleDateFormat format;
        boolean isTime = false;

        for(String timeFormat : timeFormats){
            format = new SimpleDateFormat(timeFormat);
            try {
                format.parse(text);
                return true;
            }
            catch(ParseException e){
                isTime = false;
            }
        }
        return isTime;

    }

    private boolean isBIC(String text) {
        return false;
    }

    private boolean isIBAN(String text) {
        return false;
    }

    private boolean isDate(String text) {
        String[] dateFormats = {"yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy","yyyy.MM.dd", "dd.MM.yyyy", "MM.dd.yyyy","yyyy/MM/dd", "dd/MM/yyyy", "MM/dd/yyyy"};
        SimpleDateFormat format;
        boolean isDate = false;

        for(String dateFormat : dateFormats){
            format = new SimpleDateFormat(dateFormat);
            try {
                format.parse(text);
                return true;
            }
            catch(ParseException e){
                isDate = false;
            }
        }
        return isDate;

    }

    boolean isMoneyOut = false;
    private void isMoneyOut(String text) {
        String regex = "/s*?-";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        isMoneyOut = matcher.matches();
    }

    private boolean isMoney(String text) {
        String regex = "-?([0-9]{1,3}[.,]([0-9]{3}[.,])*[0-9]{3}|[0-9]+)([.,][0-9][0-9])?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);


    return matcher.matches();
    }

    private void findCurrency(String word){
        //Check for Euro
        String[] euroAbrev = {"Euro", "euro", "€", " eur"};
        if(!currencyIdentified){
            if(containsWords(word, euroAbrev)){
                currency = "euro";
                currencyIdentified = true;
            }
        }

    }

    public static boolean containsWords(String inputString, String[] items) {

        for (String item : items) {
            if (inputString.contains(item)) {
                return true;
            }
        }
        return false;
    }

    private void sortDataByCashflows(List<RecognizedCashflow> cashflowList, List<RecognizedData> dataList){
        int dataTop, dataBottom, nextCashflowTop, currentCashflowTop;
        int margin = 10;


        for(RecognizedData data: dataList){
            dataTop = data.getText().getBoundingBox().top;
            dataBottom = data.getText().getBoundingBox().bottom;

            for(int i = 0; i < cashflowList.size(); i++){

                currentCashflowTop = cashflowList.get(i).getCash().getBoundingBox().top;

                if(dataBottom > currentCashflowTop){
                    if(i+1 < cashflowList.size()){
                        nextCashflowTop = cashflowList.get(i + 1).getCash().getBoundingBox().top;
                        // Not the last Cashflow therefore check next Cashflow for Bounds
                        if(dataTop < nextCashflowTop){
                            //the Data belongs to this Cashflow enter it
                            cashflowList.get(i).getRecognizedDataList().add(data);
                            break;
                        }
                    }
                    else{
                        // it is the Last Cashflow therefore goes in
                        //TODO data might not belong to the last Cashflow - account for that
                        cashflowList.get(i).getRecognizedDataList().add(data);
                        break;
                    }
                }
            }
        }


        for(RecognizedCashflow cashflow: cashflowList){

            for(RecognizedData data: cashflow.getRecognizedDataList()){



            }
        }

    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void checkBackstack(){



    }


    @Override
    public void calendarDaySelected(CalendarLayoutDay selectedDay) {

        //Log.i("SelectedDay", ""+selectedDay.getMONTH_NAME() + " " +selectedDay.getMONTH_DAY_NUMBER());

        //openFragment();
        if(selectedFragment != 5){
            selectedFragment = 5;

            dayPicked = true;
            this.selectedDay = selectedDay;
            openFragment();

        }

        autoSlide(true, 300);
    }


    public void autoSlide(final boolean open, int duration){

        ValueAnimator va;


        if(open){
            va = ValueAnimator.ofInt(displayWidth, 0);
        }
        else{

            va = ValueAnimator.ofInt(0, displayWidth);
        }

        va.setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int animval = (int)valueAnimator.getAnimatedValue();

                slideMechanism(Math.abs(animval), open);
            }

        });

        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                //empty Fragment and Show Progress Wheel

            }

            @Override
            public void onAnimationEnd(Animator animator) {



            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        va.start();

    }

    @Override
    public void saveNewWalletEvent(WalletEventModel data, int pos) {

        //Log.i("Is Running", "YEEEEEES");

        long id = myDB.WalletEvent.newCashflow(data);

        if(id > 0){
            WalletEventModel savedRow = myDB.WalletEvent.getCashflow((int)id);

            walletMultiDaysFragment.enterNewDataEntry(savedRow, pos);

        }
        else{
            Toast.makeText(this, "Couuldnt store the data", Toast.LENGTH_SHORT).show();
        }


    }
}
class RecognizedData{

    Text text;
    int dataType;

    public RecognizedData(Text text, int dataType) {
        setText(text);
        setDataType(dataType);
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}

class RecognizedCashflow{
    Text cash;
    boolean incoming;
    List<RecognizedData> recognizedDataList = new ArrayList<>();

    public RecognizedCashflow(Text cash, boolean incoming) {
        setCash(cash);
        setIncoming(incoming);
    }

    public Text getCash() {
        return cash;
    }

    public void setCash(Text cash) {
        this.cash = cash;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public List<RecognizedData> getRecognizedDataList() {
        return recognizedDataList;
    }

    public void setRecognizedDataList(List<RecognizedData> recognizedDataList) {
        this.recognizedDataList = recognizedDataList;
    }

}