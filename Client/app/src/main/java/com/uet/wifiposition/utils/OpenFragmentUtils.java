package com.uet.wifiposition.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.uet.wifiposition.R;
import com.uet.wifiposition.ui.animation.AnimationScreen;
import com.uet.wifiposition.ui.base.BaseFragment;
import com.uet.wifiposition.ui.main.home.ScanAndUpdateFragment;

/**
 * Created by ducnd on 11/18/17.
 */

public class OpenFragmentUtils {
    public static void openFirstScanAndUpdateFragment(FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();

        BaseFragment.openFragment(manager, transaction, new ScanAndUpdateFragment(), null, false, true, null, R.id.content_view);
    }
}
