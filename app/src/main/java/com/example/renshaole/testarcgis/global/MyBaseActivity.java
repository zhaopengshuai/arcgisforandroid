package com.example.renshaole.testarcgis.global;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.example.renshaole.testarcgis.AdaptationActivity;
import com.example.renshaole.testarcgis.SettingsActivity;
import com.example.renshaole.testarcgis.decoding.DecodingLibrary;
import com.example.renshaole.testarcgis.widget.MyGlobal1;
import com.example.renshaole.testarcgis.widget.MyMessage;
import com.example.renshaole.testarcgis.widget.MyShareUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jucheng.callback.RecvCallBack;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.DataUtils;
import cn.jucheng.jclibs.tools.MyLog;
import cn.jucheng.jclibs.tools.MyToast;

/** 封装Activity 基类 所有继承BaseActivity基类; Activity创建时将MyApplication 加入指针方便 销毁退出 */
public abstract class MyBaseActivity extends AdaptationActivity {
	private static final String TAG = "BaseActivity";
	protected AlertDialog ag;
	private long exitTime = 0;// 退出时间
	public static BaseMHandler mBaseHandler;
	private static Timer timerLianJie;
	public static MyBaseActivity currentMyBaseActivity;
	public static boolean isZiDongChongLian = true;
    public Unbinder unbinder;
    public boolean DEBUG =true;
	//casejson地址
	protected static String BLPath2 = Environment.getExternalStorageDirectory() +
			File.separator +
			"jucheng" + File.separator +
			"hulisiwei" + File.separator+
			"case.json";

