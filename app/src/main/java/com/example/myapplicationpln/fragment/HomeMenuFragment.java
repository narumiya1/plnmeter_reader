package com.example.myapplicationpln.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.activities.CamsActivity;
import com.example.myapplicationpln.activities.UpdateUserData;
import com.example.myapplicationpln.caamera.CameraDemoActivity;
import com.example.myapplicationpln.cookies.AddCookiesInterceptor;
import com.example.myapplicationpln.cookies.ReceivedCookiesInterceptor;
import com.example.myapplicationpln.cookies.TokenInterceptor;
import com.example.myapplicationpln.location.LocationTracking;
import com.example.myapplicationpln.model.Connection;
import com.example.myapplicationpln.model.DataUser;
import com.example.myapplicationpln.model.History;
import com.example.myapplicationpln.model.MeterApi;
import com.example.myapplicationpln.model.PLNDataModel;
import com.example.myapplicationpln.model.PelangganyAlamat;
import com.example.myapplicationpln.model.SpinnerSelection;
import com.example.myapplicationpln.model.SpinnerSelectx;
import com.example.myapplicationpln.model.Toastr;
import com.example.myapplicationpln.network_retrofit.ApiClient;
import com.example.myapplicationpln.network_retrofit.PLNData;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GIndeksSpinner;
import com.example.myapplicationpln.roomDb.GUserData;
import com.example.myapplicationpln.roomDb.Ghistoryi;
import com.example.myapplicationpln.roomDb.Gimage;
import com.example.myapplicationpln.roomDb.GmeterApi;
import com.example.myapplicationpln.roomDb.Gspinner;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
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
    private ArrayList<Gspinner> listGrainType;
    private AppDatabase db;
    String grain_slected;
    String valueSpinner;
    int spinnrVal;
    private static final String TAG = "CalculatorFragment";
    LocationTracking locationTracking;
    StringBuilder input, top;
    TextView hasilMeter, hasilScore, hasilIdentify;
    DatabaseReference databaseReferenceHistory;
    DatabaseReference mDatabaseRefApiMeter;
    long maxIdHistory;
    View layout;
    boolean eqlFlag = false;
    ArrayList<String> arrayListz;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    PelangganyAlamat dataUser = new PelangganyAlamat();
    MeterApi meterApi = new MeterApi();
//    SpinnerSelection spinnerSelection = new SpinnerSelection();
    SpinnerSelectx spinnerSelectx = new SpinnerSelectx();
    SessionPrefference sessionPrefference;
    String selectedValue;
    PhotoView photoView;
    private Uri image_uri;
    private String mImageFileLocation = "";
    private Dialog dialog;
    ImageButton add_photo;
    Bundle  args;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
