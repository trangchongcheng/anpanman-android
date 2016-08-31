package jp.anpanman.fanclub.main.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.util.Base64;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by linhphan on 7/14/16.
 */
public class Common {
    public static void onSslError(Activity activity, WebView view, final SslErrorHandler handler, SslError error) {
        if (activity == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("ssl証明書が正しくないページですが開いてもいいですか");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    handler.cancel();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getTimeStamp(){
        //2016-08-03T06:31:52.325Z
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calobj = Calendar.getInstance();
        return df.format(calobj.getTime());
    }

    public static String getSignature(String requestMethod, URI uri, String applicationKey, String clientKey, String timestamp) throws Exception {
        //SignatureMethod,SignatureVersion,X-NCMB-Application-Key,X-NCMB-Timestampをパラメータに追加
        StringBuilder stringBuilder = new StringBuilder(256);
        stringBuilder.append("SignatureMethod").append("=").append("HmacSHA256").append("&")
                .append("SignatureVersion").append("=").append("2").append("&")
                .append("X-NCMB-Application-Key").append("=").append(applicationKey).append("&")
                .append("X-NCMB-Timestamp").append("=").append(timestamp);

        //署名用文字列を生成
        String sign = requestMethod.toUpperCase() + '\n' +
                uri.getHost() + '\n' +
                uri.getRawPath() + '\n' +
                stringBuilder;
        //シグネチャを生成
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(clientKey.getBytes("UTF-8"), "HmacSHA256"));
        String signature = Base64.encodeToString(mac.doFinal(sign.getBytes("UTF-8")), Base64.NO_WRAP);
        AppLog.log("Signature: " + signature);

        return signature;
    }

    public static boolean compareTimeGreater(String time1, String time2){
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ"; // 2016-07-14T11:45:12+09:00
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.JAPAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date cal1 = sdf.parse(time1);
     //       Calendar cal1 = sdf.getCalendar();
            Date cal2 = sdf.parse(time2);
   //         Calendar cal2 = sdf.getCalendar();

            if (cal1.getTime() > cal2.getTime()){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
