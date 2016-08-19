package jp.anpanman.fanclub.framework.restfulService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.main.R;
import jp.anpanman.fanclub.framework.restfulService.parser.IParser;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.framework.phvtUtils.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by linhphan on 11/17/15.
 */
public class RestfulService extends AsyncTask<String, Integer, Object> {
    protected final WeakReference<Context> mContext;

    protected Method mType = Method.GET;//the method of request whether POST or GET, default value is GET
    protected Map<String, String> mParams;
    protected Map<String, String> mHeader;
    protected Map<String, String> mFileList;
    protected IParser mParser;

    private Callback mCallback;
    private int mRequestCode = Callback.UNKNOWN_CODE;
    protected int mResponseCode = Callback.UNKNOWN_CODE;

    //progress dialog
    protected ProgressDialog mProgressbar;
    private boolean isShowDialog;

    //exception
    protected Exception mException;

    //================== constructors ==============================================================

    /**
     * constructs an AsyncTask download worker. this will initialize a progress bar dialog with a STYLE_SPINNER if isShowDialog is set true
     *
     * @param isShowDialog if this argument is set true, then a dialog will be showed when this download worker is working.
     * @param mCallback    a callback which do something after the download worker is finish or error.
     */
    public RestfulService(Context context, boolean isShowDialog, Callback mCallback) {
        this.mContext = new WeakReference<>(context);
        this.mCallback = mCallback;
        this.isShowDialog = isShowDialog;
    }

    //================= setters and getters ========================================================
    public RestfulService setRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public RestfulService setType(Method type) {
        this.mType = type;
        return this;
    }

    public RestfulService setParams(Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    public void setHeader(Map<String, String> header) {
        this.mHeader = header;
    }

    public RestfulService setFileUpload(Map<String, String> files) {
        this.mFileList = files;
        return this;
    }

    public RestfulService setParser(IParser jsonParser) {
        mParser = jsonParser;
        return this;
    }

    //================== overridden methods ========================================================
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mContext.get() == null) { // or call isFinishing() if min sdk version < 17
            return;
        }

        if (!NetworkUtil.isOnline(mContext.get())) {//determine whether internet connection is available
            this.mException = new NoInternetConnectionException();
            return;
        }

