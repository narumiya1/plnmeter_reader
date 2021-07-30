package com.example.myapplicationpln.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.activities.CamsActivity;
import com.example.myapplicationpln.caamera.CameraDemoActivity;
import com.example.myapplicationpln.cookies.AddCookiesInterceptor;
import com.example.myapplicationpln.cookies.ReceivedCookiesInterceptor;
import com.example.myapplicationpln.cookies.TokenInterceptor;
import com.example.myapplicationpln.location.LocationTracking;
import com.example.myapplicationpln.model.MConnection;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.model.MeterApi;
import com.example.myapplicationpln.model.MSpinnerSelectx;
import com.example.myapplicationpln.model.MToastr;
import com.example.myapplicationpln.network_retrofit.ApiClient;
import com.example.myapplicationpln.network_retrofit.PLNData;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.example.myapplicationpln.roomDb.GIndeksSpinner;
import com.example.myapplicationpln.roomDb.GhistoryMeter;
import com.example.myapplicationpln.roomDb.Gimage;
import com.example.myapplicationpln.roomDb.GimageUploaded;
import com.example.myapplicationpln.roomDb.GMeterApi;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
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
import static android.app.Activity.RESULT_OK;

public class HomeMenuFragment extends Fragment {

    public static String EXTRA_NOMBRE = "HomeFragment.nombre";
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private AppDatabase db;
    String grain_slected;
    String valueSpinner;
    LocationTracking locationTracking;
    TextView hasilMeter, hasilScore, hasilIdentify;
    TextView  tvInputKwh;
    EditText etInputKwh;
    LinearLayout linearLayoutKWH;
    DatabaseReference databaseReferenceHistory;
    DatabaseReference mDatabaseRefApiMeter;
    double selectLastMeterFromHistory;
    //20210702
    //di explore agar bisa ditaruh di local variable
    long maxIdHistoryGlobal;
    long maxIdHistoryFromRoomGlobal;

    MeterApi meterApi = new MeterApi();
    MSpinnerSelectx MSpinnerSelectx = new MSpinnerSelectx();
    SessionPrefference sessionPrefference;
    PhotoView photoView;
    private Uri image_uri;
    private String mImageFileLocation = "";
    private Dialog dialog;
    ImageButton add_photo;
    ImageButton check_spinner;
    //20210706
//    String urlDomain = "http://110.50.85.28:8200";
    String urlDomain = "http://110.50.86.154:8200";
    ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
        hasilMeter=view.findViewById(R.id.tv_meter);
        linearLayoutKWH = view.findViewById(R.id.ll_angka_kwh);
        etInputKwh = view.findViewById(R.id.et_input_kwh);
        tvInputKwh = view.findViewById(R.id.tv_input_kwh);
        sessionPrefference = new SessionPrefference(getActivity());
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        ceklocation();
        databaseReferenceHistory = FirebaseDatabase.getInstance().getReference().child("HistoryMeter");
        DatabaseReference referenceHistory = FirebaseDatabase.getInstance().getReference();

        if (MConnection.isConnect(getContext())){
            //20200706
            //menggunakan objek Ghistory untuk get table list
            List<GhistoryMeter> listHistory = db.gHistorySpinnerDao().selectHistoryfromRoomMeter12();
            //iterasi untuk membaca table tbHistory dengan status 1, 2
            for (int i = 0 ; i<listHistory.size(); i++){
                //String fileImage = listHistory.get(i).getImagez(); // get image file
                //20210702
                //int idRoomHist = listHistory.get(i).getId();// get id file
//                GHistory history = listHistory.get(i);
                GhistoryMeter ghistoryMeter = listHistory.get(i);
                //uploadImageFromRoom(fileImage, idRoomHist);
                double meter = ghistoryMeter.getMeter();
                uploadImageFromRoom(sessionPrefference.getIdPelanggan(),ghistoryMeter);
            }

            // 20210707 get last meter api value
            DatabaseReference referenceMeter = FirebaseDatabase.getInstance().getReference();
            Query querymeter = referenceMeter.child("MeterApi").child(sessionPrefference.getPhone());
            querymeter.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                        meterApi = dataSnapshot.getValue(MeterApi.class);
                        setMessage01(meterApi);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Log.d("Not Connectzed , Call db room "," " );
            int selection = db.gHistorySpinnerDao().selectMeter() ;
            Log.d("Not Connectzed , Call db room "," selection meter" +selection);
            if (selection==0){
                hasilMeter.setText(String.valueOf(0));
            }else {
                String select = String.valueOf(selection);
                hasilMeter.setText(select);
            }


        }
        databaseReferenceHistory.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        maxIdHistoryGlobal = (snapshot.getChildrenCount());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        add_photo = view.findViewById(R.id.imageview_add);
        check_spinner = view.findViewById(R.id.check_spinner);
        photoView=view.findViewById(R.id.photo_view);
        hasilScore = view.findViewById(R.id.tv_scoreClassify);
        hasilIdentify = view.findViewById(R.id.tv_scoreIdentify);
        DatabaseReference referenceMeter = FirebaseDatabase.getInstance().getReference();


