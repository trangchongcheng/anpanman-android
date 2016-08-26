package jp.anpanman.fanclub.main.util;

/**
 * Created by linhphan on 7/14/16.
 */
public class RestfulUrl {
    //== API
    public final static String NOTIFY_HOST_SIGNUP = "https://script.mb.api.cloud.nifty.com/2015-09-01/script/init.js";
    public final static String NOTIFY_HOST_REGISTER_DEVICE_TOKEN = "https://script.mb.api.cloud.nifty.com/2015-09-01/script/device.js";
    public final static String NOTIFY_HOST_GET_USER_INFO = "https://mb.api.cloud.nifty.com/2013-09-01/classes/User/";

    //== web view urls
    public final static String WEB_VIEW_DOMAIN_PRODUCT_MODE = "http://fcapp.anpanman.jp/";
    public final static String WEB_VIEW_DOMAIN_STAGING_MODE = "http://stg-fcapp.anpanman.jp/";
    public final static String WEB_VIEW_DOMAIN = WEB_VIEW_DOMAIN_STAGING_MODE;

    public final static String URL_NEWS = WEB_VIEW_DOMAIN+ "new/";
    public final static String URL_PRESENTS = WEB_VIEW_DOMAIN+ "present/";
    public final static String URL_COUPON = WEB_VIEW_DOMAIN+ "otoku/";
    public final static String URL_ACCOUNT_SETTING = WEB_VIEW_DOMAIN+ "account/";
    public final static String URL_TERMS = WEB_VIEW_DOMAIN+ "terms/";
    public final static String URL_POLICY = WEB_VIEW_DOMAIN+ "privacy_policy/";
    public final static String URL_CONTACT = WEB_VIEW_DOMAIN+ "contact/";
    public final static String URL_GURIDINGU = WEB_VIEW_DOMAIN+ "greeting/";
    public final static String URL_WALL = WEB_VIEW_DOMAIN+ "wallpaper/";
    public final static String URL_REGISTER_MYPAGE = WEB_VIEW_DOMAIN+ "account/F-4-1.html";

    public final static String API_UPDATED_TIME =  WEB_VIEW_DOMAIN + "json/setting.json";
}
