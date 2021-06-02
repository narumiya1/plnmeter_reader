package com.example.myapplicationpln.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.History;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HistoryFragment extends Fragment {

    private View mView;
    RecyclerView mRecyclerview;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private LinearLayoutManager mManager;
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private Query needsQuery;
    private Query offersQuery;
    SessionPrefference session;
    private Dialog dialog;
    DatabaseReference databaseReference2;


    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyi, container, false);
        // Inflate the layout for this fragment

        dialog = new Dialog(getActivity());

        session = new SessionPrefference(getActivity());
        mRecyclerview = view.findViewById(R.id.recyclerViewHistory);
        mRecyclerview.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);

        Log.e(TAG, "firebaseoption");

        //add string & get from session
        String userId = session.getUserId();
        Log.d("get userzId Historiy", " : " + userId);

        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mDatabase.getReference().child("History").orderByChild("id_user").equalTo(userId);
        mUserDatabase2 = mDatabase.getReference().child("History");

        /*
        Rumus Firebase one to many
        mUserDatabase = mDatabase.getReference().child("User").orderByChild("address").equalTo("Kilmarnock");
        firebase.database().ref('Address').orderBy('User').equalTo('id');
        needsQuery = mUserDatabase.orderByChild("address").equalTo("Kilmarnock");
        offersQuery = mUserDatabase.orderByChild("type").equalTo("offer");
         */


        FirebaseRecyclerOptions<History> options =
                new FirebaseRecyclerOptions.Builder<History>().
                        setQuery(mUserDatabase, History.class).
                        build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<History, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull History model) {
                holder.setFname(model.getMeter());
                holder.setLname(model.getScore_classfy());
                holder.setAname(model.getScore_identfy());
                holder.setDate(model.getCreated_at());

                Log.d("get getMeter ", " : " + model.getMeter());

                /*
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Context context = view.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("userAddress", model.getAlamat_pelanggan());
                        intent.putExtra("userDetailId", model.getId_pelanggan());
                        intent.putExtra("userAddressId", model.getUser_address_id());

                        Log.d("Body model model", "model: " + model.getAlamat_pelanggan());
                        context.startActivity(intent);


                    }
                });

                 */
            }


            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_history, parent, false);
                Log.e(TAG, "oncreate is called");

                return new UserViewHolder(view);
            }
        };
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    //ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardView = itemView.findViewById(R.id.cvMain_addresss);

            /*
                mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, EventDetailActivity.class);

                    context.startActivity(intent);
                }
              });

             */
        }

        public void setFname(String name) {
            TextView FName = (TextView) mView.findViewById(R.id.rMeter);
            FName.setText(name);
        }


        public void setLname(String name) {
            TextView LName = (TextView) mView.findViewById(R.id.rIdentify);
            LName.setText(name);
        }

        public void setAname(String name) {
            TextView AName = (TextView) mView.findViewById(R.id.rClassify);
            AName.setText(name);
        }

        public void setDate(String d) {
            TextView DName = (TextView) mView.findViewById(R.id.txtDates);
            DName.setText(d);
        }

        public void setOrderSpinner(String status) {

        }

    }


}