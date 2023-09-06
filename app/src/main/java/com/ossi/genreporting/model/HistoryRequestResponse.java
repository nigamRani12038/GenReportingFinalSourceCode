package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class HistoryRequestResponse {
    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("TypeLeave")
    private String TypeLeave;

    public String getTypeLeave() {
        return TypeLeave;
    }

    public void setTypeLeave(String typeLeave) {
        TypeLeave = typeLeave;
    }

    public String getLeaveTime() {
        return LeaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        LeaveTime = leaveTime;
    }

    @SerializedName("LeaveTime")
    private String LeaveTime;


  public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }


    @SerializedName("Purpose")
    private String Purpose;


    @SerializedName("Status")
    private String Status;
    @SerializedName("Approve_Name")
    private String Approve_Name;
    @SerializedName("TotalDay")
    private String TotalDay;
    @SerializedName("ToDate")
    private String ToDate;
    @SerializedName("FromDate")
    private String FromDate;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getApprove_Name() {
        return Approve_Name;
    }

    public void setApprove_Name(String approve_Name) {
        Approve_Name = approve_Name;
    }

    public String getTotalDay() {
        return TotalDay;
    }

    public void setTotalDay(String totalDay) {
        TotalDay = totalDay;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("response")
    private String response;

    @SerializedName("id")
    private String id;


}
