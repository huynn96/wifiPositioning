package com.uet.wifiposition.ui.main.home;

import com.uet.wifiposition.remote.interact.main.BasePresenter;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;

import java.util.List;

/**
 * Created by ducnd on 10/13/17.
 */

public class ScanAndUpdatePresenter extends BasePresenter<ScanAndUpdateContract.View> implements ScanAndUpdateContract.Presenter {
    public ScanAndUpdatePresenter(ScanAndUpdateContract.View view) {
        super(view);
    }

    @Override
    public void getLocation(GetLocationRequest request) {
        mView.showProgress();
        subscribeHasDispose(Interactor.getInstance().getLocation(request),
                response -> {
                    mView.hideProgress();
                    mView.finishGetLocaiton(response);
                },
                error -> {
                    mView.hideProgress();
                    mView.errorGetLocation(error);
                });
    }
}
