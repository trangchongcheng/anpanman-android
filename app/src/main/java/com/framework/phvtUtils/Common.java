package com.framework.phvtUtils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by linhphan on 7/13/16.
 */
public class Common {
    /**
     * Get status bar height
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources resource = context.getResources();
        int resourceId = resource.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resource.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * Get URL domain.
     *
     * @param url
     * @return
     */
    public static String getUrlDomain(String url) {
        try {
            URL aURL = new URL(url);
            return aURL.getProtocol() + "://" + aURL.getHost();
        } catch (MalformedURLException e) {
            Log.e(Common.class.getName(), e.getMessage(), e);
            return "";
        }
    }

    /**
     * temporarily lock a view in 2 seconds
     * @param view to be locked
     */
    public static void temporarilyLockView(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);
    }

    public static Object stringToObject(String encodedObject) {
        try {
            return new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(encodedObject.getBytes()), Base64.DEFAULT)).readObject();
        } catch (Exception e) {
            Log.e("get object fail", "");
        }
        return null;
    }

    /**
     * Validate email pattern
     *
     * @param email
     * @return return true if valid, otherwise is false
     */
    public static boolean validateEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * generate a hashed string from original string
     * @param s to be hashed
     * @return a hashed string
     */
    public static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(s.getBytes());
            byte[] mdBytes = md.digest();

            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (byte mdByte1 : mdBytes) {
                sb.append(Integer.toString((mdByte1 & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Digest(in hex format):: " + sb.toString());

            //convert the byte to hex format method 2
            StringBuilder hexString = new StringBuilder();
            for (byte mdByte : mdBytes) {
                String hex = Integer.toHexString(0xff & mdByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            System.out.println("Digest(in hex format):: " + hexString.toString());

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
