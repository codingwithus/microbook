package com.junzhilu.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageDownloader {
	private static final int HARD_CACHE_CAPACITY = 0;
	private static final int SOFT_CACHE_CAPACITY = 10;
	private static ImageDownloader inst;

	public static ImageDownloader GetInstance() {
		if (inst == null) {
			inst = new ImageDownloader();

		}
		return inst;
	}

	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(
				LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to
				// soft reference cache
				sSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));

				// Log.i("removeEldestEntry",
				// "Transfer From hard cache to Soft cache");
				return true;
			} else
				return false;
		}
	};
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			SOFT_CACHE_CAPACITY / 2);

	public void Clear() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	public Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(url);
				Log.i("getBitmapFromCache", "gc Soft cache!");
			}
		}

		return null;
	}

	static public Bitmap getBitmapFromCacheOrDB(String url) {
		Bitmap bitmap = null;
		bitmap = ImageDownloader.GetInstance().getBitmapFromCache(url);
		if (bitmap == null) {
			bitmap = DataCenter.GetInstance().getImageInfoFromDB(url);
		}
		return bitmap;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, String imageUrl);
	}
}
