package jp.anpanman.fanclub.framework.phvtUtils;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Memory Cache Class Management of memory cache on SD Card or RAM
 * 
 * @author PhatVan ヴァン タン　ファット
 * 
 */
public class MemoryCache {
	
	// -------------------------------------------------------------------------------------
	private static final String TAG = "MemoryCache";
	// -------------------------------------------------------------------------------------
	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// -------------------------------------------------------------------------------------
	private long size = 0;// current allocated size
	// -------------------------------------------------------------------------------------
	private long limit = 1000000;// max memory in bytes

	// -------------------------------------------------------------------------------------
	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	// -------------------------------------------------------------------------------------
	public void setLimit(long new_limit) {
		limit = new_limit;
		AppLog.log(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	// -------------------------------------------------------------------------------------
	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			// NullPointerException sometimes happen here
			// http://code.google.com/p/osmdroid/issues/detail?id=78
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	// -------------------------------------------------------------------------------------
	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------------------
	private void checkSize() {
		//App.log(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			//App.log(TAG, "Clean cache. News size " + cache.size());
		}
	}

	// -------------------------------------------------------------------------------------
	public void clear() {
		cache.clear();
	}

	// -------------------------------------------------------------------------------------
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}