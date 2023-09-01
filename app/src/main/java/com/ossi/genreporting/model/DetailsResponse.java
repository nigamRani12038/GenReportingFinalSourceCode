package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DetailsResponse{

	@SerializedName("DetailsResponse")
	private List<DetailsResponseItem> detailsResponse;

	public void setDetailsResponse(List<DetailsResponseItem> detailsResponse){
		this.detailsResponse = detailsResponse;
	}

	public List<DetailsResponseItem> getDetailsResponse(){
		return detailsResponse;
	}
}