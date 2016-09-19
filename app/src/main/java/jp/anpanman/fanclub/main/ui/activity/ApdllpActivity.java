package jp.anpanman.fanclub.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;

/**
 * Created by chientruong on 9/19/16.
 */
public class ApdllpActivity extends BaseActivity {
    private final int DELAY_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplasActivity();
            }
        },DELAY_TIME);

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_apdllp;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }
    public void startSplasActivity(){
        Intent intent = new Intent(ApdllpActivity.this, SplashScreenActivity.class);
        startActivity(intent);
    }
}
