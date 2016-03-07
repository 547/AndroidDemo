package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.receiver.AdminReceiver;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.GestureListener;
import com.sevendream.mobilesafe.utils.ToastUntils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MobileSafe_setup4Activity extends BaseSetupActivity {

	private LinearLayout ll_stup4;
	private CheckBox cb_protectionswitch;
	private TextView tv_protectionoff;
	private Boolean protectionOnOrOff;
	
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	private Button bt_setPolicyManage;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monbliesafe_stup4);
		
		findbyid();
		mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
		protectionOnOrOff = msp.getBoolean("protectionOnOrOff", false);
		if(protectionOnOrOff){
			tv_protectionoff.setText(R.string.protectionon);
			cb_protectionswitch.setChecked(true);
		}else {
			tv_protectionoff.setText(R.string.protectionoff);
			cb_protectionswitch.setChecked(false);
		}
		// CheckBox cb_protectionswitch设置监听
		cb_protectionswitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					tv_protectionoff.setText(R.string.protectionon);
					msp.edit().putBoolean("protectionOnOrOff", true).commit();	
				}else {
					tv_protectionoff.setText(R.string.protectionoff);
					msp.edit().putBoolean("protectionOnOrOff", false).commit();
				}
				
			}
		});
		
		
		
		ll_stup4.setLongClickable(true);
		ll_stup4.setOnTouchListener(new mGestureListener(this));
		
	}
	public class mGestureListener extends GestureListener {
		public mGestureListener(Context context) {
			super(context);
		}
		@Override
		//向左滑动   重写自定义父类的方法
		public boolean left() {
			 Log.e("test", "向左滑");
			protectionOnOrOff = msp.getBoolean("protectionOnOrOff", false);
			if(protectionOnOrOff){
				 Toast.makeText(MobileSafe_setup4Activity.this, "恭喜，您已经设置完成，现在您的爱机将启动安全模式", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(MobileSafe_setup4Activity.this, MobileSafeActivity.class));
					msp.edit().putBoolean("configed", true).commit();//父类里的对象要是public的子类才可以用
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
					finish();
			}else {
				ToastUntils.showtoast(MobileSafe_setup4Activity.this, "开启防盗保护，能更好地保护您的爱机哦");
				startActivity(new Intent(MobileSafe_setup4Activity.this, MobileSafeActivity.class));
				msp.edit().putBoolean("configed", true).commit();//父类里的对象要是public的子类才可以用
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				finish();
			}
			
			
			return super.left();
		}
		//向右滑动  重写自定义父类的方法
		@Override
		public boolean right() {
			Log.e("test", "向右滑");
			startActivity(new Intent(MobileSafe_setup4Activity.this, MobileSafe_setup3Activity.class));
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			finish();
			return super.right();
		}
	}
	private void findbyid() {
		ll_stup4 = (LinearLayout) findViewById(R.id.ll_stup4);
		cb_protectionswitch = (CheckBox) findViewById(R.id.cb_protectionswitch);
		bt_setPolicyManage = (Button) findViewById(R.id.bt_setPolicyManage);
		tv_protectionoff = (TextView) findViewById(R.id.tv_protectionoff);
		
	}
	public void setPolicyManage(View view) {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"哈哈哈, 我们有了超级设备管理器, 好NB!");
		startActivity(intent);
		Log.e("4444", "4444");
	}
//	
//	public void setChecked(boolean check){
//		cb_protectionswitch.setChecked(check);
//		//根据选择的状态，更新文本描述
//		if(check){
//			tv_protectionoff.setText(R.string.protectionon);
//			msp.edit().putBoolean("protectionOnOrOff", true).commit();
//		}else {
//			tv_protectionoff.setText(R.string.protectionoff);
//			msp.edit().putBoolean("protectionOnOrOff", false).commit();
//		}
//	}
}
