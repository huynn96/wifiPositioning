package com.uet.wifiposition.remote.model.getposition;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

import java.util.List;

/**
 * Created by huynn on 19/04/2018.
 */

public class GetLocationResponse extends BaseResponse {
    @SerializedName("data")
    private List<LocationModel> candidates;

    public List<LocationModel> getCandidates() {
        return candidates;
    }

    public LocationModel getPosition() {
        return candidates.get(0);
    }

    public void setCandidates(List<LocationModel> candidates) {
        this.candidates = candidates;
    }
}
