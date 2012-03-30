/**
 * 
 */
package com.junzhilu.util;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;

import com.junzhilu.beans.UserInfo;

/**
 * @author eureka
 * 
 */
public class DataCenter {
	private static DataCenter inst;

	public static DataCenter GetInstance() {
		if (inst == null) {
			inst = new DataCenter();

		}
		return inst;
	}

	private Context mContext = null;
	private ImageDB mImageDB;

	public void SetContext(Context _context) {
		mContext = _context;
	}

	// 打开全局数据库
	public boolean OpenGlobalDatabase() {
		if (mContext == null)
			return false;
		if (mImageDB == null) {
			mImageDB = new ImageDB(mContext, "Global.db");
		}
		return true;
	}

	public boolean CloseGlobalDatabase() {
		if (mImageDB != null) {
			mImageDB.Close();
			mImageDB = null;
		}
		return true;
	}

	public Bitmap getImageInfoFromDB(String url) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		if (mImageDB != null)
			bitmap = mImageDB.getImageInfo(url);

		return bitmap;
	}

	public void saveImageInfoToDB(String keyUrl, Bitmap HeadImage) {
		if (mImageDB == null || HeadImage == null)
			return;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HeadImage.compress(Bitmap.CompressFormat.PNG, 100, out);

		mImageDB.saveImageInfo(keyUrl, out.toByteArray());
	}

	public void saveImageInfoToDB(String keyUrl, String filePath) {

		// File file = new File(filePath);
		// Bitmap HeadImage = FileUtil.ReadImageFile(file);
		// this.saveImageInfoToDB(keyUrl, HeadImage);
	}

	// 账号信息
	private UserInfo mUserInfo = null;

	public UserInfo GetUserInfo() {
		return mUserInfo;
	}

	public void SetUserInfo(UserInfo userInfo) {
		mUserInfo = userInfo;
	}

	private Boolean bExit = false;

	public void SetBExit(Boolean bExit) {
		this.bExit = bExit;
	}

	public Boolean GetBExit() {
		return bExit;
	}
}
