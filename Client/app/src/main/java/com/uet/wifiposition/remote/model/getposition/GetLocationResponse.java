package com.uet.wifiposition.remote.model.getposition;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

/**
 * Created by ducnd on 10/13/17.
 */

public class GetLocationResponse extends BaseResponse {
    @SerializedName("data")
    private LocationModel locationModel = new LocationModel();

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

}
