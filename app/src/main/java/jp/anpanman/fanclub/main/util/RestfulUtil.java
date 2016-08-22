package jp.anpanman.fanclub.main.util;

import android.content.Context;

import jp.anpanman.fanclub.framework.restfulService.RestfulService;
import jp.anpanman.fanclub.main.parser.DeviceTokenParser;
import jp.anpanman.fanclub.main.parser.SignupParser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhphan on 7/14/16.
 */
public class RestfulUtil {

    public static void signup(Context context, String gcmRegId, RestfulService.Callback callback) {
        try {
            URI uri = URI.create(RestfulUrl.NOTIFY_HOST_SIGNUP);
            String timestamp = Common.getTimeStamp();
            String signature = Common.getSignature("POST", uri, Constant.NOTIFY_APPLICATION_KEY, Constant.NOTIFY_CLIENT_KEY, timestamp);

            Map<String, String> params = new HashMap<>();
            params.put("appVersion", "1.0.0");
            params.put("deviceType", "android");
            params.put("deviceToken", gcmRegId);

            Map<String, String> header = new HashMap<>();
            header.put("X-NCMB-Application-Key", Constant.NOTIFY_APPLICATION_KEY);
            header.put("X-NCMB-Signature", signature);
            header.put("X-NCMB-Timestamp", timestamp);
            header.put("x-anp-request", "true");
//            header.put("X-NCMB-Apps-Session-Token");

            RestfulService service = new RestfulService(context, false, callback);
            service.setType(RestfulService.Method.POST);
            service.setParams(params);
            service.setHeader(header);
            service.setParser(new SignupParser());
            service.execute(RestfulUrl.NOTIFY_HOST_SIGNUP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerDeviceToken(Context context, String gcmRegId, String objectId, RestfulService.Callback callback) {
        try {
            URI uri = URI.create(RestfulUrl.NOTIFY_HOST_REGISTER_DEVICE_TOKEN);
            String timestamp = Common.getTimeStamp();
            String signature = Common.getSignature("POST", uri, Constant.NOTIFY_APPLICATION_KEY, Constant.NOTIFY_CLIENT_KEY, timestamp);

            Map<String, String> params = new HashMap<>();
            params.put("appVersion", "1.0.0");
            params.put("deviceType", "android");
            params.put("deviceToken", gcmRegId);
            params.put("objectId", objectId);

            Map<String, String> header = new HashMap<>();
            header.put("X-NCMB-Application-Key", Constant.NOTIFY_APPLICATION_KEY);
            header.put("X-NCMB-Signature", signature);
            header.put("X-NCMB-Timestamp", timestamp);
            header.put("x-anp-request", "true");
//            header.put("X-NCMB-Apps-Session-Token");

            RestfulService service = new RestfulService(context, false, callback);
            service.setType(RestfulService.Method.POST);
            service.setParams(params);
            service.setHeader(header);
            service.setParser(new DeviceTokenParser());
            service.execute(RestfulUrl.NOTIFY_HOST_REGISTER_DEVICE_TOKEN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUserInfo(Context context,String objectId, RestfulService.Callback callback) {
        try {
            URI uri = URI.create(RestfulUrl.NOTIFY_HOST_GET_USER_INFO + objectId);
            String timestamp = Common.getTimeStamp();
            String signature = Common.getSignature("GET", uri, Constant.NOTIFY_APPLICATION_KEY, Constant.NOTIFY_CLIENT_KEY, timestamp);

            Map<String, String> header = new HashMap<>();
            header.put("X-NCMB-Application-Key", Constant.NOTIFY_APPLICATION_KEY);
            header.put("X-NCMB-Signature", signature);
            header.put("X-NCMB-Timestamp", timestamp);
            header.put("x-anp-request", "true");

            RestfulService service = new RestfulService(context, false, callback);
            service.setType(RestfulService.Method.GET);
            service.setHeader(header);
            service.setParser(new SignupParser());
            service.execute(uri.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
