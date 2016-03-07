package com.sevendream.mobilesafe.service;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.db.dao.AddressDao;

import android.R.color;
import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 来电归属地显示的服务=====一定要记得到清单文件AndroidMainifest文件中注册一下service
 * @author Administrator
 *
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private	MyListener listener;
	private	OutCallReceiver receiver;
	private	WindowManager mWM;
	private	View view;
	private	SharedPreferences msp;
	private	int startX;
	private	int startY;
	private	WindowManager.LayoutParams params;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	//onCreate===表示服务被创建，一定要写的
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		msp = getSharedPreferences("config", MODE_PRIVATE);
		
		
		/**
		 * 监听来电
		 */
		listener = new MyListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//监听来电状态
		
		/***
		 * 
		 * 动态注册广播
		 */
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		
	}
	
	class MyListener extends PhoneStateListener{
		//重写当打电话状态改变的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
		
			switch (state) {
			//电话铃响状态
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("电话铃响");
				String address = AddressDao.getAddress(incomingNumber);//根据打进来的号码查询归属地
				System.out.println(address);
//				Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
				showToast(address);//展示自定义Toast
				
				break;
				//电话拨出状态
//			case TelephonyManager.CALL_STATE_OFFHOOK:
//				System.out.println("OFFHOOK");
//				break;
				//电话闲置状态===没有使用
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("IDLe");
				//当我们自己间的WindowManager==mWM不为空的时候才移除，放在WindowManager上的View
				if (mWM!=null && view!=null) {
					mWM.removeView(view);//从Window中移除View
					view = null;
				}
				
				break;
			default:
				break;
			}
			
			
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	/**
	 * 
	 * 监听去电的广播接收者===Ps:广播要到Mainfest文件注册一下
	 * 以及获取权限 <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
	 * @author Administrator
	 *
	 */
	class OutCallReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			String number = getResultData();//获取去电的号码
			String address = AddressDao.getAddress(number);//查询号码归属地
			System.out.println(address);
//			Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);//展示自定义Toast
		}
		
	}
	
	
	//onDestroy==表示服务要被销毁
	@Override
	public void onDestroy() {
	// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);//关闭来电监听
		
		/**
		 * 注销广播
		 */
		unregisterReceiver(receiver);
	
	}
	
	/**
	 * 自定义一个归属地的浮窗===实质就是自定义Toast
	 * 
	 */
	private void showToast(String text) {
		/**
		 * WindowManager可以在其他应用弹出自己的浮窗
		 * //这个需要加权限<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		 * params.type = WindowManager.LayoutParams.TYPE_PHONE;
		 */
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		final int winWidth = mWM.getDefaultDisplay().getWidth();
		final int winHeight = mWM.getDefaultDisplay().getHeight();
		
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//这个需要加权限<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		
		params.gravity = Gravity.LEFT + Gravity.TOP;//将重心位置设置成左上方，也就是（0，0），从左上方开始，而不是默认的中心位置
		params.setTitle("Toast");
		
		int lastX = msp.getInt("lastX", 0);
		int lastY = msp.getInt("lastY", 0);
		
		//设置浮窗的位置，基于左上方的偏移量
		params.x = lastX;
		params.y = lastY;
		
		view = view.inflate(this, R.layout.toast_address, null);
		
		int[] bgs =new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green,};
		int style = msp.getInt("address_style", 0);
		view.setBackgroundResource(bgs[style]);//根据存储的样式更新背景
		TextView tv_number =(TextView) view.findViewById(R.id.tv_number);		
		tv_number.setText(text);
		mWM.addView(view, params);//将View添加在屏幕上（Window）
		
		/**
		 * 
		 * 让提示框可以拖拽
		 */
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				System.err.println("ontouch=======");
				
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//先获取点坐标=====初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					
					//计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					
					//更新浮窗坐标
					params.x += dx;
					params.y += dy;
					//防止移动时浮窗移出屏幕外
					if (params.x < 0) {
						params.x = 0;
					}
					//防止移动时浮窗移出屏幕外
					if (params.y < 0) {
						params.y = 0;
					}
					//防止移动时浮窗移出屏幕外
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}
					//防止移动时浮窗移出屏幕外
					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}
					//更新浮窗坐标
					mWM.updateViewLayout(view, params);
					
					//获取坐标=====重新获取起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//记录坐标点
					msp.edit().putInt("lastX", params.x).commit();
					msp.edit().putInt("lastY", params.y).commit();
	
					break;

				default:
					break;
				}
				
				return true;
			}
		});
		
		
	}
	
}
