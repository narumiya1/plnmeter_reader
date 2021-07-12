package com.example.myapplicationpln.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MHistoryActivity extends Fragment {

    private RecyclerView courseRV;
    private ArrayList<MHistory> dataModalArrayList;
    private HistoryFragment3 dataRVAdapter;
    private DatabaseReference db;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    SessionPrefference session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mhistory, container, false);

        courseRV = view.findViewById(R.id.idRVItems);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseDatabase.getInstance().getReference();

        // creating our new array list
        dataModalArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);

        // adding horizontal layout manager for our recycler view.
        courseRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // adding our array list to our recycler view adapter class.
        dataRVAdapter = new HistoryFragment3(dataModalArrayList, getActivity());

        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);

        loadrecyclerViewData();
        return view;
    }

    private void loadrecyclerViewData() {
        session = new SessionPrefference(getActivity());
        String userId = session.getUserId();
        /*
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("History");
        DatabaseReference referenceHistor = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mDatabase.getReference().child("History").orderByChild("id_user").equalTo(userId);
        mUserDatabase2 = mDatabase.getReference().child("History");
        referenceHistor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataModalArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MHistory modelCourses1 = dataSnapshot1.getValue(MHistory.class);
                    dataModalArrayList.add(modelCourses1);
                    dataRVAdapter = new HistoryFragment3(dataModalArrayList, getActivity());
                    courseRV.setAdapter(dataRVAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query yourRef = rootRef.child("History").orderByChild("id_user").equalTo(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("meter").getValue(String.class);
                dataModalArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MHistory modelCourses1 = dataSnapshot1.getValue(MHistory.class);
                    dataModalArrayList.add(modelCourses1);
                    dataRVAdapter = new HistoryFragment3(dataModalArrayList, getActivity());
                    courseRV.setAdapter(dataRVAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        yourRef.addListenerForSingleValueEvent(eventListener);
    }


}
