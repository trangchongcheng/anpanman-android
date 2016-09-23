package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;
import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.ui.activity.MainActivity;
import jp.anpanman.fanclub.main.model.MainTabs;
import jp.anpanman.fanclub.main.util.Common;
import jp.anpanman.fanclub.main.util.MyWebViewClient;

/**
 * Created by chientruong on 8/16/16.
 */
public class WebViewFragment extends DialogFragment implements View.OnClickListener {
    private WebView mWebView;
    private ImageView imgClose;
    private ImageView imgNext;
    private ImageView imgPrevious;
    private LinearLayout ll;
    private TextView tvTitle;
    private String mUrl, mTitle;
    private boolean isDetails;
    private static final String URL = "url";
    private static final String TITLE = "title";
    private static final String IS_DETAILS = "is_details";
    private ProgressBar horizontalProgress;
    private DismissCallback callback;


    public static final String TYPE_IMAGE = "image/*";
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final int REQUEST_CODE_FROM_JS = 2;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;;
    public void setCallback(DismissCallback callback) {
        this.callback = callback;
    }

    public static WebViewFragment newInstance(String url, String title, boolean isDetails) {
        WebViewFragment f = new WebViewFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        args.putBoolean(IS_DETAILS, isDetails);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        mUrl = getArguments().getString(URL);
        mTitle = getArguments().getString(TITLE);
        isDetails = getArguments().getBoolean(IS_DETAILS);

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity a = getActivity();
        if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onStop() {
        super.onStop();
        Activity a = getActivity();
        if (MainActivity.currentTab == MainTabs.MyPage) {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_term_use, container, false);
        ll = (LinearLayout) root.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        mWebView = (WebView) root.findViewById(R.id.web_view);
        imgClose = (ImageView) root.findViewById(R.id.imgClose);
        imgClose.setVisibility(View.VISIBLE);
        tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        horizontalProgress = (ProgressBar) root.findViewById(R.id.progressBar2);

        setValue();
        setEvent();
        setupWebView();
        return root;
    }

    public void setValue() {
        if (mTitle != null) {
            tvTitle.setText(mTitle);
        }
    }

    public void setEvent() {
        imgClose.setOnClickListener(this);
//        imgPrevious.setOnClickListener(this);
//        imgNext.setOnClickListener(this);
    }

    private void setupWebView() {

        if (mUrl != null) {
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            //set responsive
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);

            //set zoomable
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);

            //Disable cache Webview
            mWebView.getSettings().setAppCacheEnabled(false);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            mWebView.setWebViewClient(new MyWebViewClient(getActivity()));
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        horizontalProgress.setVisibility(View.GONE);
                    } else {
                        horizontalProgress.setProgress(newProgress);
                        horizontalProgress.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onReceivedTitle(WebView view, String sTitle) {
                    super.onReceivedTitle(view, sTitle);
                    if (sTitle != null && sTitle.length() > 0 && isDetails) {
                        tvTitle.setText(sTitle);
                    } else {
                        tvTitle.setText(mTitle);
                    }
                }

                /*Allow upload file from local to webview
                * openFileChooser : config for each type version API
                * */
                // For Android < 3.0
                public void openFileChooser(ValueCallback<Uri[]> uploadFile) {
                    openFileChooser(uploadFile, "");
                }

                // For 3.0 <= Android < 4.1
                public void openFileChooser(ValueCallback<Uri[]> uploadFile, String acceptType) {
                    openFileChooser(uploadFile, acceptType, "");
                }

                // For 4.1 <= Android < 5.0
                public void openFileChooser(ValueCallback<Uri[]> uploadFile, String acceptType, String capture) {
                    if(mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = uploadFile;

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            AppLog.log("Unable to create Image File", ex.toString());
                        }

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }

                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(TYPE_IMAGE);

                    Intent[] intentArray;
                    if(takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }

                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                    startActivityForResult(chooserIntent,INPUT_FILE_REQUEST_CODE);

                }

                // For Android 5.0+
                @Override
                public boolean onShowFileChooser(WebView webView,
                                                           ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if(mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = filePathCallback;

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            AppLog.log("Unable to create Image File", ex.toString());
                        }

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }

                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(TYPE_IMAGE);

                    Intent[] intentArray;
                    if(takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }

                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                    startActivityForResult(chooserIntent,INPUT_FILE_REQUEST_CODE);

                    return true;
                }
            });
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("x-anp-request", "true");

            //Dont save cache url
            extraHeaders.put("Pragma", "no-cache");
            extraHeaders.put("Cache-Control", "no-cache");
            String objectId = ((AnpanmanApp) getActivity().getApplication()).getUserInfo().getObjectId();
            if (isDetails) {
                mWebView.loadUrl(mUrl, extraHeaders);
            } else {
                mWebView.loadUrl(mUrl + objectId, extraHeaders);
            }
            AppLog.log("Url-click2", mUrl);


        }

    }

    //Listener result return when upload file from webview
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;

            // Check that the response is a good one
            if(resultCode == Activity.RESULT_OK) {
                if(data == null) {
                    // If there is not data, then we may have taken a photo
                    if(mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
            return;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) {
            callback.onDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface DismissCallback {
        void onDismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String imageFileName = "JPEG_ANPANMAN_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }
}
