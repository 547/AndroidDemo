package com.sevendream.mobilesafe.view;

import com.sevendream.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
//设置中心的自定义控件
public class SettingClickView extends RelativeLayout {

	
	private TextView tv_setting_item;
	private TextView tv_setting_decs;
	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context);
		initView();
		
	}
//有style时走这个方法
	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		
	}
	//有sttrs(属性)时走这个方法
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//初始化布局
		initView();
		
	}
	//有new一个时走这个方法
	public SettingClickView(Context context) {
		super(context);
		initView();
		
	}
	//初化布局
	private void initView() {
		//就是初始化布局时将自定义好的布局文件view_setting_click。xml配置给当前的SettingClickView
		//this就是当前的布局
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tv_setting_item = (TextView) findViewById(R.id.tv_setting_item);
		tv_setting_decs = (TextView) findViewById(R.id.tv_setting_decs);
		
		
	
	}
	public void setTitle(String settingtitle) {
		
		tv_setting_item.setText(settingtitle);
	}
	
	
	public void setDesc(String desc) {
		
		tv_setting_decs.setText(desc);
	}
	


}
