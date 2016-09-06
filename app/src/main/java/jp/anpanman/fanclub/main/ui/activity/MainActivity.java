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
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.R;

import java.util.ArrayList;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtCommon.FragmentTransitionInfo;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.framework.restfulService.RestfulService;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.model.UpdatedTime;
import jp.anpanman.fanclub.main.model.UserCharacter;
import jp.anpanman.fanclub.main.model.UserInfo;
import jp.anpanman.fanclub.main.ui.fragment.CouponFragment;
import jp.anpanman.fanclub.main.ui.fragment.MyPageFragment;
import jp.anpanman.fanclub.main.ui.fragment.NewFragment;
import jp.anpanman.fanclub.main.ui.fragment.PresentFragment;
import jp.anpanman.fanclub.main.ui.fragment.WebViewFragment;
import jp.anpanman.fanclub.main.util.Common;
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

    public static MainTabs currentTab;
    private UpdatedTime currentSync;
    private boolean isNewSelect = false;
    private boolean isCouponSelect = false;
    private boolean isPresentSelect = false;
    private boolean isOtherSelect = false;

    private PushNotifyListenReceiver pushNotifyListenReceiver;
    private CustomDialogCoupon customDialogCoupon;
    //=============== constructors =================================================================


    //=============== setters and getters ==========================================================


    //=============== inherited methods ============================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentTab = MainTabs.get(savedInstanceState.getInt(ARG_CURRENT_TAB));
            switchTab(currentTab, true);
        } else {
            switchTab(MainTabs.News, true);
        }

        setDisplayBottomNav();
        String lastTime = SharedPreferencesUtil.getString(this, ARG_LASTEST_UPDATED_TIME,
                "{\"otoku\":{\"updatetime\":\"2016-07-26T21:33:41+09:00\"},\"info\":{\"updatetime\":\"2016-07-01T16:44:32+09:00\"},\"new\":{\"updatetime\":\"2016-07-26T20:46:04+09:00\"},\"present\":{\"updatetime\":\"2016-07-05T20:16:13+09:00\"}}");
        AppLog.log("Cheng-lastime",lastTime);
        if (!TextUtils.isEmpty(lastTime)) {
            currentSync = UpdatedTime.fromJson(lastTime, UpdatedTime.class);
        }

        pushNotifyListenReceiver = new PushNotifyListenReceiver();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
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

        int ot = getResources().getConfiguration().orientation;
        switch (ot) {
            case Configuration.ORIENTATION_LANDSCAPE:
                hideMenu();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                showMenu();
                break;
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

        btnHamburgerMenu = (ImageButton) findViewById(com.main.R.id.btn_img_hamburger);
        drawerLayout = (DrawerLayout) findViewById(com.main.R.id.drawer);
        lvDrawerNav = (ListView) findViewById(com.main.R.id.lv_drawer_nav);
        navigationView = (NavigationView) findViewById(com.main.R.id.navView);

        setDrawerNavigation();
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
    }

    //=============== implemented methods ==========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case com.main.R.id.btn_img_hamburger:
                //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(navigationView);
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
                openWebView(RestfulUrl.URL_PORTAL_SITE, getString(R.string.portal_site), false);
                break;
            case 6:
                openWebView(RestfulUrl.URL_TERMS, getString(R.string.terms_of_use), false);
                break;
            case 7:
                openWebView(RestfulUrl.URL_POLICY, getString(R.string.title_policy), false);
                break;
            case 8:
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

    public void hideMenu() {
        rl_bottom_nav.setVisibility(View.GONE);
        rl_top_nav.setVisibility(View.GONE);
    }

    public void showMenu() {
        rl_bottom_nav.setVisibility(View.VISIBLE);
        rl_top_nav.setVisibility(View.VISIBLE);
    }

    private void setDrawerNavigation() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View navHeaderView = inflater.inflate(com.main.R.layout.drawer_nav_header, null, false);
        View navFooterView = inflater.inflate(com.main.R.layout.drawer_nav_footer, null, false);

        // Set favorite character on side menu
        UserInfo userInfo = ((AnpanmanApp) (this.getApplication())).getUserInfo();
        UserCharacter userCharacter = UserCharacter.getUserCharacter(this, userInfo.getFavorite_character_code());
        mProfileImage = (ImageView) navHeaderView.findViewById(R.id.profile_image);
        mProfileImage.setImageResource(userCharacter.getIconResource());

        lvDrawerNav.addHeaderView(navHeaderView, null, false);
        lvDrawerNav.addFooterView(navFooterView, null, false);

        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
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
                openNewsFragment(mTransitionAnimation);
                imgNewsNew.setVisibility(View.INVISIBLE);
                break;

            case Coupon:
                openCouponFragment(mTransitionAnimation);
                imgCouponNew.setVisibility(View.INVISIBLE);
                break;

            case Present:
                openPresentFragment(mTransitionAnimation);
                imgPresentNew.setVisibility(View.INVISIBLE);
                break;

            case MyPage:
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
        syncUpdateTimeOfServer();
    }

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
//                btnNewsTab.setSelected(false);
//                btnCouponTab.setSelected(false);
//                btnPresentTab.setSelected(false);
//                btnMyPageTab.setSelected(false);
//                btnSettingTab.setSelected(false);
//                currentTab = MainTabs.Setting;
                break;
        }
    }

    private void displayNewIcons(UpdatedTime newSync) {

        if (newSync == null || currentSync == null) {
            imgNewsNew.setVisibility(View.INVISIBLE);
            imgCouponNew.setVisibility(View.INVISIBLE);
            imgPresentNew.setVisibility(View.INVISIBLE);
            imgOtherNew.setVisibility(View.INVISIBLE);

        } else {
            switch (currentTab) {
                case News:
                   // imgNewsNew.setVisibility(View.INVISIBLE);
                    if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                        imgCouponNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isCouponSelect){
                            imgCouponNew.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                        imgPresentNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isPresentSelect){
                            imgPresentNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                        imgOtherNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isOtherSelect){
                            imgOtherNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;

                case Coupon:
                   // imgCouponNew.setVisibility(View.INVISIBLE);

                    if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                        imgNewsNew.setVisibility(View.VISIBLE);
                    } else {
                        if (isNewSelect) {
                            imgNewsNew.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                        imgPresentNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isPresentSelect){
                            imgPresentNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                        imgOtherNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isOtherSelect){
                            imgOtherNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;

                case Present:
                   // imgPresentNew.setVisibility(View.INVISIBLE);

                    if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                        imgNewsNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isNewSelect){
                            imgNewsNew.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                        imgCouponNew.setVisibility(View.VISIBLE);
                    } else {
                        if (isCouponSelect){
                            imgCouponNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (Common.compareTimeGreater(newSync.getInfo().getUpdatedTime(), currentSync.getInfo().getUpdatedTime())) {
                        imgOtherNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isOtherSelect){
                            imgOtherNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;

                case Setting:
                    //imgOtherNew.setVisibility(View.INVISIBLE);

                    if (Common.compareTimeGreater(newSync.getNews().getUpdatedTime(), currentSync.getNews().getUpdatedTime())) {
                        imgNewsNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isNewSelect){
                            imgNewsNew.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (Common.compareTimeGreater(newSync.getCoupon().getUpdatedTime(), currentSync.getCoupon().getUpdatedTime())) {
                        imgCouponNew.setVisibility(View.VISIBLE);
                    } else {
                        if (isCouponSelect){
                            imgCouponNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (Common.compareTimeGreater(newSync.getPresent().getUpdatedTime(), currentSync.getPresent().getUpdatedTime())) {
                        imgPresentNew.setVisibility(View.VISIBLE);
                    } else {
                        if(isPresentSelect){
                            imgPresentNew.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;

                default:
                    break;
            }

            SharedPreferencesUtil.putString(getBaseContext(), ARG_LASTEST_UPDATED_TIME, newSync.toJson());
            currentSync = newSync;
        }
    }

    /**
     * try to retrieve the last time of data has updated on remote server
     */
    private void syncUpdateTimeOfServer() {
        RestfulUtil.getUpdatedTime(this, new RestfulService.Callback() {
            @Override
            public void onDownloadSuccessfully(Object data, int requestCode, int responseCode) {
                AppLog.log("Cheng-Update", data.toString());
                displayNewIcons((UpdatedTime) data);

            }

            @Override
            public void onDownloadFailed(Exception e, int requestCode, int responseCode) {

            }
        });
    }

    private void openNewsFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, NewFragment.class.getName(), false, null, mTransition);
        Log.e("LOG", "NewsFragment !");
    }

    private void openCouponFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, CouponFragment.class.getName(), false, null, mTransition);
    }

    private void openPresentFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, PresentFragment.class.getName(), false, null, mTransition);
    }

    private void openMyPageFragment(FragmentTransitionInfo mTransition) {
        replaceFragment(com.main.R.id.fl_main_content, MyPageFragment.class.getName(), false, null, mTransition);
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
        if (customDialogCoupon == null) {
            customDialogCoupon = new CustomDialogCoupon(MainActivity.this, url, title, message);
        }
        customDialogCoupon.show();
    }

    public void openBrower(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //=============== inner classes ================================================================
    public class PushNotifyListenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String urlPush = intent.getStringExtra("url");
            showPushDialog(urlPush, null, "");
        }

    }

    private static class DrawerAdapter extends BaseAdapter {

        private int iGroup, iItem;
        private ArrayList<String> arrGroup = new ArrayList<>();
        private ArrayList<String> arrItem = new ArrayList<>();

        public DrawerAdapter(Context context) {
            iGroup = 0;
            iItem = 0;

            for (int j = 0; j < context.getResources().getStringArray(R.array.group).length; j++) {
                arrGroup.add(context.getResources().getStringArray(R.array.group)[j]);
            }
            for (int j = 0; j < context.getResources().getStringArray(R.array.item).length; j++) {
                arrItem.add(context.getResources().getStringArray(R.array.item)[j]);
            }
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
            GroupItemHolder holder = null;
            NormalItemHolder h = null;
            switch (getItemType(i)) {
                case Group:
                    if (view == null || !(view.getTag() instanceof GroupItemHolder)) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(com.main.R.layout.drawer_nav_item_group, viewGroup, false);
                        holder = new GroupItemHolder(view);
                        view.setTag(holder);
                    }else{
                        holder = (GroupItemHolder) view.getTag();
                    }
                    break;

                default://normal item
                    if (view == null || !(view.getTag() instanceof NormalItemHolder)) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(com.main.R.layout.drawer_nav_item, viewGroup, false);
                        h = new NormalItemHolder(view);
                        view.setTag(h);
                    }else {
                        h = (NormalItemHolder) view.getTag();
                    }
                    break;
            }

            //== setup data to views
            switch (getItemType(i)) {
                case Group:
                    //GroupItemHolder holder = (GroupItemHolder) view.getTag();
                    holder.txtTitle.setText(arrGroup.get((iGroup)));
                    iGroup++;
                    iGroup = iGroup % arrGroup.size();
                    break;

                default:
                   // NormalItemHolder h = (NormalItemHolder) view.getTag();
                    //h.txtTitle.setText("Item Title");
                    h.txtTitle.setText(arrItem.get(iItem));
                    iItem++;
                    iItem = iItem % arrItem.size();
                    break;
            }

            return view;
        }

        //===========
        private ItemType getItemType(int position) {
            switch (position) {
                case 0:
                case 3:
                    return ItemType.Group;

                default:
                    return ItemType.Normal;
            }
        }

        //==========
        enum ItemType {
            Group, Normal
        }

        //===========
        private static class NormalItemHolder {
            TextView txtTitle;

            public NormalItemHolder(View root) {
                txtTitle = (TextView) root.findViewById(com.main.R.id.txt_item_title);
            }
        }

        //===========
        private static class GroupItemHolder {
            TextView txtTitle;

            public GroupItemHolder(View root) {
                txtTitle = (TextView) root.findViewById(com.main.R.id.txt_group_tile);
            }
        }
    }

    public enum MainTabs {
        News, Coupon, Present, MyPage, Setting;

        public static MainTabs get(int ordinal) {
            MainTabs tab = News;
            switch (ordinal) {
                case 0:
                    tab = News;
                    break;
                case 1:
                    tab = Coupon;
                    break;
                case 2:
                    tab = Present;
                    break;
                case 3:
                    tab = MyPage;
                    break;
                case 4:
                    tab = Setting;
                    break;
            }

            return tab;
        }
    }
}
