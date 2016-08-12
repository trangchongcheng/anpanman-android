package jp.anpanman.fanclub.main.ncmb;

import android.os.Bundle;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
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
    //NCMBDialogPushConfigurationクラスのインスタンスを作成
    static NCMBDialogPushConfiguration dialogPushConfiguration = new NCMBDialogPushConfiguration();

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
        dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG);
        NCMBPush.dialogPushHandler(this, data, dialogPushConfiguration);

        //デフォルトの通知を実行
        super.onMessageReceived(from, data);
    }
}
