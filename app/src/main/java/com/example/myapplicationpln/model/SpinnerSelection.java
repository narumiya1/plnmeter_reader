package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpinnerSelection implements Serializable {

    public SpinnerSelection() {
    }

    @SerializedName("spinner_value")
    private String spinner_value;
    @SerializedName("spinner_arr")
    private Long spinner_arr;

    public String getSpinner_value() {
        return spinner_value;
    }

    public void setSpinner_value(String spinner_value) {
        this.spinner_value = spinner_value;
    }

    public Long getSpinner_arr() {
        return spinner_arr;
    }

    public void setSpinner_arr(Long spinner_arr) {
        this.spinner_arr = spinner_arr;
    }
}
