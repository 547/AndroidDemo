package com.sevendream.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * 
 * 获取的应用基本信息实体类
 * 
 * app的Model类
 * 
 * @author Administrator
 * 
 */
public class AppInfo {
	private Drawable app_icon;
	private String app_name;
	private String app_verison;
	private String packagename;
	private boolean isUserApp;// 是否是用户app
	private String memSize;//进程占用的内存
	private int mPid;//应用的id

	public AppInfo() {
		super();

	}
/**
 * 
 * 进程管理
 * @param memSize
 * @param mPid
 * @param packagename
 */
	public AppInfo(String memSize, int mPid,String packagename) {
		super();
		this.memSize=memSize;
		this.mPid=mPid;		
		this.packagename = packagename;
	}
	public String getMemSize() {
		return memSize;
	}
	public void setMemSize(String memSize) {
		this.memSize = memSize;
	}
	public int getmPid() {
		return mPid;
	}
	public void setmPid(int mPid) {
		this.mPid = mPid;
	}
	
	/**
	 * 
	 * 软件管理
	 * @param app_icon
	 * @param app_name
	 * @param app_verison
	 * @param packagename
	 */
	
	public AppInfo(Drawable app_icon, String app_name, String app_verison,
			String packagename) {
		super();
		this.app_icon = app_icon;
		this.app_name = app_name;
		this.app_verison = app_verison;
		this.packagename = packagename;

	}

	public AppInfo(Drawable app_icon, String app_name, String app_verison,
			String packagename, boolean isUserApp) {
		super();
		this.app_icon = app_icon;
		this.app_name = app_name;
		this.app_verison = app_verison;
		this.packagename = packagename;
		this.isUserApp = isUserApp;

	}

	public Drawable getApp_icon() {
		return app_icon;
	}

	public void setApp_icon(Drawable app_icon) {
		this.app_icon = app_icon;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getApp_verison() {
		return app_verison;
	}

	public void setApp_verison(String app_verison) {
		this.app_verison = app_verison;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public String toString() {
		return "AppInfo[app_icon=" + app_icon + ",app_name=" + app_name
				+ ",app_verison=" + app_verison + ",packagename=" + packagename
				+ ",isUserApp=" + isUserApp + "]";

	}

}
