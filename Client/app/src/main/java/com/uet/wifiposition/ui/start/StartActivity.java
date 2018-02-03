package com.uet.wifiposition.ui.start;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.interact.main.Interactor;
import com.uet.wifiposition.ui.base.BaseActivity;
import com.uet.wifiposition.ui.main.ScanAndUpdateActivity;

/**
 * Created by ducnd on 10/13/17.
 */

public class StartActivity extends BaseActivity {
    private EditText edtIpAddress;
    private ImageView ivLogo;
    private Button btnLogin;

    @Override
    public int getLayoutMain() {
        return R.layout.activity_start;
    }

    @Override
    public void findViewByIds() {
        edtIpAddress = (EditText) findViewById(R.id.edt_ip_address);
        ivLogo = (ImageView) findViewById(R.id.lv_logo);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }


    @Override
    public void initComponents() {
        final View contentView = findViewById(R.id.content_view);
        final View layoutEdt = findViewById(R.id.layout_edt);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                final int height = contentView.getHeight();
                final int margin = getResources().getDimensionPixelOffset(R.dimen.margin_left_right_normal);
                ObjectAnimator animationEditEdit = ObjectAnimator.ofFloat(layoutEdt, "y", height, height / 2 + margin / 2);
                animationEditEdit.setDuration(1000);
                animationEditEdit.setInterpolator(new BounceInterpolator());

                ObjectAnimator animationIvLogo = ObjectAnimator.ofFloat(ivLogo, "y", -ivLogo.getHeight(), height / 2 - margin / 2 - ivLogo.getHeight());
                animationIvLogo.setDuration(1000);
                animationIvLogo.setInterpolator(new BounceInterpolator());

                AnimatorSet set = new AnimatorSet();
                set.playTogether(animationEditEdit, animationIvLogo);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btnLogin.setY(edtIpAddress.getHeight() + height / 2 + margin * 3.5f);
                        btnLogin.setVisibility(View.VISIBLE);

                        ObjectAnimator animationLogin = ObjectAnimator.ofFloat(btnLogin, "alpha", 0, 1.0f);
                        animationLogin.setDuration(500);
                        animationLogin.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                set.start();


            }
        });
    }

    @Override
    public void setEvents() {
        btnLogin.setOnClickListener(view -> {
            String ipAddress = edtIpAddress.getText().toString().trim();
            Interactor.setUrl(ipAddress + "/");
            Intent intent = new Intent();
            intent.setClass(StartActivity.this, ScanAndUpdateActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
