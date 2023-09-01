package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ViewLeaveStatus {

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getApprove_name() {
        return Approve_name;
    }

    public void setApprove_name(String approve_name) {
        Approve_name = approve_name;
    }

    public String getTypeLeave() {
        return TypeLeave;
    }

    public void setTypeLeave(String typeLeave) {
        TypeLeave = typeLeave;
    }

    @SerializedName("Status")
    private String Status;

    @SerializedName("Purpose")
    private String Purpose;

    @SerializedName("FromDate")
    private String FromDate;

    @SerializedName("Approve_name")
    private String Approve_name;

    @SerializedName("TypeLeave")
    private String TypeLeave;
}
