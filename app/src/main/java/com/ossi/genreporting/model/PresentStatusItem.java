package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class PresentStatusItem {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;

    public String getEmpname() {
        return Empname;
    }

    public void setEmpname(String empname) {
        Empname = empname;
    }

    public String getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(String loginTime) {
        LoginTime = loginTime;
    }

    public String getLogouttime() {
        return Logouttime;
    }

    public void setLogouttime(String logouttime) {
        Logouttime = logouttime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeAttendance() {
        return TypeAttendance;
    }

    public void setTypeAttendance(String typeAttendance) {
        TypeAttendance = typeAttendance;
    }

    @SerializedName("Empname")
    private String Empname;

    @SerializedName("LoginTime")
    private String LoginTime;

    @SerializedName("Logouttime")
    private String Logouttime;

    @SerializedName("image")
    private String image;

    @SerializedName("TypeAttendance")
    private String TypeAttendance;

    public String getPreviousWeekHours() {
        return PreviousWeekHours;
    }

    public void setPreviousWeekHours(String previousWeekHours) {
        PreviousWeekHours = previousWeekHours;
    }

    @SerializedName("PreviousWeekHours")
    private String PreviousWeekHours;
}
