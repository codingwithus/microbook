package com.junzhilu.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static int DATABASE_VERSION = 1;
	public static String IMAGE_TABLE_NAME = "T_Image";
	public static String CONTENT_COLOUMN_NAME = "_content";

	private static String CREATE_IMAGE_TABLE = "Create Table "
			+ IMAGE_TABLE_NAME + " (_id integer primary key autoincrement"
			+ ",_uin integer not null" + "," + CONTENT_COLOUMN_NAME
			+ " blob not null)";

	// user db
	public static final String USER_TABLE_NAME = "users";
	public static final String USERID = "_userId";
	public static final String USERKEY = "_userKey";
	public static final String USERSECRET = "_userSecret";

	private static String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ USER_TABLE_NAME
			+ " (_id integer primary key autoincrement, _userId varchar, _userKey varchar, _userSecret varchar )";

	DatabaseHelper(Context context, String dbName) {
		super(context, dbName, null, DATABASE_VERSION);
	}

	@Override
	// 数据库创建的时候先调用这个函数，可以在这里初始化
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_IMAGE_TABLE);
			// db.execSQL(CREATE_USER_TABLE);O
		} catch (Exception e) {
			Log.i("db", e.getMessage());
		}
	}

	@Override
	// 数据库升级的时候先调用这个函数
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + IMAGE_TABLE_NAME);
		// db.execSQL("drop table if exists " + USER_TABLE_NAME);
		onCreate(db);
	}
}
