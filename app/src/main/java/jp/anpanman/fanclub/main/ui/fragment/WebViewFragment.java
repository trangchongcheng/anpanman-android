package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import java.util.HashMap;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.model.MainTabs;
import jp.anpanman.fanclub.main.util.Common;

/**
 * Created by chientruong on 8/16/16.
 */
public class WebViewFragment extends DialogFragment implements View.OnClickListener {
    private WebView mWebView;
    private ImageView imgClose;
    private ImageView imgNext;
    private ImageView imgPrevious;
    private LinearLayout ll;
    private TextView tvTitle;
    private String mUrl, mTitle;
    private boolean isDetails;
    private static final String URL = "url";
    private static final String TITLE = "title";
    private static final String IS_DETAILS = "is_details";
    private ProgressBar horizontalProgress;
    private DismissCallback callback;

    public void setCallback(DismissCallback callback) {
        this.callback = callback;
    }

    public static WebViewFragment newInstance(String url, String title, boolean isDetails) {
        WebViewFragment f = new WebViewFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        args.putBoolean(IS_DETAILS, isDetails);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        mUrl = getArguments().getString(URL);
        mTitle = getArguments().getString(TITLE);
        isDetails = getArguments().getBoolean(IS_DETAILS);

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity a = getActivity();
        if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onStop() {
        super.onStop();
        Activity a = getActivity();
        if (MainActivity.currentTab == MainTabs.MyPage) {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
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
        ll = (LinearLayout) root.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
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

    public void setValue() {
        if (mTitle != null) {
            tvTitle.setText(mTitle);
        }
    }

    public void setEvent() {
        imgClose.setOnClickListener(this);
//        imgPrevious.setOnClickListener(this);
//        imgNext.setOnClickListener(this);
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
                    AppLog.log("Url-click1", url);
                    view.clearCache(true);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Common.onSslError(getActivity(), view, handler, error);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    AppLog.log("Cheng", "shouldOverrideUrlLoading: " + url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            mWebView.setWebChromeClient(new WebChromeClient() {
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

                @Override
                public void onReceivedTitle(WebView view, String sTitle) {
                    super.onReceivedTitle(view, sTitle);
                    if (sTitle != null && sTitle.length() > 0 && isDetails) {
                        tvTitle.setText(sTitle);
                    } else {
                        tvTitle.setText(mTitle);
                    }
                }
            });
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("x-anp-request", "true");
            String objectId = ((AnpanmanApp) getActivity().getApplication()).getUserInfo().getObjectId();
            if (isDetails) {
                mWebView.loadUrl(mUrl, extraHeaders);
            } else {
                mWebView.loadUrl(mUrl + objectId, extraHeaders);
            }
            AppLog.log("Url-click2", mUrl);


        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) {
            callback.onDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface DismissCallback {
        void onDismiss();
    }

}
