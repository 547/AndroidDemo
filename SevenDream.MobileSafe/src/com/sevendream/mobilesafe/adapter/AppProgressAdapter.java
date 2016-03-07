package com.sevendream.mobilesafe.adapter;

/**
 * 
 * 进程管理适配器
 * 
 */
import java.util.List;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.bean.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppProgressAdapter extends BaseAdapter {

	private Context context;
	// 布局加载器
	private LayoutInflater mInflater;
	private List<AppInfo> appInfos;

	// 动态改变appInfos
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}

	public AppProgressAdapter(Context context, List<AppInfo> appInfos) {

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
	public View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * 1.得到控件 2.得到数据 3.绑定数据
		 * 
		 */
		View view = null;

		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.app_progress_item, null);
		}

		// 获取布局控件

		TextView tv_progressname = (TextView) view
				.findViewById(R.id.tv_progressname);
		TextView tv_memsize = (TextView) view.findViewById(R.id.tv_memsize);

		// 获取position位置上的appinfo对象
		AppInfo appInfo = appInfos.get(position);

		tv_progressname.setText(appInfo.getPackagename());
		tv_memsize.setText("进程所占内存：" + appInfo.getMemSize());
		return view;

	}

}
