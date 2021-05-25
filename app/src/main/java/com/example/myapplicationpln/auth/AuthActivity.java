package com.example.myapplicationpln.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.activities.LoginActivity;
import com.example.myapplicationpln.activities.RegisterActivity;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthActivity extends AppCompatActivity {
    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    private Spinner spinner;
    private EditText editText;
    private TextView tv_login;
    private TextView mTextView;
    SessionPrefference sessionPrefference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        sessionPrefference = new SessionPrefference(this);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, PhoneCourtyCode.countryNamesz));
        spinner.setRight(12);
        spinner.setGravity(12);
        tv_login= findViewById(R.id.tv_login_reg);
        editText = findViewById(R.id.editTextPhone);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = PhoneCourtyCode.countryAreaCodesz[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User2");
//                sessionPrefference.setPhone(number);
                if (number.isEmpty()) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;
                Log.d("Body phoneNumber ", " phoneNumber : " + phoneNumber + " ");
                sessionPrefference.setPhone(phoneNumber);
                Intent intent = new Intent(AuthActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);

            }
        });

        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID, verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }

    //ii
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseApp.initializeApp(this);
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivity(intent);
//        }
//    }

}
