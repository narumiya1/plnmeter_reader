package com.example.myapplicationpln.model;

import android.content.Context;
import android.widget.Toast;

public class Toastr {
    public static void showToast(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
    public static void showToastLong(Context context, String message) {
        android.widget.Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
