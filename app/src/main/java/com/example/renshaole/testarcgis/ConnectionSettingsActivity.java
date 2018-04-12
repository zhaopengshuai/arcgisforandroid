package com.example.renshaole.testarcgis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.renshaole.testarcgis.global.MyBaseActivity;
import com.example.renshaole.testarcgis.widget.MyGlobal1;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.rwbt.BTHeartBeatThread;
import cn.jucheng.rwusb1.USBHeartBeatThread;
import cn.jucheng.rwwifi.NETHeartBeatThread;

/**
 * 设置连接方式
 *
 */

public class ConnectionSettingsActivity extends MyBaseActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "ConnectionSettingsActivity";
	private static MHandler mHandler;


	TextView btn_select;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contenttype);
		ButterKnife.bind(this);
		initView();

		btn_select=findViewById(R.id.connent_network);
		btn_select.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("LongLogTag")
			@Override
			public void onClick(View v) {
				Log.d(TAG,"点击连接网络设备");
				startActivity(new Intent(ConnectionSettingsActivity.this, ConnectIPActivity.class));
			}
		});
		mHandler = new MHandler(this);
		WorkService.addHandler(mHandler);
	}

	private void initView() {
		if (!datas.getBoolData(MyGlobal1.C_SHEBEI_LANYA)) {
			findViewById(R.id.connent_already_buletooh).setVisibility(
					View.INVISIBLE);
			findViewById(R.id.serach_connent_bluetooh).setVisibility(
					View.INVISIBLE);
		}
		if (!datas.getBoolData(MyGlobal1.C_SHEBEI_WIFI)) {
			findViewById(R.id.connent_network).setVisibility(View.INVISIBLE);
		}
		if (!datas.getBoolData(MyGlobal1.C_SHEBEI_USB)) {
			findViewById(R.id.connent_usb).setVisibility(View.INVISIBLE);
			findViewById(R.id.connent_usb_cong).setVisibility(View.INVISIBLE);
		}


	}

	@SuppressLint("LongLogTag")
	@OnClick({R.id.back, R.id.connent_already_buletooh, R.id.serach_connent_bluetooh, R.id.connent_network, R.id.connent_usb, R.id.connent_usb_cong})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.back://返回
				finish();
				break;
			case R.id.connent_already_buletooh://连接已配对蓝牙设备
				startActivity(new Intent(this, ConnectBTPairedActivity.class));
				break;
			case R.id.serach_connent_bluetooh://搜索并连接蓝牙设备
				startActivity(new Intent(this, SearchBTActivity.class));
				break;
			case R.id.connent_network://连接网络设备
				Log.d(TAG,"点击连接网络设备");
				startActivity(new Intent(this, ConnectIPActivity.class));
				break;
			case R.id.connent_usb://连接usb设备
				datas.setData(MyGlobal.PREFERENCES_LASTCONNECTED_TYPE,
						MyGlobal.CONNECTTYPE_USB);
				SettingsActivity.checkDeviceConnection1(this);
				break;
			case R.id.connent_usb_cong://连接USB设备作为从设备
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private static class MHandler extends Handler {

		@SuppressWarnings("unused")
		private WeakReference<ConnectionSettingsActivity> mActivity;

		private MHandler(ConnectionSettingsActivity connectSettingsActivity) {
			mActivity = new WeakReference<ConnectionSettingsActivity>(
					connectSettingsActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// ConnectionSettingsActivity theActivity = mActivity.get();
			switch (msg.what) {
				case MyGlobal.MSG_ALLTHREAD_READY: {
					break;
				}
				case BTHeartBeatThread.MSG_BTHEARTBEATTHREAD_UPDATESTATUS:
				case NETHeartBeatThread.MSG_NETHEARTBEATTHREAD_UPDATESTATUS:
				case USBHeartBeatThread.MSG_USBHEARTBEATTHREAD_UPDATESTATUS: {
					int statusOK = msg.arg1;
					// int status = msg.arg2;
					// MyLog.v(TAG,
					// "statusOK: " + statusOK + " status: "
					// + DataUtils.byteToStr((byte) status));
					// theActivity.progressBar.setIndeterminate(false);
					if (statusOK == 1) {
						// theActivity.progressBar.setProgress(100);
					} else {
						// theActivity.progressBar.setProgress(0);
					}
					break;
				}
				case MyGlobal.CMD_POS_PRINTPICTURERESULT:
				case MyGlobal.CMD_POS_WRITE_BT_FLOWCONTROL_RESULT: {
					// int result = msg.arg1;
					// MyLog.v(TAG, "Result: " + result);
					break;
				}
			}
		}
	}

	@Override
	public void exc() {

	}

	@Override
	protected void HandlerMessage(Message msg) {
	}

}
