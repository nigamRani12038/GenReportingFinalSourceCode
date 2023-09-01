package com.ossi.genreporting.model;

import com.google.gson.annotations.SerializedName;

public class HolidayListResponse {
    public String getHoliName() {
        return HoliName;
    }

    public void setHoliName(String holiName) {
        HoliName = holiName;
    }

    public String getHoliDate() {
        return HoliDate;
    }

    public void setHoliDate(String holiDate) {
        HoliDate = holiDate;
    }


    @SerializedName("HoliName")
    private String HoliName;
    @SerializedName("HoliDate")
    private String HoliDate;
}
