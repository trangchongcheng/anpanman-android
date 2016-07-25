package com.main.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

/**
 * Created by linhphan on 7/14/16.
 */
public class Common {
    public static void onSslError(Activity activity, WebView view , final SslErrorHandler handler , SslError error){
        if(activity == null){
            return;
        }
        AlertDialog. Builder  builder  =  new  AlertDialog . Builder (activity);
        builder . setMessage ( "ssl証明書が正しくないページですが開いてもいいですか" );
        builder . setPositiveButton ( "yes" ,  new  DialogInterface. OnClickListener ()  {
            @Override
            public  void  onClick ( DialogInterface  dialog ,  int  which )  {
                handler . proceed ();
            }
        });
        builder . setNegativeButton ( "no" ,  new  DialogInterface . OnClickListener ()  {
            @Override
            public  void  onClick ( DialogInterface  dialog ,  int  which )  {
                handler . cancel ();
            }
        });
        builder . setOnKeyListener ( new  DialogInterface . OnKeyListener ()  {
            @Override
            public  boolean  onKey ( DialogInterface  dialog ,  int  keyCode ,  KeyEvent event )  {
                if  ( event . getAction ()  ==  KeyEvent . ACTION_UP  &&  keyCode  ==  KeyEvent . KEYCODE_BACK )  {
                    handler . cancel ();
                    dialog . dismiss ();
                    return  true ;
                }
                return  false ;
            }
        });
        AlertDialog  dialog  =  builder . create ();
        dialog . show ();
    }
}
