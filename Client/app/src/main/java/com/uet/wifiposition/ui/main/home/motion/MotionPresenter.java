package com.uet.wifiposition.ui.main.home.motion;

import com.uet.wifiposition.remote.interact.main.BasePresenter;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;

/**
 * Created by huynn on 06/03/2018.
 */

public class MotionPresenter extends BasePresenter<MotionContact.View> implements MotionContact.Presenter {
    public MotionPresenter(MotionContact.View view) {
        super(view);
    }

    @Override
    public void postMotionInfo(PostMotionSensorInfoRequestBody request) {
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
