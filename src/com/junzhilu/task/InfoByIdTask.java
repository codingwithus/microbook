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
public class InfoByIdTask extends HttpAsyncTask {

	private String user_id;

	public InfoByIdTask(ICallBack callBack) {
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
		HashMap<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("name", "InfoByIdTask");
		user_id = (String) params[0];
		if (user_id != null) {
			OAuth auth = new OAuth();
			UserInfo user = DataCenter.GetInstance().GetUserInfo();
			String url = "http://api.t.sina.com.cn/users/show.json";
			ArrayList<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>();
			params2.add(new BasicNameValuePair("source", auth.consumerKey));
			params2.add(new BasicNameValuePair("user_id", user_id));
			HttpResponse response = auth.SignRequest(user.getToken(),
					user.getTokenSecret(), url, params2);
			String sina_data = GetSinaData(response);
			if (sina_data != null) {
				returnData.put("sina_data", sina_data);
				returnData.put("err", "0");
				return returnData;
			}
		}
		returnData.put("err", "1");
		return returnData;
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
