package com.uet.wifiposition.ui.main.home.motion;

import com.uet.wifiposition.remote.interact.main.BasePresenter;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;

/**
 * Created by huynn on 06/03/2018.
 */

public class MotionPresenter extends BasePresenter<MotionContact.View> implements MotionContact.Presenter {
    public MotionPresenter(MotionContact.View view) {
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
    public void postMotionInfo(PostRPGaussianMotionRequestBody request) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().postMotionInfo(request),
                response -> {
                    mView.hideProgress();
                    mView.finishPostMotion(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorPostMotion(error);
                });
    }

}
