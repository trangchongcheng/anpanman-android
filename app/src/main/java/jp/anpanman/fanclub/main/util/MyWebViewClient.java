package jp.anpanman.fanclub.main.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.fragment.WebViewFragment;

public class MyWebViewClient extends WebViewClient {

    private Activity activity;
    private UserInfo mUserInfo;

    public MyWebViewClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        System.out.println("your current url when webpage loading.." + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        System.out.println("your current url when webpage loading.. finish " + url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        System.out.println("when you click on any interlink on webview that time you got url : " + url);
        // Open product detail
        if (url.contains("detail.html")) {
            openWebView(url,"", true);
            AppLog.log("Url-click",url);
            return true;
        }
                  if (url.startsWith(Constant.SCHEME_ANPANMANFANCLUB)) {
                    Map<String, String> params = getParams(url);
                    // Callback: Update ObjectId
                    if (params.get(Constant.SCHEME_ID) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_UPDATE_OBJECT)) {
                        updateObjectID();
                    }

                    // Callback: Open Extend Browser on Device through url string
                    if (params.get(Constant.SCHEME_URL) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_OPEN_BROWSER)) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setData(Uri.parse(params.get(Constant.SCHEME_URL)));
                        activity.startActivity(i);
                    }
                    return true;
                }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Common.onSslError(activity, view, handler, error);
    }
    //Open Fragment Dialog load URL
    public void openWebView(String url, String title, boolean isDetails) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title, isDetails);
        fragment.show(activity.getFragmentManager(), WebViewFragment.class.getName());
    }
    //get params from scheme url
    public static Map<String, String> getParams(String url) {
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            if (url.indexOf('?') != -1) {
                String[] allStrings = url.split("\\?");
                if (allStrings.length > 1) {
                    String content = allStrings[1];
                    String[] contents = content.split("&");
                    for (String item : contents) {
                        String[] point = item.split("=");
                        if (point.length == 2) {
                            result.put(point[0], point[1]);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Update new Object ID to Shared Preferences when click url scheme has param is update_object
    public void updateObjectID(){
        mUserInfo = ((AnpanmanApp)(activity.getApplication())).getUserInfo();
        mUserInfo.setObjectId(Constant.SCHEME_ID);
        SharedPreferencesUtil.putString(activity, Constant.PREF_USER_INFO, mUserInfo.toJson());
    }
}