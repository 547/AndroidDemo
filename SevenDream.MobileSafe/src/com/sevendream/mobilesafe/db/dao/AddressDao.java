package com.sevendream.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *号码归属地查询的数据库类
 * @author Administrator
 *
 */
public class AddressDao {

	//数据库的地址必须是data/data路径下的不然不能识别,不然就不可以访问数据库
	//data/data/com.sevendream.mobilesafe(应用的包名)/files
	private static final String PATH = "data/data/com.sevendream.mobilesafe/files/address.db";
	public static String getAddress(String number) {
		
		String address = "未知号码";
		
		//创建一个SQLiteDatabase对象，使用openDatabase方法打开数据库
		/*
		 * path==数据库所在路径
		 * factory===直接用默认的，写null就可以
		 * flags==数据库访问模式===SQLiteDatabase.OPEN_READONLY==只读
		 * */
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		
		/**
		 * 手机号码特点：
		 * 第一位：1
		 * 第二位：3，4，5，6，7，8
		 * +9个数字
		 */
		/**
		 * 正则表达式：
		 * 手机号码正则表达式：^1[3-8]\d{9}$
		 * ^表示开始
		 * ￥表示结束
		 * [3-8]表示第二位数是3-8的任意数字
		 * \d表示0-9的数字
		 * {9}表示出现9次===\d{9}的意思就是0-9的数字在第三位开始出现9次
		 * a{2}==表示a要连续出现2次==aa
		 */
		
		//为保证输入的都是手机号码，就要判断是否与正则表达式匹配==如果匹配就开始查询
		if (number.matches("^1[3-8]\\d{9}$")) {
			//数据库的查询
			Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});
			if (cursor.moveToNext()) {
			address = cursor.getString(0);
		}
		cursor.close();
		
		}else if (number.matches("^\\d+$")) {//保证都是数字===+表示出现一次或者多次
			
			switch (number.length()) {
			case 3:
				address="疑似公共报警电话";
				break;
			case 4:
				address="模拟器";
				break;
			case 5:
				address="疑似服电话";
				break;
				//7、8位的号码就是本地号码
			case 7:
			case 8:
				address="本地电话";
				break;
			default:
				//01087887888
				if (number.startsWith("0") && number.length()>+10) {
					//有些区号是3位，有些区号是4位
					//先查询4位区号的
					Cursor cursor = database.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 4)});
				if (!cursor.moveToNext()) {
					//先关闭cursor，优化应用性能
					cursor.close();
					//查询3位区号
					cursor = database.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 3)});
				if (cursor.moveToNext()) {
					address = cursor.getString(0);
				}
					cursor.close();
				}else {
					address = cursor.getString(0);
				}
				}
				break;
			}
			
		}
		
		database.close();//关闭数据库
		return address;
	}
	
	
}
