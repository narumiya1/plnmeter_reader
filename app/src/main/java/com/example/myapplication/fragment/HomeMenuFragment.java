package com.example.myapplication.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.myapplication.R;
import com.example.myapplication.location.LocationTracking;
import com.example.myapplication.roomDb.AppDatabase;
import com.example.myapplication.roomDb.Gspinner;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeMenuFragment extends Fragment {

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList <String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList<Gspinner> listGrainType;
    private AppDatabase db;

    private static final String TAG = "CalculatorFragment";
    LocationTracking locationTracking;
    private Spinner list;
    StringBuilder input, top;
    TextView disp, disp2;
    View layout;
    boolean eqlFlag = false;
    ArrayList<String> arrayListz;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase,mUserDatabase2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
//        layout =inflater.inflate(R.layout.fragment_home_menu, container,false);

        ceklocation();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTracking = new LocationTracking(getActivity());
        list = view.findViewById(R.id.listItemz);
        //1 Arraylist
        arrayListz = new ArrayList<>();
        arrayListz.add("1123456");
        arrayListz.add("9876542");
        arrayListz.add("4345679");

        listGrainType = new ArrayList<>();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_5)
                .build();
        List<Integer> lables = db.gHistorySpinnerDao().getAllLItems();

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, lables);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        return view;
    }

    private void showDataInSpinner(ArrayList<String> listString) {
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, listString);
//        adapter.addAll( listGrainType.addAll(Arrays.asList(db.gHistorySpinnerDao().selectAllItems())));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
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
                return (ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED);
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
            Log.d("Body Longitude", "longitude : "+longitude+"& "+latitude);
        } else {
            locationTracking.showSettingsAlert();
        }
    }

}