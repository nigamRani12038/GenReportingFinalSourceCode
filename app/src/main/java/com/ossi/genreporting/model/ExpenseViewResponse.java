package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ExpenseViewResponse {
    @SerializedName("response")
    private String response;

    @SerializedName("Amount")
    private String Amount;

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }


    @SerializedName("FDate")
    private String FDate;

    @SerializedName("Regin")
    private String Regin;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getRegin() {
        return Regin;
    }

    public void setRegin(String regin) {
        Regin = regin;
    }


    public String getDBTime() {
        return DBTime;
    }

    public void setDBTime(String DBTime) {
        this.DBTime = DBTime;
    }

    @SerializedName("DBTime")
    private String DBTime;

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    @SerializedName("Purpose")
    private String Purpose;

}
