package com.main.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;

import com.framework.phvtActivity.BaseActivity;
import com.framework.phvtUtils.AppLog;
import com.main.R;
import com.main.util.Constant;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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



        try {

            //== SING UP
            URI uri = URI.create(Constant.NOTIFY_HOST_SIGNUP);
            String hash = getSignature("POST", uri, Constant.NOTIFY_APPLICATION_KEY, Constant.NOTIFY_CLIENT_KEY);
            AppLog.log("Signature: " + hash);

            //== get user's info
//            String objectId = "tNUVMG8mkSDBp1wV";
//            URI uri = URI.create(Constant.NOTIFY_HOST_GET_USER_INFO + objectId);
//            String hash = getSignature("GET", uri, Constant.NOTIFY_APPLICATION_KEY, Constant.NOTIFY_CLIENT_KEY);
//            AppLog.log("Signature: " + hash);




        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String getSignature(String requestMethod, URI uri, String applicationKey, String clientKey) throws Exception{
        //タイムスタンプを取得
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calobj = Calendar.getInstance();
        String _timestamp = df.format(calobj.getTime());

        AppLog.log("_timestamp: " + _timestamp);

        //SignatureMethod,SignatureVersion,X-NCMB-Application-Key,X-NCMB-Timestampをパラメータに追加
        StringBuilder stringBuilder = new StringBuilder(256);
        stringBuilder.append("SignatureMethod").append("=").append("HmacSHA256").append("&")
                .append("SignatureVersion").append("=").append("2").append("&")
                .append("X-NCMB-Application-Key").append("=").append(applicationKey).append("&")
                .append("X-NCMB-Timestamp").append("=").append(_timestamp);

        //署名用文字列を生成
        String sign = requestMethod.toUpperCase() + '\n' +
                uri.getHost() + '\n' +
                uri.getRawPath() + '\n' +
                stringBuilder;

        //シグネチャを生成
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(clientKey.getBytes("UTF-8"), "HmacSHA256"));
        return Base64.encodeToString(mac.doFinal(sign.getBytes("UTF-8")), Base64.NO_WRAP);
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
