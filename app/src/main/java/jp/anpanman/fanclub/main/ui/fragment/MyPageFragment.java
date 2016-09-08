package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UserCharacter;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.DrawerLocker;
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
    private LinearLayout llAddBadge;
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
        userInfo = ((AnpanmanApp) (getActivity().getApplication())).getUserInfo();
        MainActivity activity = (MainActivity) getActivity();
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
        tvUserName = (TextView) root.findViewById(R.id.tv_user_name);
        imgUserIcon = (ImageView) root.findViewById(R.id.img_user_icon);
        llBadge = (LinearLayout) root.findViewById(R.id.ll_badge);
        llAddBadge = (LinearLayout) root.findViewById(R.id.ll_add_badge);
        btnRegister = (Button) root.findViewById(R.id.btn_register);
        imgNickName = (ImageView) root.findViewById(R.id.img_nick_name);

        tvUserID.setText("ID:" + userInfo.getId());
        tvUserName.setText(userInfo.getNickName());
        imgUserIcon.setImageResource(userCharacter.getIconResource());

        //landscape mode
        if ("mypage_landscape".equals(root.getTag())) {
            llMypageBgLand = (LinearLayout) root.findViewById(R.id.ll_mypage_bgland);
            llMypageBgLand.setBackgroundResource(userCharacter.getBgResource());
            // nickname of user = blank
            if (TextUtils.isEmpty(userInfo.getNickName())) {
                tvUserName.setVisibility(View.GONE);
                imgNickName.setVisibility(View.VISIBLE);
            }
            // nickname of user # blank
            else {
                imgNickName.setVisibility(View.GONE);
                tvUserName.setVisibility(View.VISIBLE);
            }

            //portrait mode
        } else {
            // nickname of user = blank
            if (!TextUtils.isEmpty(userInfo.getNickName())) {
                tvUserName.setVisibility(View.GONE);
                imgNickName.setVisibility(View.VISIBLE);
                llBadge.setVisibility(View.GONE);

                btnRegister.setVisibility(View.VISIBLE);
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initAnalytics(true, Constant.GA_SELECT, Constant.GA_ONCLICK, Constant.GA_MYPAGE_ENTRY, 1);
                        openWebView(RestfulUrl.URL_REGISTER_MYPAGE, getString(R.string.button_register));
                    }
                });
                // nickname of user # blank
            } else {
                tvUserName.setVisibility(View.VISIBLE);
                llBadge.setVisibility(View.VISIBLE);
                HashMap map1 = new HashMap();
                map1.put("1", "cheng");
                map1.put("2", "hihi");
                map1.put("3", "hehe");
                map1.put("4", "cheng");
                map1.put("5", "hihi");
                map1.put("7", "hehe");
                userInfo.setBadges(map1);
                //==display badges
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.weight = 1.0f;
                for (String key : userInfo.getBadges().keySet()) {
                    if ("1".equals(key)) {
                        ImageView imageView1 = new ImageView(getContext());
                        imageView1.setLayoutParams(layoutParams);
                        imageView1.setImageResource(R.drawable.badge_1);
                        llAddBadge.addView(imageView1);
                    } else if ("2".equals(key)) {
                        ImageView imageView2 = new ImageView(getActivity());
                        imageView2.setLayoutParams(layoutParams);
                        imageView2.setImageResource(R.drawable.badge_2);
                        llAddBadge.addView(imageView2);
                    } else if ("3".equals(key)) {
                        ImageView imageView3 = new ImageView(getActivity());
                        imageView3.setLayoutParams(layoutParams);
                        imageView3.setImageResource(R.drawable.badge_3);
                        llAddBadge.addView(imageView3);
                    } else if ("4".equals(key)) {
                        ImageView imageView4 = new ImageView(getContext());
                        imageView4.setLayoutParams(layoutParams);
                        imageView4.setImageResource(R.drawable.badge_4);
                        llAddBadge.addView(imageView4);
                    } else if ("5".equals(key)) {
                        ImageView imageView5 = new ImageView(getActivity());
                        imageView5.setLayoutParams(layoutParams);
                        imageView5.setImageResource(R.drawable.badge_5);
                        llAddBadge.addView(imageView5);
                    } else if ("7".equals(key)) {
                        ImageView imageView7 = new ImageView(getContext());
                        imageView7.setLayoutParams(layoutParams);
                        imageView7.setImageResource(R.drawable.badge_7);
                        llAddBadge.addView(imageView7);
                    }
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
        initAnalytics(true, null, null, null, 1);
        AppLog.log("onResume: ");
        Activity a = getActivity();
        if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        //
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (a != null) {
                AnpanmanApp application = (AnpanmanApp) a.getApplication();
                application.initAnalyticCategory(Constant.GA_MEMBERSHIP);
            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            Activity a = getActivity();
//            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Log.d("cheng", "setUserVisibleHint: " + isVisibleToUser);

    }

    //============= inner methods ==================================================================

    public void openWebView(String url, String title) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title, false);
        fragment.show(getActivity().getFragmentManager(), WebViewFragment.class.getName());
    }

    // init Analytics Mypage Fragment
    public void initAnalytics(Boolean isOnlyCategory, String category, String action, String label, long value) {
        Activity activity = getActivity();
        if (activity != null) {
            AnpanmanApp application = (AnpanmanApp) activity.getApplication();
            if (isOnlyCategory) {
                application.initAnalyticCategory(Constant.GA_MYPAGE);
            } else {
                application.initAnalytic(category, action, label, value);
            }
        }
    }
}