package com.framework.phvtIAB;

/**
 * Framework PHATVT
 *
 * @author PhatVan ヴァン  タン　ファット
 * @since:  04 - 2016
 * In App Billing CONST
 *
 */

public class IABConst {
    //Activity number
    public static final String ACTIVITY_NUMBER = "activity_num";

    //Log Tag
    public static final String LOG_TAG = "[ IAB PHVT ]";

    //CODE Sign in Account
    public static final int CODE_SIGIN_ACCOUNT = 101;

    //Subscrip id examples
    public static final String SUBSCRIPTION_ID = "com.anjlab.test.iab.subs1";

    // License Key
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnJ3dATiehzoYo1mAZ5r2NGqLM1NX4R9fJVJMv2uj5B24pzZDw4wshiVTxOMNujYUUtGzAMSpj9HBSwcOnV9Fue9Tc+rGTFGjuol8HiiXiksQxmBnhVSgzFkBnaL7UwylF0iNIC7l24Jqe7/ZEg94OnCPhGZ4/T6Jt+1WvZt5YkKXN9dnUvHk25S7vOAJGaurwamrrptRjrxY4llo1y8mD2ePLs9az3QLlAR9vhRs69WCoA3HGHhDoNe68qLfaJrGHme5zaVoM6+I7yeTTPKmUtpIcvMjML62tYwaS5tmgC3sfQzREhAsoHvFuFRpMoxAKLGzcAVLn8slrBCc//WCMwIDAQAB";

    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    public static final String MERCHANT_ID = "";

    // Product id
    public static final IABObject[] data_items = {
            new IABObject("foodcoach.item.month", "billing_for_month", false),
            //....
          };

    /**
     * DocType :
     *
     * BILLING_RESPONSE_RESULT_OK	0	Success
     * BILLING_RESPONSE_RESULT_USER_CANCELED	1	User pressed back or canceled a dialog
     * BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE	2	Network connection is down
     * BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE	3	Billing API version is not supported for the type requested
     * BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE	4	Requested product is not available for purchase
     * BILLING_RESPONSE_RESULT_DEVELOPER_ERROR	5	Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed or properly set up for In-app Billing in Google Play, or does not have the necessary permissions in its manifest
     * BILLING_RESPONSE_RESULT_ERROR	6	Fatal error during the API action
     * BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED	7	Failure to purchase since item is already owned
     * BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED	8	Failure to consume since item is not owned
     *
     */

    public static  int BILLING_RESPONSE_RESULT_OK                       = 0;
    public static  int BILLING_RESPONSE_RESULT_USER_CANCELED            = 1;
    public static  int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE      = 2;
    public static  int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE      = 3;
    public static  int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE         = 4;
    public static  int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR          = 5;
    public static  int BILLING_RESPONSE_RESULT_ERROR                    = 6;
    public static  int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED       = 7;
    public static  int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED           = 8;

    // Google client to interact with Google API
    public static int RC_SIGN_IN = 9001;
    public static final String GOOGLE_ACCOUT = "com.google";
    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
}
