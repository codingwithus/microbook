/**
 * 
 */
package com.junzhilu.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import com.junzhilu.OAuth.OAuth;
import com.junzhilu.beans.UserInfo;
import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.util.DataCenter;

/**
 * @author eureka
 * 
 */
public class DiscussTask extends HttpAsyncTask {

	private UserInfo user;
	private String blog_id;

	public DiscussTask(ICallBack callBack) {
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
		// TODO Auto-generated method stubO
		if (params[0] != null) {
			blog_id = (String) params[0];
			// user = new UserInfo();
			user = DataCenter.GetInstance().GetUserInfo();
			OAuth auth = new OAuth();
			String url = "http://api.t.sina.com.cn/statuses/comments.json";
			ArrayList<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>();
			params2.add(new BasicNameValuePair("source", auth.consumerKey));
			params2.add(new BasicNameValuePair("id", blog_id));
			HttpResponse response = auth.SignRequest(user.getToken(),
					user.getTokenSecret(), url, params2);
			if (response != null
					& 200 == response.getStatusLine().getStatusCode()) {
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
					response.getEntity().consumeContent();
					Map<String, String> tempData = new HashMap<String, String>();
					tempData.put("sina_data", sina_data);
					tempData.put("name", "DiscussTask");
					return tempData;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null) {
			getCallBack().doCallBack((Map<String, Object>) result);
		}
	}
}
