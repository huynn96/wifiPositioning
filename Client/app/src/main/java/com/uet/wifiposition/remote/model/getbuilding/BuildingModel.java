package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ducnd on 9/29/17.
 */

public class BuildingModel {
    @SerializedName("buildingId")
    private int buildingId;

    @SerializedName("buildingName")
    private String buildingName;

    @SerializedName("buildingAddress")
    private String buildingAddress;

    public int getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }
}
