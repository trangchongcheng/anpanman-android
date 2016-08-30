package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.main.R;

import java.util.HashMap;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.Common;

/**
 * Created by chientruong on 8/16/16.
 */
public class WebViewFragment extends DialogFragment {
    private WebView mWebView;
    private ImageView imgClose;
    private TextView tvTitle;
    private Button btnOk;
    private String mUrl, mTitle;
    private static final String URL = "url";
    private static final String TITLE = "title";
    private ProgressBar horizontalProgress;
    private DismissCallback callback;

    public void setCallback(DismissCallback callback) {
        this.callback = callback;
    }

    public static WebViewFragment newInstance(String url,String title) {
        WebViewFragment f = new WebViewFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        mUrl = getArguments().getString(URL);
        mTitle = getArguments().getString(TITLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity a = getActivity();
        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onStop() {
        super.onStop();
        Activity a = getActivity();
        if (MainActivity.currentTab == MainActivity.MainTabs.MyPage){
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_term_use, container, false);
        btnOk = (Button) root.findViewById(R.id.btn_ok);
        btnOk.setVisibility(View.GONE);
        mWebView = (WebView) root.findViewById(R.id.web_view);
        imgClose = (ImageView) root.findViewById(R.id.imgClose);
        imgClose.setVisibility(View.VISIBLE);
        tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        horizontalProgress = (ProgressBar) root.findViewById(R.id.progressBar2);

        setValue();
        setEvent();
        setupWebView();
        return root;
    }

    public void setValue(){
        if(mTitle!=null){
            tvTitle.setText(mTitle);
        }
    }

    public void setEvent() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setupWebView() {

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
                    view.clearCache(true);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Common.onSslError(getActivity(), view, handler, error);
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
            String objectId = ((AnpanmanApp)getActivity().getApplication()).getUserInfo().getObjectId();
            AppLog.log( "setupWebView: "+mUrl+objectId);
            mWebView.loadUrl(mUrl+objectId, extraHeaders);
        }

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(callback!=null){
            callback.onDismiss();
        }
    }
    public interface DismissCallback{
        void onDismiss();
    }

}
