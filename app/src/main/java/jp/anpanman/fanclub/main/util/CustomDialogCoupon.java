package jp.anpanman.fanclub.main.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.main.R;

public class CustomDialogCoupon extends Dialog implements
        android.view.View.OnClickListener {
    public Activity activity;

    public CustomDialogCoupon(Activity context) {
        super(context);
        this.activity=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_coupon);
        ImageView imgClose = (ImageView) findViewById(R.id.imgClose);
        Button btnOk = (Button) findViewById(R.id.btnOk);
        imgClose.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                dismiss();
                break;
            case R.id.btnOk:
                Toast.makeText(activity, "CLICKED", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}