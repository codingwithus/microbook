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
import android.view.View;
import android.widget.ImageView;

import com.junzhilu.util.ImageDownloader;
import com.junzhilu.util.Util;

/**
 * @author eureka
 * 
 */
public class ImageViewTask extends AsyncTask {

	private View view;
	private String url;

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated mOethod stub
		url = (String) params[0];
		this.view = (View) params[1];
		Bitmap bmp = null;
		if (url != null) {
			try {
				bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-gOenerated catch block
				e.printStackTrace();
			}
			if (bmp != null) {
				bmp = Util.getRoundedCornerBitmap(bmp, 10);
				// DataCenter.GetInstance().saveImageInfoToDB(url, bmp);
				ImageDownloader.GetInstance().addBitmapToCache(url, bmp);
			}
		}

		return bmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junzhilu.task.HttpAsyncTask#getHttpRequest(java.lang.Object[])
	 */
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		if (result != null) {
			ImageView imageview;
			imageview = (ImageView) view.findViewWithTag(url);
			if (imageview != null) {
				imageview.setImageBitmap(null);
				imageview.setImageBitmap((Bitmap) result);
			}
		}
	}
}
