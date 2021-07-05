package com.example.myapplicationpln.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MDataUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddressListActivity extends AppCompatActivity {

    ImageView add_address, onBackPressedzc;
    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    DatabaseReference databaseReference;
    String name, address;
    private ArrayList<MDataUser> listGrainType;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(mLayoutManager);

        //send Query to FirebaseDatabase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference dbPreference = FirebaseDatabase.getInstance().getReference();

        listGrainType = new ArrayList<>
                ();


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:613164718268:android:d9288fda50ba75a7e598b9")
                .setProjectId("plnmeters")
                .setDatabaseUrl("https://plnmeters-default-rtdb.firebaseio.com/") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this);
        // Retrieve my other app.
        FirebaseApp app = FirebaseApp.getInstance("[DEFAULT]");
        // Get the database for the other app.
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        DatabaseReference data = secondaryDatabase.getInstance().getReference();
        data.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    for (DataSnapshot dSnapshot : ds.getChildren()) {

                        MDataUser waterClass = dSnapshot.getValue(MDataUser.class);

                        Log.d("Show", waterClass.getName() == null ? "" : waterClass.getName());
                        Log.d("Show getAddress ", waterClass.getAddress() == null ? "" : waterClass.getAddress());
                        String addrs = waterClass.getAddress();
                        String names = waterClass.getName();
                        listGrainType.add(waterClass);
                        names = waterClass.getName();
                        address = waterClass.getAddress();

                    }
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        add_address = findViewById(R.id.add_addressz);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.this, FormAddressWithSpinnerApi.class);
                startActivity(intent);
            }
        });

        onBackPressedzc = findViewById(R.id.onBackPressedzc);
        onBackPressedzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
