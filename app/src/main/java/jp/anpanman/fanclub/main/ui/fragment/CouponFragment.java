package jp.anpanman.fanclub.main.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhphan on 7/19/16.
 */
public class CouponFragment extends BaseFragment {
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

        //Disable cache Webview
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new MyWebViewClient(getActivity()));
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
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("x-anp-request", "true");
        extraHeaders.put("Pragma", "no-cache");
        extraHeaders.put("Cache-Control", "no-cache");
        String objectId = ((AnpanmanApp) getActivity().getApplication()).getUserInfo().getObjectId();
        webView.loadUrl(RestfulUrl.URL_COUPON + objectId, extraHeaders);
    }

    // init Analytics Coupon Fragment
    public void initAnalytics() {
        Activity activity = getActivity();
        if (activity != null) {
            AnpanmanApp application = (AnpanmanApp) activity.getApplication();
            application.initAnalyticCategory(Constant.GA_OTOKU);
        }

    }
}
