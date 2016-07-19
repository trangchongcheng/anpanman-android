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
import com.main.R;

/**
 * Created by linhphan on 7/15/16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //=============== properties ===================================================================
    private ImageButton btnHamburgerMenu;
    private DrawerLayout drawerLayout;
    private ListView lvDrawerNav;
    private NavigationView navigationView;


    //=============== constructors =================================================================


    //=============== setters and getters ==========================================================


    //=============== inherited methods ============================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchTab(MainTabs.Wall);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        btnHamburgerMenu = (ImageButton) findViewById(R.id.btn_img_hamburger);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        lvDrawerNav = (ListView) findViewById(R.id.lv_drawer_nav);
        navigationView = (NavigationView) findViewById(R.id.navView);
        setDrawerNavigation();
    }

    @Override
    protected void registerEventHandlers() {
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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

    private void switchTab(MainTabs tab){
        switch (tab){
            case Wall:
                break;

            case Guridingu:

                break;

            case TermsOfUse:

                break;

            case PrivacyPolicy:

                break;

            case Contact:

                break;
        }
        replaceFragment(null, R.id.fl_main_content, "tag", null);
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
