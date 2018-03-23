package com.uet.wifiposition.remote.requestbody;

import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.model.motion.Direction;

import java.util.List;

/**
 * Created by huynn on 05/03/2018.
 */

public class PostMotionSensorInfoRequestBody {
    private List<Acceleration> accelerations;
    private List<Direction> directions;

    public List<Acceleration> getAccelerations() {
        return accelerations;
    }

    public void setAccelerations(List<Acceleration> accelerations) {
        this.accelerations = accelerations;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }
}
