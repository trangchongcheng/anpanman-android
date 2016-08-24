package jp.anpanman.fanclub.main;

import android.app.Application;

import jp.anpanman.fanclub.main.model.UserInfo;

/**
 * Created by chientruong on 8/24/16.
 */
public class AnpanmanApp extends Application {
    UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