        if (isShowDialog && mContext.get() != null) {
            mProgressbar = new ProgressDialog(this.mContext.get());
            mProgressbar.setMessage("ローディング中 ...");
            mProgressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressbar.setCancelable(false);
            mProgressbar.show();
        }
    }

    /**
     * this method should be overridden in it's sub classes
     */
    @Override
    protected Object doInBackground(String... params) {
        if (mException != null)
            return null;

        String url = params[0];
        String data = null;
        try {
            if (mType == Method.POST) {
                data = sendPost(url, mParams, mHeader);
            } else if (mType == Method.MULTIPART) {
                data = sendPostMultipart(url, mFileList, mParams);
            } else {
                try {
                    data = sendGet(url, mParams, mHeader);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
            AppLog.log(getClass().getName(), "got data from server: " + data);

            return parseResponseData(data);

        } catch (IOException e) {
            mException = e;
            e.printStackTrace();

        } catch (JSONException e) {
            mException = e;
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (mContext.get() == null) { // or call isFinishing() if min sdk version < 17
            return;
        }
        if (mException == null)
            mCallback.onDownloadSuccessfully(o, mRequestCode, mResponseCode);
        else {
            mCallback.onDownloadFailed(mException, mRequestCode, mResponseCode);
        }
        try {
            if (mProgressbar != null  && mContext.get() != null && mProgressbar.isShowing()) {
                mProgressbar.dismiss();
            }
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mProgressbar != null && mProgressbar.isShowing())
            mProgressbar.setProgress(values[0]);
    }

    @Override
    protected void onCancelled(Object o) {
        if (mProgressbar != null && mProgressbar.isShowing())
            mProgressbar.dismiss();
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        if (mProgressbar != null && mProgressbar.isShowing())
            mProgressbar.dismiss();
        super.onCancelled();
    }

    //================== others methods ============================================================

    /**
     * the progressbar will be cancelable when user touches anywhere outside the dialog if this method is called.
     * default is false.
     *
     * @return the current instance.
     */
    public RestfulService setDialogCancelable() {
        if (mProgressbar != null) {
            mProgressbar.setCancelable(true);
        }
        return this;
    }

    public RestfulService setDialogCancelCallback(String buttonName, DialogInterface.OnClickListener callback) {
        if (mProgressbar != null) {
            mProgressbar.setButton(DialogInterface.BUTTON_NEGATIVE, buttonName, callback);
        }
        return this;
    }

    public RestfulService setDialogTitle(String title) {
        if (mProgressbar != null) {
            if (title != null && !title.isEmpty()) {
                mProgressbar.setTitle(title);
            }
        }
        return this;
    }

    /**
     * set a message to the dialog, if
     */
    public RestfulService setDialogMessage(String message) {
        if (mProgressbar != null) {
            if (message != null && !message.isEmpty()) {
                mProgressbar.setMessage(message);
            }
        }
        return this;
    }

    /**
     * setup the horizontal progressbar which will be showed on screen
     *
     * @return RestfulService object
     */
    public RestfulService setHorizontalProgressbar() {
        mProgressbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressbar.setMax(100);
        mProgressbar.setProgress(0);

        return this;
    }

    /**
     * try to retrieve data from remote server
     *
     * @return data from server which is presented by a string
     * @throws IOException
     */
    protected String sendGet(String path, Map<String, String> params, Map<String, String> header) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, KeyManagementException {
        String query = "";
        if (params != null)
            query = encodeQueryString(params);
        if (!query.isEmpty()) {
            query = "?" + query;
        }
        URL url = new URL(path + query);

        String result = null;

        if (path.startsWith("https:")) {

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            //== add header
            httpsURLConnection.setRequestMethod("GET");
            if (header != null && !header.isEmpty()){
                for (String key : header.keySet()){
                    httpsURLConnection.setRequestProperty(key, header.get(key));
                }
            }

            int responseCode = httpsURLConnection.getResponseCode();

            AppLog.log(getClass().getName(), "sending GET  request to URL: " + url.toString());
            AppLog.log(getClass().getName(), "get parameters: " + params);
            AppLog.log(getClass().getName(), "header parameters: " + header);
            AppLog.log(getClass().getName(), "HTTP code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                AppLog.log(getClass().getName(), "connection is failed");
                return null;
            }

            InputStream in = new BufferedInputStream(httpsURLConnection.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            result = readBufferByLine(bufferedReader);

            bufferedReader.close();
            inputStreamReader.close();
            in.close();
            httpsURLConnection.disconnect();
        } else if(path.startsWith("http:")) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //== add header
            httpURLConnection.setRequestMethod("GET");
            if (header != null && !header.isEmpty()){
                for (String key : header.keySet()){
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }

            int responseCode = httpURLConnection.getResponseCode();

            AppLog.log(getClass().getName(), "sending GET  request to URL: " + url.toString());
            AppLog.log(getClass().getName(), "get parameters: " + query);
            AppLog.log(getClass().getName(), "header parameters: " + header);
            AppLog.log(getClass().getName(), "HTTP code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                AppLog.log(getClass().getName(), "connection is failed");
                return null;
            }

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            result = readBuffer(bufferedReader);

            AppLog.log(getClass().getName(), "response result: " + result);

            bufferedReader.close();
            inputStreamReader.close();
            in.close();
            httpURLConnection.disconnect();
        }

        return result;
    }

    /**
     * try to retrieve data from remote server
     * dump server: http://www.posttestserver.com/
     *
     * @return data from server which is presented by a string
     * @throws IOException
     */
    protected String sendPost(String path, Map<String, String> params, Map<String, String> header) throws IOException {
        URL url = new URL(path);
        String result = null;
        if (path.startsWith("https:")) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            String query = encodeQueryString(params);

            //== add header
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(query.getBytes().length));
            if (header != null && !header.isEmpty()){
                for (String key : header.keySet()){
                    httpsURLConnection.setRequestProperty(key, header.get(key));
                }
            }

            //== set post request
            httpsURLConnection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            dataOutputStream.writeBytes(query);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = httpsURLConnection.getResponseCode();

            AppLog.log(getClass().getName(), "sending POST  request to URL: " + path);
            AppLog.log(getClass().getName(), "post parameters: " + params);
            AppLog.log(getClass().getName(), "header parameters: " + header);
            AppLog.log(getClass().getName(), "HTTP code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                AppLog.log(getClass().getName(), "connection is failed");
                return null;
            }

            InputStream in = new BufferedInputStream(httpsURLConnection.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            result = readBufferByLine(bufferedReader);

            bufferedReader.close();
            inputStreamReader.close();
            in.close();
            httpsURLConnection.disconnect();
        } else if (path.startsWith("http:")) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String query = encodeQueryString(params);

            //== add header
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(query.getBytes().length));
            if (header != null && !header.isEmpty()){
                for (String key : header.keySet()){
                    httpURLConnection.setRequestProperty(key, header.get(key));
                }
            }

            //== set post request
            httpURLConnection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(query);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            AppLog.log(getClass().getName(), "sending POST  request to URL: " + path);
            AppLog.log(getClass().getName(), "post parameters: " + params);
            AppLog.log(getClass().getName(), "header parameters: " + header);
            AppLog.log(getClass().getName(), "HTTP code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                AppLog.log(getClass().getName(), "connection is failed");
                return null;
            }

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            result = readBuffer(bufferedReader);

            //== close streams
            bufferedReader.close();
            inputStreamReader.close();
            in.close();
            httpURLConnection.disconnect();
        }

        return result;
    }

    //http://stackoverflow.com/questions/32353466/multipartentitybuilder-to-send-data-from-android-httpurlconnection
    //http://java-monitor.com/forum/showthread.php?t=4090
    protected String sendPostMultipart(String path, Map<String, String> fileList, Map<String, String> params) throws IOException {
        final int CONNECT_TIMEOUT = 15000;
        final int READ_TIMEOUT = 10000;
        final String CHARSET = "UTF-8";
        final String CRLF = "\r\n";
        final String TWO_HYPHENS = "--";
        final String boundary = "*****";

        HttpURLConnection httpURLConnection;
        DataOutputStream dos;
        String response = "";

        URL url = new URL(path);
        httpURLConnection = (HttpURLConnection) url.openConnection();

        //== add header
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        httpURLConnection.setRequestProperty("Accept-Charset", CHARSET);

        //== set post request
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        httpURLConnection.setReadTimeout(READ_TIMEOUT);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        //Start content wrapper
        dos = new DataOutputStream(httpURLConnection.getOutputStream());

        //==== add form field
        for (String s : params.keySet()) {
            String key = String.valueOf(s);
            String value = params.get(key);
            try {
                dos.writeBytes(TWO_HYPHENS + boundary + CRLF);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + CRLF);
                dos.writeBytes("Content-Type: text/plain; charset=" + CHARSET + CRLF);
                dos.writeBytes(CRLF);
                dos.writeBytes(value + CRLF);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AppLog.log("param: "+ key + " - " + params.get(key));
        }
//        dos.writeBytes(CRLF);
//        dos.flush();

        //== add files field
        for (String s : fileList.keySet()) {
            String key = String.valueOf(s);
            String filePath = fileList.get(key);
            Log.e("uploaded file: ", filePath);
            File uploadFile = new File(filePath);
            if (uploadFile.exists()) {
                dos.writeBytes(TWO_HYPHENS + boundary + CRLF);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + uploadFile.getName() + "\"" + CRLF);
                try {
                    dos.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName()) + CRLF);
                } catch (Exception e) {
                    dos.writeBytes("Content-Type: " + getFileType(uploadFile.getName()) + CRLF);
                    Log.e("File Type", uploadFile.getName()+"");
                }
                dos.writeBytes("Content-Transfer-Encoding: binary" + CRLF);
                dos.writeBytes(CRLF);

                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length;

                while ((length = fStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                }
                dos.writeBytes(CRLF);
                dos.writeBytes(TWO_HYPHENS + boundary + TWO_HYPHENS + CRLF);
                    /* close streams */
                fStream.close();
                AppLog.log("file: " + key + " - " + params.get(key));
            }
        }
        dos.writeBytes(CRLF);
        dos.flush();
        dos.close();

        httpURLConnection.connect();
        int statusCode = 0;
        try {
            httpURLConnection.connect();
            statusCode = httpURLConnection.getResponseCode();
        } catch (EOFException e1) {
//            if (count < 5) {
//                urlConnection.disconnect();
//                count++;
//                String temp = connectToMULTIPART_POST_service(postName);
//                if (temp != null && !temp.equals("")) {
//                    return temp;
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppLog.log(getClass().getName(), "sending POST  request to URL: " + path);
        AppLog.log(getClass().getName(), "post parameters: " + params);

        // 200 represents HTTP OK
        if (statusCode == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            response = readBuffer(bufferedReader);

            //disconnecting
            bufferedReader.close();
            inputStreamReader.close();
            in.close();
            httpURLConnection.disconnect();

        } else {
            AppLog.log(getClass().getName(), "connection is failed");
//            System.out.println(urlConnection.getResponseMessage());
//            inputStream = new BufferedInputStream(urlConnection.getInputStream());
//            strResponse = readStream(inputStream);
        }

        return response;
    }

    /**
     * read json data from buffer
     *
     * @return data which is presented by a string
     * @throws IOException
     */
    private String readBuffer(BufferedReader reader) throws IOException {
        if (reader == null) return null;
        StringBuilder builder = new StringBuilder();
        int tamp;
        while ((tamp = reader.read()) != -1) {
            builder.append((char)tamp);
        }

        return builder.toString();
    }

    /**
     * read xml data from buffer
     *
     * @return data which is presented by a string
     * @throws IOException
     */
    private String readBufferByLine(BufferedReader reader) throws IOException {
        if (reader == null) return null;
        StringBuilder builder = new StringBuilder();
        String tamp;
        while ((tamp = reader.readLine()) != null) {
            builder.append(tamp);
        }

        return builder.toString();
    }

    private String encodeQueryString(Map<String, String> params) throws UnsupportedEncodingException {
        final char PARAMETER_DELIMITER = '&';
        final char PARAMETER_EQUALS_CHAR = '=';

        StringBuilder builder = new StringBuilder();
        if (params != null) {
            boolean firstParameter = true;
            for (String key : params.keySet()) {
                if (!firstParameter) {
                    builder.append(PARAMETER_DELIMITER);
                }
                String value = params.get(key);
                if (value == null){
                    value = "";
                }
                builder.append(key)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(URLEncoder.encode(value, "UTF-8"));

                if (firstParameter)
                    firstParameter = false;
            }
        }

        return builder.toString();
    }

    /**
     * try to parse a string data that received from remote server,
     * retrieve response code then set that value to {@link #mRequestCode}
     * @param data to be parsed
     * @return an object that result from parsing of parser
     * @throws JSONException
     */
    private Object parseResponseData(String data) throws JSONException {
        if (mParser != null && data != null) {
            JSONObject root = new JSONObject(data);
            mResponseCode = root.optInt("returnCode");

            return mParser.parse(mContext.get(), data);

        }else {
            return data;
        }
    }

    /**
     * show notification progress on notification bar. this will show the progress of downloading.
     *
     * @param contentText the message will be showed in the notification
     * @param percent     the percent of downloading progress
     */
    protected void showNotificationProgress(Context context, String contentText, int percent) {
        int notId = 898989;
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(contentText)
                .setProgress(100, percent, false)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notId, notification);
    }

    protected String getTag() {
        return getClass().getName();
    }

    //================ inner classes ===============================================================
    public enum Method {
        GET, POST, MULTIPART
    }

    public interface Callback {
        int UNKNOWN_CODE = 1000;
        int RESPONSE_CODE_SUCCESSFULLY = 10000;
        int RESPONSE_CODE_NOT_FOUND = 11000;
        int RESPONSE_CODE_MISSING_PARAMS = 12000;
        int RESPONSE_CODE_INVALID_PARAMS = 13000;
        int RESPONSE_CODE_INVALID_TIME = 14500;
        int RESPONSE_CODE_INVALID_EMAIL = 16000;
        int RESPONSE_CODE_EMAIL_EXISTED = 17000;
        int RESPONSE_CODE_EMAIL_NOT_ACTIVE = 21500;
        int RESPONSE_CODE_DEACTIVATED_ACCOUNT = 21000;
        int RESPONSE_CODE_USER_MISSING_PROFILE = 31500;
        int RESPONSE_BEFORE_ENROLLED_DAY = 15500;
        int RESPONSE_MEAL_REGISTERED = 41000;


        void onDownloadSuccessfully(Object data, int requestCode, int responseCode);

        void onDownloadFailed(Exception e, int requestCode, int responseCode);
    }

    public class NoInternetConnectionException extends Exception {
        @Override
        public String getMessage() {
            return "No internet connection available";
        }
    }

    private String getFileType(String s) {
        String type = null;

        if(s.endsWith("png")) {
            type = "image/png";
        } else if(s.endsWith("jpg")) {
            type = "image/jpeg";
        }

        return type;
    }
}