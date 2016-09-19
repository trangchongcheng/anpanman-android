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
import android.view.LayoutInflater;
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

import java.util.ArrayList;

import jp.anpanman.fanclub.framework.phvtFragment.BaseFragment;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.Badges;
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
    private ArrayList<Badges> userBadges;

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

        //Hard code test dbug
//        userInfo.setNickName("Chien Truong");
//        userInfo.setFavorite_character_code("2");
//        userBadges = userInfo.getBadges();
//        userBadges.add(new Badges("1", "Cheng"));
////        userBadges.add(new Badges("2","Cheng"));
//        userBadges.add(new Badges("7", "Cheng"));

        // set user Character data basing on user info
        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
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
                        trackingAnalytics(false, null, Constant.GA_SELECT, Constant.GA_ONCLICK, Constant.GA_MYPAGE_ENTRY, 1);
                        openWebView(RestfulUrl.URL_REGISTER_MYPAGE, getString(R.string.button_register));
                    }
                });
                // nickname of user # blank
            } else {
                tvUserName.setVisibility(View.VISIBLE);
                setupBagdes();
                //if badge = blank to hide text 獲得バッジ
                if (!userInfo.getBadges().isEmpty()) {
                    llBadge.setVisibility(View.VISIBLE);
                } else {
                    llBadge.setVisibility(View.INVISIBLE);

                }
            }

        }
    }


    @Override
    protected void registerEventHandlers() {
        //Open drawer menu in MainActivity when click hamburge icon (only Porttrait)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            btnHamburgerMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null) {
                        activity.openDrawerMenu();

                    }
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        //On Portrait mode => set Animation zoom in - zoom out character icon
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            trackingAnalytics(true, Constant.GA_MYPAGE, null, null, null, 0);
            DrawableZoom.zoomImageAnimation(getActivity(), imgUserIcon);
            //On LandScape => hide status bar and tracking Google analytic for MemberShip Sreen
        } else {
            hideStatusBar();
            trackingAnalytics(true, Constant.GA_MEMBERSHIP, null, null, null, 0);
        }
    }

    //============= inner methods ==================================================================

    //Hide status bar when in landscape mode
    public void hideStatusBar() {
        if (activity != null) {
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
    }

    //Setup badge
    public void setupBagdes() {
        //1. RESET LAYOUT BADGLE
        //Returns the number of children in the group, if > 0 then remove all Children view
        if (llAddBadge.getChildCount() > 0) {
            llAddBadge.removeAllViews();
            llAddBadge.requestLayout();
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView;

        String badgeIds[] = {"1", "2", "3", "4", "5", "7", "8"};

        userBadges = userInfo.getBadges();

        //Draw user badges at first
        for (int i = 0; i < userBadges.size(); i++) {
            for (int j = 0; j < badgeIds.length; j++) {
                if (userBadges.get(i).getId().contains(badgeIds[j])) {
                    imageView = makeImageView("badge_" + userBadges.get(i).getId(), View.VISIBLE);
                    imageView.setLayoutParams(layoutParams);
                    llAddBadge.addView(imageView);
                }
            }

        }
    }


    public void openWebView(String url, String title) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title, false);
        fragment.show(getActivity().getFragmentManager(), WebViewFragment.class.getName());
    }

    // Tracking Analytics Mypage Fragment
    public void trackingAnalytics(Boolean isOnlyScreen, String nameScreen, String category, String action, String label, long value) {
        Activity activity = getActivity();
        if (activity != null) {
            AnpanmanApp application = (AnpanmanApp) activity.getApplication();
            if (isOnlyScreen) {
                application.trackingAnalyticByScreen(nameScreen);
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


    /**
     * update UI when another process callback needing update in force now
     */
    public void refreshUserInfoUI() {

        //_debug log
        AppLog.log("ANPANMAN", "  Update userinfo through Interface action ...");

        //0. Character info
        userInfo = ((AnpanmanApp) (getActivity().getApplication())).getUserInfo();
        userCharacter = UserCharacter.getUserCharacter(getActivity(), userInfo.getFavorite_character_code());
        //icon
        imgUserIcon.setImageResource(userCharacter.getIconResource());
        imgUserIcon.requestLayout();
        //id
        tvUserID.setText("ID:" + userInfo.getId());
        //name
        tvUserName.setText(userInfo.getNickName());
        //Show badges if nickname # blank
        if (!TextUtils.isEmpty(userInfo.getNickName())) {
            //if badge = blank to hide text 獲得バッジ
            if (!userInfo.getBadges().isEmpty()) {
                llBadge.setVisibility(View.VISIBLE);
            } else {
                llBadge.setVisibility(View.INVISIBLE);

            }
            //Hide button Register User
            btnRegister.setVisibility(View.GONE);
            //Visible textview nickname
            tvUserName.setVisibility(View.VISIBLE);
            //Hide nickName image --- ---
            imgNickName.setVisibility(View.GONE);

        }
        //Hide badges,show button register if nickname = blank
        else {
            //Visible button register user
            btnRegister.setVisibility(View.VISIBLE);
            //Hide icon badge
            llBadge.setVisibility(View.GONE);
            //Show nickname image --- ---
            imgNickName.setVisibility(View.VISIBLE);
            //Hide textview nickname
            tvUserName.setVisibility(View.GONE);
        }
        //Update badge
        //2. RELOAD + APPEND NEW ICON BADGLE
        setupBagdes();

    }
}