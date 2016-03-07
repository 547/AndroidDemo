package com.sevendream.mobilesafe.view;

import com.sevendream.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
//设置中心的自定义控件
public class setting_item_view extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.sevendream.mobilesafe";
	private TextView tv_setting_item;
	private TextView tv_setting_decs;
	private CheckBox cb_setting_stat;
	private String mtitl;
	private String mdesc_on;
	private String mdesc_off;
	public setting_item_view(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
		
	}
//有style时走这个方法
	public setting_item_view(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		
	}
	//有sttrs(属性)时走这个方法
	public setting_item_view(Context context, AttributeSet attrs) {
		super(context, attrs);
		//根据属性名，获取属性值
		mtitl = attrs.getAttributeValue(NAMESPACE, "settingtitle");
		mdesc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mdesc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
		//初始化布局
		initView();
		
	}
	//有new一个时走这个方法
	public setting_item_view(Context context) {
		super(context);
		initView();
		
	}
	//初化布局
	private void initView() {
		//就是初始化布局时将自定义好的布局文件setting_item。xml配置给当前的setting_item_view
		//this就是当前的布局
		View.inflate(getContext(), R.layout.setting_item, this);
		tv_setting_item = (TextView) findViewById(R.id.tv_setting_item);
		tv_setting_decs = (TextView) findViewById(R.id.tv_setting_decs);
		cb_setting_stat = (CheckBox) findViewById(R.id.cb_setting_stat);
		
		setTitle(mtitl);
	}
	public void setTitle(String settingtitle) {
		
		tv_setting_item.setText(settingtitle);
	}
	
	
	public void setDesc(String desc) {
		
		tv_setting_decs.setText(desc);
	}
	public boolean isChecked() {
		
		return cb_setting_stat.isChecked();
	}
	public void setChecked(boolean check){
		cb_setting_stat.setChecked(check);
		//根据选择的状态，更新文本描述
		if(check){
			setDesc(mdesc_on);
		}else {
			setDesc(mdesc_off);
		}
	}

}
