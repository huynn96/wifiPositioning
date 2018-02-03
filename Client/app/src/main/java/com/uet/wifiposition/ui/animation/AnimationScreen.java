package com.uet.wifiposition.ui.animation;

import com.uet.wifiposition.R;

/**
 * Created by ducnd on 11/18/17.
 */

public class AnimationScreen {
    private final int enterToLeft;
    private final int exitToLeft;
    private final int enterToRight;
    private final int exitToRight;

    public AnimationScreen(int enterToLeft, int exitToLeft, int enterToRight, int exitToRight) {
        this.enterToLeft = enterToLeft;
        this.exitToLeft = exitToLeft;
        this.enterToRight = enterToRight;
        this.exitToRight = exitToRight;
    }

    public int getEnterToLeft() {
        return enterToLeft;
    }

    public int getExitToLeft() {
        return exitToLeft;
    }

    public int getEnterToRight() {
        return enterToRight;
    }

    public int getExitToRight() {
        return exitToRight;
    }

    public static AnimationScreen getAnimationFullScreen() {
        return new AnimationScreen(R.anim.enter_to_left, R.anim.exit_to_left, R.anim.enter_to_right, R.anim.exit_to_right);
    }
}
