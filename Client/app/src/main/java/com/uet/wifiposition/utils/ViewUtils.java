package com.uet.wifiposition.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ducnd on 9/29/17.
 */

public class ViewUtils {
    public static void commitTransactionFragment(FragmentTransaction transaction, FragmentManager manager) {
        try {
            transaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
