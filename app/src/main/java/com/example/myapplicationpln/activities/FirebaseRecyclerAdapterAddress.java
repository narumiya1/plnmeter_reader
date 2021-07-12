package com.example.myapplicationpln.activities;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MConnection;
import com.example.myapplicationpln.model.MPelangganyAlamat;
import com.example.myapplicationpln.model.MToastr;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseRecyclerAdapterAddress extends AppCompatActivity {

    private View mView;
    RecyclerView mRecyclerview;
    ImageView add_address, onBackPressezd;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private LinearLayoutManager mManager;
    private static final String TAG = FirebaseRecyclerAdapterAddress.class.getSimpleName();
    private Query needsQuery;
    private Query offersQuery;
    SessionPrefference session;
    private Dialog dialog;
    DatabaseReference databaseReference2;


    public FirebaseRecyclerAdapterAddress() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        dialog = new Dialog(getApplicationContext());

        /*
        onBackPressezd = findViewById(R.id.onBackPressedz);
        onBackPressezd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content, new UserDataFragment()).commit();
                }
                Intent i = new Intent(AddressListActivity2.this, MainActivity.class);
                startActivity(i);
            }
        });
        */
        session = new SessionPrefference(this);
        mRecyclerview = findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerview.setLayoutManager(mManager);

        Log.e(TAG, "firebaseoption");

        //add string & get from session
        String userAddressIdPhone = session.getPhone();
        Log.d("get idUserAddress ", " : " + userAddressIdPhone);
        String idUserAddress = session.getUserAddressId();
        Log.d("get idUserAddress ", " : " + idUserAddress);
        String userId = session.getUserId();
        Log.d("get userzId ", " : " + userId);
        add_address = findViewById(R.id.add_addressz);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirebaseRecyclerAdapterAddress.this, FormAddress.class);
                intent.putExtra("userId", idUserAddress);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mDatabase.getReference().child("Address").orderByChild("id_user").equalTo(userId);
        mUserDatabase2 = mDatabase.getReference().child("Address");

        /*
        Rumus Firebase one to many
        mUserDatabase = mDatabase.getReference().child("User").orderByChild("address").equalTo("Kilmarnock");
        firebase.database().ref('Address').orderBy('User').equalTo('id');
        needsQuery = mUserDatabase.orderByChild("address").equalTo("Kilmarnock");
        offersQuery = mUserDatabase.orderByChild("type").equalTo("offer");
         */


        FirebaseRecyclerOptions<MPelangganyAlamat> options =
                new FirebaseRecyclerOptions.Builder<MPelangganyAlamat>().
                        setQuery(mUserDatabase, MPelangganyAlamat.class).
                        build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MPelangganyAlamat, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull MPelangganyAlamat model) {
                holder.setFname(model.getId_pelanggan());
                holder.setLname(model.getAlamat_pelanggan());
                holder.setAname(model.getUser_address_id());


                if (MConnection.isConnect(getApplicationContext())) {


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
                }else {
                    MToastr.showToast(getApplicationContext(),"No internet connection");
                }
            }


            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_address, parent, false);
                Log.e(TAG, "oncreate is called");

                return new UserViewHolder(view);
            }
        };
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);

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
            cardView = itemView.findViewById(R.id.cvMain_address);

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
            TextView FName = (TextView) mView.findViewById(R.id.rTitleTv);
            FName.setText(name);
        }


        public void setLname(String name) {
            TextView LName = (TextView) mView.findViewById(R.id.rDescriptionTv);
            LName.setText(name);
        }

        public void setAname(String name) {
            TextView AName = (TextView) mView.findViewById(R.id.rvUserAddtressId);
            AName.setText(name);
        }

        public void setOrderSpinner(String status) {

        }

    }


}
