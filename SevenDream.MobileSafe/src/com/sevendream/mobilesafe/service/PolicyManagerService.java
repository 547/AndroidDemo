package com.sevendream.mobilesafe.service;

import com.sevendream.mobilesafe.receiver.AdminReceiver;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class PolicyManagerService extends Service {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate() {
		super.onCreate();
		mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
		
	}
	//激活设备管理器
	public void activeAdmin() {
		Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "已经激活设备管理器");
		startActivity(intent);	
	}
	//一键锁屏
	public void lockScreen(String newPassWord) {
		if(mDPM.isAdminActive(mDeviceAdminSample)){
			mDPM.lockNow();
			mDPM.resetPassword(newPassWord, 0);
		}else {
			this.activeAdmin();
			this.lockScreen(null);
		}
		
	}
	//清除数据
	public void clearData() {
		if(mDPM.isAdminActive(mDeviceAdminSample)){
			mDPM.wipeData(0);	
		}else {
			this.activeAdmin();
			this.clearData();
		}
		
	}
	//卸载程序
	public void unInstall() {
	mDPM.removeActiveAdmin(mDeviceAdminSample);
	Intent intent =new Intent(Intent.ACTION_VIEW);
	intent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.setData(Uri.parse("package:"+getPackageName()));
	startActivity(intent);
	}
	

}
