package com.uet.wifiposition.remote.requestbody;

import java.util.List;

/**
 * Created by ducnd on 11/10/17.
 */

public class PostReferencePointGaussRequest {
    private int roomId;

    private int x;

    private int y;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    private List<ItemPostReferencePointGaussRequest> itemPostReferencePointGaussRequests;


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

    public List<ItemPostReferencePointGaussRequest> getItemPostReferencePointGaussRequests() {
        return itemPostReferencePointGaussRequests;
    }

    public void setItemPostReferencePointGaussRequests(List<ItemPostReferencePointGaussRequest> itemPostReferencePointGaussRequests) {
        this.itemPostReferencePointGaussRequests = itemPostReferencePointGaussRequests;
    }
}
