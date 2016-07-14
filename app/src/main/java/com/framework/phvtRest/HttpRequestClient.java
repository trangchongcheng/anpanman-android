package com.framework.phvtRest;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;


/**
 * Http request client
 * Basing on Asyntask thread
 *
 * @author  : phatvan
 *
 */

public abstract class HttpRequestClient extends AsyncTask<Object, Object, HashMap<String, Object>> {
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Log cat Tag
	 */
	public static String TAG = HttpRequestClient.class.getName();
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * String Encode used on network.
	 */
	protected String mEncode = "UTF-8";
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Context
	 */
	protected Context mContext;
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Get context
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Set context
	 * @param context
	 */
	public void setContext(Context context) {
		mContext = context;
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Get encode string
	 * @return
	 */
	public String getEncode() {
		return mEncode;
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Set encode String. Default is UTF-8
	 * @param encode
	 * @return
	 */
	public void setEncode(String encode) {
		mEncode = encode;
	}
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * Constructor with specified context (usually is IU context)
	 */
	public HttpRequestClient(Context context) {
		super();
		setContext(context);
	}
	//-------------------------------------------------------------------------------------------------------------------
}

