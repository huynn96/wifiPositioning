package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

public class GetRoomsResponse extends BaseResponse {
    @SerializedName("data")
    private List<RoomModel> roomModels;

    public List<RoomModel> getRoomModels() {
        return roomModels;
    }
}
