package com.uet.wifiposition.ui.base;

import android.os.Bundle;

/**
 * Created by ducnd on 11/11/2016.
 */

public interface ViewFragment extends ViewUI {
    void findViewByIds(android.view.View view);

    void initComponents(android.view.View view);

    void setEvents(android.view.View view);

    BaseActivity getBaseActivity();

    void reload(Bundle bundle);

    void onBackRoot();
}
