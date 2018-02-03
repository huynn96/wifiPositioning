package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;

import java.util.List;

/**
 * Created by ducnd on 10/13/17.
 */

public class GetLocationRequest {
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
}
