package com.example.myapplicationpln.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.activities.AddressListActivity2;
import com.example.myapplicationpln.activities.FormAddress;
import com.example.myapplicationpln.adapter.AddressAdapter;
import com.example.myapplicationpln.adapter.HistoryAdapter2;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Ghistoryi;
import com.example.myapplicationpln.roomDb.Gspinner;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryFragment2 extends Fragment {
    RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase db;
    private ArrayList<Ghistoryi> listHistory;
    private RecyclerView.Adapter adapter;
    SessionPrefference session;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyi2, container, false);
        listHistory = new ArrayList<>();
        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();

        mRecyclerview = view.findViewById(R.id.recyclerViewHistory2);
        mRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(layoutManager);
        listHistory.addAll(Arrays.asList(db.gHistorySpinnerDao().readDataHistory()));
        Log.d("historiyi "," list : "+listHistory);
        //Mamasang adapter pada RecyclerView
        adapter= new HistoryAdapter2(listHistory, getActivity());
        mRecyclerview.setAdapter(adapter);
        session = new SessionPrefference(getActivity());
        String idUserAddress = session.getUserAddressId();
        Log.d("get idUserAddress2 ", " : " + idUserAddress);
        return view;
    }
}
