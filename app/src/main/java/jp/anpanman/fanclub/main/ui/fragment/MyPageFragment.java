package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
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
    private LinearLayout llBadge;
    private Button btnRegister;
    private ImageView imgNickName;
    //============= inherited methods ==============================================================


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = ((AnpanmanApp)(getActivity().getApplication())).getUserInfo();
        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());
      //  userCharacter = UserCharacter.getUserCharacter(getActivity(), 8);
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
        llBadge = (LinearLayout) root.findViewById(R.id.ll_badge);
        btnRegister = (Button) root.findViewById(R.id.btn_register);
        imgNickName = (ImageView) root.findViewById(R.id.img_nick_name);

        tvUserID.setText("ID:" + userInfo.getId());
        tvUserName.setText(userInfo.getNickName());
        imgUserIcon.setImageResource(userCharacter.getIconResource());

        //landscape mode
        if ("mypage_landscape".equals(root.getTag())) {
            llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
            llMypageBgLand.setBackgroundResource(userCharacter.getBgResource());

            if (TextUtils.isEmpty(userInfo.getNickName())) {
                tvUserName.setVisibility(View.GONE);
                imgNickName.setVisibility(View.VISIBLE);
            }

        //portrait mode
        } else {
            if (TextUtils.isEmpty(userInfo.getNickName())) {
                tvUserName.setVisibility(View.GONE);
                imgNickName.setVisibility(View.VISIBLE);
                llBadge.setVisibility(View.GONE);

                btnRegister.setVisibility(View.VISIBLE);
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWebView(RestfulUrl.URL_REGISTER_MYPAGE,"Register Account");
                    }
                });
            }

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

        //unregistered
//        if (TextUtils.isEmpty(userInfo.getNickName())) {
//            if ("mypage_landscape".equals(root.getTag())) {
//                llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
//                llMypageBgLand.setBackgroundResource(R.drawable.img_orange_background);
//                imgNickName.setVisibility(View.VISIBLE);
//                tvUserName.setVisibility(View.GONE);
//             //portrait mode
//            } else {
//                changeLayout();
//                btnRegister.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openWebView(RestfulUrl.URL_REGISTER_MYPAGE,"Register Account");
//                    }
//                });
//            }
//
//        //registered
//        }else {
//            //landscape mode
//            if ("mypage_landscape".equals(root.getTag())) {
//                llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
//                llMypageBgLand.setBackgroundResource(userCharacter.getBgResource());
//
//            //portrait mode
//            }else{
//                //==display badges
//                for (String key : userInfo.getBadges().keySet()){
//                    if ("1".equals(key)){
//                        root.findViewById(R.id.badge1).setVisibility(View.VISIBLE);
//                    }else if ("2".equals(key)){
//                        root.findViewById(R.id.badge2).setVisibility(View.VISIBLE);
//                    }else if ("3".equals(key)){
//                        root.findViewById(R.id.badge3).setVisibility(View.VISIBLE);
//                    }else if ("4".equals(key)){
//                        root.findViewById(R.id.badge4).setVisibility(View.VISIBLE);
//                    }else if ("5".equals(key)){
//                        root.findViewById(R.id.badge5).setVisibility(View.VISIBLE);
//                    }else if ("6".equals(key)){
//                        root.findViewById(R.id.badge6).setVisibility(View.VISIBLE);
//                    }else if ("7".equals(key)){
//                        root.findViewById(R.id.badge7).setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//            tvUserID.setText("ID:" + userInfo.getId());
//            tvUserName.setText(userCharacter.getName());
//            imgUserIcon.setImageResource(userCharacter.getIconResource());
//        }
    }

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.log("onResume: ");
        Activity a = getActivity();
        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
//            Activity a = getActivity();
//            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Log.d("cheng", "setUserVisibleHint: "+isVisibleToUser);

    }

    //============= inner methods ==================================================================
//    public void changeLayout() {
//        tvUserName.setVisibility(View.GONE);
//        llBage.setVisibility(View.GONE);
//        btnRegister.setVisibility(View.VISIBLE);
//        imgNickName.setVisibility(View.VISIBLE);
//    }

    public void openWebView(String url, String title) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title,false);
        fragment.show(getActivity().getFragmentManager(), WebViewFragment.class.getName());
    }
}