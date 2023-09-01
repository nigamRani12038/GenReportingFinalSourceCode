package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class AllRequestResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getTDate() {
        return TDate;
    }

    public void setTDate(String TDate) {
        this.TDate = TDate;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    @SerializedName("response")
    private String response;

    @SerializedName("Type")
    private String Type;

    @SerializedName("id")
    private String id;

    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("FDate")
    private String FDate;

    @SerializedName("TDate")
    private String TDate;

    @SerializedName("Count")
    private String Count;

    @SerializedName("Purpose")
    private String Purpose;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @SerializedName("department")
    private String department;

    public String getReportingManagers() {
        return ReportingManagers;
    }

    public void setReportingManagers(String reportingManagers) {
        ReportingManagers = reportingManagers;
    }

    @SerializedName("ReportingManagers")
    private String ReportingManagers;
}
