package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ducnd on 9/29/17.
 */

public class RoomModel {
    @SerializedName("roomId")
    private int roomId;

    @SerializedName("roomName")
    private String roomName;

    @SerializedName("buildingId")
    private int buildingId;

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getBuildingId() {
        return buildingId;
    }
}
