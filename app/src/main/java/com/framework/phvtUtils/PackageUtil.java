package com.framework.phvtUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * PackageUtil 
 * @author thaonp & phatvan
 */
public class PackageUtil {
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Get the application version (Version Code)
	 * @param context
	 * @return
	 */
	public static int getApplicationVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} 
		catch(NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Check application is install or not
	 * @param context
	 * @param applicationPackageUri For example: <code>com.instagram.android</code>
	 * @return
	 */
	public static boolean isApplicationInstalled(Context context, String applicationPackageUri) {
		PackageManager packageManager = context.getPackageManager();
		boolean isInstalled;
		try {
			packageManager.getPackageInfo(applicationPackageUri, PackageManager.GET_ACTIVITIES);
			isInstalled = true;
		}
		catch (NameNotFoundException e) {
			isInstalled = false;
		}
		return isInstalled;
	}
	//-------------------------------------------------------------------------------------------------------------------
}
