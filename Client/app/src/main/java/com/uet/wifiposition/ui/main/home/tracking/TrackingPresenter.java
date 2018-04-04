package com.uet.wifiposition.ui.main.home.tracking;

import com.uet.wifiposition.remote.interact.main.BasePresenter;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;

/**
 * Created by huynn on 03/04/2018.
 */

public class TrackingPresenter  extends BasePresenter<TrackingContact.View> implements TrackingContact.Presenter {
    public TrackingPresenter(TrackingContact.View view) {
        super(view);
    }

    @Override
    public void getLocation(GetLocationRequest request) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().getLocation(request),
                response -> {
                    mView.hideProgress();
                    mView.finishGetLocation(response);
                }, error -> {
                    mView.hideProgress();
                    mView.errorGetLocation(error);
                });
    }
}
