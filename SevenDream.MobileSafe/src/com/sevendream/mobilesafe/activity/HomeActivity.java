package com.sevendream.mobilesafe.activity;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.MD5Utils;

import android.app.AlertDialog;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends BaseSetupActivity {

	TextView tv_homeitem;
	ImageView iv_homeitem;
	GridView gv_home;

	int[] mimageitem = new int[] { R.drawable.home_apps,
			R.drawable.home_callmsgsafe, R.drawable.home_netmanager,
			R.drawable.home_safe, R.drawable.home_settings,
			R.drawable.home_sysoptimize, R.drawable.home_taskmanager,
			R.drawable.home_tools, R.drawable.home_trojan };
	String[] mtextitem = new String[] { "软件管理", "通讯卫士", "流量统计", "手机防盗", "设置中心",
			"缓存清理", "进程管理", "高级工具", "手机杀毒" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		findbyid();
		gv_home.setAdapter(new HomeAdapter());// class HomeAdapter extends
												// BaseAdapter

		// 设置gridview item点击监听
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// 软件管理
					startActivity(new Intent(HomeActivity.this,
							AppManagerActivity.class));
					break;
				case 1:
					// 通讯
					startActivity(new Intent(HomeActivity.this,
							CallSafeActivity.class));
					break;
				case 3:
					showWhatPassWordDialog();

					break;
				case 4:
					// 4 是String[]
					// mtextitem的position即下标，数组下标从0开始，4是设置中心，点击设置中心跳转到
					// SettingActivity
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;
				case 5:
					// 缓存清理
					startActivity(new Intent(HomeActivity.this,
							ClearCacheActivity.class));
					break;
				case 6:
					// 进程管理
					startActivity(new Intent(HomeActivity.this,
							AppProgressManagerActivity.class));
					break;
				case 7:
					// 高级工具
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;
				default:
					break;
				}

			}

		});
	}

	// 根据是否存过密码进行判断弹出什么对话框----输入密码对话框还是设置密码对话框
	protected void showWhatPassWordDialog() {

		String passwordSaved = msp.getString("password", null);

		if (!TextUtils.isEmpty(passwordSaved)) {
			showInputPassWordDialog();
		} else {
			showSetPassWordDialog();
		}
	}

	// 输入密码对话框
	protected void showInputPassWordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.input_password_dialog, null);
		dialog.setView(view, 0, 0, 0, 0);// 0,0,0,0表示的是上下左右边距，以避免低版本弹出的Dialog有框
		final EditText ed_inputpassword = (EditText) view
				.findViewById(R.id.et_inputpassword);
		Button bt_inputok = (Button) view.findViewById(R.id.bt_inputok);
		Button bt_inputcancel = (Button) view.findViewById(R.id.bt_inputcancel);
		bt_inputok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String inputpassword = ed_inputpassword.getText().toString();
				if (!TextUtils.isEmpty(inputpassword)) {
					String passwordSaved = msp.getString("password", null);
					if (MD5Utils.encode(inputpassword).equals(passwordSaved)) {
						Toast.makeText(HomeActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this,
								MobileSafeActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		bt_inputcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	// 设置密码的对话框
	public void showSetPassWordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.set_password_dialog, null);
		dialog.setView(view, 0, 0, 0, 0);// 0,0,0,0表示的是上下左右边距，以避免低版本弹出的Dialog有框
		final EditText ed_password = (EditText) view
				.findViewById(R.id.et_password);
		final EditText ed_passwordconfirm = (EditText) view
				.findViewById(R.id.et_passwordconfirm);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = ed_password.getText().toString();
				String passwordconfirm = ed_passwordconfirm.getText()
						.toString();
				if (!TextUtils.isEmpty(password)
						&& !TextUtils.isEmpty(passwordconfirm)) {
					if (password.equals(passwordconfirm)) {
						Toast.makeText(HomeActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();
						// 保存密码到SharedPreferences===msp
						msp.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();
						dialog.dismiss();
					} else {
						Toast.makeText(HomeActivity.this, "输入密码不相同",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	private void findbyid() {
		gv_home = (GridView) findViewById(R.id.gv_home);

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mtextitem.length;
		}

		@Override
		public Object getItem(int position) {

			return mtextitem[position];
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_gridview_item, null);
			tv_homeitem = (TextView) view.findViewById(R.id.tv_homeitem);
			iv_homeitem = (ImageView) view.findViewById(R.id.iv_homeitem);
			tv_homeitem.setText(mtextitem[position]);
			iv_homeitem.setBackgroundResource(mimageitem[position]);
			return view;
		}

	};

}
