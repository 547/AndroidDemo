package com.sevendream.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

/**
 * 
 * 服务状态工具类
 * @author Administrator
 *
 */
public class ServiceStatusUtils {
	
	/**
	 * 
	 * 检测服务是否正在运行的方法
	 * @return
	 */
	public static Boolean isServiceRunning(Context context,String serviceName) {
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);//获取系统所有正在运行的服务，最多返回一百个
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();//获取正在运行的服务的名称
			System.out.println(className);
			if (className.equals(serviceName)) {
				//如果查到的正在运行的服务名等于要查的服务名，就说明该服务正在运行，就返回true
				return true;
			}
		}
		
		return false;
	}

}
