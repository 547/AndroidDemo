package com.sevendream.mobilesafe.utils;
/*
 * 
 * 清除缓存和获取缓存的自定义方法
 */
import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.os.Environment;

public class ClearCache {

	//获取缓存总量
	public static String getTotalCacheSize(Context context) throws Exception {

		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize+=getFolderSize(context.getExternalCacheDir());
		}

		return getFormatSize(cacheSize);

	}
	
	//清除所有缓存
	public static boolean clearAllCache(Context context) {
		boolean isSeccuss=deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			isSeccuss=deleteDir(context.getExternalCacheDir());
		}
		return isSeccuss;
		
	}


	//清除缓存
	private static boolean deleteDir(File dir) {
		
		if (dir!=null && dir.isDirectory()) {
			String[] children=dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success =deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			
		}
		return dir.delete();
		
		
	}

	// 获取文件
	private static long getFolderSize(File file) throws Exception {
		// TODO Auto-generated method stub
		long size=0;
		try {
			
			File[] fileList=file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				//如果下面还有文件
				if (fileList[i].isDirectory()) {
					size+=getFolderSize(fileList[i]);
					
				}else {
					size+=fileList[i].length();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return size;
	}
	
	//格式化单位
		private static String getFormatSize(double size) {
			double killByte =size/1024;
			if (killByte<1) {
				return "OK";
			}
			double megaByte=killByte/1024;
			if (megaByte<1) {
				BigDecimal result1=new BigDecimal(Double.toString(killByte));
				return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+"KB";
				
			}
			double gigaByte=megaByte/1024;
			if (gigaByte<1) {
				BigDecimal result2=new BigDecimal(Double.toString(megaByte));
				return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+"MB";
			}
			double teraByte=gigaByte/1024;
			if (teraByte<1) {
				BigDecimal result3=new BigDecimal(Double.toString(gigaByte));
				return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+"GB";
				
			}
			BigDecimal result4=new BigDecimal(Double.toString(teraByte));
			return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+"TB";
			
		}

}
