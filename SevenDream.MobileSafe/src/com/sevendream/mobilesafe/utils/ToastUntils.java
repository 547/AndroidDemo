package com.sevendream.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUntils extends Toast {

	public ToastUntils(Context context) {
		super(context);
		
	}
	// 自定义工具类ToastUntils，直接引用它的方法，但要注意要被调用的方法须是public static的
	public static void showtoast(Context context,String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

}
