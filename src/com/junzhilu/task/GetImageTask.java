/**
 * 
 */
package com.junzhilu.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.util.DataCenter;

/**
 * @author eureka
 * 
 */
public class GetImageTask extends HttpAsyncTask {

	private Bitmap bmp = null;
	private String url;

	public GetImageTask(ICallBack callBack) {
		super(callBack);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junzhilu.task.HttpAsyncTask#getHttpRequest(java.lang.Object[])
	 */
	@Override
	protected Map<String, String> getHttpRequest(Object... params) {
		// TODO Auto-generated method stub
		url = (String) params[0];
		try {
			bmp = DataCenter.GetInstance().getImageInfoFromDB(url);
			if (bmp == null) {
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
			DataCenter.GetInstance().saveImageInfoToDB((String) params[0], bmp);
			// ImageDownloader.GetInstance().addBitmapToCache((String)params[0],
			// bmp);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		if (bmp != null) {
			returnMap.put("err", "0");
			returnMap.put("bmp", bmp);
		} else {
			returnMap.put("err", "1");
		}
		returnMap.put("name", "GetImageTask");
		getCallBack().doCallBack(returnMap);
	}
}
