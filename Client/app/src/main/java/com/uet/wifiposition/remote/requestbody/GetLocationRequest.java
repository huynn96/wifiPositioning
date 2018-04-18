package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getposition.LocationModel;

import java.util.List;

/**
 * Created by ducnd on 10/13/17.
 */

public class GetLocationRequest {
    private double offset;
    private int direction;
    private int buildingId;
    private int roomId;
    private int stepCount;
    private List<InfoReferencePointInput> infos;
    private List<LocationModel> oldCandidates;

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

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public List<LocationModel> getOldCandidates() {
        return oldCandidates;
    }

    public void setOldCandidates(List<LocationModel> oldCandidates) {
        this.oldCandidates = oldCandidates;
    }
}
