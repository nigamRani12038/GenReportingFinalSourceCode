package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class EventResponse {


    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }





    String EmpName;

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    String EventType;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @SerializedName("response")
    private String response;

    @SerializedName("EventDescription")
    private String EventDescription;

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getOrganiser() {
        return Organiser;
    }

    public void setOrganiser(String organiser) {
        Organiser = organiser;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    @SerializedName("Organiser")
    private String Organiser;

    @SerializedName("Venue")
    private String Venue;

    @SerializedName("Location")
    private String Location;




}
