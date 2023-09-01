package com.ossi.genreporting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    @Expose
    private String response;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @SerializedName("Image")
    @Expose
    private String Image;
    //Image

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    @SerializedName("Column1")
    @Expose
    private String Column1;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }




    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("EmpCode")
    @Expose
    private String EmpCode;
    @SerializedName("UserID")
    @Expose
    private String UserID;

    public String getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(String loginTime) {
        LoginTime = loginTime;
    }

    @SerializedName("LoginTime")
    @Expose
    private String LoginTime;




    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLogoutID() {
        return LogoutID;
    }

    public void setLogoutID(String logoutID) {
        LogoutID = logoutID;
    }

    @SerializedName("LogoutID")
    @Expose String LogoutID;

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    @SerializedName("RoleId")
    @Expose String RoleId;

    public String getDesignation_id() {
        return Designation_id;
    }

    public void setDesignation_id(String designation_id) {
        Designation_id = designation_id;
    }

    @SerializedName("Designation_id")
    @Expose String Designation_id;


    @SerializedName("CurrentWeekHours")
    @Expose String CurrentWeekHours;

    public String getCurrentWeekHours() {
        return CurrentWeekHours;
    }

    public void setCurrentWeekHours(String currentWeekHours) {
        CurrentWeekHours = currentWeekHours;
    }

    public String getPreviousWeekHours() {
        return PreviousWeekHours;
    }

    public void setPreviousWeekHours(String previousWeekHours) {
        PreviousWeekHours = previousWeekHours;
    }

    @SerializedName("PreviousWeekHours")
    @Expose String PreviousWeekHours;



}
