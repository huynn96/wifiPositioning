package com.uet.wifiposition.ui.main.home.motion;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.ui.base.ViewUI;

/**
 * Created by huynn on 06/03/2018.
 */

public interface MotionContact {
    interface View extends ViewUI {
        void finishGetBuildings(GetBuildingsResponse response);

        void errorGetBuildings(Throwable error);

        void finishGetRooms(GetRoomsResponse response);

        void errorGetRooms(Throwable error);

        void finishPostMotion(PostMotionResponse response);

        void errorPostMotion(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getBuilding();

        void getRooms(int buildingId);

        void postMotionInfo(PostRPGaussianMotionRequestBody request);
    }
}
