package com.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.framework.phvtActivity.BaseActivity;
import com.main.R;

/**
 * Created by linhphan on 7/25/16.
 */
public class SplashScreenActivity extends BaseActivity{

    private final int DELAYED_TIME_SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoIntroScreen();
                finish();
            }
        }, DELAYED_TIME_SPLASH_SCREEN);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private void gotoIntroScreen(){
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    private void gotoTopScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
