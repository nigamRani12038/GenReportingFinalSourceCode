package com.ossi.genreporting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateProductModelResponse {
    @SerializedName("Sno")
    @Expose
    String Sno;
    @SerializedName("product_name")
    @Expose
    String  product_name;
    @SerializedName("product_description")
    @Expose
    String product_description;
    @SerializedName("employee_id")
    @Expose
    String employee_id;
    @SerializedName("assign")
    @Expose
    String assign;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @SerializedName("product_id")
    @Expose
    String product_id;


    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getMember_active() {
        return member_active;
    }

    public void setMember_active(String member_active) {
        this.member_active = member_active;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getVersion_cnt() {
        return version_cnt;
    }

    public void setVersion_cnt(String version_cnt) {
        this.version_cnt = version_cnt;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
    @SerializedName("view")
    @Expose
    String view;
    @SerializedName("member_active")
    @Expose
    String member_active;
    @SerializedName("progress")
    @Expose
    String progress;
    @SerializedName("link")
    @Expose
    String link;
    @SerializedName("version_cnt")
    @Expose
    String version_cnt;
    @SerializedName("version_name")
    @Expose
    String version_name;
    @SerializedName("start_date")
    @Expose
    String start_date;
    @SerializedName("due_date")
    @Expose
    String due_date;
    @SerializedName("release_date")
    @Expose
    String release_date;
    @SerializedName("attachement")
    @Expose

    String attachement;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("file_url")
    @Expose
    String file_url;

}
