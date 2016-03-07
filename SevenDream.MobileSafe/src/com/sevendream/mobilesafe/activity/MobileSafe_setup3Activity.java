package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.GestureListener;
import com.sevendream.mobilesafe.utils.ToastUntils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MobileSafe_setup3Activity extends BaseSetupActivity {

	private LinearLayout ll_stup3;
	private EditText et_setsafecode;
	
	private String safecode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monbliesafe_stup3);
		findbyid();
		ll_stup3.setLongClickable(true);
		ll_stup3.setOnTouchListener(new mGestureListener(this));
		safecode=msp.getString("safecode", "");
		et_setsafecode.setText(safecode);
		//获取当前手机号码
		TelephonyManager tm = (TelephonyManager)this
				.getSystemService(MobileSafe_setup3Activity.TELEPHONY_SERVICE);
		String currentPhoneNumber=tm.getLine1Number();
		Log.e("当前手机号码：",currentPhoneNumber);
		//保存手机号码
		msp.edit().putString("mPhoneNum", currentPhoneNumber).commit();

	}

	public class mGestureListener extends GestureListener {

		public mGestureListener(Context context) {
			super(context);
		}

		@Override
		// 向左滑动 重写自定义父类的方法
		public boolean left() {
			Log.e("test", "向左滑");
			safecode=et_setsafecode.getText().toString().replace("-", "").trim();
			if (!TextUtils.isEmpty(safecode)) {
				msp.edit().putString("safecode", safecode).commit();
				startActivity(new Intent(MobileSafe_setup3Activity.this,
						MobileSafe_setup4Activity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				finish();
				
			}else {
				ToastUntils.showtoast(MobileSafe_setup3Activity.this,
						"请设置安全号码！");
			}
			
			return super.left();
		}

		// 向右滑动 重写自定义父类的方法
		@Override
		public boolean right() {
			Log.e("test", "向右滑");
			startActivity(new Intent(MobileSafe_setup3Activity.this,
					MobileSafe_setup2Activity.class));
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			finish();
			return super.right();
		}
	}
	public void yes(View v){
		safecode=et_setsafecode.getText().toString().replace("-", "").trim();
		if(!TextUtils.isEmpty(safecode)){
			msp.edit().putString("safecode",safecode).commit();
			ToastUntils.showtoast(MobileSafe_setup3Activity.this, "安全号码设置成功！");
		}else {
			ToastUntils.showtoast(MobileSafe_setup3Activity.this, "请设置安全号码！");
		}
		//获取当前手机号码
				TelephonyManager tm = (TelephonyManager)this
						.getSystemService(MobileSafe_setup3Activity.TELEPHONY_SERVICE);
				String currentPhoneNumber=tm.getLine1Number();
				Log.e("当前手机号码：",currentPhoneNumber);
				//保存手机号码
				msp.edit().putString("mPhoneNum", currentPhoneNumber).commit();

	}

	public void selectpeople(View v) {
		// 跳转到系统联系人界面一定要加<uses-permission
		// android:name="android.permission.READ_CONTACTS"/>权限
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		// intent.setAction(Intent.ACTION_VIEW);
		// 只能用过时的，新的一用就甭
		// intent.setData(Contacts.People.CONTENT_URI);

		MobileSafe_setup3Activity.this.startActivityForResult(intent, 1);

	}
/**
 * 下面这段代码一定要留着；
 * 可以从自己的应用跳转到系统的联系人列表页面
 * 并点击列表里的条目，就可以获取联系人的号码，且将联系人的号码显示在自己的应用的EditText	
 */
	
	
//获取选择的联系人
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (1):
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Uri contactData=data.getData();
				Cursor c=managedQuery(contactData, null, null, null, null);
				c.moveToFirst();
				String phoneNum=this.getcontactPhone(c);
				String phone=phoneNum.replaceAll("-","").replaceAll(" ", "");// 替换-和空格
				et_setsafecode.setText(phone);
			}
		
			break;
		}
		}
	}
	// 获取选择联系人电话
	private String getcontactPhone(Cursor cutsor){
		int phonecolumn=cutsor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum=cutsor.getInt(phonecolumn);
		String phoneResult="";
		if(phoneNum>0){
			//获得联系人的ID号
			int idcolumn=cutsor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId=cutsor.getString(idcolumn);
			//获得联系人的电话号码的cursor
			Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId, null, null);
			if(phones.moveToFirst()){
				//遍历所有的电话号码
				for(;!phones.isAfterLast();phones.moveToNext()){
					int index=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type=phones.getInt(typeindex);
					String phoneNumber=phones.getString(index);
					switch (phone_type) {
					case 2:
						phoneResult=phoneNumber;
						break;

					default:
						break;
					}
				}
				if(!phones.isClosed()){
					phones.close();
				}
			}
		}
		return phoneResult;
	}
/**
 * 
 * 系统联系人的结尾
 */
	
	
	private void findbyid() {
		ll_stup3 = (LinearLayout) findViewById(R.id.ll_stup3);
		et_setsafecode = (EditText) findViewById(R.id.et_setsafecode);
//		Button bt_selectpeople = (Button) findViewById(R.id.bt_selectpeople);
//		Button bt_contactoyes = (Button) findViewById(R.id.bt_contactoyes);

	}
}
