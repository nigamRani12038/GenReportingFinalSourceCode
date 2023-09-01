package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailsResponse{

	@SerializedName("ProfileDetailsResponse")
	private List<ProfileDetailsResponseItem> profileDetailsResponse;

	public void setProfileDetailsResponse(List<ProfileDetailsResponseItem> profileDetailsResponse){
		this.profileDetailsResponse = profileDetailsResponse;
	}

	public List<ProfileDetailsResponseItem> getProfileDetailsResponse(){
		return profileDetailsResponse;
	}
}
