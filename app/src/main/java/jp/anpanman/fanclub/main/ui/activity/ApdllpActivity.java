package jp.anpanman.fanclub.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.main.util.Constant;

/**
 * Created by chientruong on 9/19/16.
 */
public class ApdllpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Delay 2 milliseconds and then start SplashActivity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplasActivity();
            }
        }, Constant.DELAY_TIME_APDLLPACTIVITY);

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
    //Start SpashActivity when delay 2000 milliseconds
    public void startSplasActivity(){
        Intent intent = new Intent(ApdllpActivity.this, SplashScreenActivity.class);
        startActivity(intent);
    }
}
