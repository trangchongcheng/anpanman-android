package jp.anpanman.fanclub.main.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jp.anpanman.fanclub.main.ui.fragment.WebViewFragment;

public class MyWebViewClient extends WebViewClient {

    private Activity activity;

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
            openWebView(url, "Details", true);
            return true;
        }
        //          if (url.startsWith(Constant.SCHEME_ANPANMANFANCLUB)) {
//                    Map<String, String> objectID = getParams(url);
//                    // Callback: Update ObjectId
//                    if (objectID.get(Constant.SCHEME_ID) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_UPDATE_OBJECT)) {
////                         Toast.makeText(getActivity(), objectID.get("id"), Toast.LENGTH_SHORT).show();
//                    }
//
//                    // Callback: Open Extend Browser on Device through url string
//                    if (objectID.get(Constant.SCHEME_URL) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_OPEN_BROWSER)) {
////                        Intent i = new Intent(Intent.ACTION_VIEW);
////                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        i.setData(Uri.parse("https://google.com"));
////                        getActivity().startActivity(i);
//                    }
//                    return true;
//                } else {
//                    view.loadUrl(url);
//                    return true;
//                }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Common.onSslError(activity, view, handler, error);
    }

    public void openWebView(String url, String title, boolean isDetails) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title, isDetails);
        fragment.show(activity.getFragmentManager(), WebViewFragment.class.getName());
    }
}