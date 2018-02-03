package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

public class GetBuildingsResponse extends BaseResponse {
    @SerializedName("data")
    private List<BuildingModel> buildingModels = new ArrayList<>();

    public List<BuildingModel> getBuildingModels() {
        return buildingModels;
    }
}
