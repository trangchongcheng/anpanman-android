package jp.anpanman.fanclub.main.ui.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.R;

import java.util.HashMap;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtCommon.FragmentTransitionInfo;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.framework.phvtUtils.StringUtil;
import jp.anpanman.fanclub.framework.restfulService.RestfulService;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.MainTabs;
import jp.anpanman.fanclub.main.model.UpdatedTime;
import jp.anpanman.fanclub.main.model.UserCharacter;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.fragment.CouponFragment;
import jp.anpanman.fanclub.main.ui.fragment.MyPageFragment;
import jp.anpanman.fanclub.main.ui.fragment.NewFragment;
import jp.anpanman.fanclub.main.ui.fragment.PresentFragment;
import jp.anpanman.fanclub.main.ui.fragment.WebViewFragment;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.Constant;
import jp.anpanman.fanclub.main.util.CustomDialogCoupon;
import jp.anpanman.fanclub.main.util.RestfulUrl;
import jp.anpanman.fanclub.main.util.RestfulUtil;

/**
 * Created by linhphan on 7/15/16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String ARG_LASTEST_UPDATED_TIME = "ARG_LASTEST_UPDATED_TIME";
    public static final String ARG_SHOULD_SHOW_PUSH_DIALOG = "ARG_SHOULD_SHOW_PUSH_DIALOG";
    public static final String ARG_CURRENT_TAB = "ARG_CURRENT_TAB";
    public static final String ARG_PUSH_DATA = "ARG_PUSH_DATA";
    public static final String ARG_PUSH_TITLE = "ARG_PUSH_TITLE";
    public static final String ARG_PUSH_MESSEAGE = "ARG_PUSH_MESSEAGE";
    public static final String ARG_PUSH_URL = "com.nifty.RichUrl";
    public static final String IS_FIRST_START_MAIN_ACTIVITY = "IS_FIRST_START_MAIN_ACTIVITY";


    //new status icon on bottom bar
    public static final String BUNDLE_KEY_ICON_NEW_IS_SHOW = "BUNDLE_KEY_ICON_NEW_IS_SHOW";
    public static final String BUNDLE_KEY_ICON_COUPON_IS_SHOW = "BUNDLE_KEY_ICON_COUPON_IS_SHOW";
    public static final String BUNDLE_KEY_ICON_PRESENT_IS_SHOW = "BUNDLE_KEY_ICON_PRESENT_IS_SHOW";
    public static final String BUNDLE_KEY_ICON_OTHER_IS_SHOW = "BUNDLE_KEY_ICON_OTHER_IS_SHOW";

    //=============== properties ===================================================================

    private ImageButton btnHamburgerMenu;
    private DrawerLayout drawerLayout;
    private ListView lvDrawerNav;
    private NavigationView navigationView;
    private RelativeLayout rl_top_nav;
    private LinearLayout rl_bottom_nav;

    //== bottom nav
    private ImageButton btnNewsTab;
    private ImageButton btnCouponTab;
    private ImageButton btnPresentTab;
    private ImageButton btnMyPageTab;
    private ImageButton btnSettingTab;

    private ImageView imgNewsNew;
    private ImageView imgCouponNew;
    private ImageView imgPresentNew;
    private ImageView imgOtherNew;
    private ImageView mProfileImage;
    private ImageView imgLogo;

    private ActionBarDrawerToggle mDrawerToggle;

    public static MainTabs currentTab;
    private UpdatedTime currentSync;
    private boolean isNewSelect = false;
    private boolean isCouponSelect = false;
    private boolean isPresentSelect = false;
    private boolean isOtherSelect = false;

    private PushNotifyListenReceiver pushNotifyListenReceiver;
    private CustomDialogCoupon customDialogCoupon;
    private DrawerAdapter drawerAdapter;
    private UserInfo userInfo;
    // State data for update new symbols
    public static HashMap<String, Integer> saveStateNewIcon = null;

    //=============== constructors =================================================================


    //=============== setters and getters ==========================================================


    //=============== inherited methods ============================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // oritiential by LANDSCAPE
        if (isLandcape()) {
            //TODO: LANDSCAPE
        }
        // oritiential by LANDSCAPE
        else {
            //TODO: PORTTRAIL
            // processing for last data staus saved from ROTATOED
            if (saveStateNewIcon != null) {
                if (saveStateNewIcon.get(BUNDLE_KEY_ICON_NEW_IS_SHOW) != null) {
                    applyStatusNewIcons();
                }

            }
            //Check bundle State -> First or return back from landscape
            if (savedInstanceState != null) {
                // apply last staus on bottom bar icon
                currentTab = MainTabs.get(savedInstanceState.getInt(ARG_CURRENT_TAB));
                switchTab(currentTab, true);
            } else {
                switchTab(MainTabs.News, true);
            }

        }

        //processing for diplay FOCUS [Booto0m Bar ICONS
        setDisplayBottomNav();

        // processing for Sync time for compare to get new                        return false; // if you want to handle the touch event

        String lastTime = SharedPreferencesUtil.getString(this, ARG_LASTEST_UPDATED_TIME, null);
        if (!TextUtils.isEmpty(lastTime)) {
            AppLog.log("UpdateTime-before" + lastTime);
            currentSync = UpdatedTime.fromJson(lastTime, UpdatedTime.class);
        }

        // processing for pushnotification dialog
        pushNotifyListenReceiver = new PushNotifyListenReceiver();

        // App is exit, recieve pushnotify -> run SplashActivity to send data -> recieve data here.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean(ARG_SHOULD_SHOW_PUSH_DIALOG, false)) {
            String title = bundle.getString(ARG_PUSH_TITLE);
            String message = bundle.getString(ARG_PUSH_MESSEAGE);
            String url = bundle.getString(ARG_PUSH_URL);
            if (url != null) {
                AppLog.log("URL" + url);
                showPushDialog(url, title, message);
            } else {
                showPushDialog("", title, message);
            }
        }
        requestPermissionReadExternal();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppLog.log("Permision Write File is Granted");
            } else {
                AppLog.log("Permision Write File is Denied");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(pushNotifyListenReceiver,
                new IntentFilter("jp.anpanman.fanclub.PUSH_NOTIFY"));
        //Hide menu Left and Menu Button when rotate.
        if (isLandcape()) {
            hideMenu();
            setDrawerLocked(true);
        } else {
            if (currentTab != MainTabs.MyPage) {
                showMenu();
            }
            setDrawerLocked(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushNotifyListenReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_CURRENT_TAB, currentTab.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getActivityLayoutId() {
        return com.main.R.layout.activity_main;
    }


    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        //== bottom nav
        btnNewsTab = (ImageButton) findViewById(com.main.R.id.btn_img_tab_news);
        btnCouponTab = (ImageButton) findViewById(com.main.R.id.btn_img_tab_coupon);
        btnPresentTab = (ImageButton) findViewById(com.main.R.id.btn_img_tab_present);
        btnMyPageTab = (ImageButton) findViewById(com.main.R.id.btn_img_tab_my_page);
        btnSettingTab = (ImageButton) findViewById(com.main.R.id.btn_img_tab_setting);

        imgNewsNew = (ImageView) findViewById(R.id.img_news_new);
        imgCouponNew = (ImageView) findViewById(R.id.img_coupon_new);
        imgPresentNew = (ImageView) findViewById(R.id.img_present_new);
        imgOtherNew = (ImageView) findViewById(R.id.img_other_new);

        rl_top_nav = (RelativeLayout) findViewById(R.id.rl_top_nav);
        rl_bottom_nav = (LinearLayout) findViewById(R.id.rl_bottom_nav);
        imgLogo = (ImageView) findViewById(R.id.img_logo);

        btnHamburgerMenu = (ImageButton) findViewById(com.main.R.id.btn_img_hamburger);
        drawerLayout = (DrawerLayout) findViewById(com.main.R.id.drawer);
        lvDrawerNav = (ListView) findViewById(com.main.R.id.lv_drawer_nav);
        navigationView = (NavigationView) findViewById(com.main.R.id.navView);

        createLeftMenuSlidingMaster();
    }

    @Override
    protected void registerEventHandlers() {
        //== bottom nav
        btnNewsTab.setOnClickListener(this);
        btnCouponTab.setOnClickListener(this);
        btnPresentTab.setOnClickListener(this);
        btnMyPageTab.setOnClickListener(this);
        btnSettingTab.setOnClickListener(this);

        btnHamburgerMenu.setOnClickListener(this);
        lvDrawerNav.setOnItemClickListener(this);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Start Animation
                //TODO:
                mProfileImage.setVisibility(View.VISIBLE);
                startAnimationAvatar();
                //DrawableZoom.zoomImageAnimation(MainActivity.this, mProfileImage);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Stop Animation
                //TODO:
                stopAnimationAvatar();
                //Clear animation and hide Avatar Image
                mProfileImage.clearAnimation();
                mProfileImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    // Request permission read External ()
    public void requestPermissionReadExternal() {
        // Processing for Permission writting DATA to SD CARD ( Save Wallper )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AppLog.log("Permissions dont granted");
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    AppLog.log("Permisson don't granted and dont show dialog again");
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }


    //====== ANIMATION EXAMPLES =====
    private Animation zoomin;//= AnimationUtils.loadAnimation(this, R.anim.zoom_in);
    private Animation zoomout;//= AnimationUtils.loadAnimation(this, R.anim.zoom_out);

    /**
     * Initilized animation for avavatar image
     * it's called by start drawer
     */

    private void initilizedAnimationAvatarDrawer() {

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);


        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //userInfo = ((AnpanmanApp) (MainActivity.this.getApplication())).getUserInfo();
                UserCharacter userCharacter = UserCharacter.getUserCharacter(MainActivity.this, userInfo.getFavorite_character_code());
                mProfileImage.setImageResource(userCharacter.getIconResource());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProfileImage.requestLayout();
                mProfileImage.startAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Nothing to do ( debug )
            }
        });

    }

    /***
     * Start animation when SLIDE LEFT MENU opened
     * Zoom- in animation will call in first time
     */
    private void startAnimationAvatar() {
        mProfileImage.requestLayout();
        mProfileImage.startAnimation(zoomin);
        //Holder for animation to start it
        // mProfileImage.setAnimation(zoomin);
        //zoomin.start();
        //zoomin.startNow();
    }

    /**
     * Stop Animation Avavtar
     * Zoom - in , Zoom - out will be stop
     */
    private void stopAnimationAvatar() {
        zoomin.cancel();
        zoomout.cancel();
    }


    //=============== implemented methods ==========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case com.main.R.id.btn_img_hamburger:
                //drawerLayout.openDrawer(navigationView);
                openDrawerMenu();
                break;

            case com.main.R.id.btn_img_tab_news:
                switchTab(MainTabs.News, false);
                isNewSelect = true;
                break;

            case com.main.R.id.btn_img_tab_coupon:
                switchTab(MainTabs.Coupon, false);
                isCouponSelect = true;
                break;

            case com.main.R.id.btn_img_tab_present:
                switchTab(MainTabs.Present, false);
                isPresentSelect = true;
                break;

            case com.main.R.id.btn_img_tab_my_page:
                switchTab(MainTabs.MyPage, false);
                break;

            case com.main.R.id.btn_img_tab_setting:
                trackingAnalytics(true, Constant.GA_INFO, null, null, null, 0);
                switchTab(MainTabs.Setting, false);
                isOtherSelect = true;
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent browserIntent;
        String objectId = ((AnpanmanApp) getApplication()).getUserInfo().getObjectId();
        switch (i) {
            case 0:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 1:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 2:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RestfulUrl.URL_WALL + objectId));
                startActivity(browserIntent);
                break;
            case 3:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RestfulUrl.URL_GURIDINGU + objectId));
                startActivity(browserIntent);
                break;
            case 4:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 5:
                trackingAnalytics(false, null, Constant.GA_SELECT, Constant.GA_ONCLICK, Constant.GA_MENU_PORTAL, 1);
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RestfulUrl.URL_PORTAL_SITE + objectId));
                startActivity(browserIntent);
                break;
            case 6:
                trackingAnalytics(false, null, Constant.GA_SELECT, Constant.GA_ONCLICK, Constant.GA_MENU_TERMS, 1);
                trackingAnalytics(true, Constant.GA_TERMS, null, null, null, 0);
                openWebView(RestfulUrl.URL_TERMS, getString(R.string.terms_of_use), false);
                break;
            case 7:
                openWebView(RestfulUrl.URL_POLICY, getString(R.string.title_policy), false);
                break;
            case 8:
                trackingAnalytics(true, Constant.GA_TUTORIAL, null, null, null, 0);
                trackingAnalytics(false, null, Constant.GA_SELECT, Constant.GA_ONCLICK, Constant.GA_MENU_FAQ, 1);
                Intent intent = new Intent(this, IntroActivity.class);
                intent.putExtra(IntroActivity.IS_FAQ, true);
                startActivity(intent);
                break;
            case 9:
                openWebView(RestfulUrl.URL_CONTACT, getString(R.string.title_contact), false);
                break;
            default:
                break;

        }
        //switchTab(MainTabs.Setting, true);
    }


    //=============== inner methods ================================================================
    public void openWebView(String url, String title, boolean isDetails) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title, isDetails);
        fragment.show(getFragmentManager(), WebViewFragment.class.getName());
    }

    //Hide lest menu when rotate
    public void setDrawerLocked(boolean enabled) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }

    //Open drawer menu when click hamburge icon
    public void openDrawerMenu() {
        drawerLayout.openDrawer(navigationView);
    }

    public boolean isLandcape() {
        int ot = getResources().getConfiguration().orientation;
        if (ot == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else if (ot == Configuration.ORIENTATION_PORTRAIT) {
            return false;
        }
        return false;
    }

    public void hideMenu() {
        rl_bottom_nav.setVisibility(View.GONE);
        rl_top_nav.setVisibility(View.GONE);
    }

    public void showMenu() {
        rl_bottom_nav.setVisibility(View.VISIBLE);
        rl_top_nav.setVisibility(View.VISIBLE);
    }

    /**
     * on Create LEFT MENU SLIDING
     * It will be Open by touch & Drag during time eslapse !
     */
    private void createLeftMenuSlidingMaster() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View navHeaderView = inflater.inflate(com.main.R.layout.drawer_nav_header, null, false);
        View navFooterView = inflater.inflate(com.main.R.layout.drawer_nav_footer, null, false);

        // Set favorite character on side menu
        userInfo = ((AnpanmanApp) (this.getApplication())).getUserInfo();
        UserCharacter userCharacter = UserCharacter.getUserCharacter(this, userInfo.getFavorite_character_code());


        // Avatar profile configuration
        // handle from view
        mProfileImage = (ImageView) navHeaderView.findViewById(R.id.profile_image);
        mProfileImage.setImageResource(userCharacter.getIconResource());

        // config imageview for animtion processing
        initilizedAnimationAvatarDrawer();

        lvDrawerNav.addHeaderView(navHeaderView, null, false);
        lvDrawerNav.addFooterView(navFooterView, null, false);

        drawerAdapter = new DrawerAdapter(this);
        lvDrawerNav.setAdapter(drawerAdapter);
    }

    public void switchTab(MainTabs newTab, boolean isFirstLaunch) {
        Log.e("**current tab**", (currentTab == null) ? "" : currentTab.toString());
        Log.e("**new tab**", newTab.toString());
        FragmentTransitionInfo mTransitionAnimation;
        if (isFirstLaunch ||
                (newTab == currentTab)) {
            mTransitionAnimation = null;

        } else {
            mTransitionAnimation = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        }
        switch (newTab) {
            case News:
                setupForFragmentMypage(false);
                openNewsFragment(mTransitionAnimation);
                imgNewsNew.setVisibility(View.INVISIBLE);
                break;

            case Coupon:
                setupForFragmentMypage(false);
                openCouponFragment(mTransitionAnimation);
                imgCouponNew.setVisibility(View.INVISIBLE);
                break;

            case Present:
                setupForFragmentMypage(false);
                openPresentFragment(mTransitionAnimation);
                imgPresentNew.setVisibility(View.INVISIBLE);
                break;

            case MyPage:
                setupForFragmentMypage(true);
                openMyPageFragment(mTransitionAnimation);
                break;

            case Setting:
                imgOtherNew.setVisibility(View.INVISIBLE);
                openSettingDialog();
                break;
        }

        if (newTab != MainTabs.Setting) {
            currentTab = newTab;
        }
        setDisplayBottomNav();
        syncUpdateTimeOfServer(newTab);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public UpdateUserInfoListener _updateUserInfoListener = new UpdateUserInfoListener() {

        @Override
        public void update(UserInfo _userInfo) {
            //update _userInfo
            userInfo = _userInfo;//((AnpanmanApp) (MainActivity.this.getApplication())).getUserInfo();
            //update character info
            // get current Fragment call back in present
            if (currentTab == MainTabs.MyPage) {
                AppLog.log("ANPANMAN", " My Page Activing ... UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE ");
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fl_main_content);
                if (f instanceof MyPageFragment) {
                    ((MyPageFragment) f).refreshUserInfoUI();
                }


            }

        }
    };


    public interface UpdateUserInfoListener {
        public void update(UserInfo _userInfo);
    }


    int i = 0;

    private void setDisplayBottomNav() {
        switch (currentTab) {
            case News:
                btnNewsTab.setSelected(true);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup) btnNewsTab.getParent()).setSelected(true);
                ((ViewGroup) btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup) btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup) btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup) btnSettingTab.getParent()).setSelected(false);
                break;

            case Coupon:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(true);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup) btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup) btnCouponTab.getParent()).setSelected(true);
                ((ViewGroup) btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup) btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup) btnSettingTab.getParent()).setSelected(false);
                break;

            case Present:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(true);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup) btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup) btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup) btnPresentTab.getParent()).setSelected(true);
                ((ViewGroup) btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup) btnSettingTab.getParent()).setSelected(false);
                break;

            case MyPage:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(true);
                btnSettingTab.setSelected(false);

                ((ViewGroup) btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup) btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup) btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup) btnMyPageTab.getParent()).setSelected(true);
                ((ViewGroup) btnSettingTab.getParent()).setSelected(false);
                break;

            case Setting:
                break;
        }
    }

    private void displayNewIcons(UpdatedTime newSync, MainTabs tabSelected) {

        if (newSync == null || currentSync == null) {
            imgNewsNew.setVisibility(View.INVISIBLE);
            imgCouponNew.setVisibility(View.INVISIBLE);
            imgPresentNew.setVisibility(View.INVISIBLE);
            imgOtherNew.setVisibility(View.INVISIBLE);
        } else {
            switch (tabSelected) {
                case News:
                    //Check UpdateTime of News is null for save to SharePrefrence
                    if(null != newSync.getNews()){
                        saveTimeToSharePreference(newSync.getNews(), MainTabs.News);
                    }
                    //Check updateTime is null?
                    if(newSync.getCoupon()!= null && currentSync.getCoupon()!=null){
                        if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                            imgCouponNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isCouponSelect) {
                                imgCouponNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getPresent()!= null && currentSync.getPresent()!=null){
                        if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                            imgPresentNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isPresentSelect) {
                                imgPresentNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getInfo()!=null && currentSync.getInfo()!= null){
                        if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                            imgOtherNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isOtherSelect) {
                                imgOtherNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }


                    break;

                case Coupon:
                    //Check CouponTime of News is null for save to SharePrefrence
                    if(null != newSync.getCoupon()){
                        saveTimeToSharePreference(newSync.getCoupon(), MainTabs.Coupon);
                    }
                    if(newSync.getNews()!= null && currentSync.getNews()!= null){
                        if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                            imgNewsNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isNewSelect) {
                                imgNewsNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getPresent()!= null && currentSync.getPresent()!= null){
                        if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                            imgPresentNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isPresentSelect) {
                                imgPresentNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getInfo()!=null && currentSync.getInfo()!= null){
                        if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                            imgOtherNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isOtherSelect) {
                                imgOtherNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    break;

                case Present:
                    //Check PresentTime of News is null for save to SharePrefrence
                    if(null != newSync.getPresent()){
                        saveTimeToSharePreference(newSync.getPresent(), MainTabs.Present);
                    }
                    if(newSync.getNews()!= null && currentSync.getNews()!= null){
                        if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                            imgNewsNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isNewSelect) {
                                imgNewsNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getCoupon()!= null && currentSync.getCoupon()!= null){
                        if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                            imgCouponNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isCouponSelect) {
                                imgCouponNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    if(newSync.getInfo()!=null && currentSync.getInfo()!= null){
                        if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                            imgOtherNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isOtherSelect) {
                                imgOtherNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    break;

                case Setting:
                    //Check SettingTime of News is null for save to SharePrefrence
                    if(null != newSync.getInfo()){
                        AppLog.log("Cheng-getinfo", newSync.getInfo().toString());
                        saveTimeToSharePreference(newSync.getInfo(), MainTabs.Setting);
                    }
                    if(newSync.getNews()!= null && currentSync.getNews()!= null){
                        if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                            imgNewsNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isNewSelect) {
                                imgNewsNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getCoupon()!= null && currentSync.getCoupon()!= null){
                        if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                            imgCouponNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isCouponSelect) {
                                imgCouponNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(newSync.getPresent()!= null && currentSync.getPresent()!= null){
                        if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                            imgPresentNew.setVisibility(View.VISIBLE);
                        } else {
                            if (isPresentSelect) {
                                imgPresentNew.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    break;

                default:
                    break;
            }

            // we will save present status New for every ICON on Bottom bar
            if (!isLandcape()) {
                saveStatusNewIcons();
            }
        }
    }

    //Save New UpdateTime when click Menu Bottom
    public void saveTimeToSharePreference(UpdatedTime.UpdatedTimeModel updateTime, MainTabs tabSellected) {
        if (tabSellected == MainTabs.News) {
            currentSync.setNews(updateTime);
        } else if (tabSellected == MainTabs.Coupon) {
            currentSync.setCoupon(updateTime);
        } else if (tabSellected == MainTabs.Present) {
            currentSync.setPresent(updateTime);
        } else if (tabSellected == MainTabs.Setting) {
            currentSync.setInfo(updateTime);
        }
        SharedPreferencesUtil.putString(getBaseContext(), ARG_LASTEST_UPDATED_TIME, currentSync.toJson());
        AppLog.log("UpdateTime-after" + SharedPreferencesUtil.getString(this, ARG_LASTEST_UPDATED_TIME, null));

    }

    /**
     * try to retrieve the last time of data has updated on remote server
     */
    private void syncUpdateTimeOfServer(final MainTabs tabSelected) {
        RestfulUtil.getUpdatedTime(this, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                //Save UpdateTime to Shared Preference
                boolean isFirstStartActivity = SharedPreferencesUtil.getBoolean(MainActivity.this, IS_FIRST_START_MAIN_ACTIVITY, false);
                if (!isFirstStartActivity) {
                    SharedPreferencesUtil.putString(getBaseContext(), ARG_LASTEST_UPDATED_TIME, ((UpdatedTime) data).toJson());
                    SharedPreferencesUtil.putBoolean(MainActivity.this, IS_FIRST_START_MAIN_ACTIVITY, true);

                }
                displayNewIcons((UpdatedTime) data, tabSelected);

            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {

            }
        });
    }

    private void openNewsFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, NewFragment.class.getName(), false, null, mTransition);
    }

    private void openCouponFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, CouponFragment.class.getName(), false, null, mTransition);
    }

    private void openPresentFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, PresentFragment.class.getName(), false, null, mTransition);
    }

    private void openMyPageFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(R.id.fl_main_content, MyPageFragment.class.getName(), false, null, mTransition);
    }

    private void openSettingDialog() {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setCallback(new WebViewFragment.DismissCallback() {
            @Override
            public void onDismiss() {
//                currentTab = MainTabs.News;
                setDisplayBottomNav();
            }
        });
        openWebView(RestfulUrl.URL_ACCOUNT_SETTING, getString(R.string.other), false);
    }

    private void showPushDialog(String url, String title, String message) {
        if (StringUtil.isEmpty(url)) {
            return;
        }
        customDialogCoupon = new CustomDialogCoupon(MainActivity.this, url, title, message);
        customDialogCoupon.show();
    }

    public void openBrower(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    // Tracking Google Analytics for MainActivitv
    public void trackingAnalytics(Boolean inOnlyScreen, String screenName, String category, String action, String label, long value) {
        AnpanmanApp application = (AnpanmanApp) getApplication();
        if (inOnlyScreen) {
            application.trackingAnalyticByScreen(screenName);
        } else {
            application.trackingWithAnalyticGoogleServices(category, action, label, value);
        }

    }

    //=============== inner classes ================================================================

    // Broadcast Receiver - listener when app is running and receiver push
    public class PushNotifyListenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String urlPush = intent.getStringExtra("url");
            //AppLog.log("Cheng-url from push ", urlPush);
            showPushDialog(urlPush, null, "");
        }

    }

    private static class DrawerAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context context;

        public DrawerAdapter(Context context) {
            this.context = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getViewTypeCount() {

            return 2;
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemType(position) != ItemType.Group;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //== inflate views
            NormalItemHolder holder;
            ItemType type = getItemType(i);
            if (view == null) {
                holder = new NormalItemHolder();
                switch (type) {
                    case Group:
                        //inflate the new layout
                        view = mInflater.inflate(R.layout.drawer_nav_item_group, viewGroup, false);
                        holder.txtTitle = (TextView) view.findViewById(R.id.txt_item_title);
                        break;

                    case Normal:
                        //inflate the new layout
                        view = mInflater.inflate(R.layout.drawer_nav_item, viewGroup, false);
                        holder.txtTitle = (TextView) view.findViewById(R.id.txt_item_title);
                        break;
                    default:
                        break;
                }
                view.setTag(holder);
            } else {
                holder = (NormalItemHolder) view.getTag();
            }
            switch (type) {
                case Group:
                    holder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                    break;
                case Normal:
                    holder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                    break;
                default:
                    break;
            }
            return view;
        }

    }

    //==============================================================================================
    private static ItemType getItemType(int position) {
        switch (position) {
            case 0:
            case 3:
                return ItemType.Group;

            default:
                return ItemType.Normal;
        }
    }

    //==============================================================================================
    enum ItemType {
        Group, Normal
    }

    //==============================================================================================
    private static class NormalItemHolder {
        TextView txtTitle;
    }

    //==========================Handle save state new icon when rotate==============================

    /**
     * NEWS SYMBOL  PROCESSING
     * Apply saved data hashMap into UI
     */
    public void applyStatusNewIcons() {

        // TOP - NEW
        if (saveStateNewIcon.get(BUNDLE_KEY_ICON_NEW_IS_SHOW).intValue() == 1)
            imgNewsNew.setVisibility(View.VISIBLE);
        else
            imgNewsNew.setVisibility(View.GONE);

        // COUPON
        if (saveStateNewIcon.get(BUNDLE_KEY_ICON_COUPON_IS_SHOW).intValue() == 1)
            imgCouponNew.setVisibility(View.VISIBLE);
        else
            imgCouponNew.setVisibility(View.GONE);

        // PRESENT
        if (saveStateNewIcon.get(BUNDLE_KEY_ICON_PRESENT_IS_SHOW).intValue() == 1)
            imgPresentNew.setVisibility(View.VISIBLE);
        else
            imgPresentNew.setVisibility(View.GONE);

        // OTHER
        if (saveStateNewIcon.get(BUNDLE_KEY_ICON_OTHER_IS_SHOW).intValue() == 1)
            imgOtherNew.setVisibility(View.VISIBLE);
        else
            imgOtherNew.setVisibility(View.GONE);
    }


    /**
     * NEWS SYMBOL  PROCESSING
     * Save state current to Hashmap data
     * 5 bottom tabs state will be save to saveStateNewIcon unit
     */
    public void saveStatusNewIcons() {

        // initilizing Hashmap data
        saveStateNewIcon = new HashMap<>();

        //Save state new icon TOP - NEW tab
        if (imgNewsNew.getVisibility() == View.VISIBLE)
            saveStateNewIcon.put(BUNDLE_KEY_ICON_NEW_IS_SHOW, 1);
        else
            saveStateNewIcon.put(BUNDLE_KEY_ICON_NEW_IS_SHOW, 0);

        //Save state new icon Coupon tab
        if (imgCouponNew.getVisibility() == View.VISIBLE)
            saveStateNewIcon.put(BUNDLE_KEY_ICON_COUPON_IS_SHOW, 1);
        else
            saveStateNewIcon.put(BUNDLE_KEY_ICON_COUPON_IS_SHOW, 0);

        //Save state new icon Present tab
        if (imgPresentNew.getVisibility() == View.VISIBLE)
            saveStateNewIcon.put(BUNDLE_KEY_ICON_PRESENT_IS_SHOW, 1);
        else
            saveStateNewIcon.put(BUNDLE_KEY_ICON_PRESENT_IS_SHOW, 0);

        // Save state new icon Others tab
        if (imgOtherNew.getVisibility() == View.VISIBLE)
            saveStateNewIcon.put(BUNDLE_KEY_ICON_OTHER_IS_SHOW, 1);
        else
            saveStateNewIcon.put(BUNDLE_KEY_ICON_OTHER_IS_SHOW, 0);

    }

    //Background drawer handle when select MyPageTab
    public void setBackgroundDrawer(boolean isChange) {
        if (isChange) {
            rl_top_nav.setBackgroundResource(R.drawable.img_background_drawer);
            btnHamburgerMenu.setBackgroundResource(R.drawable.combined_shape_white);
            imgLogo.setImageResource(R.drawable.logo_white);
        } else {
            rl_top_nav.setBackgroundResource(0);
            btnHamburgerMenu.setBackgroundResource(R.drawable.combined_shape);
            imgLogo.setImageResource(R.drawable.logo_orange);
        }
    }

    //Setup eader Mypage when select MyPageTab
    public void setupForFragmentMypage(boolean isShow) {
        if (isShow) {
            rl_top_nav.setVisibility(View.GONE);
        } else {
            rl_top_nav.setVisibility(View.VISIBLE);
        }

    }


}
