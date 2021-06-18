package com.example.myapplicationpln.model;

import android.content.Context;

public class Toastr {
    public static void showToast(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
