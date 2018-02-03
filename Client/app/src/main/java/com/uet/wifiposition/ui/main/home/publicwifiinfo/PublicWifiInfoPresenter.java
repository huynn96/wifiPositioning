package com.uet.wifiposition.ui.main.home.publicwifiinfo;

import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.remote.interact.main.BasePresenter;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.requestbody.PostReferencePointGaussRequest;

import java.util.List;

/**
 * Created by ducnd on 9/29/17.
 */

class PublicWifiInfoPresenter extends BasePresenter<PublicWifiInfoContact.View> implements PublicWifiInfoContact.Presenter {
    PublicWifiInfoPresenter(PublicWifiInfoContact.View view) {
        super(view);
    }

    @Override
    public void getBuilding() {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().getBuilding(),
                response -> {
                    mView.finishGetBuildings(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorGetBuildings(error);
                });
    }

    @Override
    public void getRooms(int buildingId) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().getRooms(buildingId),
                response -> {
                    mView.hideProgress();
                    mView.finishGetRooms(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorGetRooms(error);
                });
    }

    @Override
    public void postReferencePoint(int buildingId, int roomId, int x, int y, List<InfoReferencePointInput> infos) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().postReferencePoint(buildingId, roomId, x, y, infos),
                response -> {
                    mView.hideProgress();
                    mView.finishPostRefencePoint(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorPostReferencePoint(error);
                });
    }

    @Override
    public void postReferencePointGauss(PostReferencePointGaussRequest request) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().postReferencePointGauss(request),
                response -> {
                    mView.hideProgress();
                    mView.finishPostReferencePointGauss(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorPostReferencePointGauss(error);
                });
    }
}
