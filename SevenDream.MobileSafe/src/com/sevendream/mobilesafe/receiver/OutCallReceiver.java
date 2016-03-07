package com.sevendream.mobilesafe.receiver;

import com.sevendream.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * 
 * 监听去电的广播接收者===Ps:广播要到Mainfest文件注册一下
 * 以及获取权限 <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 * @author Administrator
 *
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String number = getResultData();//获取去电的号码
		String address = AddressDao.getAddress(number);//查询号码归属地
		System.out.println(address);
		Toast.makeText(context, address, Toast.LENGTH_LONG).show();
	}

}
