package jp.anpanman.fanclub.main.ncmb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.CustomDialogCoupon;

import com.nifty.cloud.mb.core.NCMBDialogPushConfiguration;
import com.nifty.cloud.mb.core.NCMBGcmListenerService;
import com.nifty.cloud.mb.core.NCMBPush;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 独自受信クラス
 */
public class NCMBService extends NCMBGcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String action = data.getString("action");
        String message = data.getString("message");
        AppLog.log("tag", "action:" + action);
        AppLog.log("tag", "message:" + message);
        if (data.containsKey("com.nifty.Data")) {
            try {
                JSONObject json = new JSONObject(data.getString("com.nifty.Data"));
                Iterator keys = json.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = json.getString(key);
                    AppLog.log("tag", "key: " + key);
                    AppLog.log("tag", "value: " + value);
                }
            } catch (JSONException e) {
                //エラー処理
            }
        }
        Intent i = new Intent("com.hmkcode.android.USER_ACTION");
        sendBroadcast(i);
        super.onMessageReceived(from, data);
    }
}
