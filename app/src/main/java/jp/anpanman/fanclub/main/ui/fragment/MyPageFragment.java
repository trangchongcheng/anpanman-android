package jp.anpanman.fanclub.main.ui.fragment;

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
import android.widget.ImageButton;
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
    private ImageButton btnHamburgerMenu;
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

        activity = (MainActivity) getActivity();

        // get Application user Info general data
        userInfo = ((AnpanmanApp) (getActivity().getApplication())).getUserInfo();

        // set user Character data basing on user info
        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());

        // Status bar will be hided when Device rotatoe to LANDSCAPE
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideStatusBar();
        }

    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

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
        btnHamburgerMenu = (ImageButton) root.findViewById(R.id.btn_img_hamburger_mypage);

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
            }

        }
    }


    @Override
    protected void registerEventHandlers() {
        //Open drawer menu in MainActivity when click hamburge icon (only Porttrail)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            btnHamburgerMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activity != null){
                        activity.openDrawerMenu();

                    }
                }
            });
        }
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
                application.trackingAnalyticByCategory(Constant.GA_MEMBERSHIP);
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
                application.trackingAnalyticByCategory(Constant.GA_MYPAGE);
            } else {
                application.trackingWithAnalyticGoogleServices(category, action, label, value);
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
}