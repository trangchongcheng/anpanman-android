package jp.anpanman.fanclub.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtUtils.SharedPreferencesUtil;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.util.Constant;

import com.google.android.gms.analytics.Tracker;
import com.main.R;

/**
 * Created by linhphan on 7/25/16.
 */
public class IntroActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static String PREF_INTRO_HAS_SHOWED = "PREF_INTRO_HAS_SHOWED";
    public static String IS_FAQ = "is_faq";
    public static final String BACKGROUND_RESOURCE = "background_resource";

    private Button btnSkip;
    private View viewIndicator0;
    private View viewIndicator1;
    private View viewIndicator2;
    private View viewIndicator3;
    private ViewPager viewPager;
    private IntroAdapter adapter;
    private boolean isFAQ = false;
    private Tracker mTracker;

    //============== inherited methods =============================================================
    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_intro;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        btnSkip = (Button) findViewById(R.id.btn_skip);
        viewIndicator0 = findViewById(R.id.indicator_0);
        viewIndicator1 = findViewById(R.id.indicator_1);
        viewIndicator2 = findViewById(R.id.indicator_2);
        viewIndicator3 = findViewById(R.id.indicator_3);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new IntroAdapter(getSupportFragmentManager());
        Intent intent = getIntent();
        if (intent != null) {
            isFAQ = intent.getBooleanExtra(IS_FAQ, false);
        }
        if (isFAQ) {
            btnSkip.setText(getString(R.string.button_close));
        }
    }

    @Override
    protected void registerEventHandlers() {
        btnSkip.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewIndicator0.setSelected(true);
    }

    //============== implemented methods ===========================================================

    @Override
    protected void onResume() {
        super.onResume();
        initAnalytics(true,null,null,null,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_skip:
                if (isFAQ) {
                    initAnalytics(false,Constant.GA_SELECT,Constant.GA_ONCLICK,Constant.GA_TUTORIAL_CLOSE,1);
                    finish();
                } else {
                    initAnalytics(false,Constant.GA_SELECT,Constant.GA_ONCLICK,Constant.GA_TUTORIAL_SKIP,1);
                    SharedPreferencesUtil.putBoolean(this, PREF_INTRO_HAS_SHOWED, true);
                    gotoTermsOfUseScreen();
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                viewIndicator0.setSelected(true);
                viewIndicator1.setSelected(false);
                viewIndicator2.setSelected(false);
                viewIndicator3.setSelected(false);
                break;

            case 1:
                viewIndicator0.setSelected(false);
                viewIndicator1.setSelected(true);
                viewIndicator2.setSelected(false);
                viewIndicator3.setSelected(false);
                break;

            case 2:
                viewIndicator0.setSelected(false);
                viewIndicator1.setSelected(false);
                viewIndicator2.setSelected(true);
                viewIndicator3.setSelected(false);
                break;

            case 3:
                viewIndicator0.setSelected(false);
                viewIndicator1.setSelected(false);
                viewIndicator2.setSelected(false);
                viewIndicator3.setSelected(true);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //============== inner methods =================================================================
    private void gotoTermsOfUseScreen() {
        Intent intent = new Intent(this, TermOfUseActivity.class);
        startActivity(intent);
        finish();
    }

    //============== inner classes =================================================================
    public static class IntroFragment extends Fragment {

        private FrameLayout frameTutorial;


        public static IntroFragment newInstance(int imageResource) {
            IntroFragment f = new IntroFragment();
            // put title Image and Description Image
            Bundle args = new Bundle();
            args.putInt(BACKGROUND_RESOURCE, imageResource);
            f.setArguments(args);

            return f;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_intro, container, false);
            frameTutorial = (FrameLayout) view.findViewById(R.id.frame_tutorial);
            frameTutorial.setBackgroundResource(getArguments().getInt(BACKGROUND_RESOURCE));
            return view;
        }
    }

    class IntroAdapter extends FragmentPagerAdapter {
        public IntroAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroFragment.newInstance(getApplicationContext().getResources().obtainTypedArray(R.array.background_tutorial).getResourceId(position, 0));
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    // Init Google Analytic Tutorial Screen
    public void initAnalytics(Boolean isOnlyCategory,String category, String action, String label, long value){
        AnpanmanApp application = (AnpanmanApp) getApplication();
        if(isOnlyCategory){
            application.initAnalyticCategory(Constant.GA_TUTORIAL);
        }else {
            application.initAnalytic(category,action,label,value);
        }

    }
}
