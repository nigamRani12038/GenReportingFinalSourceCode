package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModelNew{

	@SerializedName("LoginResponseModelNew")
	private List<LoginResponseModelNewItem> loginResponseModelNew;

	public void setLoginResponseModelNew(List<LoginResponseModelNewItem> loginResponseModelNew){
		this.loginResponseModelNew = loginResponseModelNew;
	}

	public List<LoginResponseModelNewItem> getLoginResponseModelNew(){
		return loginResponseModelNew;
	}
}