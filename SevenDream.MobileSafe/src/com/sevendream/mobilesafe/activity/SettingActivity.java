package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.service.AddressService;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.ServiceStatusUtils;
import com.sevendream.mobilesafe.view.SettingClickView;
import com.sevendream.mobilesafe.view.setting_item_view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends BaseSetupActivity {

	private setting_item_view setting_item_view_aotoUpdate;
	private	setting_item_view sivAddress;
	private	SettingClickView scvAddressStyle;//修改来去电的风格
	private	SettingClickView scvAddressLocation;//修改来去电提示框的位置
	

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		findbyid();
		
		initUpdateView();
		initAddressView();
		
		initAddressStyle();
		initAddressLocation();

	}
	/**
	 * 初始化自动更新开关
	 * 
	 */
	private void initUpdateView() {
		Boolean autoUpdate = msp.getBoolean("autoUpdate", true);

		if (autoUpdate) {
			setting_item_view_aotoUpdate.setChecked(true);
		} else {
			setting_item_view_aotoUpdate.setChecked(false);
		}

		setting_item_view_aotoUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (setting_item_view_aotoUpdate.isChecked()) {
					setting_item_view_aotoUpdate.setChecked(false);
					msp.edit().putBoolean("autoUpdate", false).commit();
				} else {
					setting_item_view_aotoUpdate.setChecked(true);
					msp.edit().putBoolean("autoUpdate", true).commit();
				}

			}
		});
	}
	
	
/**
 * 初始化号码归属地开关
 * 
 */
	private void initAddressView(){
		
		Boolean openAddress =msp.getBoolean("openAddress", true);
		if (openAddress) {
			System.out.println("1111111111");
			sivAddress.setChecked(true);
			startService(new Intent(SettingActivity.this,AddressService.class));
		} else {
			System.out.println("0000000000");
			sivAddress.setChecked(false);
			stopService(new Intent(SettingActivity.this,AddressService.class));
		}
		//根据归属地服务是否正在运行显示勾选状态
		Boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,"com.sevendream.mobilesafe.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);		
		}else {
			sivAddress.setChecked(false);		
		}
		
		
		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					System.out.println("222222222");
					//停止 来电归属地显示的服务
					stopService(new Intent(SettingActivity.this,AddressService.class));
					msp.edit().putBoolean("openAddress", false).commit();
				}else {
					sivAddress.setChecked(true);
					System.out.println("333333333");
					//开启 来电归属地显示的服务
					startService(new Intent(SettingActivity.this,AddressService.class));
					msp.edit().putBoolean("openAddress", true).commit();
				}
			}
		});
		
	}
	
	
	/**
	 * 
	 * 修改来、去电显示框的风格
	 */
	private void initAddressStyle() {
		int style = msp.getInt("address_style", 0);//读取保存的风格
		scvAddressStyle.setDesc(items[style]);
		scvAddressStyle.setTitle("归属地提示框风格");
		
		//设置单击事件
		scvAddressStyle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChooseDialog();//展示一个单选框的方法
			}
		});
		
	}
	/**
	 * 弹出一个选择风格的单选框
	 * 
	 */
	final String[] items = new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	protected void showSingleChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);//设置LOGO
		builder.setTitle("归属地提示框风格");
		
		int style = msp.getInt("address_style", 0);//读取保存的风格
		
		
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				msp.edit().putInt("address_style", which).commit();//保存选择的风格
				dialog.dismiss();//取消展示，让Dialog消失
				scvAddressStyle.setDesc(items[which]);//更新组合控件的描述文字
			}
		});
		
		builder.setNegativeButton("取消",null);
		builder.show();
		
	}
	
	/**
	 * 修改归属地显示位置
	 * 
	 */
	private void initAddressLocation() {
		
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setDesc("设置归属地提示框的显示位置");
		scvAddressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
		
		
	}
	
	private void findbyid() {
		setting_item_view_aotoUpdate = (setting_item_view) findViewById(R.id.setting_item_view_aotoUpdate);
		sivAddress = (setting_item_view) findViewById(R.id.siv_address);
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
	}

}
