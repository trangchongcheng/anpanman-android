package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UserCharacter;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.RestfulUrl;

/**
 * Created by linhphan on 7/19/16.
 */
public class MyPageFragment extends BaseFragment {
    private WebView webView;
    private ProgressBar horizontalProgress;
    private UserCharacter userCharacter;
    private UserInfo userInfo;

    private LinearLayout llMypageBgLand;
    private ImageView imgUserIcon;
    private TextView tvUserName;
    private TextView tvUserID;
    private LinearLayout llBage;
    private Button btnRegister;
    private ImageView imgNickName;
    //============= inherited methods ==============================================================


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = ((AnpanmanApp)(getActivity().getApplication())).getUserInfo();
//        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());
        userCharacter = UserCharacter.getUserCharacter(getActivity(), 8);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_mypage;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        tvUserID = (TextView) root.findViewById(R.id.tv_user_id);
        tvUserName= (TextView) root.findViewById(R.id.tv_user_name);
        imgUserIcon = (ImageView) root.findViewById(R.id.img_user_icon);
        llBage = (LinearLayout) root.findViewById(R.id.ll_bage);
        btnRegister = (Button) root.findViewById(R.id.btn_register);
        imgNickName = (ImageView) root.findViewById(R.id.img_nick_name);

        //unregistered
        //landscape mode
        if (!userInfo.getNickName().equals("")) {
            if ("mypage_landscape".equals(root.getTag())) {
                llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
                llMypageBgLand.setBackgroundResource(R.drawable.img_orange_background);
                imgNickName.setVisibility(View.VISIBLE);
                tvUserName.setVisibility(View.GONE);

             //portrait mode
            } else {
                changeLayout();
            }

        //registered
        }else {
            //landscape mode
            if ("mypage_landscape".equals(root.getTag())) {
                llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
                llMypageBgLand.setBackgroundResource(userCharacter.getBgResource());

            //portrait mode
            }else{
                //==display badges
                for (String key : userInfo.getBadges().keySet()){
                    if ("1".equals(key)){
                        root.findViewById(R.id.badge1).setVisibility(View.VISIBLE);
                    }else if ("2".equals(key)){
                        root.findViewById(R.id.badge2).setVisibility(View.VISIBLE);
                    }else if ("3".equals(key)){
                        root.findViewById(R.id.badge3).setVisibility(View.VISIBLE);
                    }else if ("4".equals(key)){
                        root.findViewById(R.id.badge4).setVisibility(View.VISIBLE);
                    }else if ("5".equals(key)){
                        root.findViewById(R.id.badge5).setVisibility(View.VISIBLE);
                    }else if ("6".equals(key)){
                        root.findViewById(R.id.badge6).setVisibility(View.VISIBLE);
                    }else if ("7".equals(key)){
                        root.findViewById(R.id.badge7).setVisibility(View.VISIBLE);
                    }
                }
            }
            tvUserID.setText("ID:" + userInfo.getId());
            tvUserName.setText(userCharacter.getName());
            imgUserIcon.setImageResource(userCharacter.getIconResource());
        }
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
    public void changeLayout() {
        tvUserName.setVisibility(View.GONE);
        llBage.setVisibility(View.GONE);
        btnRegister.setVisibility(View.VISIBLE);
        imgNickName.setVisibility(View.VISIBLE);
    }
}
