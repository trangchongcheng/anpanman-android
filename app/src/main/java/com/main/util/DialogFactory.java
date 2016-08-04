package com.main.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

/**
 * Created by linhphan on 8/4/16.
 */
public class DialogFactory {
    private static AlertDialog alertDialog;

    public static void showMessage(Context context, String message) {
        if (context == null) {
            return;
        }

        /**
         * if alert dialog hasn't created
         * or if was host by another context
         * then create new one
         */
        if (alertDialog == null || ((ContextThemeWrapper) alertDialog.getContext()).getBaseContext() != context) {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
            alertDialog = aBuilder.create();

        }
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }
}
