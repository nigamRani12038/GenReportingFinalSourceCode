package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class PayrollAttendanceShowResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getTotalDay() {
        return TotalDay;
    }

    public void setTotalDay(String totalDay) {
        TotalDay = totalDay;
    }

    public String getTotalPresent() {
        return TotalPresent;
    }

    public void setTotalPresent(String totalPresent) {
        TotalPresent = totalPresent;
    }

    public String getTotalLeave() {
        return TotalLeave;
    }

    public void setTotalLeave(String totalLeave) {
        TotalLeave = totalLeave;
    }

    public String getTotalAbsent() {
        return TotalAbsent;
    }

    public void setTotalAbsent(String totalAbsent) {
        TotalAbsent = totalAbsent;
    }

    public String getWeekOff() {
        return WeekOff;
    }

    public void setWeekOff(String weekOff) {
        WeekOff = weekOff;
    }

    public String getAdjustDay() {
        return AdjustDay;
    }

    public void setAdjustDay(String adjustDay) {
        AdjustDay = adjustDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("Heading")
    private String Heading;

    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("TotalDay")
    private String TotalDay;

    @SerializedName("TotalPresent")
    private String TotalPresent;

    @SerializedName("TotalLeave")
    private String TotalLeave;

    @SerializedName("TotalAbsent")
    private String TotalAbsent;

    @SerializedName("WeekOff")
    private String WeekOff;

    @SerializedName("AdjustDay")
    private String AdjustDay;

    @SerializedName("id")
    private String id;


}