//        layout =inflater.inflate(R.layout.fragment_home_menu, container,false);
        hasilMeter=view.findViewById(R.id.tv_meter);
        sessionPrefference = new SessionPrefference(getActivity());
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
        args = new Bundle();
        if(args == null){
            Toast.makeText(getActivity(), "arguments is null " , Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getActivity(), "text " + args , Toast.LENGTH_LONG).show();
            putArgs(args);
//            String strtxt = getArguments().getString("edttext");
            Log.d("getIntens getbundle from IMAGE_SAVED_PATH "," " +args);
        }
        if (Connection.isConnect(getContext())){
            Log.d("Internet Connectzed 1 "," " );
            DatabaseReference referenceMeter = FirebaseDatabase.getInstance().getReference();
            Query querymeter = referenceMeter.child("MeterApi").child(sessionPrefference.getPhone());
            querymeter.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                        meterApi = dataSnapshot.getValue(MeterApi.class);
                        Log.d("DATA getMeter_value", "onDataChange: " + meterApi.getMeter_value());
                        String meter = meterApi.getMeter_value();
                        String clasfy = meterApi.getClassify_long();
                        String idfy = meterApi.getIdentify_value();
                        String created = meterApi.getCreated_at();
                        Log.d("DATA meterApi", "meterApi: " +meter );
                        Log.d("DATA createdAt", "created: " +created );
                        hasilMeter.setText(meter);
                        hasilScore.setText(clasfy);
                        hasilIdentify.setText(idfy);

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
        if (!Connection.isConnect(getContext())){
            Log.d("Not Connectzed , Call db room "," " );
        }else {
            Log.d("Connection "," Internet Connectzed 1 " );
        }
        add_photo = view.findViewById(R.id.imageview_add);
        photoView=view.findViewById(R.id.photo_view);
        hasilScore = view.findViewById(R.id.tv_scoreClassify);
        hasilIdentify = view.findViewById(R.id.tv_scoreIdentify);
        DatabaseReference referenceMeter = FirebaseDatabase.getInstance().getReference();



        Query querymeter = referenceMeter.child("MeterApi").child(sessionPrefference.getPhone());
        querymeter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                    meterApi = dataSnapshot.getValue(MeterApi.class);
                    Log.d("DATA getMeter_value", "onDataChange: " + meterApi.getMeter_value());
                    String meter = meterApi.getMeter_value();
                    String clasfy = meterApi.getClassify_long();
                    String idfy = meterApi.getIdentify_value();
                    String created = meterApi.getCreated_at();
                    Log.d("DATA meterApi", "meterApi: " +meter );
                    Log.d("DATA createdAt", "created: " +created );
                    hasilMeter.setText(meter);
                    hasilScore.setText(clasfy);
                    hasilIdentify.setText(idfy);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Date date = new Date();
        Log.d("Body myArrays ", " myArrays : " + date + " ");

        dialog = new Dialog(getActivity());
        ceklocation();
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
//                showDialog();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
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

        // 19 05
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();
        DatabaseReference mDatabaseRefs = database.getReference();
        mDatabaseRefApiMeter = database.getReference();
        locationTracking = new LocationTracking(getActivity());
        final Spinner list = view.findViewById(R.id.listItemz);
        //1 Arraylist
        arrayListz = new ArrayList<>();
        arrayListz.add("1123456");
        arrayListz.add("9876542");
        arrayListz.add("4345679");

        listGrainType = new ArrayList<>();

//        List<Integer> lables = db.gHistorySpinnerDao().getAllLItems();
        List<String> statusImg = db.gHistorySpinnerDao().getImageStorage();
        statusImg.size();
        if(statusImg.size()==0&&statusImg.size()<0){
            Log.d("AAABL", "No file");


        }
        else if(statusImg.size()>0) {
            Log.d("statusImg", "y statusImg"+statusImg);

            mImageFileLocation =  statusImg.get(statusImg.size()-1);
            File imgFile = new File(mImageFileLocation);
            int length = (int) imgFile.length();
            Log.d("Upload length respons", "String respons length : " +length);
            if (imgFile.exists())
            {
                Bitmap bitmap ;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                        bitmapOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, 200,200, true);

                encodeTobase64(bitmap);
//                decodeBase64(mImageFileLocation);
                photoView = (PhotoView) view.findViewById(R.id.photo_view);
                photoView.setVisibility(View.VISIBLE);
                photoView.setImageBitmap(BitmapFactory.decodeFile(mImageFileLocation));

                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else
            {
                Log.d("AAABL", "No file");
            }
        }

        Integer[] myArrays = db.gHistorySpinnerDao().getAllLItemsArray();
        Log.d("Body myArrays ", " myArrays : " + myArrays + " ");
        if (Connection.isConnect(getActivity())) {

            Toastr.showToast(getActivity(), "internet connected");
            // call spinner from firebase
            String id_user = sessionPrefference.getUserId();
            String jwtTokensz = sessionPrefference.getKeyApiJwt();
            Log.d("Body id_userid_user ", " id_user : " + id_user + " ");
            Log.d("Body jwtTokensz ", " jwtTokensz : " + jwtTokensz + " ");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Address").orderByChild("id_user").equalTo(id_user);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> areasList = new ArrayList<String>();

                    int lenght = (int) dataSnapshot.getChildrenCount();
                    Log.d("Body lenght ", " lenght : " + lenght + " ");

                    for (DataSnapshot spinnerSnapshot : dataSnapshot.getChildren()) {
                        String idName = spinnerSnapshot.child("id_pelanggan").getValue(String.class);
                        Log.d("Body idName ", " idName : " + idName + " ");
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
                                Log.d("DATA CHANGEx", "onDataChange: " + dataSnapshot.getValue(SpinnerSelectx.class));
                                spinnerSelectx = dataSnapshot.getValue(SpinnerSelectx.class);
                                Log.d("DATA Yosikhi hyde", " : " + spinnerSelectx.getSpinner_value());
                                String hyde = spinnerSelectx.getSpinner_long();
                                valueSpinner = spinnerSelectx.getSpinner_value();
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
                            Toast.makeText(getActivity(), " " + areasAdapter.getItem(i) + " choosen ", Toast.LENGTH_SHORT).show();
                            Log.d("DATA CHANGE choosen", "choosen: " + areasAdapter.getItem(i) + " choosen");
                            grain_slected = areasAdapter.getItem(i);
                            mDatabaseRefs.child("SpinnerDbx").child(sessionPrefference.getPhone()).child("spinner_value").setValue(String.valueOf(i));
                            mDatabaseRefs.child("SpinnerDbx").child(sessionPrefference.getPhone()).child("spinner_long").setValue(String.valueOf(grain_slected));
                            spinnerSelectx.setSpinner_value(String.valueOf(i));
                            spinnerSelectx.setSpinner_long(areasAdapter.getItem(i));
                            Log.d("DATA CHANGE grain_slected", "grain_slected: " + areasAdapter.getItem(i) + " choosen");

                            adapterView.setTag(grain_slected);
                            String value = areasAdapter.getItem(i);
                            String id_user = sessionPrefference.getUserId();

                            //firebase check if data > 0 , insert
                            if (lenght == 0) {
                                Log.d("DATA CHANGE insertz", "insertz: ");
                            } else {
                                Log.d("DATA CHANGE updatez", "updatez: ");
                                Toast.makeText(getActivity().getApplicationContext(), "value if " + value + " choosen", Toast.LENGTH_LONG).show();

                            }

                            // 140-161 insert to room db
//                        GIndeksSpinner gIndeksSpinner = new GIndeksSpinner();
//                        gIndeksSpinner.setType(1);
//                        gIndeksSpinner.setId_idx(6);
//                        gIndeksSpinner.setValue(areasAdapter.getItem(i));
//
//                        int rowCount = db.gHistorySpinnerDao().getCountIdx();
//                        Log.d("DATA CHANGE rowCount", "rowCount: " + rowCount);
//
//                        if (rowCount == 0) {
//                            Log.d("AAABL", "idx " + rowCount);
//                            insertIdxSpinner(gIndeksSpinner);
//                        } else {
////                            cara-1, dengan fungsi
//                            gIndeksSpinner.setType(1);
//                            gIndeksSpinner.setId_idx(6);
//                            gIndeksSpinner.setValue(areasAdapter.getItem(i));
//                            Log.d("AAVAIL", " i " + i);
//                            updateSelectedGrain(gIndeksSpinner);
//                        }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    // call spinner from firebase 2
//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//
//                    dataUser = dataSnapshot.getValue(PelangganyAlamat.class);
//
//                    String ids = areaSnapshot.child("id_user").getValue(String.class);
//                    Log.d("DATA CHANGE id_user", "id_user: " +ids);
//                    String areaName = areaSnapshot.child("id_pelanggan").getValue(String.class);
//                    Log.d("DATA CHANGE areaName", "areaName: " + areaName);
//                    String idsz = areaSnapshot.child("alamat_pelanggan").getValue(String.class);
//                    Log.d("DATA CHANGE alamat_pelanggan", "alamat_pelanggan: " +idsz);
//
//
//                    Spinner areaSpinner = (Spinner) view.findViewById(R.id.listItemz);
//                    final String[] areas = {areaName};
//                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areas);
//                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    areaSpinner.setAdapter(areasAdapter);
//                }


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
            Log.d("Body selectionsz ", " selection : "+selection+" ");
            list.setSelection(selection);

            list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity()," "+adapter.getItem(i)+" choosen", Toast.LENGTH_SHORT).show();
                    Log.d("DATA CHANGE choosen", "choosen: " + adapter.getItem(i)+" choosen");
                    selectedValue=adapter.getItem(i).toString();
                    String id_user = sessionPrefference.getUserId();
                    // 140-161 insert to room db
                    String val = adapter.getItem(i).toString();
                    Log.d("DATA CHANGE rowCount", "rowCount: " +val);

                    GIndeksSpinner gIndeksSpinner = new GIndeksSpinner();
                    gIndeksSpinner.setType(1);
                    gIndeksSpinner.setId_idx(1);
                    gIndeksSpinner.setValue(val);
                    gIndeksSpinner.setValue_int(i);

                    int rowCount = db.gHistorySpinnerDao().getCountIdx();
                    Log.d("DATA CHANGE rowCount", "rowCount: " +rowCount);

                    if(rowCount==0){
                        Log.d("AAABL", "idx "+rowCount);
                        Toast.makeText(getActivity(), "index kosong",Toast.LENGTH_SHORT).show();
                        insertIdxSpinner(gIndeksSpinner);

                    }
                    else{
//                            cara-1, dengan fungsi
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





        //code : 16:40
        /*

        // call spinner from room
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, myArrays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        //get selection from db room
        int selection = db.gHistorySpinnerDao().selectIndeks() ;
        Log.d("Body selectionsz ", " selection : "+selection+" ");
        list.setSelection(selection);

        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity()," "+adapter.getItem(i)+" choosen", Toast.LENGTH_SHORT).show();
                Log.d("DATA CHANGE choosen", "choosen: " + adapter.getItem(i)+" choosen");
                selectedValue=adapter.getItem(i).toString();
                String id_user = sessionPrefference.getUserId();
                // 140-161 insert to room db
                String val = adapter.getItem(i).toString();
                Log.d("DATA CHANGE rowCount", "rowCount: " +val);

                GIndeksSpinner gIndeksSpinner = new GIndeksSpinner();
                gIndeksSpinner.setType(1);
                gIndeksSpinner.setId_idx(1);
                gIndeksSpinner.setValue(val);
                gIndeksSpinner.setValue_int(i);

                int rowCount = db.gHistorySpinnerDao().getCountIdx();
                Log.d("DATA CHANGE rowCount", "rowCount: " +rowCount);

                if(rowCount==0){
                    Log.d("AAABL", "idx "+rowCount);
                    Toast.makeText(getActivity(), "index kosong",Toast.LENGTH_SHORT).show();
                    insertIdxSpinner(gIndeksSpinner);

                }
                else{
//                            cara-1, dengan fungsi
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
         */


        return view;
    }
    private void putArgs(Bundle args) {
        String myValue = args.getString("edttext");
        Log.d("getIntens getbundle from IMAGE_SAVED_PATH "," " +myValue);

    }

    /*
     if isConnected -> connect to firebase
     else -> check local
     */
    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

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

    private void openCustomCams() {

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

        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
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

                /*
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } */

                //viewImage.setImageURI(image_uri);

                /*
                Uri selectedImage = image_uri; //data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = mImageFileLocation; //c.getString(columnIndex);
                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options ();
                options.inSampleSize=4; // InSampleSize = 4;

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    viewImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } */

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
                /*
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail); */

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
//                    decodeBase64(mImageFileLocation);
                    /*
                    //draw circle
                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Paint paint = new Paint();                          //define paint and paint color
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);
                    //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                    Canvas canvas =new Canvas(mutableBitmap);
                    canvas.drawCircle(50, 50, 50, paint);
                    //invalidate to update bitmap in imageview
                    viewImage.setImageBitmap(mutableBitmap);
                    viewImage.invalidate(); */

                    dialog.dismiss();
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
                    photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (grain_slected!=null) {
                        uploadImage(converetdImage, grain_slected);
                        Log.d("Body uploadImage grain_slected", "grain_slected  : " + grain_slected);
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
//                String name=this.getArguments().getString("NAME_KEY").toString();
//                int year=this.getArguments().getInt("YEAR_KEY");
//                Log.d("getIntens year name "," "+name+ " " +year+ " " +respuesta);
            }

        }
    }
    protected String jwt;
    private void uploadImage(Bitmap converetdImage, String grain_slected) {
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new SessionPrefference(getContext()).getKeyApiJwt();
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        if (jwtKey.equals(jwt)){
            sessionPrefference.setIsLogin(false);
            sessionPrefference.logoutUser();
        }
        File file = new File(mImageFileLocation);
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
                        GmeterApi gmeterApi = new GmeterApi();

                        if (total>=85){
                             /*
                             String classfy = String.valueOf(scoreClass);
                            hasilScore.setTextColor(getResources().getColor(R.color.yellow));
                            hasilScore.setText(classfy);

                            String idtfy = String.valueOf(scoreId);
                            hasilIdentify.setTextColor(getResources().getColor(R.color.yellow));
                            hasilIdentify.setText(idtfy);
                              */

                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.yellow));
                            hasilMeter.setText(meterfy);
                            gmeterApi.setMeter(meterfy);
                            gmeterApi.setType(1);
                            insertMeterApi(gmeterApi);

                            Log.d("Upload YELLOW", "String YELLOW  : ");

                        }else {
                            /*
                            String classfy = String.valueOf(scoreClass);
                            hasilScore.setTextColor(getResources().getColor(R.color.de_la_red));
                            hasilScore.setText(classfy);

                            String idtfy = String.valueOf(scoreId);
                            hasilIdentify.setTextColor(getResources().getColor(R.color.de_la_red));
                            hasilIdentify.setText(idtfy);

                            Log.d("Upload RED", "String RED  : ");
                            */
                            String meterfy = String.valueOf(meter);
                            hasilMeter.setTextColor(getResources().getColor(R.color.yellow));
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

//                        hasilMeter.setText(m);
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

                        Ghistoryi ghistoryi = new Ghistoryi();
                        ghistoryi.setMeter(m);
                        ghistoryi.setScore_classfy(classfy);
                        ghistoryi.setScore_identfy(idtfy);
                        ghistoryi.setCreated_at(text);
                        ghistoryi.setId_user(String.valueOf(sessionPrefference.getUserId()));
                        insertDataHistory(ghistoryi);



                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                        String jwtNull = "";
                        sessionPrefference.setKeyApiJwt(jwtNull);
                        sessionPrefference.setIsLogin(false);
                        sessionPrefference.logoutUser();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getActivity(), "TRY AGAINSZCH", Toast.LENGTH_LONG).show();

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

    private void insertDataHistory(Ghistoryi ghistoryi) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertHistoryiData(ghistoryi);
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

    private void insertMeterApi(GmeterApi gmeterApi) {
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
        uploadImage(rotateBitmap, grain_slected);
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