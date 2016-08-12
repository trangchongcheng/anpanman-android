package jp.anpanman.fanclub.framework.phvtUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File Utilites
 *
 * @author thaonp & phatvan
 */
public class FileUtil {
    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Logcat TAG
     */
    public static final String TAG = FileUtil.class.getName();
    //-------------------------------------------------------------------------------------------------------------------
    /**
     * IO buffer size about 8MB
     */
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Private constructor to prevent instantiating this class.
     */
    private FileUtil() {

    }

    ;
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Is external storage removable
     *
     * @return
     */
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Get external cache directory
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        //Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Has external cache directory
     *
     * @return
     */
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = context.getCacheDir().getPath();
        String externalStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(externalStorageState) || !FileUtil.isExternalStorageRemovable()) {
            cachePath = FileUtil.getExternalCacheDir(context).getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Save photo
     *
     * @param bitmap
     */
    public static String saveImageToExternalStorage(Context context, Bitmap bitmap, String folderName, String fileName) {
        FileOutputStream outStream = null;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyApp", "failed to create directory");
            }
        }
        String path = mediaStorageDir.getPath() + File.separator + fileName + ".jpg";
        File mediaFile = new File(path);
        try {
            outStream = new FileOutputStream(mediaFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

//			MediaScannerConnection.scanFile(context, new String[]{mediaFile.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//				public void onScanCompleted(String path, Uri uri) {
//					Log.i("ExternalStorage", "Scanned " + path + ":");
//					Log.i("ExternalStorage", "-> uri=" + uri);
//				}
//			});

            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Delete File from External Storage
     *
     * @param fileName
     */
    public static void deleteFromExternalStorage(String folderName, String fileName) {
        String fullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + folderName + File.separator + fileName + ".jpg";
        try {
            File file = new File(fullPath);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            Log.e("App", "Exception while deleting file " + e.getMessage());
        }
    }
    // --------------------------------------------------------------------------------------------------------------------

    /**
     * Load Image from External Storage
     */
    public static Bitmap loadImageFromExternalStorage(String folderName, String fileName) {
        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + folderName + File.separator + fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(file_path);
        return bitmap;
    }

    // --------------------------------------------------------------------------------------------------------------------
    public static File createImageFile(String imageFileName) throws IOException {
        // Create an image file name
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        String path = storageDir + File.separator + imageFileName + ".jpg";
        File image = new File(path);
        return image;
    }
}
