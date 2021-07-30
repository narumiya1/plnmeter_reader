package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;

import java.util.Date;
@Entity(tableName = "tblHistoryMeter")
public class GhistoryMeter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "id_user")
    private String id_user;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "id_pealanggan")
    private long id_pelanggan;
    @ColumnInfo(name = "meter")
    private double meter;
    @ColumnInfo(name = "input_kwh")
    private double input_kwh;
    @ColumnInfo(name = "score_classfy")
    private double scoreClassfification;
    @ColumnInfo(name = "score_identfy")
    private double scoreIdentification;
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

    public GhistoryMeter(int id, String id_user, String phone, long id_pelanggan, double meter, double input_kwh, double scoreClassfification, double scoreIdentification, double longitude, double latitude, Date date_time, String created_at, String imagez, int status) {
        this.id = id;
        this.id_user = id_user;
        this.phone = phone;
        this.id_pelanggan = id_pelanggan;
        this.meter = meter;
        this.input_kwh = input_kwh;
        this.scoreClassfification = scoreClassfification;
        this.scoreIdentification = scoreIdentification;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date_time = date_time;
        this.created_at = created_at;
        this.imagez = imagez;
        this.status = status;
    }

    public GhistoryMeter(String userId, String phone, long valueIdPelanggan, int i, int i1, double v, double v1, double longitudeValue, double latitudeValue, Date date, String text, String imageFilePath, int i2) {
        this.id_user=userId;
        this.phone=phone;
        this.id_pelanggan=valueIdPelanggan;
        this.input_kwh=i;
        this.scoreClassfification=i1;
        this.scoreIdentification=v;
        this.longitude=longitudeValue;
        this.latitude=latitudeValue;
        this.date_time=date;
        this.created_at=text;
        this.imagez=imageFilePath;
        this.status=i2;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(long id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public double getMeter() {
        return meter;
    }

    public void setMeter(double meter) {
        this.meter = meter;
    }

    public double getInput_kwh() {
        return input_kwh;
    }

    public void setInput_kwh(double input_kwh) {
        this.input_kwh = input_kwh;
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
