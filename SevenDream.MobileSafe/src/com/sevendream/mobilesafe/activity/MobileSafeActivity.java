package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileSafeActivity extends BaseSetupActivity {

	// BaseSetupActivity是自己写的Activity父类

	private String safecode;
	private TextView tv_safecodenum;
	private ImageView iv_lock;
	private Boolean protectionOnOrOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		boolean configed = msp.getBoolean("configed", false);

		if (configed) {
			setContentView(R.layout.activity_mobilesafe);
			tv_safecodenum = (TextView) findViewById(R.id.tv_safecodenum);//必须写在这里面不然会崩
			safecode = msp.getString("safecode", "");
			Log.e("安全号码：", safecode);
			tv_safecodenum.setText(safecode);
			
			iv_lock = (ImageView) findViewById(R.id.iv_lock);
			
			protectionOnOrOff = msp.getBoolean("protectionOnOrOff", false);
			//if(protectionOnOrOff)===if(true)
			if(protectionOnOrOff){
				iv_lock.setBackgroundResource(R.drawable.lock);
			}else {
				iv_lock.setBackgroundResource(R.drawable.unlock);
			}
			
		} else {
			startActivity(new Intent(MobileSafeActivity.this,
					MobileSafe_setup1Activity.class));
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			finish();
		}

	}

	

	public void reenterstup(View view) {
		startActivity(new Intent(MobileSafeActivity.this,
				MobileSafe_setup1Activity.class));
		finish();
	}

}
