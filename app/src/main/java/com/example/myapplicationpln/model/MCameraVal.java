package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MCameraVal implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("user_phone")
    private String user_phone;
    @SerializedName("width")
    private String width;
    @SerializedName("height")
    private String height;
    @SerializedName("x")
    private String x;
    @SerializedName("y")
    private String y;
    public MCameraVal() {
    }

    public MCameraVal(String id, String id_user, String user_phone, String width, String height, String x, String y) {
        this.id = id;
        this.id_user = id_user;
        this.user_phone = user_phone;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}