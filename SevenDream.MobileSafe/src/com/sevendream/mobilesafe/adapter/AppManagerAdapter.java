package com.sevendream.mobilesafe.adapter;

/**
 * 
 * APP管理的adapter类
 */
import java.util.List;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.bean.AppInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AppManagerAdapter extends BaseAdapter {

	private Context context;
	// 布局加载器
	private LayoutInflater mInflater;
	private List<AppInfo> appInfos;

	// 动态改变appInfos
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}

	public AppManagerAdapter(Context context, List<AppInfo> appInfos) {

		this.context = context;
		this.appInfos = appInfos;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		/**
		 * 1.得到控件 2.得到数据 3.绑定数据
		 * 
		 */
		View view = null;

		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.appmanager_item, null);
		}

		// 获取布局控件
		ImageView iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
		TextView tv_appnmae = (TextView) view.findViewById(R.id.tv_appname);
		TextView tv_appversion = (TextView) view
				.findViewById(R.id.tv_appversion);
//		Button bt_xiezai = (Button) view.findViewById(R.id.bt_xiezai);
//		bt_xiezai.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				uninstallApp(position);
//			}
//		});

		// 获取position位置上的appinfo对象
		AppInfo appInfo = appInfos.get(position);

		iv_appicon.setImageDrawable(appInfo.getApp_icon());
		tv_appnmae.setText(appInfo.getApp_name());
		tv_appversion.setText(appInfo.getApp_verison());
		return view;

	}
	
	//卸载某一应用
		private void uninstallApp(int position) {
			// TODO Auto-generated method stub
			AppInfo app = appInfos.get(position);
			String packagename = app.getPackagename();
			Uri packageUri=Uri.parse("package:"+packagename);
			Intent intent=new Intent(Intent.ACTION_DELETE,packageUri);
			context.startActivity(intent);
			
			
		}

}
