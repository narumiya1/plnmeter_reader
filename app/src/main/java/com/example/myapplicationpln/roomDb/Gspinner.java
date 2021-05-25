package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbSpinner")
public class Gspinner implements Serializable {
    //    @PrimaryKey(autoGenerate = true)
//    public int id;
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_pelanggan")
    public int id_pelanggan;
    @ColumnInfo(name = "address")
    public String address_update;
    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverters.class})
    private Date createdAt;
    @ColumnInfo(name = "user_address_id")
    public int user_address_id;
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public int getId_pelanggan() {
        return id_pelanggan;
    }

    public int setId_pelanggan(int id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
        return id_pelanggan;
    }

    public String getAddress_update() {
        return address_update;
    }

    public String setAddress_update(String address_update) {
        this.address_update = address_update;
        return address_update;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(int user_address_id) {
        this.user_address_id = user_address_id;
    }
}
