package jp.anpanman.fanclub.main.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jp.anpanman.fanclub.main.ui.activity.MainActivity;

public class PushNotifyListenReceiver extends BroadcastReceiver{
 
    @Override
    public void onReceive(Context context, Intent intent) {
        CustomDialogCoupon customDialogCoupon = new CustomDialogCoupon(context);
        customDialogCoupon.show();
    }
     
}