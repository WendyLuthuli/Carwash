package com.example.wendy.carwash.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class VehicleDataObject {
    @SerializedName("vehicle")
    private String vehicle;
    private String reg_no;
    public VehicleDataObject(){}
    public VehicleDataObject(String reg_no, String vehicle) {
        this.vehicle = vehicle;
        this.reg_no =reg_no;
    }
    public String getVehicle() {
        return vehicle;
    }
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getRegNo() {
        return reg_no;
    }
    public void setRegNo (String reg_no) {
        this.reg_no = reg_no;
    }
}
