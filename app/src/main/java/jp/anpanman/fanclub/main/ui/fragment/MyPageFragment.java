package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.RestfulUrl;

/**
 * Created by linhphan on 7/19/16.
 */
public class MyPageFragment extends BaseFragment {
    private WebView webView;
    private ProgressBar horizontalProgress;

    //============= inherited methods ==============================================================
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_mypage;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity a = getActivity();
        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    //============= inner methods ==================================================================

}
