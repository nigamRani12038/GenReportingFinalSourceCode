package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class WFHStatusResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;
    @SerializedName("Empname")
    private String Empname;

    public String getEmpname() {
        return Empname;
    }

    public void setEmpname(String empname) {
        Empname = empname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("image")
    private String image;

    public String getTypeAttendance() {
        return TypeAttendance;
    }

    public void setTypeAttendance(String typeAttendance) {
        TypeAttendance = typeAttendance;
    }

    @SerializedName("TypeAttendance")
    private String TypeAttendance;

}

