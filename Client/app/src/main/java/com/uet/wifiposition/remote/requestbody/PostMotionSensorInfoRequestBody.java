package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.motion.Acceleration;

import java.util.List;

/**
 * Created by huynn on 05/03/2018.
 */

public class PostMotionSensorInfoRequestBody {
    private int typeActivity;
    private List<Acceleration> accelerations;

    public List<Acceleration> getAccelerations() {
        return accelerations;
    }

    public void setAccelerations(List<Acceleration> accelerations) {
        this.accelerations = accelerations;
    }

    public int getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(int typeActivity) {
        this.typeActivity = typeActivity;
    }
}
