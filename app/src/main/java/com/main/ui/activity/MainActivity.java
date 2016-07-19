package com.main.ui.activity;

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

import com.framework.phvtActivity.BaseActivity;
import com.framework.phvtCommon.FragmentTransitionInfo;
import com.main.R;
import com.main.ui.fragment.ContactFragment;
import com.main.ui.fragment.GuridinguFragment;
import com.main.ui.fragment.PrivacyPolicyFragment;
import com.main.ui.fragment.TermsOfUseFragment;
import com.main.ui.fragment.WallFragment;

import java.security.acl.Group;

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
    private ImageButton btnWallTab;
    private ImageButton btnGuridinguTab;
    private ImageButton btnTermsOfUseTab;
    private ImageButton btnPrivacyPolicyTab;
    private ImageButton btnContactTab;

    public static MainTabs currentTab;


    //=============== constructors =================================================================


    //=============== setters and getters ==========================================================


    //=============== inherited methods ============================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchTab(MainTabs.Wall, false);
        setDisplayBottomNav();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        //== bottom nav
        btnWallTab = (ImageButton) findViewById(R.id.btn_img_tab_wall);
        btnGuridinguTab = (ImageButton) findViewById(R.id.btn_img_tab_guridingu);
        btnTermsOfUseTab = (ImageButton) findViewById(R.id.btn_img_tab_terms_of_use);
        btnPrivacyPolicyTab = (ImageButton) findViewById(R.id.btn_img_tab_privacy_policy);
        btnContactTab = (ImageButton) findViewById(R.id.btn_img_tab_contact);

        btnHamburgerMenu = (ImageButton) findViewById(R.id.btn_img_hamburger);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        lvDrawerNav = (ListView) findViewById(R.id.lv_drawer_nav);
        navigationView = (NavigationView) findViewById(R.id.navView);

        setDrawerNavigation();
    }

    @Override
    protected void registerEventHandlers() {
        //== bottom nav
        btnWallTab.setOnClickListener(this);
        btnGuridinguTab.setOnClickListener(this);
        btnTermsOfUseTab.setOnClickListener(this);
        btnPrivacyPolicyTab.setOnClickListener(this);
        btnContactTab.setOnClickListener(this);

        btnHamburgerMenu.setOnClickListener(this);
        lvDrawerNav.setOnItemClickListener(this);
    }

    //=============== implemented methods ==========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_img_hamburger:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(navigationView);
                break;

            case R.id.btn_img_tab_wall:
                switchTab(MainTabs.Wall, true);
                break;

            case R.id.btn_img_tab_guridingu:
                switchTab(MainTabs.Guridingu, true);
                break;

            case R.id.btn_img_tab_terms_of_use:
                switchTab(MainTabs.TermsOfUse, true);
                break;

            case R.id.btn_img_tab_privacy_policy:
                switchTab(MainTabs.PrivacyPolicy, true);
                break;

            case R.id.btn_img_tab_contact:
                switchTab(MainTabs.Contact, true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switchTab(MainTabs.Contact, true);
    }

    //=============== inner methods ================================================================
    private void setDrawerNavigation(){
        DrawerAdapter drawerAdapter = new DrawerAdapter();
        lvDrawerNav.setAdapter(drawerAdapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View navHeaderView = inflater.inflate(R.layout.drawer_nav_header, null, false);
        View navFooterView = inflater.inflate(R.layout.drawer_nav_footer, null, false);

        lvDrawerNav.addHeaderView(navHeaderView, null, false);
        lvDrawerNav.addFooterView(navFooterView, null, false);
    }

    public void switchTab(MainTabs newTab, boolean isAnimation){

        if (newTab == currentTab){
            return;
        }

        switch (newTab){
            case Wall:
                openWallFragment(isAnimation);
                break;

            case Guridingu:
                openGuridinguFragment();
                break;

            case TermsOfUse:
                openTermsOfUseFragment();
                break;

            case PrivacyPolicy:
                openPrivacyPolicyFragment();
                break;

            case Contact:
                openContactDialog();
                break;
        }
        currentTab = newTab;
        setDisplayBottomNav();
    }

    private void setDisplayBottomNav(){
        switch (currentTab){
            case Wall:
                btnWallTab.setSelected(true);
                btnGuridinguTab.setSelected(false);
                btnTermsOfUseTab.setSelected(false);
                btnPrivacyPolicyTab.setSelected(false);
                btnContactTab.setSelected(false);
                break;

            case Guridingu:
                btnWallTab.setSelected(false);
                btnGuridinguTab.setSelected(true);
                btnTermsOfUseTab.setSelected(false);
                btnPrivacyPolicyTab.setSelected(false);
                btnContactTab.setSelected(false);
                break;

            case TermsOfUse:
                btnWallTab.setSelected(false);
                btnGuridinguTab.setSelected(false);
                btnTermsOfUseTab.setSelected(true);
                btnPrivacyPolicyTab.setSelected(false);
                btnContactTab.setSelected(false);
                break;

            case PrivacyPolicy:
                btnWallTab.setSelected(false);
                btnGuridinguTab.setSelected(false);
                btnTermsOfUseTab.setSelected(false);
                btnPrivacyPolicyTab.setSelected(true);
                btnContactTab.setSelected(false);
                break;

            case Contact:
//                btnWallTab.setSelected(false);
//                btnGuridinguTab.setSelected(false);
//                btnTermsOfUseTab.setSelected(false);
//                btnPrivacyPolicyTab.setSelected(false);
//                btnContactTab.setSelected(false);
                currentTab = MainTabs.Wall;
                break;
        }
    }

    private void openWallFragment(boolean isAnimation){
        FragmentTransitionInfo transition = null;
        if (isAnimation) {
            transition = new FragmentTransitionInfo(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left, 0, 0);
        }
        replaceFragment(R.id.fl_main_content, WallFragment.class.getName(), false, null, transition);
    }

    private void openGuridinguFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(R.id.fl_main_content, GuridinguFragment.class.getName(), false, null, transition);
    }

    private void openTermsOfUseFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(R.id.fl_main_content, TermsOfUseFragment.class.getName(), false, null, transition);
    }

    private void openPrivacyPolicyFragment(){
        FragmentTransitionInfo transition = new FragmentTransitionInfo(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left, 0, 0);
        replaceFragment(R.id.fl_main_content, PrivacyPolicyFragment.class.getName(), false, null, transition);
    }

    private void openContactDialog(){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.show(getSupportFragmentManager(), ContactFragment.class.getName());
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
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_nav_item_group, viewGroup, false);
                        GroupItemHolder holder = new GroupItemHolder(view);
                        view.setTag(holder);
                    }
                    break;

                default://normal item
                    if (view == null || !(view.getTag() instanceof NormalItemHolder)) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_nav_item, viewGroup, false);
                        NormalItemHolder h = new NormalItemHolder(view);
                        view.setTag(h);
                    }
                    break;
            }

            //== setup data to views
            switch (getItemType(i)){
                case Group:
                    GroupItemHolder holder = (GroupItemHolder) view.getTag();
                    holder.txtTitle.setText("Group Title");
                    break;

                default:
                    NormalItemHolder h = (NormalItemHolder) view.getTag();
                    h.txtTitle.setText("Item Title");
                    break;
            }

            return view;
        }

        //===========
        private ItemType getItemType(int position){
            switch (position){
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
                txtTitle = (TextView) root.findViewById(R.id.txt_item_title);
            }
        }
        //===========
        private static class GroupItemHolder{
            TextView txtTitle;

            public GroupItemHolder(View root) {
                txtTitle = (TextView) root.findViewById(R.id.txt_group_tile);
            }
        }
    }

    enum MainTabs{
        Wall, Guridingu, TermsOfUse, PrivacyPolicy, Contact
    }
}
