package com.uet.wifiposition.ui.main;


import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.ui.base.BaseActivity;
import com.uet.wifiposition.utils.OpenFragmentUtils;

/**
 * Created by ducnd on 9/22/17.
 */

public class ScanAndUpdateActivity extends BaseActivity {

    @Override
    public int getLayoutMain() {
        return R.layout.activity_scan_and_update;
    }

    @Override
    public void findViewByIds() {

    }

    @Override
    public void setEvents() {
    }

    @Override
    public void initComponents() {
        setRootView(findViewById(R.id.content_view));
        Interactor.getInstance().init();

        OpenFragmentUtils.openFirstScanAndUpdateFragment(getSupportFragmentManager());
    }


}
