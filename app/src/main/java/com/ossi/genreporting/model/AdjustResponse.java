package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class AdjustResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;
}
