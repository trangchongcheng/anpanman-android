package jp.anpanman.fanclub.main.util;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.main.R;

import jp.anpanman.fanclub.framework.phvtUtils.AppLog;

/**
 * Created by chientruong on 9/13/16.
 */
public class DrawableZoom {

    public static void zoomImageAnimation(Activity activity, final View imageView){
        final Animation zoomin = AnimationUtils.loadAnimation(activity, R.anim.zoom_in);
        final Animation zoomout = AnimationUtils.loadAnimation(activity, R.anim.zoom_out);
        imageView.requestLayout();
        imageView.setAnimation(zoomin);
        //Listener zoomin animation finish to start zoomout animation
        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.requestLayout();
                imageView.setAnimation(zoomout);
                AppLog.log("animation", "onAnimationEnd: ");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
