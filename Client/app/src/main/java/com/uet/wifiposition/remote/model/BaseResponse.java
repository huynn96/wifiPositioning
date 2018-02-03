package com.uet.wifiposition.remote.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ducnd on 9/29/17.
 */

public class BaseResponse implements IBaseResponse {
    @SerializedName("errorCode")
    private int errorCode;

    @SerializedName("msg")
    private String msg = "";

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
