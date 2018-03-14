package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.motion.Acceleration;

import java.util.List;

/**
 * Created by huynn on 05/03/2018.
 */

public class PostMotionSensorInfoRequestBody {
    private List<Acceleration> accelerations;

    public List<Acceleration> getAccelerations() {
        return accelerations;
    }

    public void setAccelerations(List<Acceleration> accelerations) {
        this.accelerations = accelerations;
    }
}
