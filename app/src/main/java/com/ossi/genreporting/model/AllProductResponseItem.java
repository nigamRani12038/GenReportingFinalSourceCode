package com.ossi.genreporting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllProductResponseItem{

	@SerializedName("empname")
	private String empname;

	@SerializedName("assigned_count")
	private int assignedCount;

	@SerializedName("total_count")
	private int totalCount;

	@SerializedName("module_count")
	private int moduleCount;

	@SerializedName("link")
	private String link;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("created_by")
	private int createdBy;

	@SerializedName("task_count")
	private int taskCount;

	@SerializedName("versions")
	private List<VersionsItem> versions;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("attachement_count")
	private int attachementCount;

	@SerializedName("unassigned_count")
	private int unassignedCount;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("product_description")
	private String productDescription;

	public void setEmpname(String empname){
		this.empname = empname;
	}

	public String getEmpname(){
		return empname;
	}

	public void setAssignedCount(int assignedCount){
		this.assignedCount = assignedCount;
	}

	public int getAssignedCount(){
		return assignedCount;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}

	public void setModuleCount(int moduleCount){
		this.moduleCount = moduleCount;
	}

	public int getModuleCount(){
		return moduleCount;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setCreatedBy(int createdBy){
		this.createdBy = createdBy;
	}

	public int getCreatedBy(){
		return createdBy;
	}

	public void setTaskCount(int taskCount){
		this.taskCount = taskCount;
	}

	public int getTaskCount(){
		return taskCount;
	}

	public void setVersions(List<VersionsItem> versions){
		this.versions = versions;
	}

	public List<VersionsItem> getVersions(){
		return versions;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setAttachementCount(int attachementCount){
		this.attachementCount = attachementCount;
	}

	public int getAttachementCount(){
		return attachementCount;
	}

	public void setUnassignedCount(int unassignedCount){
		this.unassignedCount = unassignedCount;
	}

	public int getUnassignedCount(){
		return unassignedCount;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setProductDescription(String productDescription){
		this.productDescription = productDescription;
	}

	public String getProductDescription(){
		return productDescription;
	}
}