package com.uet.wifiposition.remote.requestbody;

import java.util.List;

/**
 * Created by ducnd on 11/10/17.
 */

public class ItemPostReferencePointGaussRequest {
    private String appName;

    private String macAddress;

    private List<Float> listRss;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Float> getListRss() {
        return listRss;
    }

    public void setListRss(List<Float> listRss) {
        this.listRss = listRss;
    }
}
