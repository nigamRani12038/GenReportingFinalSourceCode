package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class VersionsItem{

	@SerializedName("version_name")
	private String versionName;

	@SerializedName("due_date")
	private String dueDate;

	@SerializedName("link")
	private String link;

	public void setVersionName(String versionName){
		this.versionName = versionName;
	}

	public String getVersionName(){
		return versionName;
	}

	public void setDueDate(String dueDate){
		this.dueDate = dueDate;
	}

	public String getDueDate(){
		return dueDate;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}
}