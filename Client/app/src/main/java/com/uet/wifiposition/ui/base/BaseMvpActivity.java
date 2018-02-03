package com.uet.wifiposition.ui.base;


import com.uet.wifiposition.remote.interact.interf.IBasePresenter;

/**
 * Created by ducnd on 16/01/2017.
 */

public abstract class BaseMvpActivity<P extends IBasePresenter> extends BaseActivity implements ViewActivity {
    protected P presenter;

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
