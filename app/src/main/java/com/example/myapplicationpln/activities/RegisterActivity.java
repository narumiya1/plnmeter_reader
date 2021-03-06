package com.example.myapplicationpln.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MPelangganyAlamat;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.model.MDataUser;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GUserData;
import com.example.myapplicationpln.roomDb.Gspinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameA, id_user,id_pelanggan, username, email, alamat, password, retype_password, mobilephone;
    String phone, id_auth; FirebaseAuth mAuth;
    private TextView addresses ;
    private TextView tv_login ;
    private Button btn_register ;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    ".{6,}"               //at least 4 characters
            );

    //Firebase
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;

    //room db
    private AppDatabase db;

    MDataUser accounts = new MDataUser();
    MPelangganyAlamat MPelangganyAlamat = new MPelangganyAlamat();

    SessionPrefference sessionPrefference ;

    long maxIdAddress;
    long maxIdUser ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_reg);
        nameA = findViewById(R.id.et_insert_nama);
        username = findViewById(R.id.et_insert_username) ;
        id_pelanggan = findViewById(R.id.id_pelanggan);
        email =  findViewById(R.id.et_insert_email);
        alamat=  findViewById(R.id.et_insert_alamat);
        password =  findViewById(R.id.et_insert_password);
        retype_password= findViewById(R.id.et_insert_password_retype);
        mobilephone = findViewById(R.id.userPhone);
        btn_register = findViewById(R.id.btn_register);
        tv_login = findViewById(R.id.textview_login);
        mAuth = FirebaseAuth.getInstance();
        id_auth = mAuth.getCurrentUser().getUid();

        addresses = findViewById(R.id.et_insert_address);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        sessionPrefference = new SessionPrefference(getApplicationContext());

        //Firebase DBReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxIdUser = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Address");
        DatabaseReference dbPreference = FirebaseDatabase.getInstance().getReference();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxIdAddress = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");

        StringBuilder builders = new StringBuilder();


        if( getIntent().getExtras() != null)
        {
            //do here
            ArrayList<String> alamatz = getIntent().getStringArrayListExtra("EXTRA_SESSION_ID");
            builders.append("Jalan ").append(alamatz.get(0)).append(", Kp/Desa ").append(alamatz.get(1)).append(", ").append("RT/RW").append(" "+alamatz.get(2)).append(", Kecamatan ").append(" ").append(alamatz.get(3)).append(",Kab/Kota ").append(alamatz.get(4)).append(" ,").append(" Prov ").append(alamatz.get(5)).append("\t\r\n");
            addresses.setText(builders);
            Log.d("DATA CHANGEt alamat", "onDataChange: " + alamatz.get(1));

            //db3
            Toast.makeText(this, "CALL DB " +alamatz,Toast.LENGTH_SHORT).show();

        }else {
            addresses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone  = sessionPrefference.getPhone();
                    sessionPrefference.setPhone(phone);
                    Intent intent = new Intent(RegisterActivity.this, FormAddress.class);
                    startActivity(intent);
                }
            });
            Toast.makeText(this, "NULL",Toast.LENGTH_SHORT).show();
        }


        /*
        if (addresses.equals(null)){
            Toast.makeText(this, "NULL",Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(this, "CALL DB",Toast.LENGTH_SHORT).show();
        }
        */

         phone  = sessionPrefference.getPhone();
        String address  = alamat.getText().toString();
        mobilephone.setText(phone);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validateUsername() | !validatePassword()) {
                    return;
                }

                String userId;
                String idPelanggan;
                String name;
                String userName;
                String userEmail;
                String userAlamat;
                String userPhone;
                String passwordUser;
                String retypePasswordUser;
                String userPhoneReg;
                String userAddressId;
                Log.d("DATA  id_auth regh", "id_auth: " + id_auth);
                String id = id_auth;
                userId = String.valueOf(maxIdUser);
                userAddressId = String.valueOf(maxIdAddress);
                idPelanggan = id_pelanggan.getText().toString();
                name = nameA.getText().toString();
                userName = username.getText().toString();
                userEmail = email.getText().toString();
                userAlamat = alamat.getText().toString();
                passwordUser = password.getText().toString();
                userPhoneReg = mobilephone.getText().toString();
                sessionPrefference.setPhone(phone);

