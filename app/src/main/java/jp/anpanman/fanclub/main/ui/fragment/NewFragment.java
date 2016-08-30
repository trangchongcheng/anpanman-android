package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.RestfulUrl;

import com.main.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhphan on 7/19/16.
 */
public class NewFragment extends BaseFragment {

    private WebView webView;
    private ProgressBar horizontalProgress;

    //============= inherited methods ==============================================================
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        webView = (WebView) root.findViewById(R.id.web_view);
        horizontalProgress = (ProgressBar) root.findViewById(R.id.progressBar2);
        setupWebView();
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity a = getActivity();
        if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //============= inner methods ==================================================================
    private void setupWebView() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //set responsive
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        //set zoomable
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.clearCache(true);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Common.onSslError(getActivity(), view, handler, error);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    horizontalProgress.setVisibility(View.GONE);
                } else {
                    horizontalProgress.setProgress(newProgress);
                    horizontalProgress.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //Callback interface Webvie
                if (url.startsWith(Constant.SCHEME_ANPANMANFANCLUB)) {
                    Map<String, String> objectID = getParams(url);
                    // Callback: Update ObjectId
                    if (objectID.get(Constant.SCHEME_ID) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_UPDATE_OBJECT)) {
//                         Toast.makeText(getActivity(), objectID.get("id"), Toast.LENGTH_SHORT).show();
                    }

                    // Callback: Open Extend Browser on Device through url string
                    if (objectID.get(Constant.SCHEME_URL) != null && url.startsWith(Constant.HOST_ANPANMANFANCLUB_OPEN_BROWSER)) {
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.setData(Uri.parse("https://google.com"));
//                        getActivity().startActivity(i);
                    }
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("x-anp-request", "true");
        String objectId = ((AnpanmanApp) getActivity().getApplication()).getUserInfo().getObjectId();

        AppLog.log("setupWebView: " + RestfulUrl.URL_NEWS + objectId);
        webView.loadUrl(RestfulUrl.URL_NEWS + objectId, extraHeaders);
//        webView.loadUrl("http://phatvan.info/test_url_scheme.html", extraHeaders);
    }

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
}
