package com.news.goodlife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.animation.Animator;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.news.goodlife.CustomViews.CustomIcons.MenuIcons;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.Fragments.WalletCalendarFragment;
import com.news.goodlife.Fragments.WalletTodayFragment;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.Interfaces.CalendarSelectDayListener;
import com.news.goodlife.Tools.CameraScan.CameraScanFragment;
import com.news.goodlife.Fragments.WalletMultiDaysFragment;
import com.news.goodlife.Fragments.FinancialFragment;
import com.news.goodlife.Fragments.GoalsFragment;
import com.news.goodlife.Fragments.HealthFragment;
import com.news.goodlife.Fragments.PhysicalFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class StartActivity extends AppCompatActivity implements OnClickedCashflowItemListener, CalendarSelectDayListener {



    public FrameLayout fragment_container_one;
    public FrameLayout fragment_container_two;

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
    MenuIcons walletBTN, goalsBTN, analysisBTN, hubBTN;

    //Menu Navigation
    View menuFloatingBtn;

    //Metrics
    int displayWidth, displayHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatus();
        setStatusbarspace();


        walletBTN = findViewById(R.id.buttonWallet);
        goalsBTN = findViewById(R.id.buttonGoals);
        analysisBTN = findViewById(R.id.buttonAnalysis);
        hubBTN = findViewById(R.id.buttonHub);



        fragment_container_one = findViewById(R.id.fragment_container_one);
        fragment_container_two = findViewById(R.id.fragment_container_two);
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


        if (getResources().getBoolean(R.bool.dark)) {
            Log.i("Mode", "Night");
        } else {
            // Do night stuff here
            Log.i("Mode", "Day");
        }


        //Navigation Section


        //Overview
        overviewTodayTitle = findViewById(R.id.wallet_overview_today_title);
        overviewCalendarTitle = findViewById(R.id.wallet_overview_calendar_title);

        vibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //open Wallet First
        selectedFragment = 3;
        changeSelectedColor();

        DarkMode = true;

        listeners();
        //blur(15f);
        
        loadTools();

        fragment_container_one.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                displayWidth = fragment_container_one.getWidth();
                displayHeight = fragment_container_one.getHeight();
                fragment_container_two.setX(displayWidth);

                //overviewFragments();

                slideMechanism(0, true);

                fragment_container_one.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

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
                    vibrator.vibrate(20);
                    vibratedSlideIn = true;
                }
            }
        }
        else{
            slideProgess = Math.abs((float)move / displayWidth);
            if(slideProgess > .3f){
                if(!vibratedSlideIn){
                    vibrator.vibrate(20);
                    vibratedSlideIn = true;
                }
            }
        }

        moveMenuButtonIndicator(slideProgess);

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

    private void moveMenuButtonIndicator(float perc){

        if(selectedFragment == 3 || selectedFragment == 5){
           // Wallet is selected
            walletBTN.moveIndicator(perc);
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
                walletTodayFragment.resetIndicator(displayWidth - dpToPx(8));

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


    boolean overviewWallet = false;
    int overviewFragTwoX;
    //Overview Fragments
    public void overviewFragments(){
        overviewWallet = true;

        float scale = 0.45f;
        int centerScaledWidth = (int)(displayWidth * .5)/2;

        setScaleNavigation(scale,scale);

        //overviewFragTwoX = (int)(displayWidth /2.5);
        //setSlideWidth(overviewFragTwoX, true);

        fragment_container_one.setX(-centerScaledWidth);
        fragment_container_two.setX(centerScaledWidth);

        overviewCalendarTitle.setVisibility(View.VISIBLE);
        overviewTodayTitle.setVisibility(View.VISIBLE);

        overviewTodayTitle.setX(-centerScaledWidth);
        overviewTodayTitle.setY(displayHeight/4 - dpToPx(10));
        overviewCalendarTitle.setX(centerScaledWidth);
        overviewCalendarTitle.setY(displayHeight/4 - dpToPx(10));


    }

    public void setScaleNavigation(float fragOneScale, float fragTwoScale){

        fragment_container_one.setScaleX(fragOneScale);
        fragment_container_one.setScaleY(fragOneScale);

        fragment_container_two.setScaleX(fragTwoScale);
        fragment_container_two.setScaleY(fragTwoScale);

    }

    public void setSlideWidth(int move, boolean open){
        if(open){
            fragment_container_two.setX(displayWidth - move);
        }
        else{
            fragment_container_two.setX(move);
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

        blurViewMenu.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }

    int menuTop = 0;
    private void listeners(){


        walletBTN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                resetButtonTint();
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        walletBTN.animateEnter();
                        break;
                    case MotionEvent.ACTION_UP:
                        walletBTN.animateLeave();
                        selectedFragment = 3;
                        changeSelectedColor();
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
                resetButtonTint();
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        goalsBTN.animateEnter();
                        break;
                    case MotionEvent.ACTION_UP:
                        goalsBTN.animateLeave();
                        selectedFragment = 2;
                        changeSelectedColor();
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
                resetButtonTint();
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        analysisBTN.animateEnter();
                        break;
                    case MotionEvent.ACTION_UP:
                        analysisBTN.animateLeave();
                        selectedFragment = 3;
                        changeSelectedColor();
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

                resetButtonTint();
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        hubBTN.animateEnter();
                        break;
                    case MotionEvent.ACTION_UP:
                        hubBTN.animateLeave();
                        selectedFragment = 1;
                        changeSelectedColor();
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


    }

    boolean menuDrawn = false;
    private ViewGroup.LayoutParams menu_containerLP;
    int menu_containerHeight;

    private void changeSelectedColor(){
        resetButtonTint();
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
        walletBTN.unSelectMenu();
        goalsBTN.unSelectMenu();
        analysisBTN.unSelectMenu();
        hubBTN.unSelectMenu();

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
                walletTodayFragment = new WalletTodayFragment();
                //financeCashflow.setSharedElementReturnTransition(new DetailsTransition());
                //financeCashflow.setExitTransition(new DetailsTransition());
                ft.replace(fragment_container_one.getId(), walletTodayFragment);
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
                walletMultiDaysFragment = new WalletMultiDaysFragment(menuTop, fragment_container_one, menu_container);
                ft.replace(fragment_container_one.getId(), walletMultiDaysFragment);
                break;
        }

        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

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
        Log.i("Clicked OOOOOO", ""+position);
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
    public void calendarDaySelected(boolean test) {

        //openFragment();
        if(selectedFragment != 5){
            selectedFragment = 5;
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