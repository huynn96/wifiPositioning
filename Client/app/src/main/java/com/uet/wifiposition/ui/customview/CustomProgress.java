package com.uet.wifiposition.ui.customview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * Created by ducnd on 9/29/17.
 */

public class CustomProgress extends RelativeLayout {
    public CustomProgress(Context context) {
        super(context);
        init();
    }

    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(24f);
        }
        ProgressBar progressBar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(params);
        addView(progressBar);
        setOnTouchListener((v, event) -> {
            return true;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}
