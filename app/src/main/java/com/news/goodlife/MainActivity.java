package com.news.goodlife;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.news.goodlife.CustomViews.CustomIcons.HubIcon;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.fragments.CashflowTimelineFragment;
import com.news.goodlife.fragments.FinanceCashflow;
import com.news.goodlife.fragments.FinancialFragment;
import com.news.goodlife.fragments.FinancialFragmentOverview;
import com.news.goodlife.fragments.HealthFragment;
import com.news.goodlife.fragments.MentalFragment;
import com.news.goodlife.fragments.PhysicalFragment;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity implements OnClickedCashflowItemListener {

    String appName = "My Finances";
    TextView btn_health;
    TextView btn_physical;
    TextView btn_mental;
    TextView btn_financial;
    TextView app_titleTV;
    FrameLayout fragment_container;

    private Fragment fragment;
    int selectedFragment = 0;
    boolean DarkMode = false;

    //Statusbar Replacement
    int statusBarHeight;
    FrameLayout statusbarspace;

    BlurView blurViewMenu;
    TextView raffa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatus();
        setStatusbarspace();

        btn_health = findViewById(R.id.button_all);
        btn_physical = findViewById(R.id.button_body);
        btn_mental = findViewById(R.id.button_mind);
        btn_financial = findViewById(R.id.button_money);
        fragment_container = findViewById(R.id.fragment_container);
        statusbarspace = findViewById(R.id.statusspace);
        app_titleTV = findViewById(R.id.app_title);
        blurViewMenu = findViewById(R.id.menu_blur_container);

        DarkMode = true;

        listeners();
        blur(15f);
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
        blurViewMenu.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                menuTop = blurViewMenu.getHeight();

            }
        });

        btn_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = 1;
                //btn_health.animateSpin();
                changeSelectedColor();

            }
        });

        btn_physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = 2;
                changeSelectedColor();

            }
        });

        btn_mental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = 3;
                changeSelectedColor();

            }
        });


        btn_financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = 4;
                changeSelectedColor();

            }
        });

    }


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
    private FinanceCashflow financeCashflow;
    private FinancialFragmentOverview financialFragmentOverview;

    private void openFragment() {
        fragment_container.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch(selectedFragment){
            case 1:
                healthFragment = new HealthFragment();
                ft.replace(fragment_container.getId(), healthFragment);
                app_titleTV.setText("My Hub");
                app_titleTV.setVisibility(View.GONE);
                break;
            case 2:
                financialFragmentOverview = new FinancialFragmentOverview(this.getBaseContext());
                ft.replace(fragment_container.getId(), financialFragmentOverview);
                app_titleTV.setText(appName);
                app_titleTV.setVisibility(View.VISIBLE);
                break;
            case 3:
                financeCashflow = new FinanceCashflow(menuTop);
                ft.replace(fragment_container.getId(), financeCashflow);
                app_titleTV.setText(appName);
                app_titleTV.setVisibility(View.GONE);
                break;
            case 4:
                physicalFragment = new PhysicalFragment();
                ft.replace(fragment_container.getId(), physicalFragment);
                app_titleTV.setText(appName);
                app_titleTV.setVisibility(View.VISIBLE);
                break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
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
        statusbarspace.setVisibility(View.VISIBLE);
        statusBarHeight = getStatusBarHeight();
        ConstraintLayout.LayoutParams sbsLP = (ConstraintLayout.LayoutParams) statusbarspace.getLayoutParams();
        sbsLP.height = statusBarHeight;
        statusbarspace.setLayoutParams(sbsLP);
    }

    @Override
    public void onCashflowItemClicked(int position) {
        Log.i("Clicked OOOOOO", ""+position);
        financialFragment.inputCashflowInPopup(position);
        financialFragment.cashflowID = position;
        financialFragment.toggleRemoveShow(true);
        financialFragment.toogleAnimateBlur(true);
    }
}