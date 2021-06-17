package com.example.myapplicationpln.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.adapter.AddressAdapter;
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
    }
}
