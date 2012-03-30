/**
 * 
 */
package com.junzhilu.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.junzhilu.util.ImageDownloader;

/**
 * @author eureka
 * 
 */
public class ImageDownloadTask extends AsyncTask<Object, Object, Bitmap> {
	private String url;
	private ImageView view;

	@Override
	protected Bitmap doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		url = (String) params[0];
		view = (ImageView) params[1];
		try {
			bmp = ImageDownloader.GetInstance().getBitmapFromCache(url);
			if (bmp != null) {
				return bmp;
			} else {
				bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// º”»Îª∫¥Ê
		if (bmp != null) {
			ImageDownloader.GetInstance().addBitmapToCache((String) params[0],
					bmp);
		}
		return bmp;
	}

	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			String kk = (String) view.getTag();
			if (!result.isRecycled() & url.equalsIgnoreCase(kk)) {
				view.setImageBitmap(result);
			}
		}
	}
}
