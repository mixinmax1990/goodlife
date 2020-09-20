package com.news.goodlife.Tools.CameraScan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.goodlife.CustomViews.CustomEntries.BorderRoundView;
import com.news.goodlife.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class CameraScanFragment extends Fragment {


    public CameraScanFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tools_camerascan, container, false);

        btn_addImage = root.findViewById(R.id.btn_scanImage_addImage);
        btn_scanImageSnap = root.findViewById(R.id.btn_scanImage_Snap);
        previewImage = root.findViewById(R.id.scanImage_previewImage);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        listeners();
        return root;
    }

    private void listeners() {
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load Dialog to select image
                showImageImportDialog();
            }
        });

        btn_scanImageSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
        getActivity().startActivityForResult(intent, IMAGE_GALLERY_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean resultStorage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return resultStorage;
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        getActivity().startActivityForResult(cameraIntent, IMAGE_CAMERA_REQUEST_CODE);
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(getActivity(), cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultStorage = ContextCompat.checkSelfPermission(getContext(),
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
                        Toast.makeText(getContext(), "Permission to use Camera Denied", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Permission to open Gallary Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    //Handle Image Result

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.i("ResultCode", ""+resultCode);
        if(resultCode == RESULT_OK){


            if(requestCode == IMAGE_GALLERY_REQUEST_CODE){
                //Got Image From Camera, ready to crop
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) // Enable Image Guideline
                        .start(getActivity());
            }
            if(requestCode == IMAGE_CAMERA_REQUEST_CODE){
                //Got Image from Gallery now Crop It
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) // Enable Image Guideline
                        .start(getActivity());

            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri(); // Get Image Uri
                previewImage.setImageURI(resultUri); //Set Image Uri;


                // Get Drawable Bitmap for Text Recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) previewImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

                if(!recognizer.isOperational()){
                    Toast.makeText(getContext(), "Error with Text  Recognizer", Toast.LENGTH_SHORT).show();
                }
                else{
                    Frame frame =  new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    // get Text From Sb until there is no Text
                    for(int i = 0; i < items.size(); i++){
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }

                    Log.i("Recognized Text", ""+sb.toString());
                }

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                // SHow if there is any Error
                Exception error = result.getError();
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
