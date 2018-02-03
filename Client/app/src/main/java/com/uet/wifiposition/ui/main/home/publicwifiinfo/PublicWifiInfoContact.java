package com.uet.wifiposition.ui.main.home.publicwifiinfo;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.requestbody.PostReferencePointGaussRequest;
import com.uet.wifiposition.ui.base.ViewUI;

import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

public interface PublicWifiInfoContact {
    interface View extends ViewUI {
        void finishGetBuildings(GetBuildingsResponse response);

        void errorGetBuildings(Throwable error);

        void finishGetRooms(GetRoomsResponse response);

        void errorGetRooms(Throwable error);

        void finishPostRefencePoint(PostReferencePoint response);

        void errorPostReferencePoint(Throwable error);

        void finishPostReferencePointGauss(PostReferencePoint response);

        void errorPostReferencePointGauss(Throwable error);
    }

    interface Presenter extends IBasePresenter {
        void getBuilding();

        void getRooms(int buildingId);

        void postReferencePoint(int buildingId, int roomId, int x, int y, List<InfoReferencePointInput> infos);

        void postReferencePointGauss(PostReferencePointGaussRequest request);
    }
}
