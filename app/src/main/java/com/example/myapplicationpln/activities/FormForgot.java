package com.example.myapplicationpln.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MDataUser;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FormForgot extends AppCompatActivity {
    private Button btn;
    private EditText et_insert_forgot_password;
    DatabaseReference databaseReference;
    String phone, passwordUser, id_auth;
    SessionPrefference sessionPrefference;
    MDataUser accounts = new MDataUser();
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_forgot);
        sessionPrefference = new SessionPrefference(getApplicationContext());
        phone  = sessionPrefference.getPhone();
        mAuth = FirebaseAuth.getInstance();
        et_insert_forgot_password = findViewById(R.id.et_insert_forgot_password);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        btn = findViewById(R.id.btn_forgot2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = reference.child("User").child(phone);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            accounts = snapshot.getValue(MDataUser.class);
                            String userId,idPelanggan, name,userName, userEmail, userAlamat,getPhone;
                            userId = accounts.getId_user();
                            idPelanggan = accounts.getId_pelanggan();
                            name = accounts.getName();
                            userName = accounts.getUsername();
                            userEmail = accounts.getEmail();
                            userAlamat = accounts.getAddress();
                            Log.d("Body NAME", "onDataChange: "+accounts.getUsername());
                            passwordUser = et_insert_forgot_password.getText().toString();
                            id_auth = mAuth.getCurrentUser().getUid();
                            MDataUser accounts = new MDataUser(id_auth,userId,idPelanggan, name,userName, userEmail, userAlamat,phone,passwordUser);
                            accounts.setPassword(et_insert_forgot_password.getText().toString());
                            databaseReference.child(phone).setValue(accounts);
                            Intent intent = new Intent(FormForgot.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
