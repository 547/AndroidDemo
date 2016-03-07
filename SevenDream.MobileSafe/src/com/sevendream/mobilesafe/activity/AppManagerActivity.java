package com.sevendream.mobilesafe.activity;

/***
 * 软件管理
 * 
 * 
 */
import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.adapter.AppManagerAdapter;
import com.sevendream.mobilesafe.bean.AppInfo;
import com.sevendream.mobilesafe.service.AppInfoService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AppManagerActivity extends Activity {

	private static final String SCHEME = "package";
	private static final String APP_PKG_NAME_22 = "com.android.settings.ApplicationPkgName";
	private static final String APP_PKG_NAME_21 = "pkg";
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	protected static final int SUCCESS_GET_APPLICATION = 0;

	// 布局中的各个控件
	private LinearLayout ll_apppd;
	private ListView list_appmanager;

	private TextView tv_rom;
	private TextView tv_sd;

	// 报管理器
	private PackageManager pm;
	// 获取手机应用信息获取类
	private AppInfoService appInfoService;
	// 手机应用Modell
	private List<AppInfo> appInfos;
	// 用户应用集合
	private List<AppInfo> userAppInfos;
	// 是否是所有App程序，默认ture
	private boolean isAllApp = true;
	// appManagerAdaper适配器对象
	private AppManagerAdapter mAdapter;

	private PopupWindow mPopupWindow;
	// mHandler方法
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				// 给listView绑定数据，影藏加载的控件
				mAdapter = new AppManagerAdapter(getApplicationContext(),
						appInfos);
				// 设置数据
				list_appmanager.setAdapter(mAdapter);
				// 隐藏进度圈
				ll_apppd.setVisibility(View.GONE);// 不占空间

				break;

			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_app_manager);
		tv_rom = (TextView) findViewById(R.id.tv_rom);
		tv_sd = (TextView) findViewById(R.id.tv_sd);
		ll_apppd = (LinearLayout) findViewById(R.id.ll_apppb);
		ll_apppd.setVisibility(View.VISIBLE);// 一开始可见
		list_appmanager = (ListView) findViewById(R.id.list_appmanager);
		list_appmanager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				uninstallApp(position);
			}

			

		});
		// 实例化AppInfoService对象
		appInfoService = new AppInfoService(this);
		// 包管理器
		pm = getPackageManager();
		// 获取到r o m内存的运行剩余空间
		String romSize = getRomAvailaleSize();
		// 获取到SD卡的剩余空间

		long sd_freeSpace = getSDAvailaleSize();

		tv_rom.setText("内存可用：" + romSize);
		tv_sd.setText("SD卡可用：" + Formatter.formatFileSize(this, sd_freeSpace));
		// 在子线程中获取手机安装的应用
		new Thread() {
			public void run() {

				appInfos = appInfoService.getAppInfos();
				userAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					}
				}
				try {
					Thread.sleep(2000);// 延时，让listView慢点出来
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICATION;
				mHandler.sendMessage(msg);
			};
		}.start();

	}
	
	
//卸载某一应用
	private void uninstallApp(int position) {
		// TODO Auto-generated method stub
		AppInfo app = appInfos.get(position);
		String packagename = app.getPackagename();
		Uri packageUri=Uri.parse("package:"+packagename);
		Intent intent=new Intent(Intent.ACTION_DELETE,packageUri);
		startActivity(intent);
		
		
	}
	// 进入系统应用信息页面
	public void showInstalledAppDetails(int position) {
		// TODO Auto-generated method stub

		AppInfo app = appInfos.get(position);
		String packagename = app.getPackagename();

		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) {// 2.3(apiLevel9)以上使用SDK提供的接口
			intent.setAction(android.provider.Settings.ACTION_APPLICATION_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packagename, null);
			intent.setData(uri);
		} else {// 2.3以上，使用非公开接口
			// 2.2和2.1中，installAppDetails使用的APP_PKG_NAME不同
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);

			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packagename);
		}
		startActivity(intent);
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

	// 获取SD卡的剩余内存
	public long getSDAvailaleSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		// 获取bloc的size
		long size = stat.getBlockSize();
		// 获取空余的bloc量
		long availaBloc = stat.getAvailableBlocks();

		// 剩余的sd卡内存
		long availaSize = size * availaBloc;// 以M为单位

		return availaSize;
	}
	
	
	

	

}
