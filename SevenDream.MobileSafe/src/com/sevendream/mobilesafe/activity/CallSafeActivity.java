package com.sevendream.mobilesafe.activity;

import java.util.List;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.adapter.MyBaseAdapter;
import com.sevendream.mobilesafe.bean.BlackNumberInfo;
import com.sevendream.mobilesafe.db.dao.BlackNumberDao;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 格式化快捷键ctrl+shift+f 通讯卫士Activity
 * 
 * @author Administrator
 * 
 */
public class CallSafeActivity extends Activity {

	private ListView list_view;
	private LinearLayout ll_pb;
	private List<BlackNumberInfo> blackNumberInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}

	private Handler handle = new Handler() {

		public void handleMessage(android.os.Message msg) {
			ll_pb.setVisibility(View.INVISIBLE);
			CallSafeAdapter adapter = new CallSafeAdapter();
			list_view.setAdapter(adapter);
		};
	};

	private void initData() {

		new Thread() {
			public void run() {
				BlackNumberDao dao = new BlackNumberDao(CallSafeActivity.this);
				blackNumberInfos = dao.findAllNumber();

				handle.sendEmptyMessage(0);
			};

		}.start();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		list_view = (ListView) findViewById(R.id.list_view);
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
		ll_pb.setVisibility(View.VISIBLE);
	}

	private class CallSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blackNumberInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return blackNumberInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity.this,
						R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_Number = (TextView) convertView
						.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_mode);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_Number
					.setText(blackNumberInfos.get(position).getNumber());

			String mode = blackNumberInfos.get(position).getMode();
			if (mode.equals("1")) {
				holder.tv_mode.setText("电话+短信拦截");
			} else if (mode.equals("2")) {
				holder.tv_mode.setText("电话拦截");
			} else if (mode.equals("3")) {
				holder.tv_mode.setText("短信拦截");
			}
			return convertView;
		}

	}

	static class ViewHolder {
		TextView tv_Number;
		TextView tv_mode;
	}

}
