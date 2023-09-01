package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class AllEventsShow {

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getDBTime() {
        return DBTime;
    }

    public void setDBTime(String DBTime) {
        this.DBTime = DBTime;
    }

    @SerializedName("response")
    private String response;
    @SerializedName("id")
    private String id;

    @SerializedName("Sno")
    private String Sno;
    @SerializedName("EventName")
    private String EventName;
    @SerializedName("EventDate")
    private String EventDate;
    @SerializedName("EventDescription")
    private String EventDescription;
    @SerializedName("DBTime")
    private String DBTime;
}