        //20210706 get meter value from firebase table MterApi
        Query querymeter = referenceMeter.child("MeterApi").child(sessionPrefference.getPhone());
        querymeter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    meterApi = dataSnapshot.getValue(MeterApi.class);
                    setMessage01(meterApi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog = new Dialog(getActivity());
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        //add camera access 20 05 2021
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImages();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        // 19 05
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();
        DatabaseReference mDatabaseRefs = database.getReference();
        mDatabaseRefApiMeter = database.getReference();
        locationTracking = new LocationTracking(getActivity());
        final Spinner list = view.findViewById(R.id.listItemz);

        //20210706 check image storeage
        List<String> statusImg = db.gHistorySpinnerDao().getImageStorage();
        statusImg.size();
        if(statusImg.size()==0&&statusImg.size()<0){
            Log.d("AAABL", "No file");
        }
        else if(statusImg.size()>0) {
            //var string untuk menampung image //20210706
            String imageFile ;
            imageFile =  statusImg.get(statusImg.size()-1);
            File imgFile = new File(imageFile);
            int length = (int) imgFile.length();
            Log.d("Upload length respons", "String respons length : " +length);
            if (imgFile.exists())
            {
                Bitmap bitmap ;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(imageFile,
                        bitmapOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, 200,200, true);

                encodeTobase64(bitmap);
                photoView = (PhotoView) view.findViewById(R.id.photo_view);
                photoView.setVisibility(View.VISIBLE);
                photoView.setImageBitmap(BitmapFactory.decodeFile(imageFile));

                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else
            {
                Log.d("AAABL", "No file");
            }
        }

        Integer[] myArrays = db.gHistorySpinnerDao().getAllLItemsArray();
        if (MConnection.isConnect(getActivity())) {
            MToastr.showToast(getActivity(), "internet connected");
            // call spinner from firebase
            String id_user = sessionPrefference.getUserId();
            String jwtTokensz = sessionPrefference.getKeyApiJwt();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Address").child(sessionPrefference.getPhone());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> areasList = new ArrayList<String>();

                    int lenght = (int) dataSnapshot.getChildrenCount();

                    for (DataSnapshot spinnerSnapshot : dataSnapshot.getChildren()) {
                        String idName = spinnerSnapshot.child("id_pelanggan").getValue(String.class);
                        areasList.add(idName);
                    }

                    Spinner spinner = (Spinner) view.findViewById(R.id.listItemz);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areasList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(areasAdapter);

                    DatabaseReference references = FirebaseDatabase.getInstance().getReference();
                    Query queryx = references.child("SpinnerDbx").child(sessionPrefference.getPhone());
                    queryx.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.d("DATA CHANGEx", "onDataChange: " + dataSnapshot.getValue(MSpinnerSelectx.class));
                                MSpinnerSelectx = dataSnapshot.getValue(MSpinnerSelectx.class);
                                Log.d("DATA Yosikhi hyde", " : " + MSpinnerSelectx.getSpinner_value());
                                String hyde = MSpinnerSelectx.getSpinner_long();
                                valueSpinner = MSpinnerSelectx.getSpinner_value();
                                Log.d("DATA CHANGE hyde", " : " + hyde + " valueSpinner : " + valueSpinner + "");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Log.d("DATA valueSpinner hyde", " valueSpinner 2: " + valueSpinner + "");
                    spinner.setSelection(0);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            Toast.makeText(getActivity(), " " + areasAdapter.getItem(i) + " choosen ", Toast.LENGTH_SHORT).show();
                            grain_slected = areasAdapter.getItem(i);
                            mDatabaseRefs.child("SpinnerDbx").child(sessionPrefference.getPhone()).child("spinner_value").setValue(String.valueOf(i));
                            mDatabaseRefs.child("SpinnerDbx").child(sessionPrefference.getPhone()).child("spinner_long").setValue(String.valueOf(grain_slected));
                            MSpinnerSelectx.setSpinner_value(String.valueOf(i));
                            MSpinnerSelectx.setSpinner_long(areasAdapter.getItem(i));

                            adapterView.setTag(grain_slected);
                            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                            String value = areasAdapter.getItem(i);
                            String id_user = sessionPrefference.getUserId();

                            //firebase check if data > 0 , insert
                            if (lenght == 0) {
                                Log.d("DATA CHANGE insertz", "insertz: ");
                            } else {
                                Log.d("DATA CHANGE updatez", "updatez: ");
//                                Toast.makeText(getActivity().getApplicationContext(), "value if " + value + " choosen", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, myArrays);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);

            //get selection from db room
            int selection = db.gHistorySpinnerDao().selectIndeks() ;
            list.setSelection(selection);

            list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedValue;
//                    Toast.makeText(getActivity()," "+adapter.getItem(i)+" choosen", Toast.LENGTH_SHORT).show();
                    selectedValue=adapter.getItem(i).toString();
                    // 140-161 insert to room db
                    String val = adapter.getItem(i).toString();
                    //20210706
                    GIndeksSpinner gIndeksSpinner = new GIndeksSpinner(1, 1, val, i);

                    int rowCount = db.gHistorySpinnerDao().getCountIdx();
                    Log.d("DATA CHANGE rowCount", "rowCount: " +rowCount);

                    if(rowCount==0){
                        Log.d("AAABL", "idx "+rowCount);
//                        Toast.makeText(getActivity(), "index kosong",Toast.LENGTH_SHORT).show();
                        insertIdxSpinner(gIndeksSpinner);

                    }
                    else{
                        // cara-1, dengan fungsi
                        String vals = adapter.getItem(i).toString();
                        gIndeksSpinner.setType(1);
                        gIndeksSpinner.setId_idx(1);
                        gIndeksSpinner.setValue(vals);
                        gIndeksSpinner.setValue_int(i);
                        Log.d("AAVAIL", " i "+vals);
                        updateSelectedGrain(gIndeksSpinner);
                    }
                    Log.d("DATA CHANGE selectedValue", "selectedValue: " + selectedValue+" choosen");

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        check_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MToastr.showToast(getActivity(),grain_slected);
            }
        });
        progress = new ProgressDialog(getActivity());

        return view;
    }

