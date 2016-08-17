package jp.anpanman.fanclub.main.ui.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.RestfulUrl;

import com.main.R;

/**
 * Created by linhphan on 7/19/16.
 */
public class CouponFragment extends BaseFragment {
    private WebView webView;
    private ProgressBar progress;

    //============= inherited methods ==============================================================
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        webView = (WebView) root.findViewById(R.id.web_view);
        progress = (ProgressBar) root.findViewById(R.id.progressBar);
        setupWebView();
    }

    @Override
    protected void registerEventHandlers() {

    }

    //============= inner methods ==================================================================
    private void setupWebView(){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progress.getVisibility() == View.VISIBLE) {
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Common.onSslError(getActivity(), view, handler, error);
            }
        });
        webView.loadUrl(RestfulUrl.URL_PRESENTS);
    }
}
