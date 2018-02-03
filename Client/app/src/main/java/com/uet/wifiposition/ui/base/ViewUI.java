package com.uet.wifiposition.ui.base;

import android.os.Bundle;

/**
 * Created by ducnd on 16/01/2017.
 */

public interface ViewUI {
    int getLayoutMain();

    void showProgress();

    void hideProgress();

    void showMessage(String message);

    void showMessage(int messageId);

    boolean isDestroyView();
}
