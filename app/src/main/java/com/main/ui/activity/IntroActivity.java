package com.main.ui.activity;

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

import com.framework.phvtActivity.BaseActivity;
import com.main.R;

/**
 * Created by linhphan on 7/25/16.
 */
public class IntroActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Button btnSkip;
    private View viewIndicator0;
    private View viewIndicator1;
    private View viewIndicator2;
    private View viewIndicator3;
    private ViewPager viewPager;
    private IntroAdapter adapter;

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_skip:
                gotoTermsOfUseScreen();
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
    private void gotoTermsOfUseScreen(){
        Intent intent = new Intent(this, TermOfUseActivity.class);
        startActivity(intent);
        finish();
    }

    //============== inner classes =================================================================
    public static class IntroFragment extends Fragment {

        public IntroFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_intro, container, false);
        }
    }

    class IntroAdapter extends FragmentPagerAdapter {
        public IntroAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new IntroFragment();
        }



        @Override
        public int getCount() {
            return 4;
        }
    }
}
