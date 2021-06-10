package com.example.myapplicationpln.caamera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.cookies.AddCookiesInterceptor;
import com.example.myapplicationpln.cookies.ReceivedCookiesInterceptor;
import com.example.myapplicationpln.cookies.TokenInterceptor;
import com.example.myapplicationpln.model.History;
import com.example.myapplicationpln.network_retrofit.ApiClient;
import com.example.myapplicationpln.network_retrofit.PLNData;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Gimage;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PreviewActivity.class.getSimpleName();

    private ImageView previewImage;
    private String imageFilePath;
    private TextView hasilmetergenereate;
    private Button button;
    SessionPrefference sessionPrefference;
    DatabaseReference databaseReferenceHistory;
    DatabaseReference mDatabaseRefApiMeter;
    long maxIdHistory;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        sessionPrefference = new SessionPrefference(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();
        DatabaseReference mDatabaseRefs = database.getReference();
        mDatabaseRefApiMeter = database.getReference();
        databaseReferenceHistory = FirebaseDatabase.getInstance().getReference().child("History");
        DatabaseReference referenceHistory = FirebaseDatabase.getInstance().getReference();
        databaseReferenceHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxIdHistory = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();


        Bundle bundle = getIntent().getExtras();
//        imageFilePath = bundle.getString("camera_image_folder");
        imageFilePath = bundle.getString("camera_img");
        Log.d(TAG, "Image File Path:\t" + imageFilePath);
//        Log.d(TAG, "camera_image_folder File Path:\t" + imageFilePath);

        previewImage = (ImageView) findViewById(R.id.previewImage);
        hasilmetergenereate = findViewById(R.id.hasilmetergenereate);
        button = findViewById(R.id.generate);
        button.setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);


