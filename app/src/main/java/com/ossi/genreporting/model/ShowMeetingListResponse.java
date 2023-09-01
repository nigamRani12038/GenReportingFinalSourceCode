package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ShowMeetingListResponse {
    @SerializedName("response")
    private String response;

    @SerializedName("Sno")
    private String Sno;

    @SerializedName("Methead")
    private String Methead;

    @SerializedName("Mettime")
    private String Mettime;

    @SerializedName("MetDepament")
    private String MetDepament;

    @SerializedName("MetEmp")
    private String MetEmp;

    @SerializedName("MetDetails")
    private String MetDetails;

    @SerializedName("Metdate")
    private String Metdate;


    public String getAssign_By_Name() {
        return Assign_By_Name;
    }

    public void setAssign_By_Name(String assign_By_Name) {
        Assign_By_Name = assign_By_Name;
    }

    @SerializedName("Assign_By_Name")
    private String Assign_By_Name;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getMethead() {
        return Methead;
    }

    public void setMethead(String methead) {
        Methead = methead;
    }

    public String getMettime() {
        return Mettime;
    }

    public void setMettime(String mettime) {
        Mettime = mettime;
    }

    public String getMetDepament() {
        return MetDepament;
    }

    public void setMetDepament(String metDepament) {
        MetDepament = metDepament;
    }

    public String getMetEmp() {
        return MetEmp;
    }

    public void setMetEmp(String metEmp) {
        MetEmp = metEmp;
    }

    public String getMetDetails() {
        return MetDetails;
    }

    public void setMetDetails(String metDetails) {
        MetDetails = metDetails;
    }

    public String getMetdate() {
        return Metdate;
    }

    public void setMetdate(String metdate) {
        Metdate = metdate;
    }

    public String getMetmode() {
        return Metmode;
    }

    public void setMetmode(String metmode) {
        Metmode = metmode;
    }

    @SerializedName("Metmode")
    private String Metmode;

    public String getMetType() {
        return MetType;
    }

    public void setMetType(String metType) {
        MetType = metType;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @SerializedName("MetType")
    private String MetType;

    @SerializedName("Type")
    private String Type;

}
