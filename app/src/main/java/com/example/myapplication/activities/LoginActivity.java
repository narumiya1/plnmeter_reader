package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.auth.AuthActivity;
import com.example.myapplication.preference.SessionPrefference;
import com.example.myapplication.model.DataUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView signup;
    private EditText editTextPasswordLogin,editTextPhoneLogin;

    //Firebase
    DatabaseReference databaseReference;
    DataUser accounts = new DataUser();
    SessionPrefference sessionPrefference;

    private static final Pattern PW_PATTERN =
            Pattern.compile("^" +
//                    "(?=.*[+])" +     //at least 1 special character
                    ".{6,}"               //at least 4 characters

            );
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_login);
        sessionPrefference = new SessionPrefference(this);

        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.textview_signup);

        editTextPhoneLogin = findViewById(R.id.editTextPhoneLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhone() | !validatePassword() ) {
                    return;
                }

                String phone = editTextPhoneLogin.getText().toString();
                String pw = editTextPasswordLogin.getText().toString();

                Query query = reference.child("User2").child(phone);
                Log.d("Body getUserId login", "login: "+  sessionPrefference.getUserId());

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.d("DATA CHANGEt", "onDataChange: " + snapshot.getValue());
                            accounts = snapshot.getValue(DataUser.class);
                            Log.d("accounts", "accounts: " + accounts.getPhone());
                            if (phone.equals(accounts.getPhone()) && pw.equals(accounts.getPassword())){
                                sessionPrefference.setIsLogin(true);
                                sessionPrefference.setPhone(phone);
                                sessionPrefference.setUserId(accounts.getId_user());
                                Log.d("setPhone phone", "phone: " + editTextPhoneLogin.getText().toString());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(LoginActivity.this, "Nomor/Password tidak sesuai, silahkan cek kembali  !", Toast.LENGTH_LONG).show();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

    }


    private boolean validatePhone(){
        String numberPhoneInput = editTextPhoneLogin.getText().toString().trim();
        if (numberPhoneInput.isEmpty()) {
            editTextPhoneLogin.setError("number phone can't be empty");
            return false;
        } else if (!PW_PATTERN.matcher(numberPhoneInput).matches()) {
            editTextPhoneLogin.setError("nomor harus di awali +62 ");
            return false;
        } else {
            editTextPhoneLogin.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = editTextPasswordLogin.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            editTextPasswordLogin.setError("Field can't be empty");
            return false;
        } else if (!PW_PATTERN.matcher(passwordInput).matches()) {
            editTextPasswordLogin.setError("Password too weak");
            return false;
        } else {
            editTextPasswordLogin.setError(null);
            return true;
        }
    }

    private void openMain() {
        if (sessionPrefference.isLoggedIn()) {
//            session.setValuesz("status","1");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
