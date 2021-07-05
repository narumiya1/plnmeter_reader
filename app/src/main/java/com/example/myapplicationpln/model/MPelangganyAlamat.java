package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MPelangganyAlamat implements Serializable {
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("id_pelanggan")
    private String id_pelanggan;
    @SerializedName("alamat_pelanggan")
    private String alamat_pelanggan;
    @SerializedName("nama_pelanggan")
    private String nama_pelanggan;

    @SerializedName("user_phone")
    private String user_phone;

    @SerializedName("user_address_id")
    private String user_address_id;

    public MPelangganyAlamat(){
    }



    public MPelangganyAlamat(String id_user, String id_pelanggan, String alamat_pelanggan, String nama_pelanggan) {
        this.id_user = id_user;
        this.id_pelanggan=id_pelanggan;
        this.alamat_pelanggan = alamat_pelanggan;
        this.nama_pelanggan=nama_pelanggan;
    }



    public MPelangganyAlamat(String alamat, String id_user, String id_pelanggan, String user_phone, String user_address_id) {
        this.alamat_pelanggan = alamat;
        this.id_user = id_user;
        this.id_pelanggan = id_pelanggan;
        this.user_phone = user_phone;
        this.user_address_id = user_address_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(String user_address_id) {
        this.user_address_id = user_address_id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getAlamat_pelanggan() {
        return alamat_pelanggan;
    }

    public void setAlamat_pelanggan(String alamat_pelanggan) {
        this.alamat_pelanggan = alamat_pelanggan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }
}
