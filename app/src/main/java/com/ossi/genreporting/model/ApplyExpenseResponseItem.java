package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class ApplyExpenseResponseItem{

	@SerializedName("response")
	private String response;

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}
}