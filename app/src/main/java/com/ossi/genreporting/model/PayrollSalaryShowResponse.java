package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class PayrollSalaryShowResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @SerializedName("EmpName")
    private String EmpName;

    public String getGrossTotal() {
        return GrossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        GrossTotal = grossTotal;
    }

    public String getTotalDeducted() {
        return TotalDeducted;
    }

    public void setTotalDeducted(String totalDeducted) {
        TotalDeducted = totalDeducted;
    }

    public String getNetSalary() {
        return NetSalary;
    }

    public void setNetSalary(String netSalary) {
        NetSalary = netSalary;
    }

    public String getTotalCTC() {
        return TotalCTC;
    }

    public void setTotalCTC(String totalCTC) {
        TotalCTC = totalCTC;
    }

    public String getVoucher() {
        return Voucher;
    }

    public void setVoucher(String voucher) {
        Voucher = voucher;
    }

    @SerializedName("GrossTotal")
    private String GrossTotal;
    @SerializedName("TotalDeducted")
    private String TotalDeducted;
    @SerializedName("NetSalary")
    private String NetSalary;
    @SerializedName("TotalCTC")
    private String TotalCTC;
    @SerializedName("Voucher")
    private String Voucher;
}
