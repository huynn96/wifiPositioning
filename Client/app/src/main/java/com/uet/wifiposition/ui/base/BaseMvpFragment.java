package com.uet.wifiposition.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.uet.wifiposition.common.Constants;
import com.uet.wifiposition.utils.*;

import com.uet.wifiposition.remote.interact.interf.IBasePresenter;

import java.util.Date;


/**
 * Created by ducnd on 16/01/2017.
 */

public abstract class BaseMvpFragment<P extends IBasePresenter> extends BaseFragment {
    private long mFirstLoad;

    protected P mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFirstLoad = new Date().getTime();
        super.onViewCreated(view, savedInstanceState);
    }

    protected <T> void finishLoad(T t, Action1<T> action) {
        if (mIsDestroyView) {
            return;
        }
        if (mFirstLoad == -1) {
            action.call(t);
        } else {
            long currentTime = new Date().getTime();
            if (currentTime - mFirstLoad >= Constants.DURATION_ANIMATION) {
                action.call(t);
            } else {
                new Handler().postDelayed(() -> {
                    if (mIsDestroyView) {
                        return;
                    }
                    action.call(t);
                }, Constants.DURATION_ANIMATION - (currentTime - mFirstLoad));
            }
            mFirstLoad = -1;
        }
    }


    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroyView();
    }
}
