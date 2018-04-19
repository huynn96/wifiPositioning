package com.uet.wifiposition.remote.model.getposition;

/**
 * Created by ducnd on 11/24/17.
 */

public class LocationModel {
    private int x;
    private int y;
    private int transactionId;
    private double probability;

    public LocationModel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public LocationModel() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
