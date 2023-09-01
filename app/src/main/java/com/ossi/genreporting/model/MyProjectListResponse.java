package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class MyProjectListResponse {
    @SerializedName("response")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getProject_id() {
        return Project_id;
    }

    public void setProject_id(String project_id) {
        Project_id = project_id;
    }

    public String getProject_Name() {
        return Project_Name;
    }

    public void setProject_Name(String project_Name) {
        Project_Name = project_Name;
    }

    @SerializedName("Project_id")
    private String Project_id;

    @SerializedName("Project_Name")
    private String Project_Name;

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    //
    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("duedays")
    private String duedays;

    public String getDuedays() {
        return duedays;
    }

    public void setDuedays(String duedays) {
        this.duedays = duedays;
    }

    public String getLastdeadline() {
        return lastdeadline;
    }

    public void setLastdeadline(String lastdeadline) {
        this.lastdeadline = lastdeadline;
    }

    public String getTask_Count() {
        return Task_Count;
    }

    public void setTask_Count(String task_Count) {
        Task_Count = task_Count;
    }

    public String getCompleted_Tasks() {
        return Completed_Tasks;
    }

    public void setCompleted_Tasks(String completed_Tasks) {
        Completed_Tasks = completed_Tasks;
    }

    public String getTStatus() {
        return TStatus;
    }

    public void setTStatus(String TStatus) {
        this.TStatus = TStatus;
    }

    @SerializedName("lastdeadline")
    private String lastdeadline;

    @SerializedName("Task_Count")
    private String Task_Count;

    @SerializedName("Completed_Tasks")
    private String Completed_Tasks;

    @SerializedName("TStatus")
    private String TStatus;

    public String getTask_Status() {
        return Task_Status;
    }

    public void setTask_Status(String task_Status) {
        Task_Status = task_Status;
    }

    @SerializedName("Task_Status")
    private  String Task_Status;

    public String getTask_Detail() {
        return Task_Detail;
    }

    public void setTask_Detail(String task_Detail) {
        Task_Detail = task_Detail;
    }

    public String getAssign_By_Date() {
        return Assign_By_Date;
    }

    public void setAssign_By_Date(String assign_By_Date) {
        Assign_By_Date = assign_By_Date;
    }

    @SerializedName("Task_Detail")
    private  String Task_Detail;

    @SerializedName("Assign_By_Date")
    private  String Assign_By_Date;
}
