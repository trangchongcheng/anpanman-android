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
import com.framework.phvtUtils.NetworkUtil;
import com.framework.phvtUtils.SharedPreferencesUtil;
import com.framework.restfulService.RestfulService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.main.R;
import com.main.model.BaseModel;
import com.main.model.UserInfo;
import com.main.util.Constant;
import com.main.util.DialogFactory;
import com.main.util.RestfulUrl;
import com.main.util.RestfulUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by linhphan on 7/25/16.
 */
public class SplashScreenActivity extends BaseActivity{
    private static final int ACTION_PLAY_SERVICES_DIALOG = 100;
    private final int DELAYED_TIME_SPLASH_SCREEN = 3000;

    // Resgistration Id from GCM
    private static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    private static final String PREF_USER_INFO = "PREF_USER_INFO";
    private static final String GCM_SENDER_ID = "866234032360";//project number

    private boolean isShouldLeaveThisScreen = false;

    //=========== inherited methods ================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //== delay this screen a particular time
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoNextScreen();
            }
        }, DELAYED_TIME_SPLASH_SCREEN);


        //== check to get user info or sign up an new account
        String json = SharedPreferencesUtil.getString(this, PREF_USER_INFO, "");
        UserInfo oldUserInfo = UserInfo.fromJson(json, UserInfo.class);
        boolean isNetWorkAvailable = NetworkUtil.isOnline(this);

        if (oldUserInfo == null && !isNetWorkAvailable){
            DialogFactory.showMessage(SplashScreenActivity.this, getString(R.string.no_internet));

        }else if (oldUserInfo == null){//== oldUserInfo == null && isNetWorkAvailable
            // Read the saved gcm registration id from shared preferences.
            String gcmRegId = SharedPreferencesUtil.getString(this, PREF_GCM_REG_ID, "");
            Log.e(getClass().getName(), "registration id: "+ gcmRegId);

            if (!TextUtils.isEmpty("1")){//677556565612 // TODO: 8/5/16 this id must be replaced by gcm registration id
                signup("67755656561232411");

            }else if (isGooglePlayInstalled()) {// Check device for Play Services APK.
                new GCMRegistrationTask().execute();
            }

        }else if (isNetWorkAvailable){//== oldUserInfo != null && isNetWorkAvailable
            getUserInfo(oldUserInfo.getObjectId());

        }else{ //== oldUserInfo != null && !isNetWorkAvailable
            gotoNextScreen();
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

    private boolean isGooglePlayInstalled() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, ACTION_PLAY_SERVICES_DIALOG);
            }else{
                Toast.makeText(getApplicationContext(), "Google Play Service is not installed", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    private void signup(String gcmRegId){
        RestfulUtil.signup(this, gcmRegId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                // TODO: 8/5/16 this must be
                // TODO: 8/5/16 what should we do if the registration is duplicated many times
                UserInfo newUserInfo = (UserInfo) data;
                SharedPreferencesUtil.putString(getBaseContext(), PREF_USER_INFO, newUserInfo.toJson());
                gotoNextScreen();
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {
            }
        });
    }

    private void getUserInfo(String objectId){
        RestfulUtil.getUserInfo(this, objectId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                UserInfo newUserInfo = (UserInfo) data;
                SharedPreferencesUtil.putString(getBaseContext(), PREF_USER_INFO, newUserInfo.toJson());
                gotoNextScreen();
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {

            }
        });
    }

    private void gotoNextScreen(){
        if (!isShouldLeaveThisScreen){
            isShouldLeaveThisScreen = true;
            return;
        }

        boolean isIntroHasShowed = SharedPreferencesUtil.getBoolean(this, IntroActivity.PREF_INTRO_HAS_SHOWED, false);
        if (isIntroHasShowed){
            gotoIntroScreen();
        }else{
            gotoTopScreen();
        }
        finish();
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
            String gcmRegId = null;
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
            if (s != null){
                Toast.makeText(getApplicationContext(), "registered with GCM", Toast.LENGTH_LONG).show();
                SharedPreferencesUtil.putString(getBaseContext(), PREF_GCM_REG_ID, s);
                Log.e(getClass().getName(), "registration id: "+ s);

                signup(s);
            }
        }
    }
}
