package com.uet.wifiposition.ui.main.home;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.WifiInfoModel;
import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getposition.GetLocationResponse;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.ui.main.home.publicwifiinfo.PublicWifiInfoFragment;
import com.uet.wifiposition.ui.main.home.scanwifi.ScanWifiInfoFragment;
import com.uet.wifiposition.ui.main.home.tracking.TrackingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ducnd on 11/18/17.
 */

public class ScanAndUpdateFragment extends BaseMvpFragment<ScanAndUpdateContract.Presenter> implements ViewPager.OnPageChangeListener, View.OnClickListener, ScanAndUpdateContract.View {
    private ViewPager vpPosition;
    private ScanAndUpdateAdapter mAdpater;
    private FloatingActionButton btnReload;

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_scan_and_update;
    }

    @Override
    public void findViewByIds(View view) {
        btnReload = (FloatingActionButton) view.findViewById(R.id.btn_reload);
        vpPosition = (ViewPager) view.findViewById(R.id.vp_position);
    }

    @Override
    public void initComponents(View view) {
        mAdpater = new ScanAndUpdateAdapter(getChildFragmentManager());
        vpPosition.setOffscreenPageLimit(3);
        vpPosition.setAdapter(mAdpater);
        TabLayout tab = (TabLayout) view.findViewById(R.id.tab);
        tab.setupWithViewPager(vpPosition);

        mPresenter = new ScanAndUpdatePresenter(this);


    }

    @Override
    public void setEvents(View view) {
        vpPosition.addOnPageChangeListener(this);

        btnReload.setOnClickListener(this);

        view.findViewById(R.id.btn_location).setOnClickListener(this);
        view.findViewById(R.id.btn_choose_all).setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (position == 0) {
            btnReload.show();
        } else {
            btnReload.hide();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reload:
                Fragment fragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 0);
                ((ScanWifiInfoFragment) fragment).reload();
                break;
            case R.id.btn_location:
                ScanWifiInfoFragment scan = (ScanWifiInfoFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 0);
                List<WifiInfoModel> wifiInfoModels = scan.getListWifiInfoChoose();
                if (wifiInfoModels == null || wifiInfoModels.size() == 0) {
                    showMessage(R.string.Loading);
                    return;
                }
                PublicWifiInfoFragment publicInfo = (PublicWifiInfoFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 1);
                int buildingId = publicInfo.getBuildingId();
                int roomId = publicInfo.getRoomId();
                if (buildingId == -1 || roomId == -1) {
                    showMessage(R.string.Loading);
                    return;
                }
                List<InfoReferencePointInput> infoReferencePointInputs = new ArrayList<>();
                for (WifiInfoModel wifiInfoModel : wifiInfoModels) {
                    infoReferencePointInputs.add(new InfoReferencePointInput(wifiInfoModel.getMacAddress(), wifiInfoModel.getName(), wifiInfoModel.getLevel()));
                }

                TrackingFragment tracking = (TrackingFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 2);
                if (tracking.isPathEmpty()) {
                    ExtendGetLocationModel model = new ExtendGetLocationModel();
                    model.setFirst(true);
                    model.setX(10);
                    model.setY(7);
                    mPresenter.getLocation(buildingId, roomId, infoReferencePointInputs, model);
                } else {
                    ExtendGetLocationModel model = new ExtendGetLocationModel();
                    model.setFirst(false);
                    model.setTransactionId(tracking.getTransactionId());
                    mPresenter.getLocation(buildingId, roomId, infoReferencePointInputs, model);
                }

                break;
            case R.id.btn_choose_all:
                ScanWifiInfoFragment scanWifiInfoFragment = (ScanWifiInfoFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 0);
                scanWifiInfoFragment.chooseAll();
                break;
            default:
                break;
        }
    }

    @Override
    public void finishGetLocaiton(GetLocationResponse response) {
        showMessage("x: " + response.getLocationModel().getX() + ", y: " + response.getLocationModel().getY());
        TrackingFragment tracking = (TrackingFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 2);
        tracking.responseTracking(response.getLocationModel());
    }

    @Override
    public void errorGetLocation(Throwable error) {
        showMessage(error.getMessage());
    }
}
