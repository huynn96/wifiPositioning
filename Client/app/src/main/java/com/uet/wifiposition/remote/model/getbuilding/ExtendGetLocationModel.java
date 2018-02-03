package com.uet.wifiposition.remote.model.getbuilding;

/**
 * Created by ducnd on 11/24/17.
 */

public class ExtendGetLocationModel {
    private boolean isFirst;
    private int x;
    private int y;
    private int transactionId;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
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
}
