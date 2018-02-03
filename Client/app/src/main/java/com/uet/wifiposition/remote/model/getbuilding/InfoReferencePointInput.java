package com.uet.wifiposition.remote.model.getbuilding;

/**
 * Created by ducnd on 9/29/17.
 */

public class InfoReferencePointInput {
    private String macAddress;
    private String name;
    private float rss;

    public InfoReferencePointInput(String macAddress, String name, float rss) {
        this.macAddress = macAddress;
        this.name = name;
        this.rss = rss;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRss() {
        return rss;
    }

    public void setRss(float rss) {
        this.rss = rss;
    }
}
