package jp.anpanman.fanclub.main.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import java.util.HashMap;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.RestfulUrl;


/**
 * Created by linhphan on 7/25/16.
 */
public class TermOfUseActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String PREF_TERMS_HAS_ACCEPTED = "PREF_TERMS_HAS_ACCEPTED";

    private WebView mWebView;
    private TextView tvTitle;
    private ProgressBar horizontalProgress;
    private Button btnOk;
    private String mUrl = "https://www.google.com";

    //============== inherited methods =============================================================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_use);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            // Campain
            String data_campain = uri.getQueryParameter("id");
            AppLog.log(data_campain);
        }
        getWidgets();
        setupWebView();
        registerEventhandlers();
    }

    //============== implemented methods ===========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                trackingAnalytics(false,Constant.GA_SELECT,Constant.GA_ONCLICK,Constant.GA_TERMS_AGREEMENT,1);
                SharedPreferencesUtil.putBoolean(this, PREF_TERMS_HAS_ACCEPTED, true);
                gotoTopScreen();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingAnalytics(true,null,null,null,1);
    }

    //============== inner methods =================================================================
    private void getWidgets(){
        mWebView = (WebView) findViewById(R.id.web_view);
        horizontalProgress = (ProgressBar) findViewById(R.id.progressBar2);
        btnOk = (Button) findViewById(R.id.btn_ok);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.terms_of_use));
    }

    private void registerEventhandlers(){
        btnOk.setOnClickListener(this);
    }

    private void setupWebView(){

        if (mUrl != null) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            //set responsive
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);

            //set zoomable
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Common.onSslError(TermOfUseActivity.this, view, handler, error);
                }
            });
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100){
                        horizontalProgress.setVisibility(View.GONE);
                    }else{
                        horizontalProgress.setProgress(newProgress);
                        horizontalProgress.setVisibility(View.VISIBLE);
                    }
                }
            });
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("x-anp-request","true");
            mWebView.loadUrl(RestfulUrl.URL_TERMS, extraHeaders);
           // mWebView.loadUrl("http://phatvan.info/test_url_scheme.html", extraHeaders);
        }
    }

    private void gotoTopScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Tracking Google Analytic for TermOfUseActivity
    public void trackingAnalytics(Boolean inOnlyScreen, String category, String action, String label, long value){
        AnpanmanApp application = (AnpanmanApp) getApplication();
        if(inOnlyScreen){
            application.trackingAnalyticByScreen(Constant.GA_TERMS);
        }else {
            application.trackingWithAnalyticGoogleServices(category, action, label, value);
        }

    }

}
