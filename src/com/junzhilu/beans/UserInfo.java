/**
 * 
 */
package com.junzhilu.beans;

/**
 * @author eureka
 * 
 */
public class UserInfo {

	// private String _userId = "1929353423";
	// private String _userKey = "03911c29bd82f0af4a08b9540d42685a";
	// private String _userSecret = "09529ee95c1407a2d8ad04fd357d53f0";

	private String _userId = "";
	private String _userKey = "";
	private String _userSecret = "";

	public void setUserId(String userId) {
		// TODO Auto-generated method stub
		_userId = userId;
	}

	public void setToken(String userKey) {
		// TODO Auto-generated method stub
		_userKey = userKey;
	}

	public void setTokenSecret(String userSecret) {
		// TODO Auto-generated method stub
		_userSecret = userSecret;
	}

	public String getUserId() {
		// TODO Auto-generated method stub
		return _userId;
	}

	public String getToken() {
		// TODO Auto-generated method stub
		return _userKey;
	}

	public String getTokenSecret() {
		// TODO Auto-generated method stub
		return _userSecret;
	}

}
