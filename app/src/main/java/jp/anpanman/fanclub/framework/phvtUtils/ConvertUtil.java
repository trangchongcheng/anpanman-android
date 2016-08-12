package jp.anpanman.fanclub.framework.phvtUtils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * Convert Utils
 * @Created by hoangphuong on 10-2015
 *
 */

public class ConvertUtil {

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Logcat TAG
     */
    public static final String TAG = ConvertUtil.class.getName();

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Convert input stream to byte array.
     * @param inputStream
     * @return
     */
    public static byte[] convertInputStreamToByteArray(InputStream inputStream) {
        byte[] bytes = null;
        try {
            int count;
            byte data[] = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while((count = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, count);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            inputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return bytes;
    }

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Convert input stream to String
     * @param is InputStream
     * @return
     */
    public static String convertInputStreamToString(InputStream is) throws Exception {
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    //-------------------------------------------------------------------------------------------------------------------

}
