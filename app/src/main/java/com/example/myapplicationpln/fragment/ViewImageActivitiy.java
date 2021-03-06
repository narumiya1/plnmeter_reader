package com.example.myapplicationpln.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.R;

import java.io.File;

public class ViewImageActivitiy extends AppCompatActivity {
    private static final String IMAGE_FILE_LOCATION = "image_file_location";
    private static final String IMAGE_SAVED_PATH = "imagePath";
    private static final String TAG = ViewImageActivitiy.class.getSimpleName();
    private File imageFile;
    private ImageView imageView;
    private ImageView saveBtn;
    private ImageView closeBtn;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if(hasFocus){
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        imageView = (ImageView) findViewById(R.id.img_view);
        closeBtn = (ImageView) findViewById(R.id.img_close_btn);
        saveBtn = (ImageView) findViewById(R.id.img_save_btn);

        imageFile = new File(
                getIntent().getStringExtra(IMAGE_FILE_LOCATION)
        );

        SingleImageBitmapWorkerTask workerTask = new SingleImageBitmapWorkerTask(imageView, width, height);
        workerTask.execute(imageFile);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                boolean deleted = imageFile.delete();
                if(deleted){
                    MediaScannerConnection.scanFile(getApplicationContext(),
                            new String[]{getIntent().getStringExtra(IMAGE_FILE_LOCATION)}, null, null);
                    setResult(Activity.RESULT_CANCELED);
                    ViewImageActivitiy.super.finish();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(IMAGE_SAVED_PATH, imageFile.getAbsolutePath());
//                Log.d("getIntens img IMAGE_SAVED_PATH "," "+imageFile.getAbsolutePath());
                Bundle bundle = new Bundle();
                bundle.putString("edttext", imageFile.getAbsolutePath());
                // set Fragmentclass Arguments
                HomeMenuFragment fragobj = new HomeMenuFragment();
                fragobj.setArguments(bundle);
                Log.d("getIntens setbundle IMAGE_SAVED_PATH "," send data froma activite "+imageFile.getAbsolutePath());
                setResult(Activity.RESULT_OK, resultIntent);
                ViewImageActivitiy.super.finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}