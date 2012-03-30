/**
 * 
 */
package com.junzhilu.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
public class BlogListTask extends HttpAsyncTask {
	private static final String COUNT = "10";

	public BlogListTask(ICallBack callBack) {
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
		String type = (String) params[0];
		String user_id = (String) params[1];
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("name", "BlogListTask");
		if (type != null) {
			if (user_id != null) {
				UserInfo user = DataCenter.GetInstance().GetUserInfo();
				OAuth auth = new OAuth();
				String url = "http://api.t.sina.com.cn/statuses/user_timeline.json";
				List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
				params1.add(new BasicNameValuePair("source", auth.consumerKey));
				params1.add(new BasicNameValuePair("id", user_id));
				params1.add(new BasicNameValuePair("count", COUNT));
				if (type.equalsIgnoreCase("refresh")) {
					params1.add(new BasicNameValuePair("since_id", String
							.valueOf(params[2])));
				} else if (type.equalsIgnoreCase("more")) {
					params1.add(new BasicNameValuePair("page", String
							.valueOf(params[2])));
				} else {
					params1.add(new BasicNameValuePair("page", String
							.valueOf(params[2])));
				}

				HttpResponse response = auth.SignRequest(user.getToken(),
						user.getTokenSecret(), url, params1);
				String sina_data = GetSinaData(response);
				if (sina_data != null) {
					try {
						JSONArray sia_json = new JSONArray(sina_data);
						int size = sia_json.length();
						ArrayList<HashMap<String, Object>> returnData = new ArrayList<HashMap<String, Object>>();

						for (int i = 0; i < size; i++) {
							HashMap<String, Object> tempData = new HashMap<String, Object>();
							JSONObject jsonObject = (JSONObject) sia_json
									.opt(i);
							JSONObject json_user = jsonObject
									.getJSONObject("user");
							String screen_name = json_user
									.getString("screen_name");
							if (screen_name != null) {
								tempData.put("screen_name", screen_name);
							}
							String profile_image_url = json_user
									.getString("profile_image_url");
							if (screen_name != null) {
								tempData.put("profile_image_url",
										profile_image_url);
							}

							JSONObject retweeted_status;
							if (jsonObject.has("retweeted_status")) {
								retweeted_status = jsonObject
										.getJSONObject("retweeted_status");
							} else {
								retweeted_status = jsonObject;
							}
							String created_at = retweeted_status
									.getString("created_at");
							// idΪ΢��id jsonObject.getString("id");
							String id = jsonObject.getString("id");
							String text = retweeted_status.getString("text");
							tempData.put("created_at", created_at);
							tempData.put("id", id);
							tempData.put("text", text);

							if (retweeted_status.has("original_pic")) {
								String thumbnail_pic = retweeted_status
										.getString("thumbnail_pic");
								String bmiddle_pic = retweeted_status
										.getString("bmiddle_pic");
								String original_pic = retweeted_status
										.getString("original_pic");
								tempData.put("thumbnail_pic", thumbnail_pic);
								tempData.put("bmiddle_pic", bmiddle_pic);
								tempData.put("original_pic", original_pic);
							}
							returnData.add(tempData);
						}
						returnMap.put("data", returnData);
						returnMap.put("type", type);
						returnMap.put("err", "0");
						return returnMap;
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		getCallBack().doCallBack((Map<String, Object>) result);
	}

}
