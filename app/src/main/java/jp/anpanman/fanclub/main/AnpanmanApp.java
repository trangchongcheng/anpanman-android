package jp.anpanman.fanclub.main;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.main.R;

import jp.anpanman.fanclub.main.model.UserInfo;

/**
 * Created by chientruong on 8/24/16.
 */
public class AnpanmanApp extends Application {
    UserInfo userInfo;
    private Tracker mTracker;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
    public void initAnalytic(String categoryId, String actionId,String labelId,long valueId){
        getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryId)
                .setAction(actionId)
                .setLabel(labelId)
                .setValue(valueId)
                .build());

    }
    public void initAnalyticCategory(String categoryId){
        getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryId)
                .build());

    }
}
