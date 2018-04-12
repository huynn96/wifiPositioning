package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;

import java.util.List;

/**
 * Created by huynn on 05/03/2018.
 */

public class PostRPGaussianMotionRequestBody {
    private double offset;
    private int direction;
    private int buildingId;
    private int roomId;
    private List<InfoReferencePointInput> infos;
    private ExtendGetLocationModel extendGetLocationModel;

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

    public List<InfoReferencePointInput> getInfos() {
        return infos;
    }

    public void setInfos(List<InfoReferencePointInput> infos) {
        this.infos = infos;
    }

    public ExtendGetLocationModel getExtendGetLocationModel() {
        return extendGetLocationModel;
    }

    public void setExtendGetLocationModel(ExtendGetLocationModel extendGetLocationModel) {
        this.extendGetLocationModel = extendGetLocationModel;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
