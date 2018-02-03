package com.uet.wifiposition.ui.base;


import com.uet.wifiposition.R;

/**
 * Created by ducnd on 9/29/17.
 */

public enum  AnimationSwitchScreen {
    FULL_OPEN(R.anim.enter_to_left, R.anim.exit_to_left, R.anim.enter_to_right, R.anim.exit_to_right);
    private int openEnter;
    private int openExit;
    private int closeEnter;
    private int closeExit;

    private AnimationSwitchScreen(int openEnter, int openExit, int closeEnter, int closeExit) {
        this.openEnter = openEnter;
        this.openExit = openExit;
        this.closeEnter = closeEnter;
        this.closeExit = closeExit;
    }

    public int getOpenEnter() {
        return openEnter;
    }

    public int getOpenExit() {
        return openExit;
    }

    public int getCloseEnter() {
        return closeEnter;
    }

    public int getCloseExit() {
        return closeExit;
    }
}
