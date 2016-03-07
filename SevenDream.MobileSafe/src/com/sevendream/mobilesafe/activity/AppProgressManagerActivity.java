package com.sevendream.mobilesafe.activity;

/**
 * 进程管理界面
 * 
 */
import java.util.ArrayList;
import java.util.List;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.adapter.AppProgressAdapter;
import com.sevendream.mobilesafe.bean.AppInfo;
import com.sevendream.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppProgressManagerActivity extends Activity {

	// 手机应用Modell
	private List<AppInfo> appInfos;
	// appManagerAdaper适配器对象
	private AppProgressAdapter mAdapter;
	// mHandler方法
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				System.err.println("lssls");
				// 给listView绑定数据，影藏加载的控件

				ll_appprogresspb.setVisibility(View.GONE);
				mAdapter = new AppProgressAdapter(getApplicationContext(),
						appInfos);
				// 设置数据
				list_progressmanager.setAdapter(mAdapter);
				break;

			default:
				break;
			}

		};

	};
	private int proNum;
	private TextView tv_progressname;
	private TextView tv_memsize;
	private TextView tv_mem;
	private TextView tv_pronum;
	private ListView list_progressmanager;
	private LinearLayout ll_appprogresspb;
	private Button bt_updata;
	private Button bt_clear;
	private ActivityManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_app_progressmanager);

		initUI();
	}

	private void initUI() {
		bt_updata = (Button) findViewById(R.id.bt_updata);
		bt_updata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getRunningProgress();
				updateMemInfo();
				makeToast("已刷新");

			}

		});
		bt_clear = (Button) findViewById(R.id.bt_clear);
		bt_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (ActivityManager.RunningAppProcessInfo app : am
						.getRunningAppProcesses()) {
					if (!app.processName.equals("com.sevendream.mobilesafe")) {
						am.killBackgroundProcesses(app.processName);// 用for循环杀死进程==要活的权限=="android.permission.KILL_BACKGROUND_PROCESSES"
					}

				}
				int num = proNum - getRunningNum();
				getRunningProgress();
				updateMemInfo();
				makeToast("你已经杀死" + num + "进程");
			}
		});
		tv_mem = (TextView) findViewById(R.id.tv_mem);

		tv_pronum = (TextView) findViewById(R.id.tv_pronum);
		ll_appprogresspb = (LinearLayout) findViewById(R.id.ll_appprogresspb);
		ll_appprogresspb.setVisibility(View.VISIBLE);
		tv_progressname = (TextView) findViewById(R.id.tv_progressname);
		tv_memsize = (TextView) findViewById(R.id.tv_memsize);
		list_progressmanager = (ListView) findViewById(R.id.list_progressmanager);
		list_progressmanager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				makeAlterView(position);

			}

		});

		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		updateMemInfo();
		getRunningProgress();

	}

	private void makeAlterView(final int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.my_alterview, null);
		dialog.setView(view, 0, 0, 0, 0);// 0,0,0,0表示的是上下左右边距，以避免低版本弹出的Dialog有框
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		Button bt_queding = (Button) view.findViewById(R.id.bt_queding);
		Button bt_quxiao = (Button) view.findViewById(R.id.bt_quxiao);
		bt_queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				am.killBackgroundProcesses(am.getRunningAppProcesses().get(
						position).processName);
				dialog.dismiss();
				getRunningProgress();
				updateMemInfo();
				makeToast("你已经杀死该进程");

			}
		});
		bt_quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void makeToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	private void updateMemInfo() {
		// TODO Auto-generated method stub
		proNum = getRunningNum();

		tv_pronum.setText("进程总数：" + proNum);

		tv_mem.setText("剩余内存：" + getRomAvailaleSize());
	}

	private int getRunningNum() {
		// TODO Auto-generated method stub
		return am.getRunningAppProcesses().size();
	}

	private void getRunningProgress() {
		// TODO Auto-generated method stub
		// 在子线程中获取手机安装的应用
		new Thread() {

			public void run() {
				appInfos = appProgressInfos();

				Message msg = new Message();
				msg.what = 0;
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	// 获取到r o m内存的运行剩余空间
	public String getRomAvailaleSize() {

		ActivityManager myActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		myActivityManager.getMemoryInfo(memoryInfo);
		long memSize = memoryInfo.availMem;
		// 字符类型转换
		String romSize = Formatter.formatFileSize(getBaseContext(), memSize);
		return romSize;
	}

	// 获取数据
	public List<AppInfo> appProgressInfos() {

		// 创建要返回的集合对象
		List<AppInfo> appInfos = new ArrayList<AppInfo>();

		List<ActivityManager.RunningAppProcessInfo> mRunningPros = am
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo amPro : mRunningPros) {

			AppInfo appInfo = new AppInfo();
			// 获取该进程占用的内存
			int[] myMempid = new int[] { amPro.pid };
			appInfo.setmPid(amPro.pid);

			// 此Memory位于Android。os。Debug。MemoryInfo包中，用来统计进程的内存的信息
			Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
			// 获取进程占内存信息
			long memSize = memoryInfo[0].dalvikPrivateDirty;
			appInfo.setMemSize(Formatter.formatFileSize(getBaseContext(),
					memSize));
			appInfo.setPackagename(amPro.processName);

			appInfos.add(appInfo);

		}
		return appInfos;
	}

}
