package com.example.myapplicationpln.roomDb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbIndeks")
public class GIndeksSpinner implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id_idx;
    @ColumnInfo(name = "type")
    public int type;
    @ColumnInfo(name = "value")
    public String value;
    @ColumnInfo(name = "value_int")
    public int value_int;

    //constructor
    public GIndeksSpinner(int id_idx, int type, String value, int value_int) {
        this.id_idx = id_idx;
        this.type = type;
        this.value = value;
        this.value_int = value_int;
    }

    public int getId_idx() {
        return id_idx;
    }

    public void setId_idx(int id_idx) {
        this.id_idx = id_idx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getValue_int() {
        return value_int;
    }

    public void setValue_int(int value_int) {
        this.value_int = value_int;
    }
}