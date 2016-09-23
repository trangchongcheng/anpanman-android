package jp.anpanman.fanclub.main.util;

import com.main.BuildConfig;

import java.util.EnumSet;

/**
 * Created by linhphan on 7/14/16.
 */
public class Constant {

    // RMS keys
    public static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    public static final String PREF_USER_INFO = "PREF_USER_INFO";

    //Push GCM
    public static final String GCM_SENDER_ID = BuildConfig.GCM_SENDER_ID;

    //NCMB Keys ( Push notification )
    public static String NOTIFY_APPLICATION_KEY_NCMB = BuildConfig.PUSH_APPLICATION_KEY;
    public static String NOTIFY_CLIENT_KEY_NCMB = BuildConfig.PUSH_CLIENT_KEY;

    public static final String PUSH_ACTION = "jp.anpanman.fanclub.pushing";

    //Schema
    public static final String SCHEME_ANPANMANFANCLUB = "anpanmanfanclub://";
    public static final String HOST_ANPANMANFANCLUB_UPDATE_OBJECT = SCHEME_ANPANMANFANCLUB + "update_object";
    public static final String HOST_ANPANMANFANCLUB_OPEN_BROWSER = SCHEME_ANPANMANFANCLUB + "open_browser";

    //Favourite Character
    public static final int FAVORITE_CHARACTER_CODE_DEFAULT     = 99;
    public static final int FAVORITE_CHARACTER_CODE_MIN         = 1;
    public static final int FAVORITE_CHARACTER_CODE_MAX         = 20;

    //Google Analystic
    public static final String GA_STARTUP ="Startup";
    public static final String GA_TUTORIAL ="Tutorial";
    public static final String GA_TUTORIAL_SKIP =GA_TUTORIAL+" Skip";
    public static final String GA_TUTORIAL_CLOSE =GA_TUTORIAL+" Close";
    public static final String GA_SELECT ="Select";
    public static final String GA_TERMS ="Terms";
    public static final String GA_TERMS_AGREEMENT = GA_TERMS + " A greement";
    public static final String GA_MENU_PORTAL = "Menu Portal";
    public static final String GA_MENU_FAQ = "Menu FAQ";
    public static final String GA_MENU_TERMS = "Menu Terms";
    public static final String GA_NEW ="New";
    public static final String GA_OTOKU ="Otoku";
    public static final String GA_PRESENT="Present";
    public static final String GA_MYPAGE ="Mypage";
    public static final String GA_MYPAGE_ENTRY =GA_MYPAGE+" Entry";
    public static final String GA_ONCLICK ="onclick";
    public static final String GA_MEMBERSHIP ="Membership";
    public static final String GA_INFO ="info";
    public static final String GA_VALUE ="1";

    //Delay APDLLP Screen
    public static final int DELAY_TIME_APDLLPACTIVITY = 2000;
    
    //Delay Splash Screen
    public static final int DELAY_TIME_SPLASHACTIVITY = 4500;




    // DEBUG Interface
    // How to use :
    // 1 param  : EnumSet.of(Flag.A)
    // n params : EnumSet.of(Flag.A, Flag.B, Flag.C )

    public enum DebugFlags {
        DEBUG_NONE,
        DEBUG_MYPAGE_BACKGROUND,
        DEBUG_PASS_GCM_INSTALLED;
        public static final EnumSet<DebugFlags> ALL_OPT_DEBUG = EnumSet.allOf(DebugFlags.class);

    }

    public static EnumSet<DebugFlags> Apanman_Debug = EnumSet.of(DebugFlags.DEBUG_MYPAGE_BACKGROUND);

}
