package com.uet.wifiposition.remote.interact.main;

import android.util.Log;

import com.uet.wifiposition.common.Constants;
import com.uet.wifiposition.remote.model.IBaseResponse;
import com.uet.wifiposition.ui.base.ViewUI;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import com.uet.wifiposition.utils.*;
import com.uet.wifiposition.remote.interact.interf.IBasePresenter;

import java.io.InterruptedIOException;

/**
 * Created by ducnd on 9/29/17.
 */

public abstract class BasePresenter<V extends ViewUI> implements IBasePresenter {
    private CompositeDisposable mDispose;
    private boolean mIsDestroy;
    protected V mView;

    public BasePresenter(V view) {
        mView = view;
        mIsDestroy = false;
        mDispose = new CompositeDisposable();
    }

    protected <T extends IBaseResponse> void subscribeHasDispose(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError) {
        if (null == observable) {
            onError.call(new NullPointerException("Result NULL"));
            return;
        }
        Disposable disposable = observable.subscribe(t -> {
            if (mIsDestroy) {
                return;
            }
            onNext.call(t);
        }, error -> {
            error.printStackTrace();
            if (Constants.DEBUG) {
                mView.showMessage(error.getMessage());
            }
            if (error instanceof InterruptedIOException) {
                return;
            }
            if (mIsDestroy) {
                return;
            }
            Log.d("BasePresenter", "subscribeHasDispose message error: " + error.getMessage());
            Log.d("BasePresenter", "subscribeHasDispose message errormes: " + error.toString());
//            if (!StringUtils.isEmpty(error.getMessage()) && error.getMessage().contains(UnknownHostException.class.getName())) {
////                    mView.showMessage(R.string.Disconnect_network);
//            } else {
////                    if (Constants.DEBUG) {
////                        mView.showMessage(error.getMessage());
////                    }
//            }

            onError.call(error);
        });
        mDispose.add(disposable);
    }

    @Override
    public void onDestroy() {
        mIsDestroy = true;
        mDispose.dispose();
    }
}
