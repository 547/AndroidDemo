package com.sevendream.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.utils.BaseSetupActivity;
import com.sevendream.mobilesafe.utils.StreamUtils;

public class SplshActivity extends BaseSetupActivity {

	protected static final int CODE_UPDATE_DAILOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_CONNECT_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;

	public TextView tv_banbenhao, tv_progress;
	private String mversionName;
	private int mversionCode;
	private String mdownLoadUrl;
	private String mdescrption;
	private Message message;

	private Handler mhandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (message.what) {
			case CODE_UPDATE_DAILOG:
				showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplshActivity.this, "URL地址错误", Toast.LENGTH_SHORT).show();
				// 进入主页面的方法
				enterHome();
				break;
			case CODE_CONNECT_ERROR:
				Toast.makeText(SplshActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
				// 进入主页面的方法
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplshActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				// 进入主页面的方法
				enterHome();
				break;
			case CODE_ENTER_HOME:

				// 进入主页面的方法
				enterHome();
				break;

			default:
				break;
			}
		};
	};
	private RelativeLayout rl_splsh;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splsh);
		findbyid();
		tv_banbenhao.setText("版本名：" + getversionName());

		
		copyDB("address.db");//拷贝数据库的方法
		// 判断是否需要自动更新
		boolean autoUpdate = msp.getBoolean("auto_update", true);

		if (autoUpdate) {
			checkversioncode();
		} else {
			mhandle.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);// 延时2秒后发送消息
		}

		// 渐变的动画效果
		AlphaAnimation anim = new AlphaAnimation(0.1f, 1f);
		anim.setDuration(2000);
		rl_splsh.startAnimation(anim);

	}

	public void findbyid() {
		tv_banbenhao = (TextView) findViewById(R.id.tv_banbenhao);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		rl_splsh = (RelativeLayout) findViewById(R.id.rl_splsh);

	}

	// 获取本地APP版本名
	private String getversionName() {
		// TODO Auto-generated method stub
		PackageManager packageManager = getPackageManager();
		try {
			// 快捷键'Assign to local variable======L;
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// 获取本地APP版本号
	private int getversionCode() {
		// TODO Auto-generated method stub
		PackageManager packageManager = getPackageManager();
		try {
			// 快捷键'Assign to local variable======L;
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionName = packageInfo.versionCode;
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// 从服务器获取版本号，以便判断更新版本
	public void checkversioncode() {
		// 获取当前的时间
		final long startime = System.currentTimeMillis();

		// 建立子线程进行与服务器网络连接
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection huc = null;
				message = Message.obtain();

				try {
					// 服务器json地址
					URL url = new URL("http://10.0.2.2:8080/update.json");
					System.out.print("正在访问json");
					huc = (HttpURLConnection) url.openConnection();
					huc.setRequestMethod("GET");// 设置请求方法
					huc.setReadTimeout(2000);// 设置响应（读取）超时时间
					huc.setConnectTimeout(2000);// 设置网络连接超时时间
					huc.connect();// 连接服务器
					System.out.print("正在访问连接");
					// 获取响应码，如果为200就是连接成功
					if (huc.getResponseCode() == 200) {
						InputStream inputStream = huc.getInputStream();
						// 用utils工具类读输入流json文件中的东西
						// StreamUtils是自定义类的类名，readFromStream是StreamUtils类里面的方法名，inputStream是要传入的参数名
						String result = StreamUtils.readFromStream(inputStream);
						// 解析json文件的东西
						JSONObject jo = new JSONObject(result);
						mversionName = jo.getString("versionName");
						mversionCode = jo.getInt("versionCode");
						mdownLoadUrl = jo.getString("downLoadUrl");
						mdescrption = jo.getString("descrption");

						if (mversionCode > getversionCode()) {
							// 将在服务器获取的版本号与当前本地APP的版本号进行对比，如果服务器获取的版本号大于当前本地APP的版本号就说明有新版本
							// 弹出更新应用的对话框
							// 这个是调用对话框的方法
							message.what = CODE_UPDATE_DAILOG;
							// showUpdateDailog();//不能在子线程更新界面，要进一个全局的Handle
						} else {
							message.what = CODE_ENTER_HOME;
						}
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					// Url错误
					message.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// 网络连接错误
					message.what = CODE_CONNECT_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// 读入的json文件的错误
					message.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					// 获取当前时间
					long endtime = System.currentTimeMillis();
					// 获取整个网络连接使用的时间
					long usedtime = endtime - startime;
					// 如果整个网络连接使用的时间小于1.5秒，就强制让其睡1.5-usedtime秒，
					// 这么做是为了让SlpshActivity页面有时间展示，不然要是网速太快,
					// SlpshActivity页面就没有时间展示了
					if (usedtime < 2000) {
						try {
							Thread.sleep(2000 - usedtime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// 用Handle把消息发出去给主线程
					mhandle.sendMessage(message);
					// 最后判断网络连接是否为空，如果不为空，就把网络连接关闭
					if (huc != null) {
						huc.disconnect();
					}
				}
			}
		}.start();
	}

	// 更新应用的对话框
	protected void showUpdateDailog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发现最新版本" + mversionName);
		builder.setMessage(mdescrption);
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 调用进入主页面的方法
				enterHome();
			}
		});
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 调用下载新版本应用方法
				downLoad();
			}
		});
		// 当对话框弹出来的时候，用户却不点击对话框里的按钮，直接点击返回键，
		// 这时就会调用这个方法，跳转到主页面
		builder.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				enterHome();

			}
		});
		builder.show();
	}

	// 下载新版本应用方法
	protected void downLoad() {

		// 判断手机SD卡是否可用
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 没有下载的时候显示下载进度的TextView是gone状态的，在下载时就将显示下载进度的TextView设为可见
			tv_progress.setVisibility(View.VISIBLE);
			// 如果可用就开始下载
			String target = Environment.getExternalStorageDirectory()
					+ "/download";
			HttpUtils http = new HttpUtils();
			HttpHandler handle = http.download(mdownLoadUrl, target, true,
					true, new RequestCallBack<File>() {

						@Override
						// 正在下载，获取当前下载的文件的总大小以及当前已经下载的大小
						public void onLoading(long total, long current,
								boolean isUploading) {

							super.onLoading(total, current, isUploading);
							// 显示下载进度
							tv_progress.setText("下载进度：" + current * 100 / total
									+ "%");
						}

						@Override
						// 下载成功
						public void onSuccess(ResponseInfo<File> arg0) {
							// Toast.makeText(SplshActivity.this, "下载成功",
							// 0).show();
							// 下载成功后，要自动跳转到系统的下载页面
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							intent.setDataAndType(Uri.fromFile(arg0.result),
									"application/vnd.android.package-archive");
							// 跳转到系统安装应用界面，并弹出对话框
							startActivityForResult(intent, 0);
							// 如果用户下载了新版本但不安装，点击了取消，就会返回结果码，并回调方法onActivityResult
						}

						@Override
						// 下载失败
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(SplshActivity.this, "下载失败", Toast.LENGTH_SHORT)
									.show();
						}
					});
		} else {
			Toast.makeText(SplshActivity.this, "当前您的SD卡不可用", Toast.LENGTH_SHORT).show();
		}
	}

	// 如果用户下载了新版本但不安装，点击了取消，就会返回结果码，并回调方法onActivityResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 进入主页面的方法
	protected void enterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplshActivity.this, HomeActivity.class);
		startActivity(intent);
		// 进入主页面后要将当前页面finsh掉,这样子当用户在主页面时点击返回键就不会再回到闪屏页面（SplshActivity）了
		finish();
	}
//拷贝数据库到该路径下===data/data/com.sevendream.mobilesafe/files/address.db
	//
	private void copyDB(String dbName) {
		//初始化文件file===new File(getFilesDir(),"address.db");==new File(文件夹,"文件名");
		//getFilesDir()这个方法可以获取文件路径==data/data/com.sevendream.mobilesafe/files
		
		File destFile = new File(getFilesDir(),dbName);//要拷贝的目标地址
		
		//先判断数据库是否存在==如果存在就直接return，不执行下面的步骤
		if (destFile.exists()) {
			System.out.println("+++++数据库" + dbName + "已经存在！");
			return;
		} 
		
		FileOutputStream out = null;//文件输出流
		InputStream in = null;
	try {
		in = getAssets().open(dbName);
		out = new FileOutputStream(destFile);
		
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len=in.read(buffer))!=-1) {
			out.write(buffer, 0, len);
			
		}
		
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		try {
			//关闭输入、输出流
			in.close();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	}
	
}
