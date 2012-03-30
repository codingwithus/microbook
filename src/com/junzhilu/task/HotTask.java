/**
 * 
 */
package com.junzhilu.task;

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
public class HotTask extends HttpAsyncTask {

	private String type;
	private UserInfo user;

	public HotTask(ICallBack callBack) {
		super(callBack);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junzhilu.task.HttpAsyncTask#getHttpRequest(java.lang.Object[])
	 */
	@Override
	protected Map getHttpRequest(Object... params) {
		// TODO Auto-generated method stub
		type = (String) params[0];
		HashMap<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("type", type);
		returnData.put("name", "HotTask");
		try {
			user = DataCenter.GetInstance().GetUserInfo();
			OAuth auth = new OAuth();
			String url = "http://api.t.sina.com.cn/users/hot.json";
			ArrayList<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>();
			params2.add(new BasicNameValuePair("source", auth.consumerKey));
			params2.add(new BasicNameValuePair("category", type));
			HttpResponse response = auth.SignRequest(user.getToken(),
					user.getTokenSecret(), url, params2);
			String sina_data = GetSinaData(response);
			if (sina_data != null) {
				returnData.put("sina_data", sina_data);
				return returnData;
			}

		} catch (Exception e) {
			// TODO: handle exception
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
