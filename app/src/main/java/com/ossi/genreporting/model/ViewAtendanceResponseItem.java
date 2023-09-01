package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ViewAtendanceResponseItem{

	@SerializedName("ViewAtendanceResponseItem")
	private List<ViewAtendanceResponseItemItem> viewAtendanceResponseItem;

	public void setViewAtendanceResponseItem(List<ViewAtendanceResponseItemItem> viewAtendanceResponseItem){
		this.viewAtendanceResponseItem = viewAtendanceResponseItem;
	}

	public List<ViewAtendanceResponseItemItem> getViewAtendanceResponseItem(){
		return viewAtendanceResponseItem;
	}
}