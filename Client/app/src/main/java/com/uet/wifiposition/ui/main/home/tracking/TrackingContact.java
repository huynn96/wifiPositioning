package com.uet.wifiposition.ui.main.home.tracking;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.ui.base.ViewUI;

/**
 * Created by huynn on 03/04/2018.
 */

public interface TrackingContact {
    interface View extends ViewUI {
        void finishGetLocation(PostMotionResponse response);

        void errorGetLocation(Throwable error);

        void finishPostMotion(PostMotionResponse response);

        void errorPostMotion(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getLocation(GetLocationRequest request);

        void postMotionInfo(PostRPGaussianMotionRequestBody request);
    }
}