	// 写一个广播的内部类，当收到动作时，结束activity
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			unregisterReceiver(this);// 这一句必须加上，不然虽然能退出，但报很多错误
			finish();// 到这里，多个activity可以关闭掉程序了 但是进程仍然存在，因此加上了下边一句话，可以杀死进程
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	};

	public static MyShareUtils datas = null;
	/** 背景灯常亮 */
	private PowerManager mPowerManager = null;
	/** 背景灯常亮 */
	private WakeLock mWakeLock = null;
	/** 收到服务器心跳包的时间 */
	public static long sysXintiao = 0;
	private static Timer timerSendXinTiaoBao;
	protected static DecodingLibrary dl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//新开启的Activity加入到栈中
		AppManager.getAppManager().addActivity(this);
		//设置横屏显示
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		currentMyBaseActivity = this;
		if (dl == null) {
			dl = new DecodingLibrary(handlerState,MyBaseActivity.this);
		}
		if (datas == null)
			datas = MyShareUtils.getInstances(this);
		if (null == WorkService.workThread) {
			Intent intent = new Intent(this, WorkService.class);
			startService(intent);
		}
		if (mBaseHandler == null) {// 这个判断很重要，保证只有一个负责连接设备的handler。
			mBaseHandler = new BaseMHandler(this);
			WorkService.addHandler(mBaseHandler);
			WorkService.SetOnRecvCallBack(callback);
		}
		super.onCreate(savedInstanceState);
		MyLog.d(this.getClass().getSimpleName().toString(), "--onCreate()");
		// 在当前的activity中注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		this.registerReceiver(this.broadcastReceiver, filter);
		changLiang();
	}
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }


	// /** 模拟人复位是否成功false-未收到应答，true-复位成功已应答 */
	private Handler handlerState = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MyMessage.MLZ_XINTIAOJIANCE:
				// 记录新的接收心跳检测的时间，在发送心跳检测的计时器中会执行心跳检测是否断开的检查。
				sysXintiao = System.currentTimeMillis();

				// MyLog.d(TAG, "接收心跳包");
				break;
			case MyMessage.MLZ_BEIAN: // 备案
				MyMessage.sendMessage(MyMessage.getMsgBeian());
				break;

			default:
				currentMyBaseActivity.HandlerMessage(msg);
				break;
			}
			return false;
		}
	});

	protected abstract void HandlerMessage(Message msg);

	private static void SendXinTiaoMessage() {
		if (timerSendXinTiaoBao != null) {
			timerSendXinTiaoBao.cancel();
		}
		timerSendXinTiaoBao = new Timer(true);

		TimerTask task = new TimerTask() {
			public void run() {
				// 每次需要执行的代码放到这里面。
				MyMessage.sendMessage(MyMessage.getMsgXintiaojiance());
				// MyLog.w(TAG, "接收心跳包SendXinTiaoMessage():发送心跳包 ");
				if (System.currentTimeMillis() - sysXintiao > 30000) {
					if (!datas.getBoolData(MyGlobal1.C_DEBUG)) {
						MyLog.w(TAG, "接收心跳包2SendXinTiaoMessage():连接疑似断开了： ");
						WorkService.workThread.disconnect();
						chongXinLianJie(0);
					}
				}
				if (WorkService.workThread.isConnected()) {
					SendXinTiaoMessage();
				}
			}
		};
		timerSendXinTiaoBao.schedule(task, 2000);
	}

	/** 背景灯常亮 */
	@SuppressLint("Wakelock")
	@SuppressWarnings("deprecation")
	private void changLiang() {
		this.mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = this.mPowerManager.newWakeLock(
				PowerManager.FULL_WAKE_LOCK, "My Lock");
		this.mWakeLock.acquire();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按两次返回键退出应用程序
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 判断间隔时间 小于2秒就退出应用
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				String msg = "再按一次返回键退出系统";
				MyToast.showToast(this, msg);
				// 计算两次返回键按下的时间差
				exitTime = System.currentTimeMillis();
			} else {
				// 关闭应用程序
				onExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onExit() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // 说明动作
		this.sendBroadcast(intent);// 该函数用于发送广播
		super.finish();
		this.finish();
	}

	/** 设备是否已经连接 */
	public Boolean isConnection() {
		if (WorkService.workThread != null
				&& !WorkService.workThread.isConnected()) {
			MyToast.showToast(this, "请在【设置】中配置设备连接信息后继续。");
			return false;
		}
		return true;
	}

	public abstract void exc();

	public void onReturn(View v) {
		AppManager.getAppManager().finishActivity();
	}

	public void onSettings(View v) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		exc();
        if (null != unbinder)
            unbinder.unbind();
		MyLog.i(getClass().getSimpleName().toString(), "--onDestroy()");
		this.unregisterReceiver(this.broadcastReceiver);
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		MyLog.i(this.getClass().getSimpleName().toString(), "--onRestart()");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.d(getClass().getSimpleName().toString(), "--onResume()");
		super.onResume();
		currentMyBaseActivity = this;
	}

	@Override
	protected void onStart() {
		MyLog.i(getClass().getSimpleName().toString(), "--onStart()");
		super.onStart();
	}

	@Override
	protected void onStop() {
		MyLog.i(getClass().getSimpleName().toString(), "--onStop()");
		super.onStop();
	}

	@Override
	protected void onPause() {
		MyLog.i(getClass().getSimpleName().toString(), "--onPause()");
		super.onPause();
	}

	static {
		MyLog.setLogPath("hulisiwei");
		MyLog.isDebug = true;
		MyLog.isPrintingLog = false;
		MyLog.init();
		MyLog.d(TAG, "---又一次新的运行 " + MyLog.Second_PATH);
	}

	public static class BaseMHandler extends Handler {

		private WeakReference<MyBaseActivity> mActivity;

		public BaseMHandler(MyBaseActivity activity) {
			mActivity = new WeakReference<MyBaseActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/** DrawerService 的 onStartCommand会发送这个消息 */

			case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
				int result = msg.arg1;
				if (1 == result) {// 连接成功记录最后一次连接成功记录
					Bundle data = msg.getData();
					String connectedBTAddress = data
							.getString(MyGlobal.STRPARA1);
					String connectedBTName = data.getString(MyGlobal.STRPARA2);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE,
							MyGlobal.CONNECTTYPE_BT);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_BTADDRESS,
							connectedBTAddress);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_BTNAME,
							connectedBTName);
				} else {
					/** 1:连接，0:未连接成功原因未知，2：平板蓝牙未开启，3:bluetoothAdapter==null */
					switch (result) {
					case MyGlobal.RESULT_FAIL_PINGBANGWEIKAIQILANYA:
						MyToast.showToast(mActivity.get(),
								"请在平板的【设置】中打开平板的蓝牙功能。");
						break;
					case MyGlobal.RESULT_FAIL_CHONGQIMONIREN:
						MyToast.showToast(mActivity.get(), "请重启仿真模拟人。");
						break;
					}
				}
				break;
			}
			case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTNETRESULT: {
				int result = msg.arg1;
				if (1 == result) {// 连接成功记录最后一次连接成功记录
					Bundle data = msg.getData();
					String connectedIPAddress = data
							.getString(MyGlobal.STRPARA1);
					int connectedPortNumber = data.getInt(MyGlobal.INTPARA1);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE,
							MyGlobal.CONNECTTYPE_NET);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_IPADDRESS,
							connectedIPAddress);
					datas.setData(
							MyGlobal.PREFERENCES_LASTCONNECTED_PORTNUMBER,
							connectedPortNumber);
				} else {// 连接失败也保存连接设备信息
					Bundle data = msg.getData();
					String connectedIPAddress = data
							.getString(MyGlobal.STRPARA1);
					int connectedPortNumber = data.getInt(MyGlobal.INTPARA1);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE,
							MyGlobal.CONNECTTYPE_NET);
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_IPADDRESS,
							connectedIPAddress);
					datas.setData(
							MyGlobal.PREFERENCES_LASTCONNECTED_PORTNUMBER,
							connectedPortNumber);
				}
				break;
			}
			case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTUSBRESULT: {
				int result = msg.arg1;
				if (1 == result) {// 连接成功记录最后一次连接成功记录
					datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE,
							MyGlobal.CONNECTTYPE_USB);
					MyToast.showToast(mActivity.get(), "USB设备连接成功。");
				} else {
					// 1:连接，0:未连接成功原因未知，2：未授权成功，请重新拔插USB模拟人，
					// 3.打开设备失败4.设备初始化失败，5，设备config失败
					switch (result) {
					case MyGlobal.RESULT_FAIL_USBOPENFAIL:
						MyToast.showToast(mActivity.get(), "请确认USB设备已连接到目标设备。");
						break;
					case MyGlobal.RESULT_FAIL_USBSHEBEICHUSHIHUASHIBAI:
						MyToast.showToast(mActivity.get(),
								"USB设备初始化失败，请重新拔插USB设备。");
						break;
					case MyGlobal.RESULT_FAIL_USBWEISHOUQUAN:
						MyToast.showToast(mActivity.get(),
								"USB设备未授权，请重新拔插设备并授权此软件可以使用该USB设备。");
						break;
					case MyGlobal.RESULT_FAIL_USBCONFIGFAIL:
						MyToast.showToast(mActivity.get(),
								"USB设备设置参数失败，请重新拔插USB设备。");
						break;
					}
				}
				break;
			}
			case MyGlobal.MSG_ALLTHREAD_READY:
				chongXinLianJie(0);
				break;
			case MyGlobal.MSG_WORKTHREAD_SEND_LOSTCONNECTION:
				MyLog.d(TAG, "失去连接");
				// MyToast.showToast(mActivity.get(), "连接断开，重新连接设备。");
				chongXinLianJie(0);
				break;
			case MyGlobal.MSG_WORKTHREAD_SEND_STATECHANGED: {
				if (WorkService.workThread.getConnectState() == MyGlobal.CONNECT_STATE_NONE) {
					if (isZiDongChongLian) {
						chongXinLianJie(5000);
						MyLog.d(TAG, "llq123 5秒后重连");
					}
				} else if (WorkService.workThread.getConnectState() == MyGlobal.CONNECT_STATE_CONNECTED) {
					timerLianJie.cancel();
					// MyLog.d(TAG, "llq123 不再重连");
					sysXintiao = System.currentTimeMillis();
					SendXinTiaoMessage(); // TODO
					MyMessage.sendMessage(MyMessage.getMsgBeian(), 1000);
				}
				break;
			}
			}
		}
	}

	private static RecvCallBack callback = new RecvCallBack() {
		public void onRecv(byte[] buffer, int byteOffset, int byteCount) {
			dl.newDataChuLi(buffer, byteCount);
		}
	};

	public void testReciveMsg(String msg1) {
		byte[] buffer = DataUtils.HexStringToBytes(msg1.replace(" ", ""));
		dl.newDataChuLi(buffer, buffer.length);
	}

	public void testReciveMsg(final String msg1, long delay) {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			public void run() {
				byte[] buffer = DataUtils.HexStringToBytes(msg1
						.replace(" ", ""));
				dl.newDataChuLi(buffer, buffer.length);
			}
		};
		timer.schedule(task, delay);
	}

	/** 连接失败后N毫秒后重新尝试连接 */
	public static void chongXinLianJie(final long delay) {
		if (timerLianJie != null) {
			timerLianJie.cancel();
		}
		timerLianJie = new Timer(true);

		TimerTask task = new TimerTask() {
			public void run() {
				// 每次需要执行的代码放到这里面。
				if (!WorkService.workThread.isConnected()) {
					WorkService.workThread.disconnect();
					SettingsActivity
							.checkDeviceConnection1(currentMyBaseActivity);
				}
			}
		};
		timerLianJie.schedule(task, delay);
	}



}
