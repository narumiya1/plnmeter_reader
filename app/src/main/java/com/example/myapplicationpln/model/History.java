package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class History implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("meter")
    private String meter;
    @SerializedName("score_classfy")
    private String score_classfy;
    @SerializedName("score_identfy")
    private String score_identfy;
    @SerializedName("created_at")
    private String created_at;

    public History() {
    }

    public History(String id, String id_user, String meter, String score_classfy, String score_identfy, String created_at) {
        this.id = id;
        this.id_user = id_user;
        this.meter = meter;
        this.score_classfy = score_classfy;
        this.score_identfy = score_identfy;
        this.created_at = created_at;
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

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getScore_classfy() {
        return score_classfy;
    }

    public void setScore_classfy(String score_classfy) {
        this.score_classfy = score_classfy;
    }

    public String getScore_identfy() {
        return score_identfy;
    }

    public void setScore_identfy(String score_identfy) {
        this.score_identfy = score_identfy;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
