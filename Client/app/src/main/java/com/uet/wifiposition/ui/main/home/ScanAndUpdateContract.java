package com.uet.wifiposition.ui.main.home;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getposition.GetLocationResponse;
import com.uet.wifiposition.ui.base.ViewUI;

import java.util.List;

/**
 * Created by ducnd on 10/13/17.
 */

public interface ScanAndUpdateContract {
    interface View extends ViewUI {
        void finishGetLocaiton(GetLocationResponse response);

        void errorGetLocation(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getLocation(int buildingId, int roomId, List<InfoReferencePointInput> infoReferencePointInputs, ExtendGetLocationModel firstGetLocationModel);
    }
}