//        Bitmap bitmap = BitmapFactory.decodeByteArray(camera, 0, camera.length);
//
        if (bitmap != null) {
            previewImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        File file = new File(imageFilePath);
        Log.d(TAG, "File from path:\t" + file);

        if (file.exists()) {
            file.delete();
            Log.d(TAG, "File Deleted");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate:
                Log.d(TAG, "ONCLIcK Image File Path:\t" + imageFilePath);
//                Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
//                startActivity(intent);
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(imageFilePath,
                            bitmapOptions);

                    rotateImage(setReducedImageSize());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                rotateImage(setReducedImageSize());
                break;
            case R.id.previewImage:
                Log.d(TAG, "ONCLIcK Image File Path:\t" + imageFilePath);
                Toast.makeText(getApplicationContext(), "btn onclick", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        previewImage.setImageBitmap(rotateBitmap);
        uploadImage(rotateBitmap, imageFilePath);

    }
    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = previewImage.getWidth();
        int targetImageViewHeight = previewImage.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        //Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        //mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmap);
        Bitmap compressedOri = BitmapFactory.decodeFile(imageFilePath, bmOptions);

        Bitmap compressed = null;
        //save bitmap to byte array

        try {
            File file = new File(imageFilePath);
            int length = (int) file.length();
            Log.d("Body length", "String length : "+length);
            FileOutputStream fOut = new FileOutputStream(file);
            compressedOri.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedOri;
    }

    private void uploadImage(Bitmap rotateBitmap, String imageFilePath) {
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new SessionPrefference(getApplicationContext()).getKeyApiJwt();
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        String jwt = "";
        if (jwtKey.equals(jwt)){
            sessionPrefference.setIsLogin(false);
            sessionPrefference.logoutUser();
        }
        File file = new File(imageFilePath);
        int length = (int) file.length();
        Log.d("Upload length respons", "String respons length : " +length);
        if (length>50000){

        }
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        try {
            Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);

            HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
            loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

            TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtKey);

            OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor(getApplicationContext()))
                    .addInterceptor(new ReceivedCookiesInterceptor(getApplicationContext()))
                    .addInterceptor(loggingInterceptor2)
                    .addInterceptor(tokenInterceptor)
                    .build();

            Gson gson2 = new GsonBuilder().serializeNulls().create();

            //Retrofit retrofit = NetworkClient.getRetrofit();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(urlDomain)
                    .addConverterFactory(GsonConverterFactory.create(gson2))
                    .client(okHttpClient2)
                    .build();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
            String name = "Rifqi";
//            double latitude = locationTracking.getLatitude();
            double latitude = 11;
            int val1=(int) latitude;
//            double longitude = locationTracking.getLongitude();
            double longitude = 12;
            int val2=(int) longitude;
            Log.d("Upload longitude", "String loc  : " +longitude+" - " +latitude+ " - ");

            RequestBody req1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(name)); //change to phone number
            RequestBody req2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
            RequestBody req3 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
            ApiClient uploadApis = retrofit.create(ApiClient.class);
            Call call = uploadApis.uploadImage(parts, req1, req2, req3);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.code() == 200) {
                        Object obj = response.body();
                        PLNData plnData = (PLNData) response.body();
                        Log.d("Upload plnData", "String plnData  : " +plnData);
                        Log.d("Upload obj", "String obj  : " +obj);

                        //insert room datbase gson, created_at
                        //  22 2 21
                        Date dates = new Date();
                        String gson = new Gson().toJson(plnData);
                        Log.d("Upload gson", "String gson  : " +gson);
                        Date date = new Date();
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",
                                Locale.getDefault());
                        String text = sfd.format(date);

                        ZoneId zoneId = ZoneId.systemDefault();
                        Instant instant = Instant.now();
                        ZonedDateTime zDateTime = instant.atZone(zoneId);

                        DayOfWeek day = zDateTime.getDayOfWeek();
                        System.out.println(day.getDisplayName(TextStyle.SHORT, Locale.US));
                        System.out.println(day.getDisplayName(TextStyle.NARROW, Locale.US));

                        Month month = zDateTime.getMonth();
                        System.out.println(month.getDisplayName(TextStyle.SHORT, Locale.US));
                        System.out.println(month.getDisplayName(TextStyle.NARROW, Locale.US));
                        System.out.println(month.getDisplayName(TextStyle.FULL, Locale.US));
                        Log.d("Upload day createdAt", "createdAt  : " +day + " MONTH " +month +" textDate" +text);

                        double meter = plnData.getMeterValue();
                        double scoreId = plnData.getScoreIdentification();
                        double scoreClass = plnData.getScoreClassification();
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("meter_value").setValue(String.valueOf(meter));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("identify_value").setValue(String.valueOf(scoreId));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("classify_long").setValue(String.valueOf(scoreClass));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("created_at").setValue(String.valueOf(text));
                        Log.d("Upload meter", "String meter  : " +meter+ " - "+scoreId +" - "+scoreClass+" ");
                        String m = String.valueOf(meter);
                        double total = scoreId + scoreClass;

                        if (total>=85){

                            String meterfy = String.valueOf(total);
                            hasilmetergenereate.setTextColor(getResources().getColor(R.color.yellow));
                            hasilmetergenereate.setText(meterfy);

                            Log.d("Upload YELLOW", "String YELLOW  : ");

                        }else {

                            String meterfy = String.valueOf(total);
                            hasilmetergenereate.setTextColor(getResources().getColor(R.color.yellow));
                            hasilmetergenereate.setText(meterfy);
                        }
                        Gimage gimage = new Gimage();
                        gimage.setType(1);
                        int rowImageType = db.gHistorySpinnerDao().getCountimage();
                        if (rowImageType == 0 ){
                            gimage.setId(1);
                            gimage.setImage(imageFilePath);
                            insertData(gimage);
                        }else {
                            gimage.setId(1);
                            gimage.setImage(imageFilePath);
                            updateImage(gimage);

                        }

                        String maxIdHistoryi = String.valueOf(maxIdHistory);

                        String idtfy = String.valueOf(scoreId);
                        String classfy = String.valueOf(scoreClass);

                        History history = new History(maxIdHistoryi,sessionPrefference.getUserId(),m, classfy,idtfy,text);
                        databaseReferenceHistory.child(maxIdHistoryi).setValue(history);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        },1000);


                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        String jwtNull = "";
                        sessionPrefference.setKeyApiJwt(jwtNull);
                        sessionPrefference.setIsLogin(false);
                        sessionPrefference.logoutUser();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "TRY AGAINSZCH", Toast.LENGTH_LONG).show();

                    /*
                    String message = "";
                    String jwtNull = "";
                    Toast.makeText(getActivity(), "Status Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();

                    sessionPrefference.setKeyApiJwt(jwtNull);
                    sessionPrefference.setIsLogin(false);
                    sessionPrefference.logoutUser();
                    */
                }
            });

        } catch (Exception e) {
            String errMessage = e.getMessage();
        }

    }

    private void insertData(Gimage gimage) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertImage(gimage);
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

    private void updateImage(Gimage gimage) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateImageSelected(gimage);
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



}

