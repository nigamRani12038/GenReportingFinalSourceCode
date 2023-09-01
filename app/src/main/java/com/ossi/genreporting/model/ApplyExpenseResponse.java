package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ApplyExpenseResponse{

	@SerializedName("ApplyExpenseResponse")
	private List<ApplyExpenseResponseItem> applyExpenseResponse;

	public void setApplyExpenseResponse(List<ApplyExpenseResponseItem> applyExpenseResponse){
		this.applyExpenseResponse = applyExpenseResponse;
	}

	public List<ApplyExpenseResponseItem> getApplyExpenseResponse(){
		return applyExpenseResponse;
	}
}