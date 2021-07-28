package com.example.myapplicationpln.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class MConnection {
    public static boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected() || activeNetworkInfo.isConnectedOrConnecting()) {
//                    android.widget.Toast.makeText(context, "response.message()", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }

    public static boolean isConnecteds() {
        boolean connected = false;
        Context context = null;
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

    }


}
