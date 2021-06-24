package com.example.myapplicationpln.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.auth.AuthActivity;
import com.example.myapplicationpln.auth.PhoneCourtyCode;
import com.example.myapplicationpln.auth.VerifyPhoneActivity;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {
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
        setContentView(R.layout.forgot_passrword);

        sessionPrefference = new SessionPrefference(this);
        spinner = findViewById(R.id.spinnerCountriesforgotten);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, PhoneCourtyCode.countryNamesz));
        spinner.setRight(12);
        spinner.setGravity(12);
        editText = findViewById(R.id.editTextPhoneforgotten);
        findViewById(R.id.buttonContinueforgotten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = PhoneCourtyCode.countryAreaCodesz[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString();
                if (number.isEmpty()) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;
                Log.d("Body phoneNumber ", " phoneNumber : " + phoneNumber + " ");
                sessionPrefference.setPhone(phoneNumber);
                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyPhoneActivity2.class);
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
}
