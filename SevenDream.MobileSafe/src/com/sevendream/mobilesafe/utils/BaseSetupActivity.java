package com.sevendream.mobilesafe.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public abstract class BaseSetupActivity extends Activity {

	public SharedPreferences msp;//父类里的对象要是public的子类才可以用

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		msp = getSharedPreferences("config", MODE_PRIVATE);
	}
}
