package com.example.myapplicationpln.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.myapplicationpln.date.TimeStampConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class HistoryMeter implements Serializable {
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
}
