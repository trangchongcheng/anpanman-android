package jp.anpanman.fanclub.framework.phvtCommon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * GCM Preference
 * @author thaonp & phatvt
 */
public class GcmPreference {
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Log cat tag.
	 */
	public static final String TAG = GcmPreference.class.getName();
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get User Preference
	 * @param context
	 * @return
	 */
	public static SharedPreferences getInstance(Context context) {
		return context.getSharedPreferences(TAG, Activity.MODE_PRIVATE);
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Put Is Registered To Server.
	 * @param context
	 * @param isRegistered
	 */
	public static void putIsRegisteredToServer(Context context, boolean isRegistered) {
		SharedPreferences preference = getInstance(context);
		Editor editor = preference.edit();
		editor.putBoolean("is_registered_to_server", isRegistered);
		editor.commit();
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get Is Registered To Server.
	 * @param context
	 * @return
	 */
	public static boolean getIsRegisteredToServer(Context context) {
		SharedPreferences preference = getInstance(context);
		return preference.getBoolean("is_registered_to_server", false);
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Put Registered Version.
	 * @param context
	 * @param registeredVersion
	 */
	public static void putRegisteredVersion(Context context, int registeredVersion) {
		SharedPreferences preference = getInstance(context);
		Editor editor = preference.edit();
		editor.putInt("registered_version", registeredVersion);
		editor.commit();
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get Registered Version.
	 * @param context
	 * @return
	 */
	public static int getRegisteredVersion(Context context) {
		SharedPreferences preference = getInstance(context);
		return preference.getInt("registered_version", 0);
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Put Registration ID.
	 * @param context
	 * @param registrationId
	 */
	public static void putRegistrationId(Context context, String registrationId) {
		SharedPreferences preference = getInstance(context);
		Editor editor = preference.edit();
		editor.putString("registration_id", registrationId);
		editor.commit();
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Get Registered Version.
	 * @param context
	 * @return
	 */
	public static String getRegistrationId(Context context) {
		SharedPreferences preference = getInstance(context);
		return preference.getString("registration_id", "");
	}
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Clear GCM Preference.
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences preference = getInstance(context);
		Editor editor = preference.edit();
		editor.putInt("registered_version", 0);
		editor.putString("registration_id", "");
		editor.putBoolean("is_registered_to_server", false);
		editor.commit();
	}
	//--------------------------------------------------------------------------------------------------------------------
}
