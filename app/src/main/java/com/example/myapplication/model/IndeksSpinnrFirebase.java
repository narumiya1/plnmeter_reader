package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndeksSpinnrFirebase implements Serializable {
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("value")
    private String value;
    @SerializedName("type")
    private String type;

    public IndeksSpinnrFirebase() {
    }

    public IndeksSpinnrFirebase(String id_user, String value, String valueOf) {
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
