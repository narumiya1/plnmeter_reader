package com.example.myapplicationpln.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.auth.AuthActivity;
import com.example.myapplicationpln.auth.PhoneCourtyCode;
import com.example.myapplicationpln.cookies.AddCookiesInterceptor;
import com.example.myapplicationpln.cookies.JavaNetCookieJar;
import com.example.myapplicationpln.cookies.ReceivedCookiesInterceptor;
import com.example.myapplicationpln.cookies.TokenInterceptor;
import com.example.myapplicationpln.network_retrofit.ApiClient;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.model.MDataUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView signup;
    private TextView forgot_pswrd;
    private EditText editTextPasswordLogin,editTextPhoneLogin;
    //Firebase
    DatabaseReference databaseReference;
    MDataUser accounts = new MDataUser();
    SessionPrefference sessionPrefference;
    private Spinner spinner;
    private static final Pattern PW_PATTERN =
            Pattern.compile("^" +
//                    "(?=.*[+])" +     //at least 1 special character
                    ".{6,}"               //at least 4 characters

            );
    ProgressDialog progressDialog;
    String jwt;
    String number;
    String phoneNumb;
    final Handler handler = new Handler(Looper.getMainLooper());
    private String mImageFileLocation = "";
    String urlDomain = "http://110.50.86.154:8200";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_login);
        sessionPrefference = new SessionPrefference(this);
        spinner = findViewById(R.id.spinnerNumber);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, PhoneCourtyCode.countryNamesz));
        spinner.setRight(12);
        spinner.setGravity(12);
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.textview_signup);
        forgot_pswrd=findViewById(R.id.forgot_pasword);
        forgot_pswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        editTextPhoneLogin = findViewById(R.id.editTextPhoneLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhone() | !validatePassword() ) {
                    return;
                }

                String phone = editTextPhoneLogin.getText().toString();
                String pw = editTextPasswordLogin.getText().toString();
                String code = PhoneCourtyCode.countryAreaCodesz[spinner.getSelectedItemPosition()];
                number = editTextPhoneLogin.getText().toString();
                phoneNumb = "+" + code + number;
                Log.d("phoneNumb", " " +phoneNumb);
                String id_user;

                Query query = reference.child("User").child(phoneNumb);
                Log.d("Body getUserId login", "login: "+  sessionPrefference.getUserId());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.d("DATA CHANGEt", "onDataChange: " + snapshot.getValue());
                            accounts = snapshot.getValue(MDataUser.class);
                            Log.d("accounts", "accounts: " + accounts.getPhone());
                            Log.d("accountsUser", "accounts.getId_user: " + accounts.getId_pelanggan());

                            loginJwtApi(phoneNumb, pw, accounts.getId_pelanggan());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Body error", "error: "+ error);

                    }
                });

                /*
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

                 */

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
    private void loginJwtApi(String phone, String pw, String id_user) {
//        String urlDomain = "http://110.50.85.28:8200";
        JavaNetCookieJar jncj = new JavaNetCookieJar(CookieHandler.getDefault());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.cookieJar(jncj)
                .addInterceptor(new AddCookiesInterceptor(getApplicationContext()))
                .addInterceptor(new ReceivedCookiesInterceptor(getApplicationContext()))
                .addInterceptor(loggingInterceptor)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().create();


        //Retrofit retrofits = NetworkClient.getRetrofit();
        Retrofit retrofits = new Retrofit.Builder()
                .baseUrl(urlDomain)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();


        Log.d("PHONE <->", "Response: " + phone);
        Log.d("Password <->", "Response: " + pw);
        Log.d("accountsUser accountsUser", " ======== " + id_user);


        RequestBody reqq1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phone));
        RequestBody reqq2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pw));

        ApiClient uploadApiss = retrofits.create(ApiClient.class);

        Call<ResponseBody> calls = uploadApiss.insertLogin(phone, pw);
        jwt = "";
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    closeProgress();
                    Log.d("Body API <>", "Response: " + response.body().toString());
                    sessionPrefference.setPhone(phone);
                    sessionPrefference.setIdPelanggan(id_user);
                    Log.d("accountsUser accountsUser", " 2.======== " + id_user);

                    Log.d("Body Token 1 <>", "Response: " + jwt);
                    //add delay
                    //String jwt = "";
                    ResponseBody responseBody = response.body();
                    try {
                        byte[] myByte = responseBody.bytes();
                        jwt = new String(myByte, StandardCharsets.UTF_8);
                        jwt = jwt.substring(1, jwt.length() - 1);
                        Log.d("Body Token 2 <>", "Response: " + jwt);
                        //String strResponse = responseBody.string();
                        //String coba = "disini";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            closeProgress();
                            File file = new File(mImageFileLocation);
                            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                            try {
                                //20210130
                                HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
                                loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

                                TokenInterceptor tokenInterceptor = new TokenInterceptor(jwt);

                                Log.d("Body Token <>", "Response: " + jwt);

                                sessionPrefference.setPhone(phone);
                                sessionPrefference.setPassword(pw);
                                sessionPrefference.setKeyApiJwt(jwt);
                                sessionPrefference.setIsLogin(true);
                                OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                                        .addInterceptor(new AddCookiesInterceptor(getApplicationContext()))
                                        .addInterceptor(new ReceivedCookiesInterceptor(getApplicationContext()))
                                        .addInterceptor(loggingInterceptor2)
                                        .addInterceptor(tokenInterceptor)
                                        .build();

                                Gson gson2 = new GsonBuilder().serializeNulls().create();

                                //Retrofit retrofit = NetworkClient.getRetrofit();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(urlDomain)
                                        .addConverterFactory(GsonConverterFactory.create(gson2))
                                        .client(okHttpClient2)
                                        .build();

                                Log.d("Body <>", "Response: "+response.body().toString());
                                finishAffinity();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                            } catch (Exception e) {
                                String errMessage = e.getMessage();
                            }

                        }

                    }, 1000);

                } else {

                    Toast.makeText(LoginActivity.this, "Nomor/Password tidak sesuai, silahkan cek kembali  !", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                String message = "";
                Log.d("Body -->>", "Error: ");
                Toast.makeText(LoginActivity.this, "Server Error !", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void closeProgress() {
        progressDialog.dismiss();

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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
