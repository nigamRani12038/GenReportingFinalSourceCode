package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ApplyLeaveResponseItem {

    @SerializedName("response")
    private String response;

    public void setResponse(String response){
        this.response = response;
    }

    public String getResponse(){
        return response;
    }
}
