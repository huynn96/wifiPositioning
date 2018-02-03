package com.uet.wifiposition.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by ducnd on 21/01/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements ViewActivity {
    protected View mRootView;
    protected boolean mIsClearMemoryActivity;
    private View mProgress;
    protected boolean mIsDestroyView = true;
    private boolean mIsStarted;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        boolean isHideStatusBar = false;

        mIsDestroyView = false;
        beforeInit(savedInstanceState);
        if (!mIsClearMemoryActivity) {
            setViewRoot();
            findViewByIds();
            initComponents();
            setEvents();
        }
    }


    public void setViewRoot() {
        setContentView(getLayoutMain());
    }

    protected void beforeInit(@Nullable Bundle savedInstanceState) {
//        if (savedInstanceState != null ) {
//            mIsClearMemoryActivity = true;
//        }
    }

    /**
     * Implement ViewUI interface
     */

    @Override
    public void hideProgress() {
        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }
    }

    protected void setProgress(View progress) {
        mProgress = progress;
    }

    @Override
    public void showProgress() {
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        showSimpleSnack(message);
    }

    @Override
    public void showMessage(int messageId) {
        showSimpleSnack(messageId);
    }


    public void showSimpleSnack(final String message) {
        if (mRootView == null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return;
        }
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showSimpleSnack(final int messageId) {
        if (mRootView == null) {
            Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
            return;
        }
        Snackbar snackbar = Snackbar.make(mRootView, messageId, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void setRootView(View rootView) {
        mRootView = rootView;
    }

    public boolean hideKeyBoard() {
        View view = getCurrentFocus();
        if (view == null) {
            return false;
        }
//        view.setFocusable(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        mIsDestroyView = true;
        super.onDestroy();
    }

    @Override
    public final void onBackRoot() {
        super.onBackPressed();
    }

    @Override
    public BaseFragment findFragmentByTag(String tag) {
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public boolean isDestroyView() {
        return mIsDestroyView;
    }

    //
    public void hideStatusbar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        BaseFragment baseFragment = BaseFragment.getCurrenFragment(getSupportFragmentManager());
        if (null == baseFragment) {
            super.onBackPressed();
        } else {
            baseFragment.onBackRoot();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStarted = true;
        Log.d("LollyApp", "onStart activity: " + getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        mIsStarted = false;
        Log.d("LollyApp", "onStop activity: " + getClass().getSimpleName());
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean isStarted() {
        return mIsStarted;
    }
}
