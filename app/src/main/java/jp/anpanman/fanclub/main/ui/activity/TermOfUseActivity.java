package jp.anpanman.fanclub.main.ui.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.main.R;

import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.Common;


/**
 * Created by linhphan on 7/25/16.
 */
public class TermOfUseActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    private ProgressBar mProgress;
    private Button btnOk;
    private String mUrl = "https://www.google.com";

    //============== inherited methods =============================================================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_use);

        getWidgets();
        setupWebView();
        registerEventhandlers();
    }

    //============== implemented methods ===========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                gotoTopScreen();
                break;
        }
    }

    //============== inner methods =================================================================
    private void getWidgets(){
        mWebView = (WebView) findViewById(R.id.web_view);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    private void registerEventhandlers(){
        btnOk.setOnClickListener(this);
    }

    private void setupWebView(){

        if (mUrl != null) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (mProgress.getVisibility() == View.VISIBLE) {
                        mProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Common.onSslError(TermOfUseActivity.this, view, handler, error);
                }
            });
            mWebView.loadUrl(mUrl);
        }
    }

    private void gotoTopScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
