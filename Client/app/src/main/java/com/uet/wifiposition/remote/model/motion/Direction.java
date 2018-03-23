package com.uet.wifiposition.remote.model.motion;

/**
 * Created by huynn on 23/03/2018.
 */

public class Direction {
    private int direction;
    private int pitch;
    private int roll;
    private long timestamp;

    public Direction(int direction, int pitch, int roll, long timestamp) {
        this.direction = direction;
        this.pitch = pitch;
        this.roll = roll;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}
