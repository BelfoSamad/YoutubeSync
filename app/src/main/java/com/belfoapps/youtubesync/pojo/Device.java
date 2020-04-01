package com.belfoapps.youtubesync.pojo;

public class Device {

    private String deviceName;
    private String endPoint;

    public Device(String deviceName, String endPoint) {
        this.deviceName = deviceName;
        this.endPoint = endPoint;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
