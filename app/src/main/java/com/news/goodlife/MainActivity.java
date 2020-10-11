package com.news.goodlife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.CustomViews.CustomIcons.HubIcon;
import com.news.goodlife.CustomViews.ElasticEdgeView;
import com.news.goodlife.Interfaces.OnClickedCashflowItemListener;
import com.news.goodlife.Tools.CameraScan.CameraScanFragment;
import com.news.goodlife.fragments.CashflowTimelineFragment;
import com.news.goodlife.fragments.FinanceCashflow;
import com.news.goodlife.fragments.FinancialFragment;
import com.news.goodlife.fragments.FinancialFragmentOverview;
import com.news.goodlife.fragments.HealthFragment;
import com.news.goodlife.fragments.MentalFragment;
import com.news.goodlife.fragments.PhysicalFragment;
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

public class MainActivity extends AppCompatActivity implements OnClickedCashflowItemListener {

    String appName = "My Finances";
    TextView btn_health;
    TextView btn_physical;
    TextView btn_mental;
    TextView btn_financial;
    TextView app_titleTV;
    TextView btn_scanCamera;
    FrameLayout fragment_container;

    private Fragment fragment;
    int selectedFragment = 0;
    boolean DarkMode = false;

    //Statusbar Replacement
    int statusBarHeight;
    FrameLayout statusbarspace;

    BlurView blurViewMenu;
    TextView raffa;

    ElasticEdgeView elasticEdgeView;
    ConstraintLayout mainContainer;


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
        btn_scanCamera = findViewById(R.id.button_camerascan);
        fragment_container = findViewById(R.id.fragment_container);
        statusbarspace = findViewById(R.id.statusspace);
        app_titleTV = findViewById(R.id.app_title);
        blurViewMenu = findViewById(R.id.menu_blur_container);
        elasticEdgeView = findViewById(R.id.elasticEdge);
        mainContainer = findViewById(R.id.main_container);

        //Recognize Image
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        previewImage = findViewById(R.id.previewImage);


        DarkMode = true;

        listeners();
        blur(15f);
        
        loadTools();
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
        btn_scanCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageImportDialog();
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
    private CameraScanFragment cameraScanFragment;

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
        Log.i("ResultCode", "" + resultCode);
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
                            Log.i("Line", " - " + line.getValue() + " TextType = "+ type +" - Top: " + line.getBoundingBox().top);
                            if(type == 1){
                                //TODO is Money Out code
                                RecognizedCashflow recognizedCashflow = new RecognizedCashflow(line, true);
                                recognizedCashflowList.add(recognizedCashflow);

                            }
                            else{
                                RecognizedData recognizedData = new RecognizedData(line, type);
                                recognizedDataList.add(recognizedData);
                            }


                            Log.i("Line", " - " + line.getValue() + " TextType = "+ type +" - Top: " + line.getBoundingBox().top);



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
        Log.i("RecognizedCashflows_B", " - "+cashflowList.get(1).getRecognizedDataList().size());

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

        Log.i("Show Cashflow Results", " -----------------------------------------------------");
        for(RecognizedCashflow cashflow: cashflowList){

            for(RecognizedData data: cashflow.getRecognizedDataList()){

                    Log.i("CashflowData", "Cash = "+cashflow.getCash().getValue()+" ; Date = "+data.getText().getValue());

            }
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