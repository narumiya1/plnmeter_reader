package com.example.myapplicationpln.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.adapter.AddressAdapter;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Gspinner;

import java.util.ArrayList;
import java.util.Arrays;

public class AddressListActivity2 extends AppCompatActivity {
    RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase db;
    private ArrayList<Gspinner> listAddress;
    private RecyclerView.Adapter adapter;
    ImageView add_addressz2;
    SessionPrefference session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list2);

        listAddress = new ArrayList<>();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();

        mRecyclerview = findViewById(R.id.recyclerView2);
        mRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerview.setLayoutManager(layoutManager);

        listAddress.addAll(Arrays.asList(db.gHistorySpinnerDao().readDataAddress()));
        Log.d("adapterz no internet conn", " " + listAddress);
        //Mamasang adapter pada RecyclerView
        adapter= new AddressAdapter(listAddress, this);
        mRecyclerview.setAdapter(adapter);
        session = new SessionPrefference(this);
        String idUserAddress = session.getUserAddressId();
        Log.d("get idUserAddress2 ", " : " + idUserAddress);
        add_addressz2 = findViewById(R.id.add_addressz2);
        add_addressz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity2.this, FormAddress.class);
                intent.putExtra("userId", idUserAddress);
                startActivity(intent);
            }
        });
    }
}
