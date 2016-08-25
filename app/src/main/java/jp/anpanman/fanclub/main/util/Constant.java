package jp.anpanman.fanclub.main.util;

import java.util.EnumSet;

/**
 * Created by linhphan on 7/14/16.
 */
public class Constant {
    public static String ANPANMAN_SHAREDPREFERENCES_FILE = "apanpanman_sharedpref";
    public static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    public static final String PREF_USER_INFO = "PREF_USER_INFO";
    //    public static final String GCM_SENDER_ID = "866234032360";//project number
    public static final String GCM_SENDER_ID = "87274862508";//project number
    public static String NOTIFY_APPLICATION_KEY = "053783832795fdd71457a12a03ac4e2815b5d91f9a8a78335162e38e67ff044d";
    public static String NOTIFY_CLIENT_KEY = "ce128960dd747c968359493771eb18c4cd368d9e41e88e5091e3f043711d094d";
    public static String NOTIFY_APPLICATION_KEY_NCMB = "2599db8a34a793b8e4634ee93738472983466aeccb859f4ada3d40df1eb5775a";
    public static String NOTIFY_CLIENT_KEY_NCMB = "06bd0fc517d3e66d8489bc32ce1f68a479925cc398bd323d82674705f73fa8c9";

    public static final String PUSH_ACTION = "jp.anpanman.fanclub.pushing";

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
