package com.uet.wifiposition.ui.base;

/**
 * Created by ducnd on 16/01/2017.
 */

public interface ViewActivity extends ViewUI {
    void setViewRoot();

    void findViewByIds();

    void initComponents();

    void setEvents();

    void onBackRoot();

    BaseFragment findFragmentByTag(String tag);

}
