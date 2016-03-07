package com.sevendream.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.GestureListener;

public class MobileSafe_setup1Activity extends Activity {

	private LinearLayout ll_stup1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monbliesafe_stup1);
		findbyid();
//利用自定义类GestureListener来实现手势识别----左右滑动
		
		 //setLongClickable是必须的  
		ll_stup1.setLongClickable(true);
		ll_stup1.setOnTouchListener(new mGestureListener(this));//利用自定义类GestureListener来实现手势识别----左右滑动
	}
	//利用自定义类GestureListener来实现手势识别----左右滑动	======这是子类继承自定义类GestureListener
	 /** 
     * 继承GestureListener，重写left和right方法 
     */ 
	public class mGestureListener extends GestureListener {
		public mGestureListener(Context context) {
			super(context);
		}
		@Override
		//向左滑动   重写自定义父类的方法
		public boolean left() {
			 Log.e("test", "向左滑"); 
			startActivity(new Intent(MobileSafe_setup1Activity.this, MobileSafe_setup2Activity.class));
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			finish();
			return super.left();
		}
		//向右滑动  重写自定义父类的方法
		@Override
		public boolean right() {
			Log.e("test", "向右滑");
			Toast.makeText(MobileSafe_setup1Activity.this, "这是第一页，请向左滑动", Toast.LENGTH_SHORT).show();
			
			return super.right();
		}
	}

	private void findbyid() {
		//需要监听左右滑动事件的view ===就是activity_monbliesafe_stup1。xml布局文件的根节点的ID
		ll_stup1 = (LinearLayout) findViewById(R.id.ll_stup1);
		
	}
	
}
