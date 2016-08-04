package com.main.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.framework.phvtActivity.BaseActivity;
import com.framework.phvtUtils.AppLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.main.R;
import com.main.util.Constant;
import com.main.util.DialogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by linhphan on 7/25/16.
 */
public class SplashScreenActivity extends BaseActivity implements Handler.Callback{

    private final int DELAYED_TIME_SPLASH_SCREEN = 3000;

    // Resgistration Id from GCM
    private static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    private static final String GCM_SENDER_ID = "866234032360";//project number
    private static final String WEB_SERVER_URL = "YOUR_WER_SERVER_URL";

    private static final int ACTION_PLAY_SERVICES_DIALOG = 100;
    protected static final int MSG_REGISTER_WITH_GCM = 101;
    protected static final int MSG_REGISTER_WEB_SERVER = 102;
    protected static final int MSG_REGISTER_WEB_SERVER_SUCCESS = 103;
    protected static final int MSG_REGISTER_WEB_SERVER_FAILURE = 104;

    private String gcmRegId;

    private SharedPreferences prefs;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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





        // Read saved registration id from shared preferences.
        gcmRegId = getSharedPreferences().getString(PREF_GCM_REG_ID, "");
        Log.e(getClass().getName(), "registration id: "+ gcmRegId);

        if (TextUtils.isEmpty(gcmRegId) && isGooglePlayInstalled()) {// Check device for Play Services APK.
            handler.sendEmptyMessage(MSG_REGISTER_WITH_GCM);
        }else{
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoIntroScreen();
                finish();
            }
        }, DELAYED_TIME_SPLASH_SCREEN);
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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REGISTER_WITH_GCM:
                new GCMRegistrationTask().execute();
                break;
            case MSG_REGISTER_WEB_SERVER:
                //send sender id to our server
//                    new WebServerRegistrationTask().execute();
                gotoIntroScreen();
                finish();

                break;
            case MSG_REGISTER_WEB_SERVER_SUCCESS:
                Toast.makeText(getApplicationContext(),
                        "registered with web server", Toast.LENGTH_LONG).show();
                break;
            case MSG_REGISTER_WEB_SERVER_FAILURE:
                Toast.makeText(getApplicationContext(),
                        "registration with web server failed",
                        Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }


    private boolean isGooglePlayInstalled() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, ACTION_PLAY_SERVICES_DIALOG);
            }else{
                Toast.makeText(getApplicationContext(), "Google Play Service is not installed", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private synchronized SharedPreferences getSharedPreferences() {
        if (prefs == null) {
            prefs = getApplicationContext().getSharedPreferences(
                    Constant.ANPANMAN_SHAREDPREFERENCES_FILE, Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public synchronized void saveInSharedPref(String result) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_GCM_REG_ID, result);
        editor.apply();
    }

    private String getSignature(String requestMethod, URI uri, String applicationKey, String clientKey) throws Exception{
        //タイムスタンプを取得
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
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

    //=========== inner classes ====================================================================
    /**
     * registering sender id to GCM server.
     */
    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {
        private IOException ioException;
        @Override
        protected String doInBackground(Void... params) {
            try {
                if (isGooglePlayInstalled()) {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    gcmRegId = gcm.register(GCM_SENDER_ID);
                }

            } catch (IOException e) {
                ioException = e;
                e.printStackTrace();
            }

            return gcmRegId;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ioException != null){
                DialogFactory.showMessage(SplashScreenActivity.this, getString(R.string.no_internet));

            }else if (s != null){
                Toast.makeText(getApplicationContext(), "registered with GCM", Toast.LENGTH_LONG).show();
//                mTxtMessage.setText(s);
                saveInSharedPref(s);
                Log.e(getClass().getName(), "registration id: "+ s);
                handler.sendEmptyMessage(MSG_REGISTER_WEB_SERVER);
            }
        }
    }
}
