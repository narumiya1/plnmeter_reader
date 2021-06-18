package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;

import java.util.Date;

@Entity(tableName = "tbUserData")
public class GUserData {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_user")
    public int id_user;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "id_pel")
    public String id_pel;
    @ColumnInfo(name = "status")
    public String status;
    public GUserData() {
    }

    public GUserData(int id_user, String name, String username, String email, String address, String phone, String id_pel, String status) {
        this.id_user = id_user;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.id_pel = id_pel;
        this.status = status;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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

    public String getId_pel() {
        return id_pel;
    }

    public void setId_pel(String id_pel) {
        this.id_pel = id_pel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
