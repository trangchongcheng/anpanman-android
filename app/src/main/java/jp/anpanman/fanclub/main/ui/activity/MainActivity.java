package jp.anpanman.fanclub.main.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

import java.util.ArrayList;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtCommon.FragmentTransitionInfo;
import jp.anpanman.fanclub.main.ui.fragment.CouponFragment;
import jp.anpanman.fanclub.main.ui.fragment.MyPageFragment;
import jp.anpanman.fanclub.main.ui.fragment.NewFragment;
import jp.anpanman.fanclub.main.ui.fragment.PresentFragment;
import jp.anpanman.fanclub.main.ui.fragment.SettingFragment;

/**
 * Created by linhphan on 7/15/16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //=============== properties ===================================================================
    private ImageButton btnHamburgerMenu;
    private DrawerLayout drawerLayout;
    private ListView lvDrawerNav;
    private NavigationView navigationView;

    //== bottom nav
    private ImageButton btnNewsTab;
    private ImageButton btnCouponTab;
    private ImageButton btnPresentTab;
    private ImageButton btnMyPageTab;
    private ImageButton btnSettingTab;

    public static MainTabs currentTab;

    public static int iGroup = 0, iItem = 0;
    public static ArrayList<String> arrGroup = new ArrayList<>();
    public static ArrayList<String> arrItem = new ArrayList<>();

    //=============== constructors =================================================================


    //=============== setters and getters ==========================================================


    //=============== inherited methods ============================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchTab(MainTabs.News, false);
        setDisplayBottomNav();

        for (int j = 0; j < getResources().getStringArray(R.array.group).length; j++) {
            arrGroup.add(getResources().getStringArray(R.array.group)[j]);
        }
        for (int j = 0; j < getResources().getStringArray(R.array.item).length; j++) {
            arrItem.add(getResources().getStringArray(R.array.item)[j]);
        }
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
        switch (view.getId()){
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
        switchTab(MainTabs.Setting, true);
    }

    //=============== inner methods ================================================================
    private void setDrawerNavigation(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View navHeaderView = inflater.inflate(com.main.R.layout.drawer_nav_header, null, false);
        View navFooterView = inflater.inflate(com.main.R.layout.drawer_nav_footer, null, false);

        lvDrawerNav.addHeaderView(navHeaderView, null, false);
        lvDrawerNav.addFooterView(navFooterView, null, false);

        DrawerAdapter drawerAdapter = new DrawerAdapter();
        lvDrawerNav.setAdapter(drawerAdapter);
    }

    public void switchTab(MainTabs newTab, boolean isAnimation){

        if (newTab == currentTab){
            return;
        }

        switch (newTab){
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

    private void setDisplayBottomNav(){
        switch (currentTab){
            case News:
                btnNewsTab.setSelected(true);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup)btnNewsTab.getParent()).setSelected(true);
                ((ViewGroup)btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup)btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup)btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup)btnSettingTab.getParent()).setSelected(false);
                break;

            case Coupon:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(true);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup)btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup)btnCouponTab.getParent()).setSelected(true);
                ((ViewGroup)btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup)btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup)btnSettingTab.getParent()).setSelected(false);
                break;

            case Present:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(true);
                btnMyPageTab.setSelected(false);
                btnSettingTab.setSelected(false);

                ((ViewGroup)btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup)btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup)btnPresentTab.getParent()).setSelected(true);
                ((ViewGroup)btnMyPageTab.getParent()).setSelected(false);
                ((ViewGroup)btnSettingTab.getParent()).setSelected(false);
                break;

            case MyPage:
                btnNewsTab.setSelected(false);
                btnCouponTab.setSelected(false);
                btnPresentTab.setSelected(false);
                btnMyPageTab.setSelected(true);
                btnSettingTab.setSelected(false);

                ((ViewGroup)btnNewsTab.getParent()).setSelected(false);
                ((ViewGroup)btnCouponTab.getParent()).setSelected(false);
                ((ViewGroup)btnPresentTab.getParent()).setSelected(false);
                ((ViewGroup)btnMyPageTab.getParent()).setSelected(true);
                ((ViewGroup)btnSettingTab.getParent()).setSelected(false);
                break;

            case Setting:
//                btnNewsTab.setSelected(false);
//                btnCouponTab.setSelected(false);
//                btnPresentTab.setSelected(false);
//                btnMyPageTab.setSelected(false);
//                btnSettingTab.setSelected(false);
                currentTab = MainTabs.News;
                break;
        }
    }

    private void openNewsFragment(boolean isAnimation){
        FragmentTransitionInfo transition = null;
        if (isAnimation) {
            transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        }
        replaceFragment(com.main.R.id.fl_main_content, NewFragment.class.getName(), false, null, transition);
    }

    private void openCouponFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, CouponFragment.class.getName(), false, null, transition);
    }

    private void openPresentFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, PresentFragment.class.getName(), false, null, transition);
    }

    private void openMyPageFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(com.main.R.anim.slide_enter_right_left, com.main.R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(com.main.R.id.fl_main_content, MyPageFragment.class.getName(), false, null, transition);
    }

    private void openSettingDialog(){
       SettingFragment fragment = new SettingFragment();
        fragment.show(getSupportFragmentManager(), SettingFragment.class.getName());
    }

    //=============== inner classes ================================================================
    private static class DrawerAdapter extends BaseAdapter{

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
            switch (getItemType(i)){
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
            switch (getItemType(i)){
                case Group:
                    GroupItemHolder holder = (GroupItemHolder) view.getTag();
                    holder.txtTitle.setText(arrGroup.get((iGroup)));
                    iGroup++;
                    break;

                default:
                    NormalItemHolder h = (NormalItemHolder) view.getTag();
                    //h.txtTitle.setText("Item Title");
                        h.txtTitle.setText(arrItem.get(iItem));
                    iItem++;
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
        enum ItemType{
            Group, Normal
        }

        //===========
        private static class NormalItemHolder{
            TextView txtTitle;

            public NormalItemHolder(View root) {
                txtTitle = (TextView) root.findViewById(com.main.R.id.txt_item_title);
            }
        }
        //===========
        private static class GroupItemHolder{
            TextView txtTitle;

            public GroupItemHolder(View root) {
                txtTitle = (TextView) root.findViewById(com.main.R.id.txt_group_tile);
            }
        }
    }

    enum MainTabs{
        News, Coupon, Present, MyPage, Setting
    }
}