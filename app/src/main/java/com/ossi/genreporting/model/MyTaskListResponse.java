package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class MyTaskListResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }



    public String getProject_Name() {
        return Project_Name;
    }

    public void setProject_Name(String project_Name) {
        Project_Name = project_Name;
    }

    public String getProject_id() {
        return Project_id;
    }

    public void setProject_id(String project_id) {
        Project_id = project_id;
    }
    @SerializedName("response")
    private String response;
    @SerializedName("Project_Name")
    private String Project_Name;



    @SerializedName("Project_id")
    private String Project_id;

    @SerializedName("Task_Status")
    private String Task_Status;

    @SerializedName("EmpName")
    private String EmpName;

    @SerializedName("DueDate")
    private String DueDate;

    @SerializedName("Task_Submit_Date")
    private String Task_Submit_Date;

    @SerializedName("Task_Module")
    private String Task_Module;

    @SerializedName("Task_Detail")
    private String Task_Detail;


    public String getTask_id() {
        return Task_id;
    }

    public void setTask_id(String task_id) {
        Task_id = task_id;
    }

    @SerializedName("Task_id")
private String Task_id;

    public String getTask_Status() {
        return Task_Status;
    }

    public void setTask_Status(String task_Status) {
        Task_Status = task_Status;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getTask_Submit_Date() {
        return Task_Submit_Date;
    }

    public void setTask_Submit_Date(String task_Submit_Date) {
        Task_Submit_Date = task_Submit_Date;
    }

    public String getTask_Module() {
        return Task_Module;
    }

    public void setTask_Module(String task_Module) {
        Task_Module = task_Module;
    }

    public String getTask_Detail() {
        return Task_Detail;
    }

    public void setTask_Detail(String task_Detail) {
        Task_Detail = task_Detail;
    }

    public String getTask_Deadline() {
        return Task_Deadline;
    }

    public void setTask_Deadline(String task_Deadline) {
        Task_Deadline = task_Deadline;
    }

    @SerializedName("Task_Deadline")
    private String Task_Deadline;



}
