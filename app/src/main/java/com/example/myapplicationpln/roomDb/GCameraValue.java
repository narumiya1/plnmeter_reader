package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "tbCamera")
public class GCameraValue implements Serializable {
    @PrimaryKey(autoGenerate = false)
    public int id;
    @ColumnInfo(name = "id_user")
    public String id_user;
    @ColumnInfo(name = "user_phone")
    public String user_phone;
    @ColumnInfo(name = "width")
    public int width;
    @ColumnInfo(name = "height")
    public int height;
    @ColumnInfo(name = "type")
    public int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}