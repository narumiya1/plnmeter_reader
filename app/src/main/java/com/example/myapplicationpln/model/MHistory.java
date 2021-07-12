package com.example.myapplicationpln.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MHistory implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("meter")
    private double meter;
    @SerializedName("scoreClassification")
    private double scoreClassification;
    @SerializedName("scoreIdentification")
    private double scoreIdentification;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverters.class})
    private Date created_at;
    /*
    @SerializedName("created_at")
    private String created_at;//datetime

     */
    public MHistory() {
    }

    //20210702
    //ganti parameter type untuk created_at menjadi Date Time
    public MHistory(long id, String id_user, double meter, double score_classfy, double score_identfy, double longitude,double latitude,Date created_at) {
        this.id = id;
        this.id_user = id_user;
        this.meter = meter;
        this.scoreClassification = score_classfy;
        this.scoreIdentification = score_identfy;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public double getScoreClassification() {
        return scoreClassification;
    }

    public void setScoreClassification(double scoreClassification) {
        this.scoreClassification = scoreClassification;
    }

    public double getScoreIdentification() {
        return scoreIdentification;
    }

    public void setScoreIdentification(double scoreIdentification) {
        this.scoreIdentification = scoreIdentification;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /*
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

     */

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date createdAt) {
        this.created_at = createdAt;
    }
}
