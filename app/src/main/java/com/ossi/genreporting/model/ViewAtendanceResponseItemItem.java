package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ViewAtendanceResponseItemItem{

	@SerializedName("YearNo")
	private String yearNo;

	@SerializedName("AdjustDay")
	private String AdjustDay;

	@SerializedName("Sno")
	private String sno;

	@SerializedName("TotalAbsent")
	private String TotalAbsent;

	@SerializedName("response")
	private String response;

	@SerializedName("adjustCode")
	private String adjustCode;

	@SerializedName("TotalPresent")
	private String TotalPresent;

	@SerializedName("id")
	private String id;

	public String getAdjustDay() {
		return AdjustDay;
	}

	public void setAdjustDay(String adjustDay) {
		AdjustDay = adjustDay;
	}

	public String getTotalAbsent() {
		return TotalAbsent;
	}

	public void setTotalAbsent(String totalAbsent) {
		TotalAbsent = totalAbsent;
	}

	public String getTotalPresent() {
		return TotalPresent;
	}

	public void setTotalPresent(String totalPresent) {
		TotalPresent = totalPresent;
	}

	public String getAdjustBySno() {
		return AdjustBySno;
	}

	public void setAdjustBySno(String adjustBySno) {
		AdjustBySno = adjustBySno;
	}

	@SerializedName("AdjustBySno")
	private String AdjustBySno;

	@SerializedName("MonthNo")
	private String monthNo;

	public void setYearNo(String yearNo){
		this.yearNo = yearNo;
	}

	public String getYearNo(){
		return yearNo;
	}



	public void setSno(String sno){
		this.sno = sno;
	}

	public String getSno(){
		return sno;
	}



	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setAdjustCode(String adjustCode){
		this.adjustCode = adjustCode;
	}

	public String getAdjustCode(){
		return adjustCode;
	}



	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}



	public void setMonthNo(String monthNo){
		this.monthNo = monthNo;
	}

	public String getMonthNo(){
		return monthNo;
	}
}