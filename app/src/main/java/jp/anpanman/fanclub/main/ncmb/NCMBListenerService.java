//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package jp.anpanman.fanclub.main.ncmb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import com.google.android.gms.gcm.GcmListenerService;
import java.io.File;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

import jp.anpanman.fanclub.main.gcm.*;
import jp.anpanman.fanclub.main.util.Constant;

public class NCMBListenerService extends GcmListenerService {
    static final String OPEN_PUSH_START_ACTIVITY_KEY = "openPushStartActivity";
    static final String SMALL_ICON_KEY = "smallIcon";
    static final String NOTIFICATION_OVERLAP_KEY = "notificationOverlap";

    public NCMBListenerService() {
    }

    public void onMessageReceived(String from, Bundle data) {
        this.sendNotification(data);
    }

    private void sendNotification(Bundle pushData) {
        if(pushData.containsKey("message") || pushData.containsKey("title")) {
            Builder notificationBuilder = this.notificationSettings(pushData);
            ApplicationInfo appInfo = null;

            try {
                appInfo = this.getPackageManager().getApplicationInfo(this.getPackageName(), 128);
            } catch (NameNotFoundException var8) {
                throw new IllegalArgumentException(var8);
            }

            boolean containsKey = appInfo.metaData.containsKey("notificationOverlap");
            int overlap = appInfo.metaData.getInt("notificationOverlap");
            int notificationId = (new Random()).nextInt();
            if(overlap == 0 && containsKey) {
                notificationId = 0;
            }

            NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

    public Builder notificationSettings(Bundle pushData) {
        ApplicationInfo appInfo = null;
        Class startClass = null;
        String applicationName = null;
        String activityName = null;
        String packageName = null;
        int channelIcon = 0;

        try {
            appInfo = this.getPackageManager().getApplicationInfo(this.getPackageName(), 128);
            applicationName = this.getPackageManager().getApplicationLabel(this.getPackageManager().getApplicationInfo(this.getPackageName(), 0)).toString();
            activityName = appInfo.packageName + appInfo.metaData.getString("openPushStartActivity");
            packageName = appInfo.packageName;
            String intent = pushData.getString("com.nifty.Channel");
            if(intent != null) {
                File componentName = new File(this.getDir("NCMB", 0), "channels");
                File pendingIntent = new File(componentName, intent);
                if(pendingIntent.exists()) {
                    JSONObject message = NCMBLocalFile.readFile(pendingIntent);
                    if(message.has("activityClass")) {
                        activityName = message.getString("activityClass");
                    }

                    if(message.has("icon")) {
                        channelIcon = message.getInt("icon");
                    }

                    if(message.has("activityPackage")) {
                        packageName = message.getString("activityPackage");
                    }
                }
            }

            startClass = Class.forName(activityName);
        } catch (ClassNotFoundException | JSONException | NameNotFoundException var17) {
            throw new IllegalArgumentException(var17);
        }

        Intent intent1 = new Intent(this, startClass);
        intent1.addFlags(67108864);
        ComponentName componentName1 = new ComponentName(packageName, activityName);
        intent1.setComponent(componentName1);
        intent1.putExtras(pushData);
        intent1.setAction(Constant.PUSH_ACTION);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, (new Random()).nextInt(), intent1, 268435456);
        String message1 = "";
        String title = "";
        if(pushData.getString("title") != null) {
            title = pushData.getString("title");
        } else {
            title = applicationName;
        }

        if(pushData.getString("message") != null) {
            message1 = pushData.getString("message");
        }

        int userSmallIcon = appInfo.metaData.getInt("smallIcon");
        int icon;
        if(channelIcon != 0) {
            icon = channelIcon;
        } else if(userSmallIcon != 0) {
            icon = userSmallIcon;
        } else {
            icon = appInfo.icon;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(2);
        Builder notificationBuilder = (new Builder(this)).setSmallIcon(icon).setContentTitle(title).setContentText(message1).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent1);
        return notificationBuilder;
    }
}