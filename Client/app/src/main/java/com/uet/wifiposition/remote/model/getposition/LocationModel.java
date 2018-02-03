package com.uet.wifiposition.remote.model.getposition;

/**
 * Created by ducnd on 11/24/17.
 */

public class LocationModel {
    private float x;
    private float y;
    private int transactionId;

    public LocationModel(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public LocationModel() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