    private void showProgress() {
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private void closeProgress() {
        progress.dismiss();
    }

    private void setMessage01(MeterApi meterApi) {
        Log.d("DATA getMeter_value", "onDataChange: " + meterApi.getMeter_value());
        String meter = meterApi.getMeter_value();
        String clasfy = meterApi.getClassify_long();
        String idfy = meterApi.getIdentify_value();
        String created = meterApi.getCreated_at();
        Log.d("DATA meterApi", "meterApi: " +meter );
        Log.d("DATA createdAt", "created: " +created );
        hasilMeter.setText(meterApi.getMeter_value());
        hasilScore.setText(clasfy);
        hasilIdentify.setText(idfy);
    }
    private void selectImages() {
        dialog.setTitle("Image");
        dialog.setContentView(R.layout.dialog_custom_design);
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_CODE_READ_GALLERY);
                    }
                } else {
                    //permission already granted
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });

        TextView cammera = dialog.findViewById(R.id.cammera);
        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ||
                            (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE_OPEN_CAMERA);

                    } else {
                        //permission already granted
                        openCamera();
                    }
                } else {
                    // system OS < Marshmallow
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    openCamera();
                }

            }
        });
        TextView cammera2 = dialog.findViewById(R.id.cammera2);
        cammera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ||
                            (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE_OPEN_CAMERA);

                    } else {

                        //user camera 2
//                        openCustomCams();
//                         permission already granted
                       Intent intent2 = new Intent(getActivity(), CameraDemoActivity.class);
                       intent2.putExtra(EXTRA_NOMBRE,mImageFileLocation);
                        Log.d("getIntens put"," "+mImageFileLocation);
                        startActivityForResult(intent2, 4);


                    }
                } else {

                     Intent intent2 = new Intent(getActivity(), CameraDemoActivity.class);
                    startActivity(intent2);

                    // system OS < Marshmallow
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    openCamera();*/
                }

            }
        });

        dialog.show();
    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        //image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, 1);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        boolean isEmulated = Environment.isExternalStorageEmulated();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
        return image;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults );
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            break;
                        }
                    }

                }

                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                            bitmapOptions);

                    rotateImage(setReducedImageSize());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                mImageFileLocation = picturePath;

                Bitmap compressedOri = BitmapFactory.decodeFile(mImageFileLocation);

                Bitmap compressed = null;
                //save bitmap to byte array

                try {
                    File file = new File(mImageFileLocation);
                    int length = (int) file.length();
                    Log.d("Body length", "String length : "+length);
                    FileOutputStream fOut = new FileOutputStream(file);
                    compressedOri.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // InSampleSize = 4;

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    rotateImage(setReducedImageSize());
                    int length = (int) mImageFileLocation.length();
                    if (length<40000){
                        Log.d("Body Max Size", "5mb " +length);
                    }
                    Log.d("Body length", "length : "+length);
                    Bitmap converetdImage = getResizedBitmap(bitmap, 650);

                    photoView.setImageBitmap(converetdImage);
                    encodeTobase64(converetdImage);

                    dialog.dismiss();
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
                    photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (grain_slected!=null && MConnection.isConnect(getActivity())) {
                        List<Integer> selectIdFromHistory = db.gHistorySpinnerDao().selectIdfromRoomHistoryCount();
                        int countHistory = selectIdFromHistory.size();
                        int countAdd = countHistory+1;
                        //20210706
                        if (countHistory==0){
                            uploadImage(converetdImage, grain_slected,countAdd, 0.0 );
                        }else {
                            selectLastMeterFromHistory = db.gHistorySpinnerDao().selectRoomMeterLast(countHistory);
                            Log.d("Body uploadImage grain_slected", "grain_slected  : " + selectLastMeterFromHistory);
                            Log.d("Body uploadImage grain_slected", "grain_slected  : " + selectIdFromHistory);
                            uploadImage(converetdImage, grain_slected,countAdd, selectLastMeterFromHistory );

                        }
                        Log.d("Body uploadImage grain_slected", "grain_slected  : " + grain_slected);
                    }else {
                        // insert image to Tb image room
                        Date date = new Date();
                        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                                Locale.getDefault());
                        String text = sfd.format(date);
                        String datetime = sfd.format(date);
                        GHistory GHistory = new GHistory();
                        GHistory.setId_user(sessionPrefference.getUserId());
                        GHistory.setDate_time(date);
                        GHistory.setCreated_at(text);
                        GHistory.setScoreClassfification(0.0);
                        GHistory.setScoreIdentification(0.0);
                        GHistory.setMeter(0.0);
//                        ghistoryi.setStatus(2);
                        GHistory.setStatus(1);
                        GHistory.setImagez(mImageFileLocation);
                        insertDataHistory2(GHistory);

                        String idpel = sessionPrefference.getIdPelanggan();
                        long valueIdPelanggan = Long.parseLong( idpel );
                        double longitudeValue,latitudeValue;
                        longitudeValue = locationTracking.getLongitude();
                        latitudeValue = locationTracking.getLatitude();
                        GhistoryMeter ghistoryMeter = new GhistoryMeter(sessionPrefference.getUserId(),sessionPrefference.getPhone(),valueIdPelanggan,0,0,0.0, 0.0,longitudeValue, latitudeValue,date, text, mImageFileLocation, 1);
                        insertDataHistoryMeter(ghistoryMeter);

//                        MToastr.showToast(getActivity(), "NO INTERNET CONNECTION");
                        GimageUploaded gimageUploaded=new GimageUploaded();
                        gimageUploaded.setImage(mImageFileLocation);
                        insertImageTemp(gimageUploaded);
                    }
                      /*
                        cek jenis grain
                        if (beras = panggil function upload image beras)
                        else if (kopi = uploadImagekopi)
                        else if(gandum = uploadImageGandum)
                        dibuatkan upload image
                      */
                    dialog.dismiss();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 10) {

            } else if (requestCode == 11) {

            }else if (requestCode ==14){

            }else if (requestCode == 19){

            }else if (requestCode == 4){
                String respuesta = data.getStringExtra(CamsActivity.EXTRA_NOMBRE);
            }

        }
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

    protected String jwt;

    //20210702
    //perhatikan parameter variable converetdImage dan grain_slected sepertinya tidak terpakai malah menggunakan global variable
    //2021 07 29
    //tambah parameter lastValue dari firebase/local setelah countAdd
    private void uploadImage(Bitmap converetdImage, String grain_slected, int countAdd, double selectLastMeterFromHistory) {
        String jwtKey =  new SessionPrefference(getContext()).getKeyApiJwt();

        sessionPrefference.setIdPelanggan(grain_slected);
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        if (jwtKey.equals(jwt)){
            sessionPrefference.setIsLogin(false);
            sessionPrefference.logoutUser();
        }
        File file = new File(mImageFileLocation);
        int length = (int) file.length();
//        String dateFr = file.getPath();
        //20210708
        FileTime fileTime;
        try {
            Path path = Paths.get(mImageFileLocation);
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            fileTime = attrs.creationTime();
            String obj = " ";
        }catch (Exception e){
            Log.d("file time created fail", e.getMessage());
        }

//        Date dateCreated = file
        Date lastModDate = new Date(file.lastModified());
//        Log.d("Body dateFr", " : " +dateFr + " lastModDate : " +lastModDate );
        Log.d("Upload length respons", "String respons length : " +length);
        if (length>50000){

        }
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        try {
            showProgress();
            Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);

            HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
            loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

            TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtKey);

            OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor(getActivity()))
                    .addInterceptor(new ReceivedCookiesInterceptor(getActivity()))
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
            double latitude = locationTracking.getLatitude();
            int val1=(int) latitude;
            double longitude = locationTracking.getLongitude();
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
                        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                                Locale.getDefault());
                        String text = sfd.format(date);
                        String datetime = sfd.format(date);
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

                        double lastValue = selectLastMeterFromHistory;
                        double meter = plnData.getMeterValue();
                        double selisih =   lastValue - meter ;
                        double scoreId = plnData.getScoreIdentification();
                        double scoreClass = plnData.getScoreClassification();
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("meter_value").setValue(String.valueOf(meter));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("identify_value").setValue(String.valueOf(scoreId));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("classify_long").setValue(String.valueOf(scoreClass));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("created_at").setValue(String.valueOf(text));
                        Log.d("Upload meter", "String meter  : " +meter+ " - "+scoreId +" - "+scoreClass+" ");
                        String m = String.valueOf(meter);
                        double total = 85;
                        GMeterApi gmeterApi = new GMeterApi();

                        if (scoreClass >=85 && scoreId >=85){
                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.yellow));
                            hasilMeter.setText(meterfy);
                            gmeterApi.setMeter(meterfy);
                            gmeterApi.setType(1);
                            insertMeterApi(gmeterApi);
                        }else {
                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.de_la_red));
                            hasilMeter.setText(meterfy);
                            gmeterApi.setMeter(meterfy);
                            gmeterApi.setType(1);
                            insertMeterApi(gmeterApi);
                        }
                        Gimage gimage = new Gimage();
                        gimage.setType(1);
                        int rowImageType = db.gHistorySpinnerDao().getCountimage();
                        if (rowImageType == 0 ){
                            gimage.setId(1);
                            gimage.setImage(mImageFileLocation);
                            insertData(gimage);
                        }else {
                            gimage.setId(1);
                            gimage.setImage(mImageFileLocation);
                            updateImage(gimage);

                        }

                        Log.d("selisih"," "+selisih);

                        double longitudeVal,latitudeVal;
                        longitudeVal = locationTracking.getLongitude();
                        latitudeVal = locationTracking.getLatitude();
                        MHistory MHistory = new MHistory(countAdd, sessionPrefference.getUserId(), meter, scoreClass, scoreId,longitudeVal, latitudeVal, date);
                        databaseReferenceHistory.child(sessionPrefference.getPhone()).child(String.valueOf("Electricity")).child(grain_slected).child(String.valueOf(countAdd)).setValue(MHistory);

