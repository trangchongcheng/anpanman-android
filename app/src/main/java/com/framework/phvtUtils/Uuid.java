package com.framework.phvtUtils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by linhphan on 7/13/16.
 */
public class Uuid {

    public static final String PREFERENCE_KEY_UUID = "PREFERENCE_KEY_UUID";    // UUID

    /**
     * try to generate an unique device id
     * @return an unique device id
     */
    public static String getUniqueDeviceId(Context context) {

        if (context == null) return null;
        String uuid = sharedPreferencesUtil.getStringFromPreference(context, PREFERENCE_KEY_UUID, null);
        if (!TextUtils.isEmpty(uuid)){
            sharedPreferencesUtil.putStringToPreference(context, PREFERENCE_KEY_UUID, uuid);
            return uuid;
        }

        uuid = getTelephoneManagerId(context);
        if ("000000000000000".equals(uuid)){
            uuid = null;
        }

        if( uuid == null || uuid.isEmpty()){
            uuid = getAndroidId(context);
        }

        if (uuid == null || uuid.isEmpty()){
            uuid = getRandomUuid();
        }

        return uuid;
    }

    /**
     * @return the ANDROID_ID
     * A 64-bit number (as a hex string) that is randomly
     * generated when the user first sets up the device and should remain
     * constant for the lifetime of the user's device. The value may
     * change if a factory reset is performed on the device.
     * <p class="note"><strong>Note:</strong> When a device has <a
     * href="{@docRoot}about/versions/android-4.2.html#MultipleUsers">multiple users</a>
     * (available on certain devices running Android 4.2 or higher), each user appears as a
     * completely separate device, so the {@code ANDROID_ID} value is unique to each
     * user.</p>
     */
    private static String getAndroidId(Context context){
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (androidId != null && androidId.equals("9774d56d682e549c")){ //ANDROID_ID will work on most android device but due to a manufacturer bug, it will return a constant android id 9774d56d682e549c.
            return null;
        }

        return androidId;
    }

    /**
     * Use with {@link Context#getSystemService} to retrieve a
     * {@link android.telephony.TelephonyManager} for handling management the
     * telephony features of the device.
     *
     * @see Context#getSystemService
     * @see android.telephony.TelephonyManager
     */
    private static String getTelephoneManagerId(Context context){
        String telephoneId = null;
        try {
            telephoneId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }catch (SecurityException e){
            e.printStackTrace();
        }
        return telephoneId;
    }

    /**
     * <p>
     * Generates a variant 2, version 4 (randomly generated number) UUID as per
     * <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>.
     * </p>
     */
    public static String getRandomUuid(){
        return UUID.randomUUID().toString();
    }
}
