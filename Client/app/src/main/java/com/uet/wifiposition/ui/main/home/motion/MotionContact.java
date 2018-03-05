package com.uet.wifiposition.ui.main.home.motion;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.ui.base.ViewUI;

/**
 * Created by huynn on 06/03/2018.
 */

public interface MotionContact {
    interface View extends ViewUI {
        void finishPostMotion(PostReferencePoint response);

        void errorPostMotion(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void postMotionInfo(PostMotionSensorInfoRequestBody request);
    }
}
