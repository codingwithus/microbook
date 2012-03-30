package com.junzhilu.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.util.Log;

import com.junzhilu.interfaces.ICallBack;

/*
 * ������Http�첽��Ļ���
 */
abstract public class HttpAsyncTask extends PreReadTask<Object, Object, Object> {

	private ICallBack callBack = null;

	abstract protected Map getHttpRequest(Object... params);

	public HttpAsyncTask(ICallBack callBack) {
		this.setCallBack(callBack);
	}

	@Override
	protected Map doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Map map = null;
		map = getHttpRequest(params);
		return map;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	public void setCallBack(ICallBack callBack) {
		this.callBack = callBack;
	}

	public ICallBack getCallBack() {
		return callBack;
	}

	public String GetSinaData(HttpResponse response) {
		if (response != null) {
			if (200 == response.getStatusLine().getStatusCode()) {
				try {
					InputStream is = response.getEntity().getContent();
					Reader reader = new BufferedReader(
							new InputStreamReader(is), 4000);
					StringBuilder buffer = new StringBuilder((int) response
							.getEntity().getContentLength());
					try {
						char[] tmp = new char[1024];
						int l;
						while ((l = reader.read(tmp)) != -1) {
							buffer.append(tmp, 0, l);
						}
					} finally {
						reader.close();
					}
					String sina_data = buffer.toString();
					// Log.e("json", "rs:" + string);
					response.getEntity().consumeContent();
					return sina_data;
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("HttpAsyncTask json ", e.toString());
				}
			}

		}
		return null;
	}
}
