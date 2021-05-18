package com.example.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormAddressWithSpinnerApi extends AppCompatActivity {
    Spinner sp_provinsi,
            sp_kota_kabupaten,
            sp_kecamatan,
            sp_kelurahan;

    String linkProvinsiAll = "https://dev.farizdotid.com/api/daerahindonesia/provinsi";

    String linkProvinsi = "https://dev.farizdotid.com/api/daerahindonesia/provinsi/36";
    String linkKabupaten_Kota = "https://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi=";
    String linkKecamatan = "https://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota=";
    String linkKelurahan = "https://dev.farizdotid.com/api/daerahindonesia/kelurahan?id_kecamatan=";

    ArrayList<String> listNamaProvinsi = new ArrayList<>();
    ArrayList<String> listNamaKabupaten_Kota = new ArrayList<>();
    ArrayList<String> listNamaKecamatan = new ArrayList<>();
    ArrayList<String> listNamaKelurahan = new ArrayList<>();

    ArrayList<String> listIDProvinsi = new ArrayList<>();
    ArrayList<String> listIDKabupaten_Kota = new ArrayList<>();
    ArrayList<String> listIDKecamatan = new ArrayList<>();
    ArrayList<String> listIDKelurahan = new ArrayList<>();

    Context context;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_address_spinner);

        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        sp_provinsi = findViewById(R.id.sp_provinsi);
        sp_kota_kabupaten = findViewById(R.id.sp_kota_kabupaten);
        sp_kecamatan = findViewById(R.id.sp_kecamatan);
        sp_kelurahan = findViewById(R.id.sp_kelurahan);

        showData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                linkProvinsiAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listIDProvinsi.clear();
                listNamaProvinsi.clear();
                try {
//                    Mengambil daerah Banten
//                    JSONObject jsonObject = new JSONObject(response);
//                    String id = jsonObject.getString("id");
//                    String nama = jsonObject.getString("nama");
//                    listIDProvinsi.add(id);
//                    listNamaProvinsi.add(nama);

//                    Mengambil semua data provinsi
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("provinsi");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String nama = object.getString("nama");

                        listIDProvinsi.add(id);
                        listNamaProvinsi.add(nama);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listNamaProvinsi);
                    sp_provinsi.setAdapter(arrayAdapter);

                    progressDialog.dismiss();
                    sp_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            showDataKota(listIDProvinsi.get(position));
                            Log.d("Body Province", "Province: "+listIDProvinsi.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showDataKota(String s) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                linkKabupaten_Kota + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listIDKabupaten_Kota.clear();
                        listNamaKabupaten_Kota.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("kota_kabupaten");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String nama = object.getString("nama");

                                listIDKabupaten_Kota.add(id);
                                listNamaKabupaten_Kota.add(nama);
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listNamaKabupaten_Kota);
                            sp_kota_kabupaten.setAdapter(arrayAdapter);

                            progressDialog.dismiss();

                            sp_kota_kabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    showDataKecamatan(listIDKabupaten_Kota.get(position));
                                    Log.d("Body sp_kota_kabupaten", "sp_kota_kabupaten: "+listIDKabupaten_Kota.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showDataKecamatan(String s) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                linkKecamatan + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listIDKecamatan.clear();
                        listNamaKecamatan.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("kecamatan");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String nama = object.getString("nama");

                                listIDKecamatan.add(id);
                                listNamaKecamatan.add(nama);
                            }

                            progressDialog.dismiss();
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listNamaKecamatan);
                            sp_kecamatan.setAdapter(arrayAdapter);

                            sp_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    showDataKelurahan(listIDKecamatan.get(position));
                                    Log.d("Body showDataKelurahan", "showDataKelurahan: "+listIDKecamatan.get(position) );
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showDataKelurahan(String s) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                linkKelurahan + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listIDKelurahan.clear();
                        listNamaKelurahan.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("kelurahan");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String nama = object.getString("nama");

                                listIDKelurahan.add(id);
                                listNamaKelurahan.add(nama);
                            }
                            progressDialog.dismiss();
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, listNamaKelurahan);
                            sp_kelurahan.setAdapter(arrayAdapter);
                            Log.d("Body sp_kelurahan", "sp_kelurahan: "+sp_kelurahan);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}