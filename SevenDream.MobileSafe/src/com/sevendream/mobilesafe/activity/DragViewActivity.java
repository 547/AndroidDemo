package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * 修改归属地提示框显示位置的activity
 * @author Administrator
 *
 */
public class DragViewActivity extends BaseSetupActivity {
	
	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrag;
	private	int startX;
	private	int startY;
	private	int winWidth;
	private	int winHeight;
	private	long firstClickTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_drag_view);
		
		
		
		
		findById();
		
		
		
		/**
		 * 控件显示在屏幕上要经过的步骤
		 * 1.onMeasure==测量
		 * 2.onLayout===安放位置
		 * 3.onDraw===绘制
		 */
		int lastX = msp.getInt("lastX", 0);
		int lastY = msp.getInt("lastY", 0);
		
		
		//获取屏幕宽高
	winWidth = getWindowManager().getDefaultDisplay().getWidth();
	winHeight = getWindowManager().getDefaultDisplay().getHeight();
		
	
	//根据记录的位置决定哪个文字提示显示或隐藏
	if (lastY>winHeight/2) {
		tvBottom.setVisibility(View.INVISIBLE);
		tvTop.setVisibility(View.VISIBLE);
	}else {
		tvTop.setVisibility(View.INVISIBLE);
		tvBottom.setVisibility(View.VISIBLE);
	}
		
	
	//ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(), lastY + ivDrag.getHeight());//要将控件显示在指定位置，不能直接用这个方法，因为还没有测量完成
	
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) ivDrag.getLayoutParams();//拿到布局的参数==对象，
			layoutParams.leftMargin = lastX;//设置左边距
			layoutParams.topMargin = lastY;//设置top边距
			ivDrag.setLayoutParams(layoutParams);//重新设置位置
			
			ivDrag.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doubleClick();
				}
			});
			
			
		drag();
		
	}
	
	private void drag() {
		/**
		 * 
		 */
		ivDrag.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
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
					
					//更新左上右下距离
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;
					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;
					
					//判断是否超出屏幕的边界，注意状态栏的高度
					if (l<0 || r>winWidth || t<0 || b>winHeight - ivDrag.getHeight()) {
						break;
					}
					
					if (t>winHeight/2) {
						tvBottom.setVisibility(View.INVISIBLE);
						tvTop.setVisibility(View.VISIBLE);
					}else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					
					
					//更新界面
					ivDrag.layout(l, t, r, b);
					
					//重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//记录坐标点
					msp.edit().putInt("lastX", ivDrag.getLeft()).commit();
					msp.edit().putInt("lastY", ivDrag.getTop()).commit();
					break;

				default:
					break;
				}
				
				return false;//true会拦截事件传递，false就会继续将事件往下传递，拦截了事件，下一个点击事件就不会响应的
			}
		});
		
	}
	/***
	 * 
	 * 谷歌源码==实现多击事件
	 */
	long[] mHits = new long[2];//数组长度表示要点击的次数
	public void doubleClick() { 
		
		
		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		mHits[mHits.length - 1] = SystemClock.uptimeMillis();//uptimeMillis==手机开机后经过的时间，默认开机时间为0,开机后开始计算时间
		if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
//			Toast.makeText(this, "这是2击事件", Toast.LENGTH_SHORT).show();
			
			//把图片居中
			ivDrag.layout(winWidth/2 - ivDrag.getWidth()/2, ivDrag.getTop(), winWidth/2 + ivDrag.getWidth()/2, ivDrag.getBottom());
			//记录坐标点
			msp.edit().putInt("lastX", ivDrag.getLeft()).commit();
			msp.edit().putInt("lastY", ivDrag.getTop()).commit();
		}
		
		
		
//		if (firstClickTime>0) {//发现之前点过一次
//			
//			if (System.currentTimeMillis() - firstClickTime <500){//判断两次点击的间隔时间是否小于0.5，如果是就归为是双击事件
//				Toast.makeText(this, "双击", Toast.LENGTH_SHORT).show();
//				firstClickTime = 0;//重置第一次点击时间
//				return;
//			}
//			
//		}
//		firstClickTime = System.currentTimeMillis();//获取第一次点击的时间
	}
	
	public void findById() {
	 tvTop= (TextView) findViewById(R.id.tv_top);
	 tvBottom = (TextView) findViewById(R.id.tv_bottom);
	 ivDrag = (ImageView) findViewById(R.id.iv_drag);
	}

}
