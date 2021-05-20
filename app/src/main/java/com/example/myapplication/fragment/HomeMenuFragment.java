package com.example.myapplication.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import androidx.room.Room;

import com.example.myapplication.R;
import com.example.myapplication.location.LocationTracking;
import com.example.myapplication.model.DataUser;
import com.example.myapplication.model.IndeksSpinnrFirebase;
import com.example.myapplication.model.PelangganyAlamat;
import com.example.myapplication.preference.SessionPrefference;
import com.example.myapplication.roomDb.AppDatabase;
import com.example.myapplication.roomDb.GIndeksSpinner;
import com.example.myapplication.roomDb.Gspinner;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class HomeMenuFragment extends Fragment {

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList<Gspinner> listGrainType;
    private AppDatabase db;

    private static final String TAG = "CalculatorFragment";
    LocationTracking locationTracking;
    StringBuilder input, top;
    TextView disp, disp2;
    View layout;
    boolean eqlFlag = false;
    ArrayList<String> arrayListz;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    PelangganyAlamat dataUser = new PelangganyAlamat();
    SessionPrefference sessionPrefference;
    String selectedValue;
    PhotoView photoView;
    private Uri image_uri;
    private String mImageFileLocation = "";
    private Dialog dialog;
    ImageButton add_photo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
//        layout =inflater.inflate(R.layout.fragment_home_menu, container,false);
        add_photo = view.findViewById(R.id.imageview_add);
        photoView=view.findViewById(R.id.photo_view);
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
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        // 19 05
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();
        locationTracking = new LocationTracking(getActivity());
        final Spinner list = view.findViewById(R.id.listItemz);
        //1 Arraylist
        arrayListz = new ArrayList<>();
        arrayListz.add("1123456");
        arrayListz.add("9876542");
        arrayListz.add("4345679");

        listGrainType = new ArrayList<>();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
//        List<Integer> lables = db.gHistorySpinnerDao().getAllLItems();
        Integer[] myArrays = db.gHistorySpinnerDao().getAllLItemsArray();
        Log.d("Body myArrays ", " myArrays : " + myArrays + " ");

        // call spinner from firebase
        sessionPrefference = new SessionPrefference(getActivity());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Address").orderByChild("id_user").equalTo(sessionPrefference.getUserId());
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

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), " " + areasAdapter.getItem(i) + " choosen ", Toast.LENGTH_SHORT).show();
                        Log.d("DATA CHANGE choosen", "choosen: " + areasAdapter.getItem(i) + " choosen");

                        String value = areasAdapter.getItem(i);
                        String id_user = sessionPrefference.getUserId();

                        //firebase check if data > 0 , insert
                        if (lenght == 0) {
                            Log.d("DATA CHANGE insertz", "insertz: ");
                        } else {
                            Log.d("DATA CHANGE updatez", "updatez: ");
                            Toast.makeText(getActivity().getApplicationContext(), "value if " + value +" choosen", Toast.LENGTH_LONG).show();

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


                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
                    photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

            }

        }
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

// tambahkan login web api disini
        // dicek apabila login berhasil jalankan uploadimage
        // 110.50.85.28:8200/account/login
        // http://110.50.85.28:8200/account/login
        // x-www-form-urlencoded
        // Phone : +6281907123427
        // Password : 1m4dm1n
        // post

        photoView.setImageBitmap(rotateBitmap);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*
        cek jenis grain
        if (beras = panggil function upload image beras)
        else if (kopi = uploadImagekopi)
        else if(gandum = uploadImageGandum)
        dibuatkan upload image
        */

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