//                        databaseReferenceHistory.child(String.valueOf(countAdd)).setValue(MHistory);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        },1000);
                        double longitudeValue = locationTracking.getLatitude();
                        double langitudeValue = locationTracking.getLongitude();
                        GHistory gHistory = new GHistory(countAdd,sessionPrefference.getUserId(), meter,scoreClass, scoreId, longitudeValue, langitudeValue,date,text,mImageFileLocation,3);
                        insertDataHistory(gHistory);
                        if (selisih <= 0){
                            double selisiNegatv = selisih*-1 ;
                            etInputKwh.setVisibility(View.GONE);
                            linearLayoutKWH.setVisibility(View.GONE);
                            tvInputKwh.setVisibility(View.GONE);
                        }else {
                            etInputKwh.setVisibility(View.VISIBLE);
                            linearLayoutKWH.setVisibility(View.VISIBLE);
                            tvInputKwh.setVisibility(View.VISIBLE);
                            tvInputKwh.setText(String.valueOf(selisih));

                        }

                        String getinputKwh = etInputKwh.getText().toString();
                        String idpel = sessionPrefference.getIdPelanggan();
                        long valueIdPelanggan = Long.parseLong( idpel );
//                        long valueKwh = Long.parseLong( getinputKwh );

                        GhistoryMeter ghistoryMeter = new GhistoryMeter(countAdd, sessionPrefference.getUserId(),sessionPrefference.getPhone(),valueIdPelanggan,meter,selisih,scoreClass, scoreId,longitude, latitude,date, text, mImageFileLocation, 3);
                        insertDataHistoryMeter(ghistoryMeter);
                        closeProgress();

                    } else {
//                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                        String jwtNull = "";
                        sessionPrefference.setKeyApiJwt(jwtNull);
                        sessionPrefference.setIsLogin(false);
                        sessionPrefference.logoutUser();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
//                    Toast.makeText(getActivity(), " ", Toast.LENGTH_LONG).show();
                    closeProgress();
                    Date date = new Date();
                    SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                            Locale.getDefault());
                    String text = sfd.format(date);
                    String datetime = sfd.format(date);
                    double longitudeValue = locationTracking.getLatitude();
                    double langitudeValue = locationTracking.getLongitude();
                    GHistory GHistory = new GHistory(countAdd,sessionPrefference.getUserId(),0.0,0.0,0.0,longitudeValue, langitudeValue,date,text,mImageFileLocation,2);
                    /*
                    GHistory.setId_user(sessionPrefference.getUserId());
                    GHistory.setCreated_at(text);
                    GHistory.setScore_identfy(String.valueOf(0));
                    GHistory.setScore_classfy(String.valueOf(0));
                    GHistory.setMeter(String.valueOf(0));
                    GHistory.setStatus(2);
                    GHistory.setImagez(mImageFileLocation);
                     */
