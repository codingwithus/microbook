/**
 * 
 */
package com.junzhilu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.junzhilu.beans.UserInfo;
import com.junzhilu.util.DataCenter;

/**
 * @author eureka
 * 
 */
public class CoverActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cover);

		SharedPreferences settings = getSharedPreferences("microbook", 0);
		String userId = settings.getString("userId", null);
		String userKey = settings.getString("userKey", null);
		String userSecret = settings.getString("userSecret", null);
		if (userId != null & userKey != null & userSecret != null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(userId);
			userInfo.setToken(userKey);
			userInfo.setTokenSecret(userSecret);
			DataCenter.GetInstance().SetUserInfo(userInfo);
			Intent intent = new Intent(this, PageListActivity.class);
			startActivity(intent);
			finish();
		} else {
			Button login = (Button) findViewById(R.id.login);
			login.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(CoverActivity.this,
							SinaAuthActivity.class);
					startActivity(intent);
				}
			});
			login.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (DataCenter.GetInstance().GetBExit()) {
			finish();
		}
	}
}
