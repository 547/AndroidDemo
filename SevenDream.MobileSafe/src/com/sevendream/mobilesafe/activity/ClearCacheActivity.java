package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.ClearCache;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ClearCacheActivity extends Activity {

	String totalCache;
	TextView tv_cache;
	TextView clearCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear_cache);
		tv_cache = (TextView) findViewById(R.id.tv_cacheSize);
		clearCache = (TextView) findViewById(R.id.tv_cache);

		showCache();

	}

	private void showCache() {
		// TODO Auto-generated method stub
		// 获取缓存大小
		try {
			totalCache = ClearCache.getTotalCacheSize(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (totalCache.equals("OK")) {
			tv_cache.setText("当前系统缓存很少，无需清理");
			clearCache.setVisibility(View.INVISIBLE);
		} else {
			clearCache.setVisibility(View.VISIBLE);
			tv_cache.setText("当前系统缓存：" + totalCache);
		}

	}

	private void makeToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	// 清理缓存
	public void clearCache(View view) {
		// TODO Auto-generated method stub
		boolean isSeccess = ClearCache.clearAllCache(this);
		if (isSeccess) {

			showCache();
			makeToast("缓存清理完毕");

		}
	}
}