//                    Toast.makeText(getActivity(), "server error, db inserted to local db", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                    insertDataHistory2(GHistory);

                    String idpel = sessionPrefference.getIdPelanggan();
                    long valueIdPelanggan = Long.parseLong( idpel );
                    GhistoryMeter ghistoryMeter = new GhistoryMeter(countAdd, sessionPrefference.getUserId(),sessionPrefference.getPhone(),valueIdPelanggan,0,0,0.0, 0.0,longitude, latitude,date, text, mImageFileLocation, 2);
                    insertDataHistoryMeter(ghistoryMeter);

                }
            });

        } catch (Exception e) {
            closeProgress();
            String errMessage = e.getMessage();
        }

    }
    private void uploadImageFromRoom(String idPelanggan, GhistoryMeter history) {  //String fileImage, int idRoomHist) {
        String fileImage = history.getImagez();
        int idRoomHist = history.getId();
        Date dateFrRoom = history.getDate_time();

        String jwtKey =  new SessionPrefference(getContext()).getKeyApiJwt();
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        if (jwtKey.equals(jwt)){
            sessionPrefference.setIsLogin(false);
            sessionPrefference.logoutUser();
        }
        File file = new File(fileImage);
        int length = (int) file.length();
        String imageName = file.getName();

        //cek mengapa coding ini berisi blok kosong
        //jika sedang melakukan pengetesan, harap dituliskan keterangan testing dan deskripsi
        if (length>50000){
            // check file image size
        }
        //starting mengirim gambar ke web api
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        try {
            Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);

            HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
            loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

            TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtKey);

            OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor(getActivity()))
                    .addInterceptor(new ReceivedCookiesInterceptor(getActivity()))
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
            double latitude = locationTracking.getLatitude();
            double longitude = locationTracking.getLongitude();
            Log.d("Upload longitudex", "String loc  : " +longitude+" - " +latitude+ " - ");

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

                        //insert room datbase gson, created_at
                        //  22 2 21
                        Date date = new Date();
                        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                                Locale.getDefault());
                        String text = sfd.format(date);
                        String datetime = sfd.format(date);

                        //angka meter hasil pembacaan oleh web api beserta nilai score-score
                        double meter = plnData.getMeterValue();
                        double scoreId = plnData.getScoreIdentification();
                        double scoreClass = plnData.getScoreClassification();

                        //insert angka meter dan score-score  ke firebase
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("meter_value").setValue(String.valueOf(meter));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("identify_value").setValue(String.valueOf(scoreId));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("classify_long").setValue(String.valueOf(scoreClass));
                        mDatabaseRefApiMeter.child("MeterApi").child(sessionPrefference.getPhone()).child("created_at").setValue(String.valueOf(text));

                        Log.d("Upload meter", "String meter  : " +meter+ " - "+scoreId +" - "+scoreClass+" ");

                        //insert angka meter ke lokal
                        String m = String.valueOf(meter);
                        GMeterApi gmeterApi = new GMeterApi();

                        //cek apabila salah satu score<85 maka akan diberi notifikasi (warna, dll)
                        //20210702
                        double minimumScore = 85.0;
                        if (scoreId>=minimumScore && scoreClass> minimumScore){
                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.yellow));
                            hasilMeter.setText(meterfy);
                            gmeterApi.setMeter(meterfy);
                            gmeterApi.setType(1);
                            insertMeterApi(gmeterApi);


                        }else {
                            //salah satu score < 85
                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.de_la_red));
                            hasilMeter.setText(meterfy);
                            gmeterApi.setMeter(meterfy);
                            gmeterApi.setType(1);
                            insertMeterApi(gmeterApi);
                        }

                        Gimage gimage = new Gimage();
                        gimage.setType(1);
                        int rowImageType = db.gHistorySpinnerDao().getCountimage();
                        if (rowImageType == 0 ){
                            gimage.setId(1);
                            gimage.setImage(fileImage);
                            insertData(gimage);
                        }else {
                            gimage.setId(1);
                            gimage.setImage(fileImage);
                            updateImage(gimage);

                        }

                        String idtfy = String.valueOf(scoreId);
                        String classfy = String.valueOf(scoreClass);

                        GimageUploaded gUserData = new GimageUploaded();
                        gUserData.setStatus(1);
                        int status = 3;
                        db.gHistorySpinnerDao().updateImageStatus(status);

                        //20210502
                        double longitudeValue = locationTracking.getLatitude();
                        double langitudeValue = locationTracking.getLongitude();
                        GHistory item = new GHistory(idRoomHist,sessionPrefference.getUserId(),meter,scoreClass,scoreId,longitudeValue,langitudeValue,dateFrRoom,text,fileImage,3);
                        updateHistoryFroomRoom(item);
                        String idpel = sessionPrefference.getIdPelanggan();
                        long valueIdPelanggan = Long.parseLong( idpel );
                        String phone = sessionPrefference.getPhone();
                        Integer number = Integer.valueOf(idRoomHist);
                        GhistoryMeter ghistoryMeter = new GhistoryMeter(number,sessionPrefference.getUserId(),phone,valueIdPelanggan,meter, 0,scoreClass,scoreId,longitudeValue, langitudeValue,dateFrRoom,text,fileImage,3 );
                        updateHistoryFroomRoomMeter(ghistoryMeter);
                        double longitudeVal,latitudeVal;
                        longitudeVal = locationTracking.getLongitude();
                        latitudeVal = locationTracking.getLatitude();
                        MHistory history = new MHistory(idRoomHist,sessionPrefference.getUserId(),meter, scoreClass,scoreId,longitudeVal, latitudeVal,dateFrRoom);
