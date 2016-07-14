package com.framework.phvtUtils;

import android.util.Log;

import com.main.BuildConfig;

/**
 * Framework PHATVT
 *
 * @author PhatVan ヴァン  タン　ファット
 * @since:  11-2015
 *
 */

public class AppLog {
    //TAG Class
    static String TAG = "PHVT";


    // --------------------------------------------------------
    // Show Log info
    synchronized public static void log(String content) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, content);
        }
    }

    synchronized public static void log(String tag, String content) {
            if (BuildConfig.DEBUG) {
                Log.i(tag, content);
            }
    }

    // --------------------------------------------------------
    // Show Log Error
    synchronized public static void log_error(String content) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, content);
        }
    }

    // --------------------------------------------------------
    // Show Log URL
    synchronized public static void log_url(String content) {
        if (BuildConfig.DEBUG) {
            Log.i("", "--------------------------------------");
            Log.i(TAG, content);
            Log.i("", "--------------------------------------");
        }
    }
}
