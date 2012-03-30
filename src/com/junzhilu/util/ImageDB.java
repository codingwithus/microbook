/**
 * 
 */
package com.junzhilu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author eureka
 * 
 */
public class ImageDB {
	private final Context mContext;
	private DatabaseHelper mDBHelper = null;
	private String mDBName = null;

	public ImageDB(Context ctx, String dbName) {
		// TODO Auto-generated constructor stub
		mContext = ctx;
		mDBName = dbName;
		mDBHelper = new DatabaseHelper(mContext, mDBName);
	}

	public void Close() {
		if (mDBHelper != null) {
			try {
				mDBHelper.close();
				mDBHelper = null;
			} catch (Exception e) {
				mDBHelper = null;
			}
		}
	}

	public Bitmap getImageInfo(String keyUrl) {
		Bitmap bitmap = null;
		try {
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			String sql = "select * from " + DatabaseHelper.IMAGE_TABLE_NAME
					+ " where _uin='" + MD5.md5_string(keyUrl) + "'"
					+ " order by _id";
			Cursor result = db.rawQuery(sql, null);
			if (result != null) {
				if (result.moveToFirst()) {
					byte[] bytes = result
							.getBlob(result
									.getColumnIndex(DatabaseHelper.CONTENT_COLOUMN_NAME));
					bitmap = BitmapFactory.decodeByteArray(bytes, 0,
							bytes.length);
				}
				result.close();
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public boolean saveImageInfo(String keyUrl, byte[] HeadImageData) {
		try {
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			String sql = "delete from " + DatabaseHelper.IMAGE_TABLE_NAME
					+ " where _id not in ( select _id  from "
					+ DatabaseHelper.IMAGE_TABLE_NAME
					+ " order by _id desc limit 30)";
			db.execSQL(sql);
			ContentValues values = new ContentValues();
			values.put("_uin", MD5.md5_string(keyUrl));
			values.put(DatabaseHelper.CONTENT_COLOUMN_NAME, HeadImageData);
			Boolean ret = db.insert(DatabaseHelper.IMAGE_TABLE_NAME, null,
					values) == -1 ? false : true;
			db.close();
			return ret;

		} catch (Exception e) {
			Log.i("db", e.getMessage());
		}
		return false;

	}

	// public UserInfo GetUserInfo(){
	// try {
	// SQLiteDatabase db = mDBHelper.getWritableDatabase();
	// String sql =
	// "select * from "+DatabaseHelper.USER_TABLE_NAME+" order by _id desc limitc 1";
	// Cursor result = db.rawQuery(sql, null);
	// if (result != null) {
	//
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return null;
	// }
}
