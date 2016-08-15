package jp.anpanman.fanclub.main.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.main.R;
import jp.anpanman.fanclub.main.ui.activity.SplashScreenActivity;

/**
 * Created by linhphan on 11/16/15.
 */
public class GcmIntentService extends IntentService{

    public static final String ARG_MESSAGE = "ARG_MESSAGE";

    public static final int NOTIFICATION_ID = 1000;
    NotificationManager mNotificationManager;

    public GcmIntentService() {
        super(GcmIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if ("com.google.android.c2dm.intent.REGISTRATION".equals(intent.getAction())){
            String message = "GCM service has been registered";
            showNotification(message);

        }else{//== com.google.android.c2dm.intent.RECEIVE
            if (!extras.isEmpty()){
                // read extras as sent from server
                String message = extras.getString("message");
                showNotification(message);
                makeVibrate();
            }
        }


        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void showNotification(String msg) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.putExtra(ARG_MESSAGE, msg);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Anpanman")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        builder.setContentIntent(pendingIntent);

        Notification notify = builder.build();
        notify.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(NOTIFICATION_ID, notify);
    }

    private void makeVibrate(){
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(10000);
    }
}
