package com.sevendream.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.GestureListener;
import com.sevendream.mobilesafe.utils.ToastUntils;
import com.sevendream.mobilesafe.view.setting_item_view;

public class MobileSafe_setup2Activity extends BaseSetupActivity {

	private LinearLayout ll_stup2;
	private setting_item_view setting_item_view_bindsim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monbliesafe_stup2);
		findbyid();
		ll_stup2.setLongClickable(true);
		ll_stup2.setOnTouchListener(new mGestureListener(this));
		// 先检查一下SharedPreferences中simnum中有没有值
		final String simnum = msp.getString("simnum", null);
		if (!TextUtils.isEmpty(simnum)) {
			// 有，就将checox设为被选中状态
			setting_item_view_bindsim.setChecked(true);
		} else {
			setting_item_view_bindsim.setChecked(false);
		}
		setting_item_view_bindsim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (setting_item_view_bindsim.isChecked()) {
					setting_item_view_bindsim.setChecked(false);
					msp.edit().remove("simnum").commit();// 删除SharedPreferences中的sim卡的序列号

				} else {
					// 整个setting_item_view_bindsim不选中的时候，checox设置为选中
					setting_item_view_bindsim.setChecked(true);
					// 每一个sim卡都有一唯一的序列号
					// 获取SIM卡的序列号要获取权限：android.permission.READ_PHONE_STATE
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simnum = tm.getSimSerialNumber();
					Log.e("sim序列号：", simnum);
					msp.edit().putString("simnum", simnum).commit();// 保存sim卡的序列号到SharedPreferences中
				}

			}
		});
	}

	public class mGestureListener extends GestureListener {
		public mGestureListener(Context context) {
			super(context);
		}

		@Override
		// 向左滑动 重写自定义父类的方法
		public boolean left() {
			Log.e("test", "向左滑");
			String simnum = msp.getString("simnum", null);
			if (!TextUtils.isEmpty(simnum)) {
				
				startActivity(new Intent(MobileSafe_setup2Activity.this,
						MobileSafe_setup3Activity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				finish();
			} else {
				// 自定义工具类ToastUntils，直接引用它的方法，但要注意要被调用的方法须是public static的
				ToastUntils.showtoast(MobileSafe_setup2Activity.this,
						"请绑定sim卡！");
			}
			return super.left();
		}

		// 向右滑动 重写自定义父类的方法
		@Override
		public boolean right() {
			Log.e("test", "向右滑");
			startActivity(new Intent(MobileSafe_setup2Activity.this,
					MobileSafe_setup1Activity.class));
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			finish();
			return super.right();
		}
	}

	private void findbyid() {
		ll_stup2 = (LinearLayout) findViewById(R.id.ll_stup2);
		setting_item_view_bindsim = (setting_item_view) findViewById(R.id.setting_item_view_bindsim);

	}
}
