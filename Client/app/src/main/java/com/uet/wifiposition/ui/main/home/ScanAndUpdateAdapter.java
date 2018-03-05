package com.uet.wifiposition.ui.main.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uet.wifiposition.ui.base.BaseFragment;
import com.uet.wifiposition.ui.main.home.motion.MotionFragment;
import com.uet.wifiposition.ui.main.home.publicwifiinfo.PublicWifiInfoFragment;
import com.uet.wifiposition.ui.main.home.scanwifi.ScanWifiInfoFragment;
import com.uet.wifiposition.ui.main.home.tracking.TrackingFragment;

/**
 * Created by ducnd on 9/22/17.
 */

public class ScanAndUpdateAdapter extends FragmentPagerAdapter {
    private final ScanWifiInfoFragment fragment1;
    private final PublicWifiInfoFragment fragment2;

    public ScanAndUpdateAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new ScanWifiInfoFragment();
        fragment2 = new PublicWifiInfoFragment();
        fragment2.setInterf(fragment1);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment result;
        switch (position) {
            case 0:
                result = fragment1;
                break;
            case 1:
                result = fragment2;
                break;
            case 2:
                result = new TrackingFragment();
                break;
            default:
                result = new MotionFragment();
                break;
        }
        return result;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String result;
        switch (position) {
            case 0:
                result = "Wifi info";
                break;
            case 1:
                result = "Update database";
                break;
            case 2:
                result = "Tracking";
                break;
            default:
                result = "Motion";
                break;
        }
        return result;
    }
}
