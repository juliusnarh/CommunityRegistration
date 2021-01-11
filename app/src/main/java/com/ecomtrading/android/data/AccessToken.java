package com.ecomtrading.android.data;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("access_token") String accessToken;
    @SerializedName("token_type") String tokenType;
    @SerializedName("expires_in") int expiresIn;

    public AccessToken(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
