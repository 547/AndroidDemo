package com.sevendream.mobilesafe.service;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.activity.BackgroundActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class RocketService extends Service {

	private	int startX;
	private	int startY;
	private	int winWidth;
	private int winHeight;
	private	View view;
	private	WindowManager mWM;
	private	WindowManager.LayoutParams params;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void showRocket() {
		/**
		 * WindowManager可以在其他应用弹出自己的浮窗
		 * //这个需要加权限<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		 * params.type = WindowManager.LayoutParams.TYPE_PHONE;
		 */
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//这个需要加权限<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		
		params.gravity = Gravity.LEFT + Gravity.TOP;//将重心位置设置成左上方，也就是（0，0），从左上方开始，而不是默认的中心位置
		params.setTitle("Toast");
		
		view = View.inflate(this, R.layout.rocket, null);//初始化火箭布局
		//初始化火箭喷火帧动画
		ImageView ivRocket = (ImageView) view.findViewById(R.id.iv_rocket);
		ivRocket.setBackgroundResource(R.drawable.anim_rocket);
		AnimationDrawable anim = (AnimationDrawable) ivRocket.getBackground();
		anim.start();
		
		
		
		mWM.addView(view, params);
		
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
					//发射火箭
					if (params.x + view.getWidth()/2 > winWidth/2 - 30 && params.x + view.getWidth()/2 > winWidth/2 + 30 && params.y + view.getHeight() > winHeight-20 && params.y + view.getHeight() < winHeight) {
						
						sendRocket();//发射火箭
						
						//显示火箭烟雾
						Intent intent = new Intent(RocketService.this,BackgroundActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动一个栈来存放Activity
						startActivity(intent);
						
//						startActivity(new Intent(RocketService.this,BackgroundActivity.class));
					}
					
	
					break;

				default:
					break;
				}
				
				return true;
			}
		});
		
		
	}	
	
	private Handler mHandle = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//更新火箭的位置
			params.y = msg.arg1;
			mWM.updateViewLayout(view, params);
		};
	};
	/**
	 * 
	 * 发射火箭
	 */
	protected void sendRocket() {
		//设置火箭居中
		
		params.x = winWidth/2 - view.getWidth()/2;
		mWM.updateViewLayout(view, params);
		
		
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				int pos = winHeight - params.y;//火箭移动的总距离
				for (int i = 0; i <=10; i++) {
					//等待一段时间再更新火箭位置，用于控制火箭发射速度
					try {
						Thread.sleep(60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int y = pos - pos/10*i;
					
					Message msg = Message.obtain();
					msg.arg1 = y;
					mHandle.sendMessage(msg);
					
					
				}
			}
		}.start();
		
		
		
	}

}
