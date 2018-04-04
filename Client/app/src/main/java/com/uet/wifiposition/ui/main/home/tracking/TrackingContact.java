package com.uet.wifiposition.ui.main.home.tracking;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.GetLocationResponse;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.ui.base.ViewUI;

/**
 * Created by huynn on 03/04/2018.
 */

public interface TrackingContact {
    interface View extends ViewUI {
        void finishGetLocation(GetLocationResponse response);

        void errorGetLocation(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getLocation(GetLocationRequest request);
    }
}
