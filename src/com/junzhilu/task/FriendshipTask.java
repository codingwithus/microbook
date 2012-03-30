/**
 * 
 */
package com.junzhilu.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.junzhilu.OAuth.OAuth;
import com.junzhilu.beans.UserInfo;
import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.util.DataCenter;

/**
 * @author eureka
 * 
 */
public class FriendshipTask extends HttpAsyncTask {

	private String target_id;
	private UserInfo user;
	private String sina_data;

	public FriendshipTask(ICallBack callBack) {
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
		target_id = (String) params[0];
		if (target_id != null) {
			user = DataCenter.GetInstance().GetUserInfo();
			OAuth auth = new OAuth();
			String url = "http://api.t.sina.com.cn/friendships/show.json";
			ArrayList<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>();
			params2.add(new BasicNameValuePair("source", auth.consumerKey));
			params2.add(new BasicNameValuePair("target_id", target_id));
			HttpResponse response = auth.SignRequest(user.getToken(),
					user.getTokenSecret(), url, params2);
			sina_data = GetSinaData(response);
			if (sina_data != null) {
				try {
					JSONObject data = new JSONObject(sina_data);
					JSONObject json_source = data.getJSONObject("source");
					// JSONObject json_target = data.getJSONObject("target");
					if (json_source != null) {
						boolean following = json_source.getBoolean("following");
						Map<String, Object> tempData = new HashMap<String, Object>();
						tempData.put("name", "FriendshipTask");
						tempData.put("bfollowing", following);
						return tempData;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
