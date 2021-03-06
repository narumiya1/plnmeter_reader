package com.example.myapplicationpln.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.auth.VerifyPhoneActivity;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity2 extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    SessionPrefference sessionPrefference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone2);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar2);
        editText = findViewById(R.id.editTextCode2);

        sessionPrefference = new SessionPrefference(this);

        String phonenumber = getIntent().getStringExtra("phonenumber");


        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyPhoneNumberWithCode(verificationId, code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String id_auth = mAuth.getCurrentUser().getUid();
                            Log.d("DATA CHANGEt id_auth ver ", " - : " + id_auth);
                            String phone = sessionPrefference.getPhone();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phone);
                            if (databaseReference.child("email")!=null){
                                Intent intent = new Intent(VerifyPhoneActivity2.this, FormForgot.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(getApplicationContext(), "create new password", Toast.LENGTH_LONG).show();



                                startActivity(intent);

                            }else if (databaseReference.child("email").equals(null)){

                                Intent intent = new Intent(VerifyPhoneActivity2.this, FormForgot.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(getApplicationContext(), "! Silahkan isi password untuk melanjutkan", Toast.LENGTH_LONG).show();

                                startActivity(intent);


                            }


                        } else {
                            Toast.makeText(VerifyPhoneActivity2.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                10,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyPhoneNumberWithCode(verificationId, code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity2.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}