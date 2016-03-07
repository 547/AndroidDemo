package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 归属地查询页面
 * @author Administrator
 *
 */
public class AddressActivity extends Activity {
	
	private EditText etNumber;
	private TextView tvResult;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_address);
		
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		tvResult.setText("");
		//给文本框加一个监听，当文本框内的字发生变化的时候就自动查询
		etNumber.addTextChangedListener(new TextWatcher() {
			//onTextChanged==文本框正在发生变化时调用
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText("该号码：" + address);
			}
			//beforeTextChanged==文本框发生变化前调用
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			//afterTextChanged==文本框发生变化后调用
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	//开始查询按钮绑定的方法
	public void query(View view) {
		String number = etNumber.getText().toString().trim();
		//判断输入框中是否有字
		if (!TextUtils.isEmpty(number)) {
			String address = AddressDao.getAddress(number);
			tvResult.setText("该号码：" + address);
		}else {
			/**
			 * Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 * findViewById(R.id.pw).startAnimation(shake);
			 * 以上两句代码可以让editText出现颤抖的动画
			 */
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        etNumber.startAnimation(shake);
	        //d调用让手机震动的方法
	        vibrate();
		}
		
		
	}
	
	/**
	 * 让手机震动的方法
	 * PS：让手机震动是要权限的===<uses-permission android:name="android.permission.VIBRATE"/>
	 */
	private void vibrate() {
	
	Vibrator vibrator =	(Vibrator) getSystemService(VIBRATOR_SERVICE);
	//让手机连续震动1秒
//	vibrator.vibrate(1000);	
	/**
	 * 让手机有节奏地震动
	 * vibrate(new long[]{200,300,200,400}, -1);
	 * ===参数1：先等待0.2秒，震动0.3秒，等待0.2秒，震动0.4秒，
	 * ===参数2：为-1表示只执行一次，不循环；为0时表示从头开始循环，一直震动，不停
	 * ===参数2的值是表示从上面的震动时间数组的第几个位置开始循环，当它为1的时候就是从0.3秒开始循环
	 */
	vibrator.vibrate(new long[]{200,300,200,400},-1);
	//取消震动
//	vibrator.cancel();
	}

}
