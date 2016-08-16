package jp.anpanman.fanclub.main.ncmb;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;

import java.util.List;

/**
 * 独自受信クラス
 */
public class NCMBService extends NCMBListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String action = data.getString("action");
        String message = data.getString("message");
        AppLog.log("tag", "action:" + action);
        AppLog.log("tag", "message:" + message);

        if (!this.getPackageName().equalsIgnoreCase(((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName())) {
            AppLog.log("cheng","flase");
            super.onMessageReceived(from, data);
        } else {
            AppLog.log("cheng","true");
            Intent i = new Intent("jp.anpanman.fanclub.PUSH_NOTIFY");
            sendBroadcast(i);
        }
    }

    public static boolean isAppRunning(Context context) {


        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services =
                activityManager.getRunningTasks(Integer.MAX_VALUE);
        if
                (services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName())) {
            return true;
        }
        return false;
    }
}
