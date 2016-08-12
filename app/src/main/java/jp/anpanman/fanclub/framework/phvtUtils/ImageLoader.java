package jp.anpanman.fanclub.framework.phvtUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.main.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Lazy Loader Image without SD Card
 * Load Image from URL
 *
 * @author PhatVan ヴァン タン　ファット
 */

public class ImageLoader {

    //-------------------------------------------------------------------------------------
    // Default image when data not comple
     final int stub_id= R.mipmap.ic_launcher;
//    final int stub_id = R.drawable.ic_cast_dark;

    //-------------------------------------------------------------------------------------
    // Cache
    MemoryCache memoryCache = new MemoryCache();
    //-------------------------------------------------------------------------------------
    //private boolean scaleBitmap = false;
    //-------------------------------------------------------------------------------------
    //private int scaleBitmapWidth, scaleBitmapHeight = 0;
    //-------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private boolean downSample = true;
    //-------------------------------------------------------------------------------------
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    //-------------------------------------------------------------------------------------
    ExecutorService executorService;


    //-------------------------------------------------------------------------------------
    public ImageLoader(Context context, String folderName) {
        executorService = Executors.newFixedThreadPool(5);
    }

    //-------------------------------------------------------------------------------------
    public void displayImage(String url, ImageView imageView, int idImageDefault) {
        imageViews.put(imageView, url);

        // load bitmap from RAM
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            // load bitmap from HTTP POST
            queuePhoto(url, imageView);
            imageView.setImageResource(idImageDefault);
        }
    }

    //-------------------------------------------------------------------------------------
    public void displayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);

        // load bitmap from RAM
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    //-------------------------------------------------------------------------------------
    public void displayImageFromBitmap(Bitmap bit, ImageView imageView) {
        if (bit != null) {
            imageView.setImageBitmap(bit);
        }
    }

    //-------------------------------------------------------------------------------------
    /*
	 * Added new method to set the bitmap size after downloaded from link.
	 */
//	public void setBitmapSizeParam(int width, int height) {
//		scaleBitmap = true;
//		scaleBitmapWidth = width;
//		scaleBitmapHeight = height;
//	}
//	
//	//-------------------------------------------------------------------------------------
//	public void setBitmapWidthParamOnly(int width) {
//		scaleBitmap = true;
//		scaleBitmapWidth = width;
//	}

    //-------------------------------------------------------------------------------------
	/*
	 * Added new method to enable of disable bitmap down sampling
	 */
    public void setDownSampleBitmap(boolean downSample) {
        this.downSample = downSample;
    }

    //-------------------------------------------------------------------------------------
    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    //-------------------------------------------------------------------------------------
    // Phatvt Fix - Png - Gif - follow DirectLinks

    //-------------------------------------------------------------------------------------
//	Bitmap getBitmap(String url) {
//		Log.i("BITMAP", url);
//		String k = url.substring(url.lastIndexOf('.') + 1).trim();
//		Log.i("BITMAP", "EXT = " + k);
//
//		if ((k.equalsIgnoreCase("png") || k.equalsIgnoreCase("gif") || k.equalsIgnoreCase("jpg")) && k.length() == 3)
//			return getBitmapPng(url);
//		else
//			return getBitmapGif(url);
//	}

    private Bitmap getBitmap(String url) {
        try {

            //App.logUrl("[ImageLoader] Requesting image  with URL : \n" + url);
            if (url != null)
                if (url.isEmpty() || url.length() == 0) {
                    //App.log("'Cause Url Image is empty. So this processing will not request ..." );
                    return null;
                }
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


//-------------------------------------------------------------------------------------
//	private Bitmap getBitmapGif(String param) {
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost(param);
//		HttpResponse response;
//		try {
//			Log.i("BITMAP GIF", param);
//			response = httpclient.execute(httppost);
//			HttpEntity entity = response.getEntity();
//			InputStream is = entity.getContent();
//			Bitmap bmp = BitmapFactory.decodeStream(is);
//			bmp = scaleBitmap(bmp);
//			return bmp;
//		} catch (ClientProtocolException e1) {
//			e1.printStackTrace();
//			return null;
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			return null;
//		}
//	}


//-------------------------------------------------------------------------------------
//	private Bitmap scaleBitmap(Bitmap bitmap) {
//		if (scaleBitmap) {
//			if (scaleBitmapHeight == 0) {
//				bitmap = Bitmap.createScaledBitmap(bitmap, scaleBitmapWidth, bitmap.getHeight(), false);
//			} else {
//				bitmap = Bitmap.createScaledBitmap(bitmap, scaleBitmapWidth, scaleBitmapHeight, false);
//			}
//		}
//		return bitmap;
//	}

    //-------------------------------------------------------------------------------------
    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    //-------------------------------------------------------------------------------------
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    //-------------------------------------------------------------------------------------
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //-------------------------------------------------------------------------------------
    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            // else
            // photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    //-------------------------------------------------------------------------------------
    public void clearCache() {
        memoryCache.clear();
    }

}
