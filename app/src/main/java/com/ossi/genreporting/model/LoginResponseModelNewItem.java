package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModelNewItem{

	@SerializedName("LoginTime")
	private String loginTime;

	@SerializedName("response")
	private String response;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("LogoutID")
	private String logoutID;

	@SerializedName("Designation_id")
	private String designationId;

	@SerializedName("Image")
	private String image;

	@SerializedName("EmpCode")
	private String empCode;

	@SerializedName("RoleId")
	private String roleId;

	@SerializedName("Name")
	private String name;

	public void setLoginTime(String loginTime){
		this.loginTime = loginTime;
	}

	public String getLoginTime(){
		return loginTime;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}

	public void setLogoutID(String logoutID){
		this.logoutID = logoutID;
	}

	public String getLogoutID(){
		return logoutID;
	}

	public void setDesignationId(String designationId){
		this.designationId = designationId;
	}

	public String getDesignationId(){
		return designationId;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setEmpCode(String empCode){
		this.empCode = empCode;
	}

	public String getEmpCode(){
		return empCode;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return roleId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}