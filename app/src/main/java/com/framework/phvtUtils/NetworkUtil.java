package com.framework.phvtUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoangphuong on 10/22/15.
 */
public class NetworkUtil {

    //-------------------------------------------------------------------------------------------------------------------
    public enum RequestMethod {
        GET, POST
    }
    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Logcat tag
     */
    public static final String TAG = NetworkUtil.class.getName();


    // Check status Wifi - 3g
    public static void checkInfoNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //if WIFi connected
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        //if Mobile connected
        boolean isMobileConn = networkInfo.isConnected();

        //        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        //        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
    }


    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Check network is connected or not. Should use this method.
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }
    //-------------------------------------------------------------------------------------------------------------------
    /**
     *
     * Combine parameter for GET or DELETE method.
     *
     * @param parameters
     * @param encode     encode string. eg: "UTF-8"
     * @return combined parameters
     * @throws Exception
     */
    public static String combineParameters(HashMap<String, String> parameters, String encode) throws Exception {
        String combinedParams = "";
        if (parameters != null) {
            if (parameters.size() == 0) {
                return combinedParams;
            }
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String paramString = entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), encode);
                if (combinedParams.length() > 1) {
                    combinedParams += "&" + paramString;
                } else {
                    combinedParams += paramString;
                }
            }
        }
        return combinedParams;
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add header fields to request header
     *
     * @param connection
     * @param headers
     * @return
     */
    public static HttpURLConnection addHeaders(HttpURLConnection connection, HashMap<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection;
    }

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Execute request URL (API) with specific request method, headers and parameters.
     *
     * @param api
     * @param method
     * @param headers
     * @param params
     * @param encode
     * @return String
     */

    public static final int READ_TIME_OUT              = 10000; // Miliseconds
    public static final int CONNECTTION_TIME_OUT       = 15000; // Miliseconds
    public static final int RESPONSE_CODE_OK           = HttpURLConnection.HTTP_OK;   // response ok

    public static String executeRequest(String api, RequestMethod method, HashMap<String, String> headers, HashMap<String, String> params, String encode) {
        URL url;
        HttpURLConnection connection = null;
        String response = "";

        //________ GET ______________________________________________________
        if (method == RequestMethod.GET) {
            try {
                //add params
                if (params != null) {
                    String combinedParams = combineParameters(params, encode);
                    url = new URL(api + "?" + combinedParams);
                } else {
                    url = new URL(api);
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(READ_TIME_OUT);
                connection.setConnectTimeout(CONNECTTION_TIME_OUT);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        //________ POST  ______________________________________________________
        else if (method == RequestMethod.POST) {
            try {
                url = new URL(api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(READ_TIME_OUT);
                connection.setConnectTimeout(CONNECTTION_TIME_OUT);
                //add params
                if (params != null) {
                    String combinedParams = combineParameters(params, encode);
                    OutputStream os = connection.getOutputStream();
                    os.write(combinedParams.getBytes());
                    os.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //________ HEADER ______________________________________________________
        if (headers != null) {
            connection = addHeaders(connection, headers);
        }


        //________ RESPONSE ______________________________________________________
        try {
            if (connection.getResponseCode() == RESPONSE_CODE_OK) {
                response = ConvertUtil.convertInputStreamToString(connection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        //________ RETURN ______________________________________________________
        return response;
    }
}
