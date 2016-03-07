package com.sevendream.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 监听手机开机的广播
 * 
 * @author Administrator 广播要在清单文件AndroidManifest.xml中先注册receiver和intent-filter
 *         也要加一个权限：android.permission.RECEIVE_BOOT_COMPLETED
 */
public class resetup_receiver extends BroadcastReceiver {

	@Override
	// 一旦接受到广播就会运行此方法
	public void onReceive(Context context, Intent intent) {
		/**
		 * SharedPreferences,MODE_PRIVATE是context里面的，
		 * 在activity能直接用是因为activity本身继承了context
		 * BroadcastReceiver则不是context里的所以需要写成context.getSharedPreferences
		 */

		SharedPreferences msp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String sinnum = msp.getString("sinnum", null);
		if (!TextUtils.isEmpty(sinnum)) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String currentsimnum = tm.getSimSerialNumber();

			if (sinnum.equals(currentsimnum)) {
				Log.e("当前sim卡序列号没有改变:", currentsimnum);
			} else {
				Log.e("当前sim卡序列号已经改变:", currentsimnum);
				String safecode = msp.getString("safecode", "");
				String phoneNumber = msp.getString("mPhoneNum", "");
				/**
				 * 自动发送短信
				 * 注意：需要权限：android.permission.SEND_SMS
				 */
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(safecode, null, "请注意，您手机号为：" + phoneNumber
						+ "的朋友，目前手机里的手机卡已经被换！", null, null);
			}
		}
	}

}
