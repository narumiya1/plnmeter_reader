package com.example.myapplicationpln.network_retrofit;

import com.google.gson.annotations.SerializedName;

public class UserToken {
    @SerializedName("id")
    private int id;
    @SerializedName("numbers")
    private String numbers;
    @SerializedName("token")
    private String token;

    public UserToken(int id,String numbers, String token) {
        this.numbers = numbers;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getPasswords() {
        return token;
    }

    public void setPasswords(String passwords) {
        this.token = passwords;
    }
}
