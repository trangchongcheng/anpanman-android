package jp.anpanman.fanclub.main.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.main.R;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.NetworkUtil;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.framework.restfulService.RestfulService;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.DialogFactory;
import jp.anpanman.fanclub.main.util.RestfulUtil;

/**
 * Created by linhphan on 7/25/16.
 */
public class SplashScreenActivity extends BaseActivity {
    private static final int ACTION_PLAY_SERVICES_DIALOG = 100;
    private final int DELAY_TIME = 2000;
    private UserInfo mLocalUserInfo;

    //=========== inherited methods ================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //== delay this screen a particular time
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainProcess();
            }
        }, DELAY_TIME);
    }

    public void mainProcess() {
        // Initialize NCMB
        NCMB.initialize(this, Constant.NOTIFY_APPLICATION_KEY_NCMB,
                Constant.NOTIFY_CLIENT_KEY_NCMB);

        // Check to get user info or sign up an new account
        String json = SharedPreferencesUtil.getString(this, Constant.PREF_USER_INFO, "");
        mLocalUserInfo = UserInfo.fromJson(json, UserInfo.class);

        boolean isNetWorkAvailable = NetworkUtil.isOnline(this);
        if (mLocalUserInfo == null && !isNetWorkAvailable) {
            DialogFactory.showMessage(this, getString(R.string.no_internet));

        } else if (mLocalUserInfo == null) {//== mLocalUserInfo == null && isNetWorkAvailable
            // Read the saved gcm registration id from shared preferences.
            registrationId();

        } else if (isNetWorkAvailable) {//== mLocalUserInfo != null && isNetWorkAvailable
            getUserInfo(mLocalUserInfo.getObjectId());

        } else { //== mLocalUserInfo != null && !isNetWorkAvailable
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

    private void getUserInfo(String objectId) {
        RestfulUtil.getUserInfo(this, objectId, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                mLocalUserInfo = (UserInfo) data;
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
                                    saveError.printStackTrace();
                                    AppLog.log("SplasScreenActivity Error1", saveError.toString());
                                }
                            }
                        } else {
                            //ID取得失敗
                            AppLog.log("SplasScreenActivity Error2", e.toString());
                        }
                    }
                });
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

    private void signup(final String deviceToken) {
        //RegisterID Push
        AppLog.log("Device Token", deviceToken);
        RestfulUtil.signup(this, deviceToken, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                // TODO: 8/5/16 what should we do if the registration is duplicated many times
                mLocalUserInfo = (UserInfo) data;
                registerDeviceToken(deviceToken, mLocalUserInfo.getObjectId());
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

    private void gotoNextScreen() {
        saveUserInfoToLocal();

        boolean isIntroHasShowed = SharedPreferencesUtil.getBoolean(this, IntroActivity.PREF_INTRO_HAS_SHOWED, false);
        boolean isTermsAccepted = SharedPreferencesUtil.getBoolean(this, TermOfUseActivity.PREF_TERMS_HAS_ACCEPTED, false);
        if (!isIntroHasShowed) {
            gotoIntroScreen();
        } else if (!isTermsAccepted){
            gotoTermsOfUseScreen();
        }else{
            gotoTopScreen();
        }
        finish();
    }

    public void saveUserInfoToLocal() {
        ((AnpanmanApp)(getApplication())).setUserInfo(mLocalUserInfo);
        SharedPreferencesUtil.putString(getBaseContext(), Constant.PREF_USER_INFO, mLocalUserInfo.toJson());
    }

    private void gotoIntroScreen() {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    private void gotoTermsOfUseScreen(){
        Intent intent = new Intent(this, TermOfUseActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoTopScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        if (Constant.PUSH_ACTION.equals(getIntent().getAction()) && getIntent().getExtras() != null){
            Bundle bundle = new Bundle();
            bundle.putBoolean(MainActivity.ARG_SHOULD_SHOW_PUSH_DIALOG, true);
            bundle.putString(MainActivity.ARG_PUSH_MESSEAGE,getIntent().getExtras().getString("message"));
            bundle.putString(MainActivity.ARG_PUSH_TITLE,getIntent().getExtras().getString("title"));
            bundle.putString(MainActivity.ARG_PUSH_URL,getIntent().getExtras().getString("com.nifty.RichUrl"));

            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    //=========== inner classes ====================================================================
    /**
     * registering sender id to GCM server.
     */
}