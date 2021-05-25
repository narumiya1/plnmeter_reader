package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataUser implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("id_user")
    private String id_user;

    @SerializedName("id_pelanggan")
    private String id_pelanggan;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    public DataUser(){
    }

    public DataUser(String name){
        this.name=name;
    }
    public DataUser(String id,String id_user,String id_pelanggan, String name,String username, String email, String address, String phone, String password) {
        this.id= id;
        this.id_user = id_user;
        this.id_pelanggan = id_pelanggan;
        this.name=name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
