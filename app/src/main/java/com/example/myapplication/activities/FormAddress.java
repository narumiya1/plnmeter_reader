package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.DataUser;
import com.example.myapplication.model.PelangganyAlamat;
import com.example.myapplication.preference.SessionPrefference;
import com.example.myapplication.roomDb.AppDatabase;
import com.example.myapplication.roomDb.GIndeksSpinner;
import com.example.myapplication.roomDb.Gspinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class FormAddress extends AppCompatActivity {
    EditText adddress, id_pelanggan_new, desa, rt_rw, kec, kota_kab, prov;
    Button send;
    private AppDatabase db;

    DatabaseReference databaseReference2;

    SessionPrefference sessionPrefference;

    PelangganyAlamat alamatPelanggan;


    long maxId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_address);

        adddress = findViewById(R.id.addresselanggan);
        id_pelanggan_new=findViewById(R.id.id_pelanggan_new);
        String addres_pelanggan = adddress.getText().toString();

        Intent intent = getIntent();
        String id = intent.getStringExtra("userId");
        Log.d("DATA intentz userIdea ", "idea: " +id);

        sessionPrefference = new SessionPrefference(this);
        alamatPelanggan = new PelangganyAlamat();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Address");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        alamatPelanggan.setAlamat_pelanggan(addres_pelanggan);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxId = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        adddress = findViewById(R.id.addressz);
        desa = findViewById(R.id.desa);
        rt_rw = findViewById(R.id.rt_rw);
        kec = findViewById(R.id.kec);
        kota_kab = findViewById(R.id.kota);
        prov = findViewById(R.id.prov);

        Log.d("DATA CHANGEt phone", "onDataChange: " + sessionPrefference.getPhone());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> address_user = new ArrayList<String>();
                String sessionId = adddress.getText().toString();
                String desaId = desa.getText().toString();
                String rt_rwnId = rt_rw.getText().toString();
                address_user.add(sessionId);
                address_user.add(desaId);
                address_user.add(rt_rwnId);
                address_user.add(kec.getText().toString());
                address_user.add(kota_kab.getText().toString());
                address_user.add(prov.getText().toString());
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
//                intent.putExtra("EXTRA_SESSION_ID", adddress.getText().toString());
                intent.putStringArrayListExtra("EXTRA_SESSION_ID", address_user);

                Log.d("DATA CHANGEt g", "onDataChange: " + adddress.getText().toString());
                Log.d("DATA CHANGEt alamat", "onDataChange: " + address_user);

                startActivity(intent);

            }
        });

        */



        send = findViewById(R.id.btnSendAlamat);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamat, id_user ;
                id_user = sessionPrefference.getUserId();
                alamat = adddress.getText().toString();
                String id_pelanggan = id_pelanggan_new.getText().toString();
                String userAddressId = String.valueOf(maxId+1);

                int idpel=Integer.valueOf(id_pelanggan);
                int userAddressIdIdx=Integer.valueOf(userAddressId);

                Date dates = new Date();

                Gspinner spinner = new Gspinner();
                spinner.setId_pelanggan(idpel);
                spinner.setAddress_update(alamat);
                spinner.setCreatedAt(dates);
                spinner.setUser_address_id(userAddressIdIdx);
                insertIdx(spinner);

                Log.d("DATA intentz userIdea ", "idea: " +alamatPelanggan.getAlamat_pelanggan());
                Log.d("DATA intentzid_user id_user ", "id_user: " +id_user);

                PelangganyAlamat pelangganyAlamat1 = new PelangganyAlamat(alamat,id_user,id_pelanggan,sessionPrefference.getPhone(), userAddressId);
                databaseReference2.child(userAddressId).setValue(pelangganyAlamat1);

                Intent intent1 = new Intent(FormAddress.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }



    private void insertIdx(Gspinner idx) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertIdx(idx);
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
