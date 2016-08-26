package jp.anpanman.fanclub.main.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.main.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomDialogCoupon extends Dialog implements
        android.view.View.OnClickListener {
    public Activity activity;
    private String url;
    private String title;
    private ImageView imgClose;
    private ImageView imgCoupon;
    private Button btnOk;
    private String message;

    public CustomDialogCoupon(Activity context, String url, String title, String message) {
        super(context);
        this.activity = context;
        this.url = url;
        this.title = title;
        this.message = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_coupon);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgCoupon = (ImageView) findViewById(R.id.imgCoupon);
        btnOk = (Button) findViewById(R.id.btnOk);
        imgClose.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity)
                    .load(url)
                    .placeholder(R.drawable.bitmap)
                    .into(imgCoupon);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            case R.id.btnOk:
                if (!TextUtils.isEmpty(url)) {
                    showAlertDialog();
                } else {
                    Toast.makeText(activity, "Link not found", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //Show dialog confirm
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Download");
        builder.setMessage("Do you want to download image?");
        builder.setCancelable(false);
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new ImageLoadTask(url, activity).execute();
                dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static class ImageLoadTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<Context> context;
        private String url;

        public ImageLoadTask(String url, Context context) {
            this.url = url;
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                if (myBitmap != null && this.context.get() != null) {
                    MediaStore.Images.Media.insertImage(context.get().getContentResolver(), myBitmap, "Anpanman", "Image");
                }
                return myBitmap != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean isDownloaded) {
            super.onPostExecute(isDownloaded);
            if (isDownloaded && this.context.get() != null) {
                Toast.makeText(this.context.get(), R.string.download_finish, Toast.LENGTH_SHORT).show();
            }else if (this.context.get() != null){
                Toast.makeText(this.context.get(),"Download fail", Toast.LENGTH_SHORT).show();
            }
        }

    }
}