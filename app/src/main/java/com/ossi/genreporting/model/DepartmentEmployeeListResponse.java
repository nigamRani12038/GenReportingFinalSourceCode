package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class DepartmentEmployeeListResponse {
    Boolean selected=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @SerializedName("EmpName")
    private String EmpName;

    public String getJobProfile() {
        return JobProfile;
    }

    public void setJobProfile(String jobProfile) {
        JobProfile = jobProfile;
    }

    @SerializedName("JobProfile")
    private String JobProfile;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
