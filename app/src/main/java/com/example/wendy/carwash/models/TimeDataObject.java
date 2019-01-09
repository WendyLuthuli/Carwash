package com.example.wendy.carwash.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class TimeDataObject {
    @SerializedName("timefrom")
    private String timeslot_id;
    private String timefrom;
    public TimeDataObject(){}
    public TimeDataObject(String timeslot_id, String timefrom) {
        this.timefrom = timefrom;
        this.timeslot_id =timeslot_id;
    }
    public String getTimeFrom() {
        return timefrom;
    }
    public void setTimeFrom(String timefrom) {
        this.timefrom = timefrom;
    }

    public String getTimeslotID() {
        return timeslot_id;
    }
    public void setTimeslotID (String timeslot_id) {
        this.timeslot_id = timeslot_id;
    }
}
