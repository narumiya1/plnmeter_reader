package com.example.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.preference.SessionPrefference;

public class SplashActivity extends AppCompatActivity {
    private static final int TIME = 3000;
    SessionPrefference session;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(this);
        session = new SessionPrefference(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMain();
            }
        }, TIME);
    }

    void openMain() {
        if (session.isLoggedIn()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent i = null;

            i = new Intent(this, LoginActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Toast.makeText(SplashActivity.this, "Silahkan login kembali !", Toast.LENGTH_LONG).show();

        }
        finish();
//            closeProgress();

    }
    private void showProgress() {

        progressDialog.setMessage("Loading . . .");
        progressDialog.show();

    }

    private void closeProgress() {
        progressDialog.dismiss();
    }

}