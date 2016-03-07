package com.sevendream.mobilesafe.activity;

import javax.crypto.spec.IvParameterSpec;

import com.sevendream.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
/**
 * 
 * 火箭烟雾的背景
 * @author Administrator
 *
 */
public class BackgroundActivity extends Activity {
	
	private ImageView ivBottom;
	private ImageView ivTop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_bg);
		
		findById();
		
		//渐变动画
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);
		anim.setFillAfter(true);//动画结束后保持状态，不要恢复原状
		
		//运行动画
		ivBottom.startAnimation(anim);
		ivTop.startAnimation(anim);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
				
			}
		}, 1000);//延迟1秒钟后，结束finish Activity
		
	}

	private void findById() {
		// TODO Auto-generated method stub
	ivBottom = (ImageView) findViewById(R.id.iv_bottom);
	ivTop = (ImageView) findViewById(R.id.iv_top);
	}

}
