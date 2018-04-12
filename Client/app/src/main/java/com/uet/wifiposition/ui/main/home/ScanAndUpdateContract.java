package com.uet.wifiposition.ui.main.home;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.ui.base.ViewUI;

/**
 * Created by ducnd on 10/13/17.
 */

public interface ScanAndUpdateContract {
    interface View extends ViewUI {
        void finishGetLocaiton(PostMotionResponse response);

        void errorGetLocation(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getLocation(GetLocationRequest request);
    }
}
