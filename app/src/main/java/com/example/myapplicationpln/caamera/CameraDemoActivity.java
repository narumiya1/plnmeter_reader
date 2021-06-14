package com.example.myapplicationpln.caamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.CameraVal;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GCameraValue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraDemoActivity extends Activity implements SurfaceHolder.Callback,
        View.OnClickListener {

    private static final String TAG = CameraDemoActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button flipCamera;
    private Button flashCameraButton;
    private Button captureImage;
    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    private ImageView imageVew;
    private File imageFile;
    CameraVal cameraVal = new CameraVal();
    SessionPrefference sessionPrefference;
    DatabaseReference databaseReferenceCameraWidthHeight;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerademo_activity);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            askPerms();
        }
        // camera surface view created
        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        flipCamera = (Button) findViewById(R.id.flipCamera);
        flashCameraButton = (Button) findViewById(R.id.flash);
        captureImage = (Button) findViewById(R.id.captureImage);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        imageVew = findViewById(R.id.imageVew);

        surfaceHolder.addCallback(this);
        flipCamera.setOnClickListener(this);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Camera.getNumberOfCameras() > 1) {
            flipCamera.setVisibility(View.VISIBLE);
        }
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCameraButton.setVisibility(View.GONE);
        }
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();

        sessionPrefference = new SessionPrefference(this);
        databaseReferenceCameraWidthHeight = FirebaseDatabase.getInstance().getReference().child("CameraVal").child(sessionPrefference.getUserId());
        databaseReferenceCameraWidthHeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cameraVal = snapshot.getValue(CameraVal.class);
                    Log.d("DATA getHeight", "onDataChange: " + cameraVal.getX());
                    Log.d("DATA getWidth", "onDataChange: " + cameraVal.getY());

                    databaseReferenceCameraWidthHeight = FirebaseDatabase.getInstance().getReference().child("CameraVal");

                    if (cameraVal.getY().equals(0) && cameraVal.getX().equals(0)){
                        imageVew.getLayoutParams().width = 400;
                        imageVew.getLayoutParams().height = 200;
                        int x = 250;
                        imageVew.setX(x);
                        int y = 250;
                        imageVew.setX(y);
                        String setX = String.valueOf(x);
                        String setY = String.valueOf(y);
                        String id = String.valueOf(1);
                        String h = String.valueOf( imageVew.getLayoutParams().width = 400);
                        String w = String.valueOf(imageVew.getLayoutParams().height = 200);
                        CameraVal cameraVal = new CameraVal(id, sessionPrefference.getUserId(), sessionPrefference.getPhone(), w, h, setX, setY);
                        databaseReferenceCameraWidthHeight.child(sessionPrefference.getUserId()).setValue(cameraVal);
                        GCameraValue gCameraValue = new GCameraValue();
                        gCameraValue.setId_user(sessionPrefference.getUserId());
                        gCameraValue.setUser_phone(sessionPrefference.getPhone());
                        gCameraValue.setHeight(Integer.parseInt(h));
                        gCameraValue.setWidth(Integer.parseInt(w));
                        int rowCamType = db.gHistorySpinnerDao().getCountCamera();
                        if (rowCamType == 0) {
                            gCameraValue.setId_user(sessionPrefference.getUserId());
                            gCameraValue.setUser_phone(sessionPrefference.getPhone());
                            gCameraValue.setHeight(Integer.parseInt(h));
                            gCameraValue.setWidth(Integer.parseInt(w));
                            gCameraValue.setId(1);
                            gCameraValue.setType(1);
                            insertData(gCameraValue);
                        } else {
                            gCameraValue.setId_user(sessionPrefference.getUserId());
                            gCameraValue.setUser_phone(sessionPrefference.getPhone());
                            gCameraValue.setHeight(Integer.parseInt(h));
                            gCameraValue.setWidth(Integer.parseInt(w));
                            gCameraValue.setId(1);
                            gCameraValue.setType(1);
                            updateCamValue(gCameraValue);

                        }
                    }else {
                        imageVew.getLayoutParams().height = Integer.parseInt(cameraVal.getHeight());
                        imageVew.getLayoutParams().width = Integer.parseInt(cameraVal.getWidth());
                        imageVew.setX(Float.parseFloat(cameraVal.getX()));
                        imageVew.setX(Float.parseFloat(cameraVal.getX()));
                        imageVew.requestLayout();

                        String h = String.valueOf(cameraVal.getHeight());
                        String w = String.valueOf(cameraVal.getWidth());
                        String setX = String.valueOf(cameraVal.getX());
                        String setY = String.valueOf(cameraVal.getY());
                        String id = String.valueOf(1);

                        CameraVal cameraVal = new CameraVal(id, sessionPrefference.getUserId(), sessionPrefference.getPhone(), w, h, setX, setY);
                        databaseReferenceCameraWidthHeight.child(sessionPrefference.getUserId()).setValue(cameraVal);

                        GCameraValue gCameraValue = new GCameraValue();
                        gCameraValue.setId_user(sessionPrefference.getUserId());
                        gCameraValue.setUser_phone(sessionPrefference.getPhone());
                        gCameraValue.setHeight(Integer.parseInt(h));
                        gCameraValue.setWidth(Integer.parseInt(w));
                        int rowCamType = db.gHistorySpinnerDao().getCountCamera();
                        if (rowCamType == 0) {
                            gCameraValue.setId_user(sessionPrefference.getUserId());
                            gCameraValue.setUser_phone(sessionPrefference.getPhone());
                            gCameraValue.setHeight(Integer.parseInt(h));
                            gCameraValue.setWidth(Integer.parseInt(w));
                            gCameraValue.setId(1);
                            gCameraValue.setType(1);
                            insertData(gCameraValue);
                        } else {
                            gCameraValue.setId_user(sessionPrefference.getUserId());
                            gCameraValue.setUser_phone(sessionPrefference.getPhone());
                            gCameraValue.setHeight(Integer.parseInt(h));
                            gCameraValue.setWidth(Integer.parseInt(w));
                            gCameraValue.setId(1);
                            gCameraValue.setType(1);
                            updateCamValue(gCameraValue);

                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void insertData(GCameraValue gCameraValue) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertCamera(gCameraValue);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " + status);
            }
        }.execute();
    }
    private void updateCamValue(GCameraValue gCameraValue) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateCameraValue(gCameraValue);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " +status);
            }
        }.execute();
    }

    private void askPerms() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission Granted");
        } else {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog();
        }

    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();

        showFlashButton(params);

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    private void showFlashButton(Camera.Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

        flashCameraButton.setVisibility(showFlash ? View.VISIBLE
                : View.INVISIBLE);

    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash:
                flashOnButton();
                break;
            case R.id.flipCamera:
                flipCamera();
                break;
            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void takeImage() {
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    /*
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "IMAGE_" + timeStamp + "_";
                    boolean isEmulated = Environment.isExternalStorageEmulated();
                    File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                    File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
                    imageFileName = image.getAbsolutePath();
                    */

                    //String path = String.valueOf(new SavePhotoAsync().execute(data));
                    String path = new SavePhotoAsync().execute(data).get();
                    Log.d(TAG, "Path:\t" + path);

                    Intent camIntent = new Intent(getBaseContext(), PreviewActivity.class);
                    camIntent.putExtra("camera_img", path);
//                    camIntent.putExtra("camera_image_folder", imageFileName);
//                    Log.d(TAG, "camera_image_folder path :\t" + imageFileName);

                    startActivity(camIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class SavePhotoAsync extends AsyncTask<byte[], Void, String> {

        private File imageFile;

        @Override
        protected String doInBackground(byte[]... bytes) {
            // convert byte array into bitmap
            Bitmap loadedImage = null;
            Bitmap rotatedBitmap = null;
            loadedImage = BitmapFactory.decodeByteArray(bytes[0], 0,
                    bytes[0].length);

            // rotate Image
            Matrix rotateMatrix = new Matrix();
            rotateMatrix.postRotate(rotation);
            rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                    loadedImage.getWidth(), loadedImage.getHeight(),
                    rotateMatrix, false);
            String state = Environment.getExternalStorageState();
            File folder = null;
            if (state.contains(Environment.MEDIA_MOUNTED)) {
                folder = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CameraDm");
            } else {
                folder = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CameraDm");
            }

            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFile = new File(folder.getAbsolutePath()
                        + File.separator
                        + timeStamp
                        + ".jpg");
                Log.d(TAG, "imageFilez:\t" + imageFile);
                Log.d(TAG, "imageFilez:\t\t" + timeStamp);

                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Image Saved",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Image Not saved",
                                Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);

            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(imageFile);
                fout.write(ostream.toByteArray());
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN,
                    System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA,
                    imageFile.getAbsolutePath());

            CameraDemoActivity.this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return imageFile.getAbsolutePath();
        }

    }

    private void flipCamera() {
        int id = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(CameraDemoActivity.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private AlertDialog.Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.drawable.ic_flash_on);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }

    private void flashOnButton() {
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH
                        : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {

            }

        }
    }

}