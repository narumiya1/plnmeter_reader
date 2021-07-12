package com.example.myapplicationpln.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MSpinnerSelectx implements Serializable {
    @SerializedName("spinner_value")
    private String spinner_value;
    @SerializedName("spinner_long")
    private String spinner_long;

    public MSpinnerSelectx() {
    }

    public String getSpinner_value() {
        return spinner_value;
    }

    public void setSpinner_value(String spinner_value) {
        this.spinner_value = spinner_value;
    }

    public String getSpinner_long() {
        return spinner_long;
    }

    public void setSpinner_long(String spinner_long) {
        this.spinner_long = spinner_long;
    }
}
