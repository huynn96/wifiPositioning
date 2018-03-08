package com.uet.wifiposition.remote.model.getbuilding;

import com.google.gson.annotations.SerializedName;
import com.uet.wifiposition.remote.model.BaseResponse;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

public class PostReferencePoint extends BaseResponse {
    @SerializedName("data")
    private String msg;

    @SerializedName("result")
    private List<Integer> result;

    public List<Integer> getResult() {
        return result;
    }

    @SerializedName("lastTime")
    private long lastTime;

    public long getLastTime() {
        return lastTime;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