//                userPhone = sessionPrefference.getPhone();

                String getPhone = phone;
                Log.d("Body getPhone", "Response: "+getPhone);

                retypePasswordUser = retype_password.getText().toString();

                if (!TextUtils.isEmpty(passwordUser) && !TextUtils.isEmpty(retypePasswordUser)) {
                    if (passwordUser.equals(retypePasswordUser)) {
                        Toast.makeText(RegisterActivity.this, " Isi data suksesz", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, "password doesnt match", Toast.LENGTH_SHORT).show();
                    }
                }

                //db1
                Log.d("DATAz userId", "userId: " + userId);
                MDataUser accounts = new MDataUser(id_auth,userId,idPelanggan, name,userName, userEmail, userAlamat,getPhone, passwordUser);
                databaseReference.child(phone).setValue(accounts);
                // user db room
                int staatus = 1;
                String stats = String.valueOf(staatus) ;
                GUserData gUserData = new GUserData();
                gUserData.setId_user(Integer.parseInt(userId));
                gUserData.setName(name);
                gUserData.setUsername(userName);
                gUserData.setEmail(userEmail);
                gUserData.setAddress(userAlamat);
                gUserData.setPhone(getPhone);
                gUserData.setId_pel(idPelanggan);
                gUserData.setStatus(stats);
                insertData(gUserData);

                //db2 edit 19 05 2021
                MPelangganyAlamat MPelangganyAlamat1 = new MPelangganyAlamat(userAlamat,userId,idPelanggan,sessionPrefference.getPhone(), userAddressId);
                databaseReference2.child(sessionPrefference.getPhone()).child(userAddressId).setValue(MPelangganyAlamat1);
//                PelangganyAlamat pelangganyAlamat1 = new PelangganyAlamat(userId,idPelanggan,userAlamat,userAddressId);
//                databaseReference2.child(userAddressId).setValue(pelangganyAlamat1);

                sessionPrefference.setUserId(userId);
                sessionPrefference.setUserAddressId(userAddressId);
                sessionPrefference.setIdPelanggan(idPelanggan);
                sessionPrefference.setPhone(getPhone);
                // reference.ref(address).orderBy(userPhone).equalTo(userPhone);
                Log.d("Body userId", "userId: "+userId);
                Log.d("Body userAddressId", "userAddressId: "+userAddressId);
                Log.d("Body idPelanggan", "idPelanggan: "+idPelanggan);
                Log.d("Body phone", "phone: "+phone);

                sessionPrefference.setIsLogin(true);

                //room db
                int idpel=Integer.valueOf(idPelanggan);
                Gspinner spinner = new Gspinner();
                spinner.setId_pelanggan(idpel);
                spinner.setAddress_update(userAlamat);
                insertIdx(spinner);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "Register Succes", Toast.LENGTH_LONG).show();

            }
        });



        login();

        Query query = reference.child("User").child(phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    accounts = snapshot.getValue(MDataUser.class);
                    nameA.setText(accounts.getUsername());
                    alamat.setText(accounts.getAddress());
                    email.setText(accounts.getEmail());
                    Log.d("Body NAME", "onDataChange: "+accounts.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        Query query2 = dbPreference.child("Address").child(address);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pelangganyAlamat = snapshot.getValue(PelangganyAlamat.class);
//                    alamat.setText(pelangganyAlamat.getAlamat_pelanggan());
                    Log.d("Body NAME", "onDataChange: "+pelangganyAlamat.getAlamat_pelanggan());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

    }

    private void insertData(GUserData gUserData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertUserData(gUserData);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " + status);
            }
        }.execute();
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


    private void login() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intents);
            }
        });

    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = nameA.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            nameA.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 40) {
            nameA.setError("Username too long");
            return false;
        } else {
            nameA.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();
        String passwordReType = retype_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else if(!passwordInput.equals(passwordReType)){
            retype_password.setError("Password doesn't match");
            Toast.makeText(RegisterActivity.this, "passwordd doesnt match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
}
