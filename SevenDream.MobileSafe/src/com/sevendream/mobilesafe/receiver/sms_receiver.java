package com.sevendream.mobilesafe.receiver;

import com.sevendream.mobilesafe.R;
import com.sevendream.mobilesafe.service.LocationService;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 拦截短信 要加权限android.permission.RECEIVE_SMS
 * 广播要在清单文件AndroidManifest.xml中先注册receiver和intent-filter 同时要设最大优先级 <receiver
 * android:name=".receiver.sms_receiver"> <intent-filter
 * android:priority="2147483647">优先级 <action
 * android:name="android.provider.Telephony.SMS_RECEIVED"/> </intent-filter>
 * </receiver>
 * 
 * @author Administrator
 * 
 */

public class sms_receiver extends BroadcastReceiver {

	private static MediaPlayer player;// 在两个判断语句利用同一个对象，要定意成静态全局就不会报空指针了，也不会崩
	private static Vibrator mVibrator;
	private SharedPreferences msp;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public void onReceive(Context context, Intent intent) {

		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);//设备管理组件
		
		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : objects) {// 短信最多140字节,
										// 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String originatingAddress = message.getOriginatingAddress();// 短信来源号码
			String messageBody = message.getMessageBody().trim();// 短信内容
			messageBody=messageBody.replace("_", "");
			messageBody=messageBody.replace("-", "");
			Log.e("2222:", originatingAddress);
			Log.e("2222:", messageBody);
			msp = context.getSharedPreferences("config", context.MODE_PRIVATE);
			String safecode=msp.getString("sefecode", null);

				
				if ("播放音乐".equals(messageBody)) {
					// 设置系统音乐音量
					AudioManager mAudioManager = (AudioManager) context
							.getSystemService(context.AUDIO_SERVICE);
					int max = mAudioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max,
							max);

					// 在两个判断语句利用同一个对象，要定意成静态全局就不会报空指针了，也不会崩
					player = MediaPlayer.create(context, R.raw.going_home);
					player.setVolume(1f, 1f);
					player.setLooping(true);
					player.start();
					abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
				} else if ("停止播放音乐".equals(messageBody)) {
					// 在两个判断语句利用同一个对象，要定意成静态全局就不会报空指针了，也不会崩
					player.stop();
					abortBroadcast();
				}
				 else if ("手机定位".equals(messageBody)) {
				 // 获取经纬度坐标
				 context.startService(new Intent(context,
				 LocationService.class));// 开启定位服务
				
				 String location = msp.getString("location",
				 "getting location...");
				 String phoneNumber = msp.getString("mPhoneNum", "");
//				 SmsManager sm = SmsManager.getDefault();
//					sm.sendTextMessage(safecode, null, "请注意，您手机号为：" + phoneNumber
//							+ "的朋友，目前手机的位置为:"+location, null, null);
				
				 System.out.println("location:" + location);
				
				 abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
				 }
				else if ("远程清除数据".equals(messageBody)) {
					System.out.println("远程清除数据");

					mDPM.wipeData(0);
					
					
					abortBroadcast();
				} else if ("远程锁屏".equals(messageBody)) {
					System.out.println("远程清除数据");

					mDPM.lockNow();
					mDPM.resetPassword("1234", 0);
					abortBroadcast();
				} else if ("开始震动".equals(messageBody)) {
					/**
					 * 震动要设置权限android.permission.VIBRATE
					 */
					mVibrator = (Vibrator) context.getApplicationContext()
							.getSystemService(android.app.Service.VIBRATOR_SERVICE);
					/**
					 * 设置震动周期
					 * new long[]{10000,1000,10000,1000,10000}
					 *            震动频率，相邻震动隔得时长
					 */
					mVibrator.vibrate(new long[]{10000,100,10000,100,10000}, 0);
					abortBroadcast();
				}else if ("取消震动".equals(messageBody)) {
					/**
					 * 震动要设置权限android.permission.VIBRATE
					 * 
					 */
					mVibrator.cancel();
					abortBroadcast();
				}else if ("卸载程序".equals(messageBody)) {
					mDPM.removeActiveAdmin(mDeviceAdminSample);
					Intent intent1 =new Intent(Intent.ACTION_VIEW);
					intent1.addCategory(Intent.CATEGORY_DEFAULT);
					intent1.setData(Uri.parse("package:"+context.getPackageName()));
					context.startActivity(intent1);
					abortBroadcast();
				}
				
			}
		
	
		}

	

}


