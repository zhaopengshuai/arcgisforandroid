package com.example.renshaole.testarcgis;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.example.renshaole.testarcgis.global.MyBaseActivity;
import com.example.renshaole.testarcgis.widget.MyGlobal1;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.MyLog;
import cn.jucheng.jclibs.tools.MyToast;
import cn.jucheng.rwusb1.MyUSBDriver.TTYTermios;
import cn.jucheng.rwusb1.MyUSBDriver.TTYTermios.FlowControl;
import cn.jucheng.rwusb1.MyUSBDriver.TTYTermios.Parity;
import cn.jucheng.rwusb1.MyUSBDriver.TTYTermios.StopBits;
import cn.jucheng.rwusb1.USBDriver.USBPort;


/**
 * 设置
 */
public class SettingsActivity extends MyBaseActivity {
	private static final String TAG = "Settings";

	@BindView(R.id.set_button)
	TextView setButton;

	private Button btnConnectDevice;
	private Button btnSelectDevice;
	private int debugCount = 0;
	private TextView txtLanYaZhuangTaiText;
	private MHandler mHandler;
	private TextView tvSavedDevice;
	private static boolean isCH34USB = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		ButterKnife.bind(this);
		mHandler = new MHandler(this);
		WorkService.addHandler(mHandler);
		initSettings1();
	}

	/**
	 * 连接设备
	 *
	 * @param context
	 */
	public static void checkDeviceConnection1(Context context) {
		if (WorkService.workThread != null
				&& !WorkService.workThread.isConnected()
				&& !WorkService.workThread.isConnecting()) {// 未连接则自动连接
			int type = datas
					.getIntData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE);
			if (type != 0) {// 连接成功过
				switch (type) {
					case MyGlobal.CONNECTTYPE_BT:
						// MyLog.i(TAG, "checkDeviceConnection():主动尝试连接 bt");
						WorkService.workThread.connectBt(datas.getData(MyGlobal.PREFERENCES_LASTCONNECTED_BTADDRESS));
						break;
					case MyGlobal.CONNECTTYPE_NET:
						// MyLog.i(TAG, "checkDeviceConnection():主动尝试连接 net");
						WorkService.workThread
								.connectNet(
										datas.getData(MyGlobal.PREFERENCES_LASTCONNECTED_IPADDRESS),
										datas.getIntData(MyGlobal.PREFERENCES_LASTCONNECTED_PORTNUMBER));
						break;
					case MyGlobal.CONNECTTYPE_USB:
						MyLog.i(TAG, "checkDeviceConnection():主动尝试连接 usb");
						if (isCH34USB) {
							connectUSB();
						} else {
							connectUSB(context);
						}
						break;
				}
			} else {
				MyLog.i(TAG, "checkDeviceConnection():主动尝试连接 没有连接成功过");
			}
		} else {
			// MyLog.i(TAG, "checkDeviceConnection():不需要或不能重新连接");
		}
	}

	/**
	 * 检查设备是否连接，如果没有连接，如果日志中记录了上次连接方式及上次连接设备的信息，自动连接。
	 */
	public void checkDeviceConnection(View v) {
		checkDeviceConnection1(getApplicationContext());
	}

	/**
	 * 目标设备的vid pid
	 */
	public static int id[] = {0x10c4, 0x8200};

	/**
	 * 连接普通usb
	 */
	private static void connectUSB(Context context) {
		final UsbManager mUsbManager = (UsbManager) context
				.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		if (deviceList.size() > 0) {
			// 初始化选择对话框布局，并添加按钮和事件

			while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
				final UsbDevice device = deviceIterator.next();
				if (device.getVendorId() == id[0]
						&& device.getProductId() == id[1]) {

					PendingIntent mPermissionIntent = PendingIntent
							.getBroadcast(context, 0, new Intent(context
											.getApplicationContext().getPackageName()),
									0);
					if (!mUsbManager.hasPermission(device)) {
						MyLog.d(TAG, MyGlobal.toast_usbpermit);
						mUsbManager
								.requestPermission(device, mPermissionIntent);
						MyToast.showToast(context.getApplicationContext(),
								MyGlobal.toast_usbpermit);
					} else {
						MyLog.d(TAG, MyGlobal.toast_usbpermit
								+ "  --------------other");
						USBPort port = new USBPort(mUsbManager, device);
						TTYTermios serial = new TTYTermios(9600,
								FlowControl.NONE, Parity.NONE, StopBits.ONE, 8);
						WorkService.workThread.connectUsb(port, serial);
					}
					break;
				}
			}
		}
	}

	/**
	 * 连接串口转的usb
	 */
	private static void connectUSB() {
		WorkService.workThread
				.connectUsb(SettingsActivity.currentMyBaseActivity);
	}

	/**
	 * 选择连接方式并设置连接
	 */
	public void onSelect(View v) {
		WorkService.workThread.disconnect();
		isZiDongChongLian = false;
		Intent intent = new Intent(this, ConnectionSettingsActivity.class);
		startActivityForResult(intent, 1);
		finish();
	}

	/**
	 * 进入关于页面
	 */
	public void onAbout(View v) {
//		WorkService.workThread.disconnect();
//		Intent intent = new Intent(this, AboutActivity.class);
//		startActivity(intent);
	}

	public void onDebug(View v) {
		debugCount++;
		if (debugCount > 11) {// 10秒钟内点击12下，显示测试死和活的按钮
//			findViewById(R.id.chkLanYa).setVisibility(View.VISIBLE);
//			findViewById(R.id.chkWiFi).setVisibility(View.VISIBLE);
//			findViewById(R.id.chkUSB).setVisibility(View.VISIBLE);
//			findViewById(R.id.btnDebug).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv1).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv2).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv3).setVisibility(View.VISIBLE);
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				debugCount = 0;
			}
		}, 30000);
	}

	/**
	 * 保存设置信息
	 */
	public void onSave(View v) {
		try {
			this.finish();
		} catch (Exception e) {
			MyLog.e(TAG, "onSave():  " + e.getMessage());
		}
	}

	/**
	 * 普通设置
	 */
	public void onSettings1(View v) {
		View view = View.inflate(this, R.layout.activity_setting, null);
		this.setContentView(view);
		initSettings1();
	}

	/**
	 * 销毁当前页面
	 */
	public void Cancel(View v) {
		finish();
	}

	@SuppressLint({"DefaultLocale", "ResourceType"})
	private void initSettings1() {
		btnConnectDevice = (Button) this.findViewById(R.id.btnConnectDevice);
		btnSelectDevice = (Button) this.findViewById(R.id.btnSelectDevice);
		txtLanYaZhuangTaiText = (TextView) this.findViewById(R.id.connect_type);
		txtLanYaZhuangTaiText.setEnabled(false);
		tvSavedDevice = (TextView) findViewById(R.id.connect_equipment);
//		Switch btnDebug = (Switch) findViewById(R.id.btnDebug);
//		CheckBox chkLanYa = (CheckBox) findViewById(R.id.chkLanYa);
//		CheckBox chkWiFi = (CheckBox) findViewById(R.id.chkWiFi);
//		CheckBox chkUSB = (CheckBox) findViewById(R.id.chkUSB);
//		TextView chk_tv1 = (TextView) findViewById(R.id.chk_tv1);
//		TextView chk_tv2 = (TextView) findViewById(R.id.chk_tv2);
//		TextView chk_tv3 = (TextView) findViewById(R.id.chk_tv3);
//		if (datas.getBoolData(MyGlobal1.C_DEBUG)) {
//			btnDebug.setVisibility(View.VISIBLE);
//			chkLanYa.setVisibility(View.VISIBLE);
//			chkWiFi.setVisibility(View.VISIBLE);
//			chkUSB.setVisibility(View.VISIBLE);
//			chk_tv1.setVisibility(View.VISIBLE);
//			chk_tv2.setVisibility(View.VISIBLE);
//			chk_tv3.setVisibility(View.VISIBLE);
//		}
//		btnDebug.setChecked(datas.getBoolData(MyGlobal1.C_DEBUG));
//		btnDebug.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//										 boolean isChecked) {
//				datas.setData(MyGlobal1.C_DEBUG, isChecked);
//			}
//		});
//		chkLanYa.setChecked(datas.getBoolData(MyGlobal1.C_SHEBEI_LANYA));
//		chkWiFi.setChecked(datas.getBoolData(MyGlobal1.C_SHEBEI_WIFI));
//		chkUSB.setChecked(datas.getBoolData(MyGlobal1.C_SHEBEI_USB));
//		chkLanYa.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//										 boolean isChecked) {
//				datas.setData(MyGlobal1.C_SHEBEI_LANYA, isChecked);
//			}
//		});
//		chkWiFi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//										 boolean isChecked) {
//				datas.setData(MyGlobal1.C_SHEBEI_WIFI, isChecked);
//			}
//		});
//		chkUSB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//										 boolean isChecked) {
//				datas.setData(MyGlobal1.C_SHEBEI_USB, isChecked);
//			}
//		});
		if (WorkService.workThread != null)
			txtLanYaZhuangTaiText.setText(WorkService.workThread.getText());

		int type = datas.getIntData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE);
		if (type != 0) {// 连接成功过
			switch (type) {
				case MyGlobal.CONNECTTYPE_BT:
					tvSavedDevice.setText(datas
							.getData(MyGlobal.PREFERENCES_LASTCONNECTED_BTNAME));
					break;
				case MyGlobal.CONNECTTYPE_NET:
					tvSavedDevice.setText(datas
							.getData(MyGlobal.PREFERENCES_LASTCONNECTED_IPADDRESS));
					break;
				case MyGlobal.CONNECTTYPE_USB:
					tvSavedDevice.setText("USB设备");
					break;
				// default:
				// MyLog.d(TAG, "连接成功过:其他" + type);
			}
			btnConnectDevice.setBackgroundColor(getResources().getColor(R.color.orange));
		} else {
			btnConnectDevice.setBackgroundColor(getResources().getColor(R.color.gray));
			tvSavedDevice.setText("无");
		}
		initBtnConnectDeviceState();
	}

	private void initBtnConnectDeviceState() {
		if (WorkService.workThread != null
				&& (WorkService.workThread.isConnected()
				|| WorkService.workThread.isConnecting() || MyBaseActivity.datas
				.getIntData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE) == 0)) {
			btnConnectDevice.setEnabled(false);
		} else {
			btnConnectDevice.setEnabled(true);
		}
	}

	@OnClick(R.id.set_button)
	public void onViewClicked() {
		debugCount++;
		if (debugCount > 11) {// 10秒钟内点击12下，显示测试死和活的按钮
//			findViewById(R.id.chkLanYa).setVisibility(View.VISIBLE);
//			findViewById(R.id.chkWiFi).setVisibility(View.VISIBLE);
//			findViewById(R.id.chkUSB).setVisibility(View.VISIBLE);
//			findViewById(R.id.btnDebug).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv1).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv2).setVisibility(View.VISIBLE);
//			findViewById(R.id.chk_tv3).setVisibility(View.VISIBLE);
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				debugCount = 0;
			}
		}, 30000);
	}

	// TODO MHandler
	private static class MHandler extends Handler {
		private WeakReference<SettingsActivity> mActivity;

		public MHandler(SettingsActivity activity) {
			mActivity = new WeakReference<SettingsActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			SettingsActivity theActivity = mActivity.get();
			switch (msg.what) {
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
					int result = msg.arg1;
					if (1 == result) {// 连接成功记录最后一次连接成功记录
						Bundle data = msg.getData();
						String connectedBTName = data.getString(MyGlobal.STRPARA2);
						theActivity.tvSavedDevice.setText(connectedBTName);
					}
					break;
				}
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTNETRESULT: {
					int result = msg.arg1;
					if (1 == result) {// 连接成功记录最后一次连接成功记录
						Bundle data = msg.getData();
						String connectedIPAddress = data
								.getString(MyGlobal.STRPARA1);
						theActivity.tvSavedDevice.setText(connectedIPAddress);
					}
					break;
				}
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTUSBRESULT: {
					int result = msg.arg1;
					if (1 == result) {// 连接成功记录最后一次连接成功记录
						theActivity.tvSavedDevice.setText("USB设备");
					}
					break;
				}
				case MyGlobal.MSG_WORKTHREAD_SEND_STATECHANGED: {
					theActivity.txtLanYaZhuangTaiText
							.setText(WorkService.workThread.getText());
					theActivity.initBtnConnectDeviceState();
					break;
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WorkService.delHandler(mHandler);
		mHandler = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		btnSelectDevice.setEnabled(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			isZiDongChongLian = true;
			chongXinLianJie(500);
		}
	}

	@Override
	public void exc() {
	}

	@Override
	protected void HandlerMessage(Message msg) {
	}

}
