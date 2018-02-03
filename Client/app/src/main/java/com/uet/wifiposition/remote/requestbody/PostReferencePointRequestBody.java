package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;

import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

public class PostReferencePointRequestBody {
    private int buildingId;
    private int roomId;
    private int x;
    private int y;
    private List<InfoReferencePointInput> infos;

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<InfoReferencePointInput> getInfos() {
        return infos;
    }

    public void setInfos(List<InfoReferencePointInput> infos) {
        this.infos = infos;
    }
}
