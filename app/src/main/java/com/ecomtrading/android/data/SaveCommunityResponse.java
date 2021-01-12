package com.ecomtrading.android.data;

import com.google.gson.annotations.SerializedName;

public class SaveCommunityResponse {

    public SaveCommunityResponse() {
    }

    @SerializedName("Status") int statusCode;
    @SerializedName("Message") String message;
    @SerializedName("ID") int id;

    public SaveCommunityResponse(int statusCode, String message, int id) {
        this.statusCode = statusCode;
        this.message = message;
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
