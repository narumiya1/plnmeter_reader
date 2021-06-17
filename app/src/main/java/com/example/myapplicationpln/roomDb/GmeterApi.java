package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbMeterApi")
public class GmeterApi {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "meter")
    public String meter;
    @ColumnInfo(name = "idfy")
    public String idfy;
    @ColumnInfo(name = "classify")
    public String classify;
    @ColumnInfo(name = "type")
    public int type;

    public GmeterApi() {
    }

    public GmeterApi(String meter, String idfy, String classify, int type) {
        this.meter = meter;
        this.idfy = idfy;
        this.classify = classify;
        this.type = type;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getIdfy() {
        return idfy;
    }

    public void setIdfy(String idfy) {
        this.idfy = idfy;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
