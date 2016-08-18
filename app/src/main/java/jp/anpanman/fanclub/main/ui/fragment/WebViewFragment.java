package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.main.util.Common;

/**
 * Created by chientruong on 8/16/16.
 */
public class WebViewFragment extends DialogFragment {
    private WebView mWebView;
    private ImageView imgClose;
    private TextView tvTitle;
    private ProgressBar mProgress;
    private LinearLayout ll;
    private String mUrl, mTitle;
    private static final String URL = "url";
    private static final String TITLE = "title";

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
        mProgress = (ProgressBar) root.findViewById(R.id.progressBar);
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
                    if (mProgress.getVisibility() == View.VISIBLE) {
                        mProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Common.onSslError(getActivity(), view, handler, error);
                }
            });
            mWebView.loadUrl(mUrl);
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
