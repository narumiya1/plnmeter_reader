package com.example.myapplicationpln.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.myapplicationpln.activities.LoginActivity;

public class SessionPrefference {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "app";
    private static final String IS_LOGIN = "IsLoged";

    public static final String KEY_USER = "user";
    public static final String KEY_ALARM = "alarm";
    public static final String KEY_PHONE = "phonenumbr";
    public static final String KEY_PASSWORD = "passowrd";
    public static final String KEY_API_JWT = "jwt";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USERID = "userid";
    public static final String KEY_MAXID = "useridea";
    public static final String KEY_MAXID_BY_FORM = "userideaForm";

    public static final String KEY_IDPELANGGAN = "idpel";


    public SessionPrefference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USERID, "");
    }

    public String getUserAddressId() {
        return pref.getString(KEY_MAXID, "");
    }
    public String getUserAddressIdByFormAddress() {
        return pref.getString(KEY_MAXID_BY_FORM, "");
    }

    public String getIdPelanggan() {
        return pref.getString(KEY_IDPELANGGAN, "");
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }
    public String getKeyApiJwt() {
        return pref.getString(KEY_API_JWT, "");
    }
    public String getImage() {
        return pref.getString(KEY_IMAGE, "");
    }

    public void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    public void setUserId(String userId) {
        editor.putString(KEY_USERID, userId);
        editor.commit();
    }

    public void setIdPelanggan(String idPelanggan) {
        editor.putString(KEY_IDPELANGGAN, idPelanggan);
        editor.commit();
    }

    public void setUserAddressId(String maxId) {
        editor.putString(KEY_MAXID, maxId);
        editor.commit();
    }

    public void setUserAddressIdByFormAddress(String maxId) {
        editor.putString(KEY_MAXID_BY_FORM, maxId);
        editor.commit();
    }

    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }
    public void setKeyApiJwt(String apiJwt) {
        editor.putString(KEY_API_JWT, apiJwt);
        editor.commit();
    }
    public void setImage(String img) {
        editor.putString(KEY_API_JWT, img);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_LOGIN, false);

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void setIsLogin(Boolean v) {
        editor.putBoolean(IS_LOGIN, v);
//        editor.putBoolean(IS_LOGIN, v);
        editor.commit();
    }



}
