package com.sevendream.mobilesafe.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sevendream.mobilesafe.bean.AppInfo;

import android.R.integer;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * 
 * 获取手机应用程序的类
 * @author Administrator
 *
 */
public class AppInfoService {
	
	
	private Context context;
	private PackageManager pm;
	
	public AppInfoService(Context contex) {
		this.context=contex;
		pm=contex.getPackageManager();
		
	}
	/**
	 * 得到手机中所有应用的信息
	 * 
	 */
	
	public List<AppInfo> getAppInfos(){
		
		//创建要返回的集合对象
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		//获取手机中所有安装的应用集合
		List<ApplicationInfo> applicationInfos=pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		//遍历所有的应用集合
		for (ApplicationInfo info:applicationInfos) {
		
			AppInfo appInfo=new AppInfo();
			//获取应用程序的图标
			Drawable app_icon =info.loadIcon(pm);
			appInfo.setApp_icon(app_icon);
			//获取应用的名字
			String app_name=info.loadLabel(pm).toString();
			appInfo.setApp_name(app_name);
			//获取应用的包名
			 String packageName= info.packageName;
			 appInfo.setPackagename(packageName);
			 
			
			//获取应用的版本哈
			try {
				PackageInfo packageInfo=pm.getPackageInfo(packageName, 0);
				String app_verison =packageInfo.versionName;
				appInfo.setApp_verison(app_verison);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//判断应用是否是用户程序
			boolean isUserApp=filterApp(info);
			appInfo.setUserApp(isUserApp);
			appInfos.add(appInfo);
			
			
		}
		
		return appInfos;
		
		
	}
	
	//判断应用是否是用户程序
	public boolean filterApp(ApplicationInfo info) {
		// 原来是系统应用，用户手动升级
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0) {
			return true;
			
			//用户自己安装的应用程序
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM)==0) {
			return true;
		}
		return false;
	}

}
