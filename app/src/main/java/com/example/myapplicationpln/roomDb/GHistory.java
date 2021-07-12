package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbHistory")
public class GHistory implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "id_user")
    private String id_user;
    @ColumnInfo(name = "meter")
    private double meter;   //ganti ke double
    @ColumnInfo(name = "score_classfy")
    private double scoreClassfification;    //ganti ke double
    @ColumnInfo(name = "score_identfy")
    private double scoreIdentification;    //ganti ke double
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "date_time")
    @TypeConverters({TimeStampConverters.class})
    private Date date_time;
    @ColumnInfo(name = "created_at")
    private String created_at;   //ganti ke datetime
//    private Date created_ats;
    @ColumnInfo(name = "imagez")
    private String imagez;
    @ColumnInfo(name = "status")
    private int status;
    public GHistory() {
    }


    public GHistory( String id_user, double meter, double score_classfy, double score_identfy, double longitude, double latitude,Date date_time,String created_at, String imagez,int status) {

        this.id_user = id_user;
        this.meter = meter;
        this.scoreClassfification = score_classfy;
        this.scoreIdentification = score_identfy;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date_time = date_time;
        this.created_at = created_at;
        this.imagez=imagez;
        this.status=status;
    }
    public GHistory(int id, String id_user, double meter, double score_classfy, double score_identfy, double longitude, double latitude,Date date_time,String created_at, String imagez,int status) {
        this.id = id;
        this.id_user = id_user;
        this.meter = meter;
        this.scoreClassfification = score_classfy;
        this.scoreIdentification = score_identfy;
        this.longitude=longitude;
        this.latitude=latitude;
        this.date_time = date_time;
        this.created_at = created_at;
        this.imagez=imagez;
        this.status=status;
    }
    public GHistory(String id_user, double meter, double score_classfy, double score_identfy ,Date date_time, String created_at, String imagez,int status) {
        this.id_user = id_user;
        this.meter = meter;
        this.scoreClassfification = score_classfy;
        this.scoreIdentification = score_identfy;
        this.date_time = date_time;
        this.created_at = created_at;
        this.imagez=imagez;
        this.status=status;
    }

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

    public double getMeter() {
        return meter;
    }

    public void setMeter(double meter) {
        this.meter = meter;
    }

    public double getScoreClassfification() {
        return scoreClassfification;
    }

    public void setScoreClassfification(double scoreClassfification) {
        this.scoreClassfification = scoreClassfification;
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

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImagez() {
        return imagez;
    }

    public void setImagez(String imagez) {
        this.imagez = imagez;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
