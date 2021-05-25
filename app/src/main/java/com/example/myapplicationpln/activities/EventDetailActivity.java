package com.example.myapplicationpln.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Gspinner;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

public class EventDetailActivity extends AppCompatActivity {
    EditText id, alamatl;
    TextView address_updated;
    Button update;
    private DatabaseReference database;
    private AppDatabase db;

    private Query mUserDatabase;
    private FirebaseDatabase mDatabase;
    SessionPrefference sessionPrefference;
    String idUser;
    String userAddressId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_actvity);
        Intent intent = getIntent();
        String address = intent.getStringExtra("userAddress");
        Log.d("DATA userAddress ", "userAddress: " + address);
        String idPel = intent.getStringExtra("userDetailId");
        Log.d("DATA  userDetailId ", "userDetailId: " + idPel);
        userAddressId = intent.getStringExtra("userAddressId");
        Log.d("DATA  get userAddressId ", "userAddressId: " + userAddressId);

        address_updated=findViewById(R.id.id_AddressUpdated);
        db = Room.databaseBuilder(getBaseContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();

        int selection = db.gHistorySpinnerDao().selectIndeks(Integer.valueOf(userAddressId)) ;
        Log.d("Body selections ", " selection : "+selection+" ");
        sessionPrefference = new SessionPrefference(this);
        id = findViewById(R.id.idp_el_detail);
        alamatl = findViewById(R.id.alamat_detail);
        update = findViewById(R.id.button_udpate);
        database = FirebaseDatabase.getInstance().getReference();
        idUser = sessionPrefference.getUserId();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();


//        mDatabase = FirebaseDatabase.getInstance();
//        mUserDatabase = mDatabase.getReference().child("Address").orderByChild("id_user").equalTo(idUser);

        id.setText(idPel);
        alamatl.setText(address);
        address_updated.setText(userAddressId);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String update_alamat = alamatl.getText().toString();
                String id_pln_update = id.getText().toString();
                int rowGrainType = db.gHistorySpinnerDao().getCount(Integer.parseInt(idPel));
                if(rowGrainType > 0){
                    //update to room

                    int idpels=Integer.valueOf(id_pln_update);
                    int userAddressIdIdx=Integer.valueOf(userAddressId);

                    Gspinner idx = new Gspinner();
//                    idx.setId_pelanggan(idpels);
//                    updateSelectedGrain(idx);
                    int id = selection;
                    int id_pelanggan = idx.setId_pelanggan(idpels);
                    String alamat = idx.setAddress_update(update_alamat);
                    Date date = new Date();
                    db.gHistorySpinnerDao().updateIndeks(userAddressIdIdx,update_alamat,idpels);
                    Log.d("AAVAIL", " idx "+idx.getId_pelanggan());
                }else {
                    Log.d("AAABL", "idx "+rowGrainType);
                }





                /*
                get address id
                mDatabaseRef.child("Address").child(String.valueOf(1)).child("alamat_pelanggan").setValue(String.valueOf(update_alamat));
                 */
                mDatabaseRef.child("Address").child(userAddressId).child("alamat_pelanggan").setValue(String.valueOf(update_alamat));
                mDatabaseRef.child("Address").child(userAddressId).child("id_pelanggan").setValue(String.valueOf(id_pln_update));
                mDatabaseRef.child("Address").child(userAddressId).child("user_address_id").setValue(String.valueOf(userAddressId));
                /* user db 2 not updated
                mDatabaseRef.child("User2").child(sessionPrefference.getPhone()).child("address").setValue(String.valueOf(update_alamat));
                mDatabaseRef.child("User2").child(sessionPrefference.getPhone()).child("id_pelanggan").setValue(String.valueOf(id_pln_update));
                 */

                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

    }

    private void updateSelectedGrain(Gspinner idx) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateGrainSelected(idx);
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


}
