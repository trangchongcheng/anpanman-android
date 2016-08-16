package jp.anpanman.fanclub.main.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.NetworkUtil;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.framework.restfulService.RestfulService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.DialogFactory;
import jp.anpanman.fanclub.main.util.RestfulUtil;

import com.main.R;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;


import java.util.List;

/**
 * Created by linhphan on 7/25/16.
 */
public class SplashScreenActivity extends BaseActivity {
    private static final int ACTION_PLAY_SERVICES_DIALOG = 100;
    private final int DELAYED_TIME_SPLASH_SCREEN = 3000;

    private boolean isShouldLeaveThisScreen = false;
    static Boolean TestCompletion = false;

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

        Log.d("ahihi", "onCreate: "+getApplicationContext().getPackageName());
        //InitializeI NCMB
        NCMB.initialize(this, Constant.NOTIFY_APPLICATION_KEY_NCMB,
                Constant.NOTIFY_CLIENT_KEY_NCMB);

        //== check to get user info or sign up an new account
        String json = SharedPreferencesUtil.getString(this, Constant.PREF_USER_INFO, "");
        UserInfo oldUserInfo = UserInfo.fromJson(json, UserInfo.class);
        boolean isNetWorkAvailable = NetworkUtil.isOnline(this);

        if (oldUserInfo == null && !isNetWorkAvailable) {
            DialogFactory.showMessage(SplashScreenActivity.this, getString(R.string.no_internet));

        } else if (oldUserInfo == null) {//== oldUserInfo == null && isNetWorkAvailable
            // Read the saved gcm registration id from shared preferences.
            registrationId();

        } else if (isNetWorkAvailable) {//== oldUserInfo != null && isNetWorkAvailable
            getUserInfo(oldUserInfo.getObjectId());

        } else { //== oldUserInfo != null && !isNetWorkAvailable
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
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, ACTION_PLAY_SERVICES_DIALOG);
            } else {
                Toast.makeText(getApplicationContext(), "Google Play Service is not installed", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    private void signup(final String deviceToken) {
        //RegisterID Push
        AppLog.log("Device Token", deviceToken);
        RestfulUtil.signup(this, deviceToken, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                // TODO: 8/5/16 this must be
                // TODO: 8/5/16 what should we do if the registration is duplicated many times
                UserInfo newUserInfo = (UserInfo) data;
                SharedPreferencesUtil.putString(getBaseContext(), Constant.PREF_USER_INFO, newUserInfo.toJson());
                registerDeviceToken(deviceToken, newUserInfo.getObjectId());
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {
            }
        });
    }

    private void registerDeviceToken(String gcmRegId, String objectId) {
        //RegisterID Push
        RestfulUtil.registerDeviceToken(this, gcmRegId, objectId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                gotoNextScreen();
                AppLog.log(data.toString());
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {
            }
        });
    }

    private void getUserInfo(String objectId) {
        RestfulUtil.getUserInfo(this, objectId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                UserInfo newUserInfo = (UserInfo) data;
                SharedPreferencesUtil.putString(getBaseContext(), Constant.PREF_USER_INFO, newUserInfo.toJson());
                gotoNextScreen();
            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {

            }
        });
    }

    public void registrationId() {

        //DEBUG FOR PASSING GCM check
        if( Constant.Apanman_Debug.contains(Constant.DebugFlags.DEBUG_PASS_GCM_INSTALLED)){
            gotoNextScreen();
            return;
        }

        if (!isGooglePlayInstalled()){
            DialogFactory.showMessage(SplashScreenActivity.this, "you must have google play store to continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            return;
        }


        //installationの作成
        //GCMからRegistrationIdを取得
        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
        installation.getRegistrationIdInBackground(Constant.GCM_SENDER_ID
                , new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e == null) {
                            //成功
                            try {
                                //mBaaSに端末情報を保存
                                installation.save();
                                getDeviceTokenToSignup();
                            } catch (NCMBException saveError) {
                                //保存失敗
                                AppLog.log("Has NCMBException", saveError.toString());
                                if (NCMBException.DUPLICATE_VALUE.equals(saveError.getCode())) {
                                    //保存失敗 : registrationID重複
                                    updateInstallation(installation);
                                } else {
                                    //保存失敗 : その他
                                    AppLog.log("SplasScreenActivity Error1", saveError.toString());
                                }
                            }
                        } else {
                            //ID取得失敗
                            AppLog.log("SplasScreenActivity Error2", e.toString());
                        }
                        TestCompletion = true;
                    }
                });
    }

    //Update Install when user Re-Intalled app
    public static void updateInstallation(final NCMBInstallation installation) {

        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();

        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());

        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {

                //検索された端末情報のobjectIdを設定
                installation.setObjectId(results.get(0).getObjectId());

                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }


    // 登録端末のdeviceTokenを取得する
    public void getDeviceTokenToSignup() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void[] params) {
                String deviceToken = null;
                try {
                    NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
                    deviceToken = installation.getDeviceToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TestCompletion = true;
                return deviceToken;
            }

            @Override
            protected void onPostExecute(String deviceToken) {
                if (deviceToken != null) {
                    SharedPreferencesUtil.putString(getBaseContext(), Constant.GCM_SENDER_ID, deviceToken);
                    AppLog.log(deviceToken);
                    signup(deviceToken);
                }
            }
        }.execute(null, null, null);
    }

    private void gotoNextScreen() {
        if (!isShouldLeaveThisScreen) {
            isShouldLeaveThisScreen = true;
            return;
        }

        boolean isIntroHasShowed = SharedPreferencesUtil.getBoolean(this, IntroActivity.PREF_INTRO_HAS_SHOWED, false);
        if (isIntroHasShowed) {
            gotoIntroScreen();
        } else {
            gotoTopScreen();
        }
        finish();
    }

    private void gotoIntroScreen() {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    private void gotoTopScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        if (Constant.PUSH_ACTION.equals(getIntent().getAction()) && getIntent().getExtras() != null){
            Bundle bundle = new Bundle();
            bundle.putBoolean(MainActivity.ARG_SHOULD_SHOW_PUSH_DIALOG, true);
            bundle.putString(MainActivity.ARG_PUSH_MESSEAGE,getIntent().getExtras().getString("message"));
            bundle.putString(MainActivity.ARG_PUSH_TITLE,getIntent().getExtras().getString("title"));

            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    //=========== inner classes ====================================================================
    /**
     * registering sender id to GCM server.
     */
}
