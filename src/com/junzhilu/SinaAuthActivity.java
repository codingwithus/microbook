/**
 * 
 */
package com.junzhilu;

import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.junzhilu.beans.UserInfo;
import com.junzhilu.util.DataCenter;

/**
 * @author eureka
 * 
 */
public class SinaAuthActivity extends Activity {
	private String authUrl = null;
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private DefaultOAuthProvider httpOauthprovider;
	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sinaauth);

		authUrl = getAuthUrl();
		if (authUrl == null) {
			Log.i("micro book", "authUrl is null");
			finish();
		}
		wv = (WebView) findViewById(R.id.web);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		wv.loadUrl(authUrl);
		wv.addJavascriptInterface(new JavaScriptInterface(), "Methods");
		WebViewClient wvc = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (url.equals("http://api.t.sina.com.cn/oauth/authorize")) {
					wv.setVisibility(View.INVISIBLE);
					view.loadUrl("javascript:window.Methods.getHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				}
				super.onPageFinished(view, url);
			}
		};
		wv.setWebViewClient(wvc);
	}

	private String getAuthUrl() {
		// TODO Auto-generated method stub
		String consumerKey = "2433105834";
		String consumerSecret = "3adde77b9dcf3a669882620b226c52ec";
		String authUrl = null;

		httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);
		httpOauthprovider = new DefaultOAuthProvider(
				"http://api.t.sina.com.cn/oauth/request_token",
				"http://api.t.sina.com.cn/oauth/access_token",
				"http://api.t.sina.com.cn/oauth/authorize");
		try {
			authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer,
					"");
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authUrl;
	}

	public String getPin(String html) {
		String ret = "";
		String regEx = "[0-9]{6}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		boolean result = m.find();
		if (result) {
			ret = m.group(0);
		}
		return ret;
	}

	class JavaScriptInterface {
		public void getHTML(String html) {
			String pin = getPin(html);
			// ����ͻ�ȡ����������Ҫ��pin��
			// ���pin�����oauth_verifierֵ,������һ����ȡAccess Token��Access
			// Secret��
			Log.e("micro book : pin ", pin);
			String err = "";
			if (pin.equalsIgnoreCase("")) {
				// err="��¼������������";
				// Toast toast = Toast.makeText(getApplicationContext(),
				// err, Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.CENTER, 0, 0);
				// toast.show();
			} else {
				try {
					httpOauthprovider.setOAuth10a(true);
					httpOauthprovider.retrieveAccessToken(httpOauthConsumer,
							pin);
					SortedSet<String> user_id = httpOauthprovider
							.getResponseParameters().get("user_id");
					String userId = user_id.first();
					String userKey = httpOauthConsumer.getToken();
					String userSecret = httpOauthConsumer.getTokenSecret();

					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(userId);
					userInfo.setToken(userKey);
					userInfo.setTokenSecret(userSecret);
					DataCenter.GetInstance().SetUserInfo(userInfo);

					SharedPreferences settings = getSharedPreferences(
							"microbook", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("userId", userId);
					editor.putString("userKey", userKey);
					editor.putString("userSecret", userSecret);
					editor.commit();

					Intent intent = new Intent(SinaAuthActivity.this, PageListActivity.class);
					startActivity(intent);

					SinaAuthActivity.this.finish();
					return;
				} catch (OAuthMessageSignerException ex) {
					ex.printStackTrace();
					err = ex.toString();
				} catch (OAuthNotAuthorizedException ex) {
					ex.printStackTrace();
					err = ex.toString();
				} catch (OAuthExpectationFailedException ex) {
					ex.printStackTrace();
					err = ex.toString();
				} catch (OAuthCommunicationException ex) {
					ex.printStackTrace();
					err = ex.toString();

				}
				Toast toast = Toast.makeText(getApplicationContext(),
						err, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				SinaAuthActivity.this.finish();
			}

		}
	}
}
