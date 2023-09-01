package com.ossi.genreporting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeListResponse {
    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @SerializedName("Sno")
    @Expose
    private String Sno;
    @SerializedName("officeEmail")
    @Expose
    private String officeEmail;
    @SerializedName("EmpName")
    @Expose
    private String EmpName;

}
