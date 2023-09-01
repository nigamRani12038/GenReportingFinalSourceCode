package com.ossi.genreporting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceStatusResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getPresent() {
        return Present;
    }

    public void setPresent(String present) {
        Present = present;
    }

    public String getAbsent() {
        return Absent;
    }

    public void setAbsent(String absent) {
        Absent = absent;
    }

    public String getWFH() {
        return WFH;
    }

    public void setWFH(String WFH) {
        this.WFH = WFH;
    }

    public String getLeave() {
        return Leave;
    }

    public void setLeave(String leave) {
        Leave = leave;
    }

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("Present")
    @Expose
    private String Present;

    @SerializedName("Absent")
    @Expose
    private String Absent;

    @SerializedName("WFH")
    @Expose
    private String WFH;

    @SerializedName("Leave")
    @Expose
    private String Leave;

    public String getBiometric() {
        return Biometric;
    }

    public void setBiometric(String biometric) {
        Biometric = biometric;
    }

    @SerializedName("Biometric")
    @Expose
    private String Biometric;

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    @SerializedName("Field")
    @Expose
    private String Field;
}
