package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class PayrollSalaryHistoryResponse {
    @SerializedName("response")
    private String response;


    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("EarnGross")
    private String EarnGross;

    @SerializedName("Basic")
    private String Basic;
    @SerializedName("TotalDay")
    private String TotalDay;

    @SerializedName("LWP")
    private String LWP;

    @SerializedName("PDay")
    private String PDay;

    @SerializedName("ADay")
    private String ADay;

    @SerializedName("TDS")
    private String TDS;

    @SerializedName("PF")
    private String PF;

    @SerializedName("EPF")
    private String EPF;

    public String getTotalCTC() {
        return TotalCTC;
    }

    public void setTotalCTC(String totalCTC) {
        TotalCTC = totalCTC;
    }

    @SerializedName("TotalCTC")
    private String TotalCTC;

    public String getTotalDay() {
        return TotalDay;
    }

    public void setTotalDay(String totalDay) {
        TotalDay = totalDay;
    }

    public String getLWP() {
        return LWP;
    }

    public void setLWP(String LWP) {
        this.LWP = LWP;
    }

    public String getPDay() {
        return PDay;
    }

    public void setPDay(String PDay) {
        this.PDay = PDay;
    }

    public String getADay() {
        return ADay;
    }

    public void setADay(String ADay) {
        this.ADay = ADay;
    }

    public String getTDS() {
        return TDS;
    }

    public void setTDS(String TDS) {
        this.TDS = TDS;
    }

    public String getPF() {
        return PF;
    }

    public void setPF(String PF) {
        this.PF = PF;
    }

    public String getEPF() {
        return EPF;
    }

    public void setEPF(String EPF) {
        this.EPF = EPF;
    }

    public String getESIC() {
        return ESIC;
    }

    public void setESIC(String ESIC) {
        this.ESIC = ESIC;
    }

    public String getEESIC() {
        return EESIC;
    }

    public void setEESIC(String EESIC) {
        this.EESIC = EESIC;
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

    public String getVoucher() {
        return Voucher;
    }

    public void setVoucher(String voucher) {
        Voucher = voucher;
    }

    @SerializedName("ESIC")
    private String ESIC;

    @SerializedName("EESIC")
    private String EESIC;

    @SerializedName("TotalDeducted")
    private String TotalDeducted;

    @SerializedName("NetSalary")
    private String NetSalary;

    @SerializedName("Voucher")
    private String Voucher;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }



    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getEarnGross() {
        return EarnGross;
    }

    public void setEarnGross(String earnGross) {
        EarnGross = earnGross;
    }

    public String getBasic() {
        return Basic;
    }

    public void setBasic(String basic) {
        Basic = basic;
    }

    public String getGrossTotal() {
        return GrossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        GrossTotal = grossTotal;
    }

    @SerializedName("GrossTotal")
    private String GrossTotal;


}
