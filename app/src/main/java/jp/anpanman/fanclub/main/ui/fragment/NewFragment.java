package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.MyWebViewClient;
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
        initAnalytics();
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
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new MyWebViewClient(getActivity()));
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("x-anp-request", "true");
        extraHeaders.put("Pragma", "no-cache");
        extraHeaders.put("Cache-Control", "no-cache");
        String objectId = ((AnpanmanApp) getActivity().getApplication()).getUserInfo().getObjectId();

        AppLog.log("setupWebView: " + RestfulUrl.URL_NEWS + objectId);
        webView.loadUrl(RestfulUrl.URL_NEWS + objectId, extraHeaders);
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

//        webView.loadUrl("http://phatvan.info/test_url_scheme.html", extraHeaders);
    }
    // init Analytics TOP - NEW Fragment
    public void initAnalytics(){
        Activity activity = getActivity();
        if (activity != null) {
            AnpanmanApp application = (AnpanmanApp) activity.getApplication();
            application.initAnalyticCategory(Constant.GA_NEW);
        }
    }

}
