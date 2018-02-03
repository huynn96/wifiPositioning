package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

/**
 * Created by ducnd on 9/29/17.
 */

public class PostReferencePoint extends BaseResponse {
    @SerializedName("data")
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }
}
