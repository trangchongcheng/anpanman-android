package jp.anpanman.fanclub.main.ui.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import java.util.Set;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UserCharacter;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.DrawableZoom;
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
    private MainActivity activity;
    //============= inherited methods ==============================================================


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = ((AnpanmanApp) (getActivity().getApplication())).getUserInfo();
        activity = (MainActivity) getActivity();
        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());
        //  userCharacter = UserCharacter.getUserCharacter(getActivity(), 8);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Hide status bar
            hideStatusBar();
        }
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
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
            if (TextUtils.isEmpty(userInfo.getNickName())) {
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

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1.0f;
                ImageView imageView;

                String badgeIds[] = {"1", "2", "3", "4", "5", "7", "8"};
                Set<String> userBadges = userInfo.getBadges().keySet();

                //Draw user badges at first
                for (String key : userBadges) {
                    if (userBadges.contains(key)) {
                        imageView = makeImageView("badge_" + key, View.VISIBLE);
                        imageView.setLayoutParams(layoutParams);
                        llAddBadge.addView(imageView);
                    }
                }

                for (int i = 0; i < badgeIds.length; i++) {
                    if (!userBadges.contains(badgeIds[i])) {
                        imageView = makeImageView("badge_" + badgeIds[i], View.INVISIBLE);
                        imageView.setLayoutParams(layoutParams);
                        llAddBadge.addView(imageView);
                    }
                }

//                for (String key : userInfo.getBadges().keySet()) {
//                    AppLog.log("key: " + key);
//
//                    if ("1".equals(key)) {
//                        ImageView imageView1 = new ImageView(getContext());
//                        imageView1.setLayoutParams(layoutParams);
//                        imageView1.setImageResource(R.drawable.badge_1);
//                        llAddBadge.addView(imageView1);
//                    } else if ("2".equals(key)) {
//                        ImageView imageView2 = new ImageView(getActivity());
//                        imageView2.setLayoutParams(layoutParams);
//                        imageView2.setImageResource(R.drawable.badge_2);
//                        llAddBadge.addView(imageView2);
//                    } else if ("3".equals(key)) {
//                        ImageView imageView3 = new ImageView(getActivity());
//                        imageView3.setLayoutParams(layoutParams);
//                        imageView3.setImageResource(R.drawable.badge_3);
//                        llAddBadge.addView(imageView3);
//                    } else if ("4".equals(key)) {
//                        ImageView imageView4 = new ImageView(getContext());
//                        imageView4.setLayoutParams(layoutParams);
//                        imageView4.setImageResource(R.drawable.badge_4);
//                        llAddBadge.addView(imageView4);
//                    } else if ("5".equals(key)) {
//                        ImageView imageView5 = new ImageView(getActivity());
//                        imageView5.setLayoutParams(layoutParams);
//                        imageView5.setImageResource(R.drawable.badge_5);
//                        llAddBadge.addView(imageView5);
//                    } else if ("7".equals(key)) {
//                        ImageView imageView7 = new ImageView(getContext());
//                        imageView7.setLayoutParams(layoutParams);
//                        imageView7.setImageResource(R.drawable.badge_7);
//                        llAddBadge.addView(imageView7);
//                    }
//                }
            }

        }
    }


    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initAnalytics(true, null, null, null, 1);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        //
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Animation zoom in - zoom out character icon
            DrawableZoom.zoomImageAnimation(getActivity(), imgUserIcon);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (activity != null) {
                // hideStatusBar(activity);
                AnpanmanApp application = (AnpanmanApp) activity.getApplication();
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

    public ImageView makeImageView(String resourceName, int visible) {
        ImageView imageView;
        imageView = new ImageView(getContext());
        imageView.setImageResource(this.getResources().getIdentifier(resourceName, "drawable", activity.getPackageName()));
        imageView.setVisibility(visible);
        return imageView;
    }

    //Hiding status bar when rorate screen Mypage
    public void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) { //ye olde method
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else { // Jellybean and up, new hotness
            View decorView = activity.getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

}