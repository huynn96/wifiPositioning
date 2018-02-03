package com.uet.wifiposition.ui.main.home.tracking;

import android.view.View;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.getposition.LocationModel;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.ui.customview.TrackingView;

/**
 * Created by ducnd on 11/24/17.
 */

public class TrackingFragment extends BaseMvpFragment {
    private TrackingView trackingView;
    private int transactionId;

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_tracking;
    }

    @Override
    public void findViewByIds(View view) {
        trackingView = view.findViewById(R.id.tracking_view);
    }

    @Override
    public void initComponents(View view) {

    }

    @Override
    public void setEvents(View view) {

    }

    public void responseTracking(LocationModel locationModel) {
        trackingView.addPath(locationModel.getX(), locationModel.getY());
        transactionId = locationModel.getTransactionId();
    }

    public boolean isPathEmpty() {
        return trackingView.isPathEmpty();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