//                        databaseReferenceHistory.child(sessionPrefference.getPhone()).child(String.valueOf(idRoomHist)).setValue(history);
                        databaseReferenceHistory.child(sessionPrefference.getPhone()).child(String.valueOf("Electricity")).child(String.valueOf(idPelanggan)).child(String.valueOf(idRoomHist)).setValue(history);

//                        databaseReferenceHistory.child(String.valueOf(idRoomHist)).setValue(history);


                    } else {
//                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                        String jwtNull = "";
                        sessionPrefference.setKeyApiJwt(jwtNull);
                        sessionPrefference.setIsLogin(false);
                        sessionPrefference.logoutUser();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
//                    Toast.makeText(getActivity(), "TRY AGAINSZCH", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            String errMessage = e.getMessage();
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

    private void insertDataHistoryMeter(GhistoryMeter ghistoryMeter) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertHistoryiDataMeter(ghistoryMeter);
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

    private void insertMeterApi(GMeterApi gmeterApi) {
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
                Log.d("Upload history row added sucessfullys", "String status  : " + status);
            }
        }.execute();
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d("Upload Bitmap bitmap respons", "String Bitmap bitmap  : " +bitmap);
        float bitmapRatio = (float)width / (float) height;
        Log.d("Upload Bitmap bitmap respons", "String Bitmap bitmap  : " +bitmapRatio);
        if (bitmapRatio > 1) {
            width = i;
            height = (int) (width / bitmapRatio);
        } else {
            height = i;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);

    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
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

        photoView.setImageBitmap(rotateBitmap);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        List<Integer> selectIdFromHistory = db.gHistorySpinnerDao().selectIdfromRoomHistoryCount();
        int countHistory = selectIdFromHistory.size();
        int countAdd = countHistory+1;
        double selectLastMeterFromHistory = db.gHistorySpinnerDao().selectRoomMeterLast(countHistory);
        Log.d("Body uploadImage grain_slected", "grain_slected  : " + selectLastMeterFromHistory);
        Log.d("Body uploadImage grain_slected", "grain_slected  : " + selectIdFromHistory);
        uploadImage(rotateBitmap, grain_slected, countAdd, selectLastMeterFromHistory);
        dialog.dismiss();

    }

    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = photoView.getWidth();
        int targetImageViewHeight = photoView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        //Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        //mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmap);
        Bitmap compressedOri = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);

        Bitmap compressed = null;
        //save bitmap to byte array

        try {
            File file = new File(mImageFileLocation);
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
    private void updateImage(Gimage img) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateImageSelected(img);
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

    private void updateHistoryFroomRoomMeter(GhistoryMeter item) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateHistoryFroomRoomMeter(item);
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

    private void updateHistoryFroomRoom(GHistory item) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateHistoryFroomRoom(item);
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
    private void updateSelectedGrain(GIndeksSpinner gIndeksSpinner) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateGrainSelectedGspinner(gIndeksSpinner);
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


    private void insertIdxSpinner(GIndeksSpinner gIndeksSpinner) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertIdIdx(gIndeksSpinner);
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
                return (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    private void ceklocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationTracking = new LocationTracking(getActivity());

        if (locationTracking.canGetLocation()) {
            double longitude = locationTracking.getLongitude();
            double latitude = locationTracking.getLatitude();
            Log.d("Body Longitude", "longitude : " + longitude + "& " + latitude);
        } else {
            locationTracking.showSettingsAlert();
        }
    }

}