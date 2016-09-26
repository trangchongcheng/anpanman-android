package jp.anpanman.fanclub.main.ncmb;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.StringUtil;

/**
 * 独自受信クラス
 */
public class NCMBService extends NCMBListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");
        String url = data.getString("com.nifty.RichUrl");
        AppLog.log("tag", "title:" + title);
        AppLog.log("tag", "message:" + message);
        AppLog.log("tag", "url:" + url);
        //App is exit
        if (!this.getPackageName().equalsIgnoreCase(((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName())) {
            AppLog.log("app is exit","flase");
            super.onMessageReceived(from, data);
        }
        //app is running
        else {
            AppLog.log("app is running","true");
            if (StringUtil.isEmpty(url)) {
                return;
            }

            Intent i = new Intent("jp.anpanman.fanclub.PUSH_NOTIFY");
            i.putExtra("url",url);
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
