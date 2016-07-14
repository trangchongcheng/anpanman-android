package com.main.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.framework.phvtActivity.BaseActivity;

/**
 * Created by linhphan on 7/14/16.
 */
public class SampleActivity extends BaseActivity implements View.OnClickListener{

    //=============== properties ===================================================================
    private int mInt;

    //=============== constructors =================================================================
    public SampleActivity(int mInt) {
        this.mInt = mInt;
    }


    //=============== setters and getters ==========================================================
    public int getInt() {
        return mInt;
    }

    public void setInt(int i) {
        this.mInt = i;
    }

    //=============== inherited methods ============================================================
    @Override
    protected int getActivityLayoutId() {
        return 0;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    //=============== implemented methods ==========================================================
    @Override
    public void onClick(View view) {

    }

    //=============== inner methods ================================================================


    //=============== inner classes ================================================================
}
