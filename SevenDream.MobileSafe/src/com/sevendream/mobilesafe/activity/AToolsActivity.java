package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/*
 * 高级工具activity
 * */
public class AToolsActivity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_atools);
}

//电话号码查询
public void numberAdressQuery(View view) {
	// TODO Auto-generated method stub
	startActivity(new Intent(this,AddressActivity.class));
}

}
