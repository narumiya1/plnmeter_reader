package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeterApi implements Serializable {
    @SerializedName("meter_value")
    private String meter_value;
    @SerializedName("identify_value")
    private String identify_value;
    @SerializedName("classify_long")
    private String classify_long;
    @SerializedName("created_at")
    private String created_at;

    public MeterApi() {
    }

    public String getMeter_value() {
        return meter_value;
    }

    public void setMeter_value(String meter_value) {
        this.meter_value = meter_value;
    }

    public String getIdentify_value() {
        return identify_value;
    }

    public void setIdentify_value(String identify_value) {
        this.identify_value = identify_value;
    }

    public String getClassify_long() {
        return classify_long;
    }

    public void setClassify_long(String classify_long) {
        this.classify_long = classify_long;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
