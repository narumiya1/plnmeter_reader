package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class History implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("meter")
    private double meter;
    @SerializedName("score_classfy")
    private double score_classfy;
    @SerializedName("score_identfy")
    private double score_identfy;
    @SerializedName("created_at")
    private String created_at;

    public History() {
    }

    //20210702
    //ganti parameter type untuk created_at menjadi Date Time
    public History(long id, String id_user, double meter, double score_classfy, double score_identfy, String created_at) {
        this.id = id;
        this.id_user = id_user;
        this.meter = meter;
        this.score_classfy = score_classfy;
        this.score_identfy = score_identfy;
        this.created_at = created_at;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public double getMeter() {
        return meter;
    }

    public void setMeter(double meter) {
        this.meter = meter;
    }

    public double getScore_classfy() {
        return score_classfy;
    }

    public void setScore_classfy(double score_classfy) {
        this.score_classfy = score_classfy;
    }

    public double getScore_identfy() {
        return score_identfy;
    }

    public void setScore_identfy(double score_identfy) {
        this.score_identfy = score_identfy;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
