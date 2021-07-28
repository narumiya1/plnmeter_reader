package com.example.myapplicationpln.caamera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
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
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.cookies.AddCookiesInterceptor;
import com.example.myapplicationpln.cookies.ReceivedCookiesInterceptor;
import com.example.myapplicationpln.cookies.TokenInterceptor;
import com.example.myapplicationpln.location.LocationTracking;
import com.example.myapplicationpln.model.MConnection;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.model.MToastr;
import com.example.myapplicationpln.network_retrofit.ApiClient;
import com.example.myapplicationpln.network_retrofit.PLNData;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.example.myapplicationpln.roomDb.Gimage;
import com.example.myapplicationpln.roomDb.GimageUploaded;
import com.example.myapplicationpln.roomDb.GMeterApi;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PreviewActivity.class.getSimpleName();
    LocationTracking locationTracking;

    private ImageView previewImage;
    private String imageFilePath;
    private TextView hasilmetergenereate;
    private Button button;
    SessionPrefference sessionPrefference;
    DatabaseReference databaseReferenceHistory;
    DatabaseReference mDatabaseRefApiMeter;
    long maxIdHistory;
    private AppDatabase db;
//    String urlDomain = "http://110.50.85.28:8200";
    String urlDomain = "http://110.50.86.154:8200";
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        locationTracking = new LocationTracking(getApplicationContext());

        sessionPrefference = new SessionPrefference(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();
        DatabaseReference mDatabaseRefs = database.getReference();
        mDatabaseRefApiMeter = database.getReference();
        databaseReferenceHistory = FirebaseDatabase.getInstance().getReference().child("HistoryMeter");
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
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();


        Bundle bundle = getIntent().getExtras();
//        imageFilePath = bundle.getString("camera_image_folder");
        imageFilePath = bundle.getString("camera_img");
        Log.d(TAG, "Image File Path:\t" + imageFilePath);

        previewImage = (ImageView) findViewById(R.id.previewImage);
        hasilmetergenereate = findViewById(R.id.hasilmetergenereate);
        button = findViewById(R.id.generate);
        button.setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);


        if (bitmap != null) {
            previewImage.setImageBitmap(bitmap);
        }
        if (MConnection.isConnect(getApplicationContext())){
            //20210714
            //add db room id
            List<Integer> selectIdFromHistory = db.gHistorySpinnerDao().selectIdfromRoomHistoryCount();
            int countHistory = selectIdFromHistory.size();
            int countAdd = countHistory+1;
            uploadImage(imageFilePath, countAdd);
        }else {

            Date date = new Date();
            SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                    Locale.getDefault());
            String text = sfd.format(date);
            String dateTime = sfd.format(date);
            //2021 07 07 add id +1
            GHistory gHistoryItem = new GHistory(sessionPrefference.getUserId(),0.0, 0.0, 0.0,date,text,imageFilePath,1);

            /*

            GHistory.setId_user(sessionPrefference.getUserId());
            GHistory.setCreated_at(text);
            GHistory.setScore_identfy(String.valueOf(0));
            GHistory.setScore_classfy(String.valueOf(0));
            GHistory.setMeter(String.valueOf(0));
            GHistory.setStatus(1);
            GHistory.setImagez(imageFilePath);

             */

            insertDataHistory2(gHistoryItem);
            MToastr.showToast(getApplicationContext(), "NO INTERNET CONNECTION, SAVED TO LOCAL DB");

            GimageUploaded gimageUploaded=new GimageUploaded();
            gimageUploaded.setImage(imageFilePath);
            gimageUploaded.setStatus(0);
            insertImageTemp(gimageUploaded);
            Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private void insertDataHistory2(GHistory GHistory) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertHistoryiData(GHistory);
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

    private void insertImageTemp(GimageUploaded gimageUploaded) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertImageTemp(gimageUploaded);
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
        List<Integer> selectIdFromHistory = db.gHistorySpinnerDao().selectIdfromRoomHistoryCount();
        int countHistory = selectIdFromHistory.size();
        int countAdd = countHistory+1;
        //20210706
        uploadImage(imageFilePath, countAdd);

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

    private void uploadImage(String imageFilePath, int countAdd) {
        String jwtKey =  new SessionPrefference(getApplicationContext()).getKeyApiJwt();
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        String jwt = "";
        if (jwtKey.equals(jwt)){
            sessionPrefference.setIsLogin(false);
            sessionPrefference.logoutUser();
        }
        File file = new File(imageFilePath);
        int length = (int) file.length();
        String dateFr = file.getPath();
        Date lastModDate = new Date(file.lastModified());
        Log.d("Body dateFr Preview", " : " +dateFr + " lastModDate Preview : " +lastModDate );
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
                        String dateTime = sfd.format(date);

                        /*
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

                         */
                        double meter = plnData.getMeterValue();
                        double scoreId = plnData.getScoreIdentification();
                        double scoreClass = plnData.getScoreClassification();
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("meter_value").setValue(String.valueOf(meter));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("identify_value").setValue(String.valueOf(scoreId));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("classify_long").setValue(String.valueOf(scoreClass));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("created_at").setValue(String.valueOf(text));
                        Log.d("Upload meter", "String meter  : " +meter+ " - "+scoreId +" - "+scoreClass+" ");
                        String m = String.valueOf(meter);
                        double totalMinimum = 85;

                        if (scoreId>=85 && scoreClass >=85){

                            hasilmetergenereate.setTextColor(getResources().getColor(R.color.yellow));
                            hasilmetergenereate.setText(m);

                            Log.d("Upload YELLOW", "String YELLOW  : ");

                        }else {

                            hasilmetergenereate.setTextColor(getResources().getColor(R.color.yellow));
                            hasilmetergenereate.setText(m);
                        }
                        List<String> metersCount = db.gHistorySpinnerDao().getMetet();
                        GMeterApi gmeterApi = new GMeterApi();
                        if (metersCount.equals(0)){
                            gmeterApi.setMeter(m);
                            gmeterApi.setType(1);
                            insertMeterApiVal(gmeterApi);
                        }else{
                            gmeterApi.setMeter(m);
                            gmeterApi.setType(1);
                            updateMeterApiVal(gmeterApi);
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
                        double longitudeVal,latitudeVal;
                        longitudeVal = locationTracking.getLongitude();
                        latitudeVal = locationTracking.getLatitude();
                        MHistory MHistory = new MHistory(countAdd, sessionPrefference.getUserId(), meter, scoreId, scoreClass,longitudeVal, latitudeVal, date);
//                        databaseReferenceHistory.child(sessionPrefference.getPhone()).child(String.valueOf(countAdd)).setValue(MHistory);
                        databaseReferenceHistory.child(sessionPrefference.getPhone()).child(String.valueOf("ElectricCity")).child(sessionPrefference.getIdPelanggan()).child(String.valueOf(countAdd)).setValue(MHistory);

//                        databaseReferenceHistory.child(maxIdHistoryi).setValue(MHistory);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        },1000);
                        GHistory itesm = new GHistory(sessionPrefference.getUserId(), meter, scoreClass, scoreId, longitudeVal, latitudeVal, date, text, imageFilePath, 3);
//                        GHistory item = new GHistory(sessionPrefference.getUserId(),meter,scoreClass,scoreId,longitudeVal, latitudeVal,date,text,imageFilePath,3);
                        /*
                        GHistory.setMeter(m);
                        GHistory.setId_user(sessionPrefference.getUserId());
                        GHistory.setCreated_at(text);
                        GHistory.setScore_classfy(classfy);
                        GHistory.setScore_identfy(idtfy);

                         */
                        insertDataHistory(itesm);
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        String jwtNull = "";
                        sessionPrefference.setKeyApiJwt(jwtNull);
                        sessionPrefference.setIsLogin(false);
                        sessionPrefference.logoutUser();

                    }
                    Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                     startActivity(intent);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "TRY AGAINSZCH", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                    startActivity(intent);
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

    private void insertDataHistory(GHistory GHistory) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertHistoryiData(GHistory);
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

    private void updateMeterApiVal(GMeterApi gmeterApi) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertMeterData(gmeterApi);
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

    private void insertMeterApiVal(GMeterApi gmeterApi) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateMeterApiVal(gmeterApi);
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


    private void ceklocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationTracking = new LocationTracking(getApplicationContext());

        if (locationTracking.canGetLocation()) {
            double longitude = locationTracking.getLongitude();
            double latitude = locationTracking.getLatitude();
            Log.d("Body Longitude", "longitude : " + longitude + "& " + latitude);
        } else {
            locationTracking.showSettingsAlert();
        }
    }
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}

