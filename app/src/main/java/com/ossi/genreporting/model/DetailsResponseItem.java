package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class DetailsResponseItem{
	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getApply() {
		return Apply;
	}

	public void setApply(String apply) {
		Apply = apply;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	//Image
	@SerializedName("Image")
	private String Image;

	@SerializedName("Apply")
	private String Apply;

	@SerializedName("Status")
	private String Status;

	@SerializedName("Reject_Expenses")
	private String rejectExpenses;

	@SerializedName("Used_WFH")
	private String usedWFH;

	@SerializedName("Total_EL")
	private String Total_EL;

	@SerializedName("Total_CL")
	private String Total_CL;

	@SerializedName("Total_Expenses")
	private String totalExpenses;

	@SerializedName("Remaining_WFHLeave")
	private String remainingWFHLeave;

	@SerializedName("EmpCode")
	private String empCode;

	@SerializedName("Loan_Status")
	private String loanStatus;

	@SerializedName("Reject_WFH")
	private String rejectWFH;

	@SerializedName("Pending_Expenses")
	private String pendingExpenses;

	@SerializedName("Approved_Expenses")
	private String approvedExpenses;

	public String getTotal_EL() {
		return Total_EL;
	}

	public void setTotal_EL(String total_EL) {
		Total_EL = total_EL;
	}

	public String getTotal_CL() {
		return Total_CL;
	}

	public void setTotal_CL(String total_CL) {
		Total_CL = total_CL;
	}

	public String getTotal_SL() {
		return Total_SL;
	}

	public void setTotal_SL(String total_SL) {
		Total_SL = total_SL;
	}

	@SerializedName("Total_SL")
	private String Total_SL;

	@SerializedName("ID")
	private String iD;

	@SerializedName("Pending_WFH")
	private String pendingWFH;

	@SerializedName("Loan Amount")
	private String loanAmount;

	@SerializedName("Approved_WFH")
	private String approvedWFH;

	@SerializedName("Available_CL")
	private String Available_CL;

	@SerializedName("Available_EL")
	private String Available_EL;

	public String getApprovedLeave() {
		return ApprovedLeave;
	}

	public void setApprovedLeave(String approvedLeave) {
		ApprovedLeave = approvedLeave;
	}

	public String getPendingLeave() {
		return PendingLeave;
	}

	public void setPendingLeave(String pendingLeave) {
		PendingLeave = pendingLeave;
	}

	public String getRejectLeave() {
		return RejectLeave;
	}

	public void setRejectLeave(String rejectLeave) {
		RejectLeave = rejectLeave;
	}

	@SerializedName("ApprovedLeave")
	private String ApprovedLeave;

	@SerializedName("PendingLeave")
	private String PendingLeave;

	@SerializedName("RejectLeave")
	private String RejectLeave;
	//ApprovedLeave

	public String getAvailable_CL() {
		return Available_CL;
	}

	public void setAvailable_CL(String available_CL) {
		Available_CL = available_CL;
	}

	public String getAvailable_EL() {
		return Available_EL;
	}

	public void setAvailable_EL(String available_EL) {
		Available_EL = available_EL;
	}

	public String getAvailable_SL() {
		return Available_SL;
	}

	public void setAvailable_SL(String available_SL) {
		Available_SL = available_SL;
	}

	@SerializedName("Available_SL")
	private String Available_SL;

	public void setRejectExpenses(String rejectExpenses){
		this.rejectExpenses = rejectExpenses;
	}

	public String getRejectExpenses(){
		return rejectExpenses;
	}

	public void setUsedWFH(String usedWFH){
		this.usedWFH = usedWFH;
	}

	public String getUsedWFH(){
		return usedWFH;
	}




	public void setTotalExpenses(String totalExpenses){
		this.totalExpenses = totalExpenses;
	}

	public String getTotalExpenses(){
		return totalExpenses;
	}

	public void setRemainingWFHLeave(String remainingWFHLeave){
		this.remainingWFHLeave = remainingWFHLeave;
	}

	public String getRemainingWFHLeave(){
		return remainingWFHLeave;
	}

	public void setEmpCode(String empCode){
		this.empCode = empCode;
	}

	public String getEmpCode(){
		return empCode;
	}

	public void setLoanStatus(String loanStatus){
		this.loanStatus = loanStatus;
	}

	public String getLoanStatus(){
		return loanStatus;
	}

	public void setRejectWFH(String rejectWFH){
		this.rejectWFH = rejectWFH;
	}

	public String getRejectWFH(){
		return rejectWFH;
	}

	public void setPendingExpenses(String pendingExpenses){
		this.pendingExpenses = pendingExpenses;
	}

	public String getPendingExpenses(){
		return pendingExpenses;
	}

	public void setApprovedExpenses(String approvedExpenses){
		this.approvedExpenses = approvedExpenses;
	}

	public String getApprovedExpenses(){
		return approvedExpenses;
	}




	public void setID(String iD){
		this.iD = iD;
	}

	public String getID(){
		return iD;
	}

	public void setPendingWFH(String pendingWFH){
		this.pendingWFH = pendingWFH;
	}

	public String getPendingWFH(){
		return pendingWFH;
	}

	public void setLoanAmount(String loanAmount){
		this.loanAmount = loanAmount;
	}

	public String getLoanAmount(){
		return loanAmount;
	}

	public void setApprovedWFH(String approvedWFH){
		this.approvedWFH = approvedWFH;
	}

	public String getApprovedWFH(){
		return approvedWFH;
	}
}