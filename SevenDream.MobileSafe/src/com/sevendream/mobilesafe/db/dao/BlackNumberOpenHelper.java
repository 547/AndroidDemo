package com.sevendream.mobilesafe.db.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public BlackNumberOpenHelper(Context context) {
		super(context, "safe.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	/**
	 *  创建表
	 *  
	 *  blacknumber==表名
	 *  
	 *  mode==拦截模式（电话拦截、短信拦截、电话+短信拦截）
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
