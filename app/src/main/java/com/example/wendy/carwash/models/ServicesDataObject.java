package com.example.wendy.carwash.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class ServicesDataObject {
    @SerializedName("service_name")
    private String service_name;
    private String service_id;
    public ServicesDataObject(){}
    public ServicesDataObject(String service_id, String service_name) {
        this.service_name = service_name;
        this.service_id =service_id;
    }
    public String getServiceName() {
        return service_name;
    }
    public void setServiceName(String service_name) {
        this.service_name = service_name;
    }

    public String getServiceID() {
        return service_id;
    }
    public void setServiceID (String service_id) {
        this.service_id = service_id;
    }
}
