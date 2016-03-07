package com.sevendream.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.sevendream.mobilesafe.bean.BlackNumberInfo;

import android.R.bool;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

/**
 * 
 * 黑名单的数据库类
 * 
 * @author Administrator
 * 
 */
public class BlackNumberDao {
	private final BlackNumberOpenHelper helper;

	// 构造方法
	public BlackNumberDao(Context content) {
		helper = new BlackNumberOpenHelper(content);
	}

	/**
	 * 像数据库添加数据=电话号码、拦截模式
	 * 
	 * @param number
	 * @param model
	 */
	public boolean add(String number, String mode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);
		long rowid = db.insert("blacknumber", null, contentValues);
		db.close();
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 像数据库删除数据=电话号码
	 * 
	 * @param number
	 * @param model
	 */
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowNumber = db.delete("blacknumber", "number=?",
				new String[] { number });
		db.close();
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 像数据库修改数据=通过电话号码修改拦截模式
	 * 
	 * @param number
	 * @param model
	 */
	public boolean changeNumberModel(String number, String mode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		int rowNumber = db.update("blacknumber", values, "number=?",
				new String[] { number });
		db.close();
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 通过电话号码查找拦截模式
	 * 
	 * @param number
	 * @return
	 */
	public String findNumber(String number) {

		String mode = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;

	}

	/**
	 * 
	 * 查找出所有的黑名单
	 * 
	 * @param number
	 * @return
	 */
	public List<BlackNumberInfo> findAllNumber() {

		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.query("blacknumber", new String[] { "number", "mode" }, null,
						null, null, null, null);
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(3000);
		return blackNumberInfos;

	}

	/**
	 * 
	 * 分页加载数据
	 * 
	 * @param pageNumber
	 *            ==表示当前是哪一页
	 * @param pageSize
	 *            ===表示每一页有多少条数据 limit==限制当前有多少数据 offset==跳过，从第几条开始
	 * @return
	 */
	public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pageSize),
						String.valueOf(pageSize * pageNumber) });
		ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfo.setMode(cursor.getString(0));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}

}
