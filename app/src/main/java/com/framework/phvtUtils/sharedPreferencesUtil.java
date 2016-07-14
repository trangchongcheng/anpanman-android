package com.framework.phvtUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Task Utilities
 *
 * @author hoang phuong
 */
public class sharedPreferencesUtil {
    //--------------------------------------------------------------------------------------------------------------------
    /**
     * Log cat tag.
     */
    public static final String TAG = sharedPreferencesUtil.class.getName();
    //--------------------------------------------------------------------------------------------------------------------
    public static final String PREFERENCE_FILE_NAME = "food_coach";      // 設定ファイル名

    //--------------------------------------------------------------------------------------------------------------------



    //========================== Share Preference =========================================================================
    public static int getIntFromPreference(Context context, String key, int defaultValue) {
        //if (context == null) return 0;
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        return preference.getInt(key, defaultValue);
    }

    public static void clearPreference(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void putIntToPreference(Context context, String key, int value) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getStringFromPreference(Context context, String key, String defaultValue) {
        if (context == null) return null;
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        return preference.getString(key, defaultValue);
    }

    public static void putStringToPreference(Context context, String key, String value) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putStringListToPreference(Context context, Map<String, String> list) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        for (String key : list.keySet()) {
            String value = list.get(key);
            editor.putString(key, value);
        }
        editor.apply();
    }

    public static Map<String, String> getStringListFromPreference(Context context, Map<String, String> list){
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        for (String key : list.keySet()) {
            String value = list.get(key);
            String storedValue = preference.getString(key, value);
            list.put(key, storedValue);
        }
        return list;
    }

    public static boolean getBooleanFromPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        return preference.getBoolean(key, defaultValue);
    }

    public static void putBooleanToPreference(Context context, String key, Boolean value) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static long getLongFromPreference(Context context, String key, long defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        return preference.getLong(key, defaultValue);
    }

    public static void putLongToPreference(Context context, String key, long value) {
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    //========================== save - get object - string ==================================================================
    public static String objectToString(Serializable object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(object);
            byte[] data = out.toByteArray();
            out.close();

            out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
            b64.write(data);
            b64.close();
            out.close();

            return new String(out.toByteArray());
        } catch (Exception e) {
            Log.e("save object fail", object.getClass().toString() + e.toString());
        }
        return null;
    }
}
