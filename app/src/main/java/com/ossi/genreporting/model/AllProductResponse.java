package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllProductResponse{

	@SerializedName("AllProductResponse")
	private List<AllProductResponseItem> allProductResponse;

	public void setAllProductResponse(List<AllProductResponseItem> allProductResponse){
		this.allProductResponse = allProductResponse;
	}

	public List<AllProductResponseItem> getAllProductResponse(){
		return allProductResponse;
	}
}