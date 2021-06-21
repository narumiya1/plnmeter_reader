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
public class Ghistoryi implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "id_user")
    private String id_user;
    @ColumnInfo(name = "meter")
    private String meter;
    @ColumnInfo(name = "score_classfy")
    private String score_classfy;
    @ColumnInfo(name = "score_identfy")
    private String score_identfy;
    @ColumnInfo(name = "created_at")
    private String created_at;
    @ColumnInfo(name = "imagez")
    private String imagez;
    public Ghistoryi() {
    }

    public Ghistoryi(int id, String id_user, String meter, String score_classfy, String score_identfy, String created_at) {
        this.id = id;
        this.id_user = id_user;
        this.meter = meter;
        this.score_classfy = score_classfy;
        this.score_identfy = score_identfy;
        this.created_at = created_at;
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

    public String getImagez() {
        return imagez;
    }

    public void setImagez(String imagez) {
        this.imagez = imagez;
    }
}
