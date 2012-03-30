package com.junzhilu.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.util.HttpUtil;

public class UserTimeLineTask extends HttpAsyncTask {

	public UserTimeLineTask(ICallBack callBack) {
		super(callBack);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Map<String, String> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return super.doInBackground(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Map<String, String> map = (Map<String, String>) result;
		String sina_data = map.get("sina_data");
		if (sina_data == null) {
			return;
		}
		try {
			JSONArray sia_json = new JSONArray(sina_data);
			int size = sia_json.length();
			ArrayList<HashMap<String, Object>> returnData = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < size; i++) {
				HashMap<String, Object> tempData = new HashMap<String, Object>();
				JSONObject jsonObject = (JSONObject) sia_json.opt(i);
				JSONObject retweeted_status;
				if (jsonObject.has("retweeted_status")) {
					retweeted_status = jsonObject
							.getJSONObject("retweeted_status");
				} else {
					retweeted_status = jsonObject;
				}
				String created_at = retweeted_status.getString("created_at");
				// idÎªÎ¢²©id jsonObject.getString("id");
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
			HashMap<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("data", returnData);
			returnMap.put("type", map.get("type"));
			getCallBack().doCallBack(returnMap);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	protected Map<String, String> getHttpRequest(Object... params) {
		// TODO Auto-generated method stub
		if (params[0] != null) {
			String sina_data = HttpUtil.httpClientConn((String) params[1]);
			Map<String, String> tempData = new HashMap<String, String>();
			tempData.put("sina_data", sina_data);
			tempData.put("type", (String) params[0]);
			return tempData;
		}
		return null;
	}
}
