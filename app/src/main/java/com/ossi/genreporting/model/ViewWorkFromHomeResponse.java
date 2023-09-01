package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ViewWorkFromHomeResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getLeaveStatus() {
        return LeaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        LeaveStatus = leaveStatus;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }





    public String getApprovedName() {
        return ApprovedName;
    }

    public void setApprovedName(String approvedName) {
        ApprovedName = approvedName;
    }

    @SerializedName("response")
    private String response;

    @SerializedName("LeaveStatus")
    private String LeaveStatus;

    @SerializedName("Purpose")
    private String Purpose;

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    @SerializedName("FDate")
    private String FDate;

    @SerializedName("ApprovedName")
    private String ApprovedName;

}
