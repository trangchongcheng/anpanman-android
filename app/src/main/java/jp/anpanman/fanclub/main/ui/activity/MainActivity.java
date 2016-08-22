package jp.anpanman.fanclub.main.ui.activity;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

import java.util.ArrayList;
import java.util.Set;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtCommon.FragmentTransitionInfo;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.ui.fragment.CouponFragment;
import jp.anpanman.fanclub.main.ui.fragment.MyPageFragment;
import jp.anpanman.fanclub.main.ui.fragment.NewFragment;
import jp.anpanman.fanclub.main.ui.fragment.PresentFragment;
import jp.anpanman.fanclub.main.ui.fragment.SettingFragment;
import jp.anpanman.fanclub.main.ui.fragment.WebViewFragment;
import jp.anpanman.fanclub.main.util.CustomDialogCoupon;
import jp.anpanman.fanclub.main.util.RestfulUrl;

/**
 * Created by linhphan on 7/15/16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String ARG_SHOULD_SHOW_PUSH_DIALOG = "ARG_SHOULD_SHOW_PUSH_DIALOG";
    public static final String ARG_CURRENT_TAB = "ARG_CURRENT_TAB";
    public static final String ARG_PUSH_DATA = "ARG_PUSH_DATA";
    public static final String ARG_PUSH_TITLE = "ARG_PUSH_TITLE";
    public static final String ARG_PUSH_MESSEAGE = "ARG_PUSH_MESSEAGE";
    public static final String ARG_URL = "ARG_URL";

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

    public static MainTabs currentTab;

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
            switchTab(currentTab, false);
        } else {
            switchTab(MainTabs.News, false);
        }

        setDisplayBottomNav();

        pushNotifyListenReceiver = new PushNotifyListenReceiver();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean(ARG_SHOULD_SHOW_PUSH_DIALOG, false)) {
            String title = bundle.getString(ARG_PUSH_TITLE);
            String message = bundle.getString(ARG_PUSH_MESSEAGE);
            showPushDialog(title, message);
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
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(navigationView);
                break;

            case com.main.R.id.btn_img_tab_news:
                switchTab(MainTabs.News, true);
                break;

            case com.main.R.id.btn_img_tab_coupon:
                switchTab(MainTabs.Coupon, true);
                break;

            case com.main.R.id.btn_img_tab_present:
                switchTab(MainTabs.Present, true);
                break;

            case com.main.R.id.btn_img_tab_my_page:
                switchTab(MainTabs.MyPage, true);
                break;

            case com.main.R.id.btn_img_tab_setting:
                switchTab(MainTabs.Setting, true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 1:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 2:
                openWebView(RestfulUrl.URL_WALL, getString(R.string.wall_paper));
                break;
            case 3:
                openWebView(RestfulUrl.URL_GURIDINGU, getString(R.string.guiridingu));
                break;
            case 4:
                AppLog.log("cheng", "onItemClick: " + i);
                break;
            case 5:
                openWebView(RestfulUrl.URL_TERMS, getString(R.string.terms_of_use));
                break;
            case 6:
                openWebView(RestfulUrl.URL_TERMS, getString(R.string.terms_of_use));
                break;
            case 7:
                openWebView(RestfulUrl.URL_POLICY, getString(R.string.title_policy));
                break;
            case 8:
                openWebView(RestfulUrl.URL_CONTACT, getString(R.string.title_contact));
                break;
            default:
                break;

        }
        //switchTab(MainTabs.Setting, true);
    }

    //=============== inner methods ================================================================
    public void openWebView(String url, String title) {
        DialogFragment fragment = WebViewFragment.newInstance(url, title);
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

        lvDrawerNav.addHeaderView(navHeaderView, null, false);
        lvDrawerNav.addFooterView(navFooterView, null, false);

        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        lvDrawerNav.setAdapter(drawerAdapter);
    }

    public void switchTab(MainTabs newTab, boolean isAnimation) {
        Log.e("**current tab**", (currentTab == null) ? "" : currentTab.toString());
        Log.e("**new tab**", newTab.toString());
        if (newTab == currentTab) {
            return;
        }

        switch (newTab) {
            case News:
                openNewsFragment(isAnimation);
                break;

            case Coupon:
                openCouponFragment();
                break;

            case Present:
                openPresentFragment();
                break;

            case MyPage:
                openMyPageFragment();
                break;

            case Setting:
                openSettingDialog();
                break;
        }
        currentTab = newTab;
        setDisplayBottomNav();
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

    private void openNewsFragment(boolean isAnimation) {
        FragmentTransitionInfo transition = null;
        if (isAnimation) {
            transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        }
        replaceFragment(com.main.R.id.fl_main_content, NewFragment.class.getName(), false, null, transition);
    }

    private void openCouponFragment() {
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, CouponFragment.class.getName(), false, null, transition);
    }

    private void openPresentFragment() {
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, PresentFragment.class.getName(), false, null, transition);
    }

    private void openMyPageFragment() {
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, MyPageFragment.class.getName(), false, null, transition);
    }

    private void openSettingDialog() {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setCallback(new WebViewFragment.DismissCallback() {
            @Override
            public void onDismiss() {
                currentTab = MainTabs.News;
                setDisplayBottomNav();
            }
        });
        openWebView(RestfulUrl.URL_ACCOUNT_SETTING, getString(R.string.other));
    }

    private void showPushDialog(String title, String message) {
        if (customDialogCoupon == null) {
            customDialogCoupon = new CustomDialogCoupon(MainActivity.this);
        }
        customDialogCoupon.show();
    }

    //=============== inner classes ================================================================
    public class PushNotifyListenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showPushDialog(null, null);
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
            return 8;
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
            switch (getItemType(i)) {
                case Group:
                    if (view == null || !(view.getTag() instanceof GroupItemHolder)) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(com.main.R.layout.drawer_nav_item_group, viewGroup, false);
                        GroupItemHolder holder = new GroupItemHolder(view);
                        view.setTag(holder);
                    }
                    break;

                default://normal item
                    if (view == null || !(view.getTag() instanceof NormalItemHolder)) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(com.main.R.layout.drawer_nav_item, viewGroup, false);
                        NormalItemHolder h = new NormalItemHolder(view);
                        view.setTag(h);
                    }
                    break;
            }

            //== setup data to views
            switch (getItemType(i)) {
                case Group:
                    GroupItemHolder holder = (GroupItemHolder) view.getTag();
                    holder.txtTitle.setText(arrGroup.get((iGroup)));
                    iGroup++;
                    iGroup = iGroup % arrGroup.size();
                    break;

                default:
                    NormalItemHolder h = (NormalItemHolder) view.getTag();
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

    enum MainTabs {
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
