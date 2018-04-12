package com.example.renshaole.testarcgis;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.renshaole.testarcgis.global.MyBaseActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.MyToast;


/**
 * 搜索蓝牙设备
 */
public class SearchBTActivity extends MyBaseActivity {

	@BindView(R.id.progressBar_SearchStatus)
    ProgressBar progressBarSearchStatus;

	@BindView(R.id.linearlayout_devices)
    LinearLayout linearlayoutdevices;

	private ProgressDialog dialog;

	private BroadcastReceiver broadcastReceiver = null;
	private IntentFilter intentFilter = null;

	private static Handler mHandler = null;
	private static String TAG = "SearchBTActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bluetooh);
		ButterKnife.bind(this);

		dialog = new ProgressDialog(this);

		initBroadcast();

		mHandler = new MHandler(this);
		WorkService.addHandler(mHandler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WorkService.delHandler(mHandler);
		mHandler = null;
		uninitBroadcast();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initBroadcast() {
		broadcastReceiver = new BroadcastReceiver() {

			@SuppressLint("ResourceType")
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					if (device == null)
						return;
					final String address = device.getAddress();
					String name = device.getName();
					if (name == null)
						name = "BT";
					else if (name.equals(address))
						name = "BT";
					Button button = new Button(context);
					button.setBackgroundColor(getResources().getColor(R.color.drug_bton));
					button.setTextAppearance(context, R.dimen.text_size20);
					button.setTextColor(getResources().getColor(
							R.color.baise));

					button.setHeight(getResources().getDimensionPixelSize(
							R.dimen.x100));
					button.setGravity(Gravity.CENTER);
					button.setText("   " + name + ": " + address);
					button.setTextSize(20);
					button.setGravity(Gravity.CENTER_VERTICAL
							| Gravity.LEFT);

					button.setOnClickListener(new OnClickListener() {

						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							// 只有没有连接且没有在用，这个才能改变状�??
							dialog.setMessage(MyGlobal.toast_connecting + " "
									+ address);
							dialog.setIndeterminate(true);
							dialog.setCancelable(false);
							dialog.show();
							WorkService.workThread.connectBt(address);
						}
					});
					linearlayoutdevices.addView(button);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
						.equals(action)) {
					progressBarSearchStatus.setIndeterminate(true);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					progressBarSearchStatus.setIndeterminate(false);
				}
			}
		};
		intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	private void uninitBroadcast() {
		if (broadcastReceiver != null)
			unregisterReceiver(broadcastReceiver);
	}

	@OnClick({R.id.back_bluetooth, R.id.serach_buton})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.back_bluetooth://返回
				finish();
				break;
			case R.id.serach_buton://搜索
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				if (null == adapter) {
					finish();
					return;
				}

				if (!adapter.isEnabled()) {
					if (adapter.enable()) {
						while (!adapter.isEnabled())
							;
						Log.v(TAG, "Enable BluetoothAdapter");
					} else {
						finish();
						return;
					}
				}

				adapter.cancelDiscovery();
				linearlayoutdevices.removeAllViews();
				adapter.startDiscovery();
				break;
		}
	}

	private static class MHandler extends Handler {

		private WeakReference<SearchBTActivity> mActivity;

		private MHandler(SearchBTActivity activity) {
			mActivity = new WeakReference<SearchBTActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			SearchBTActivity theActivity = mActivity.get();
			switch (msg.what) {
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
					int result = msg.arg1;
					MyToast.showToast(theActivity,
							(result == 1) ? MyGlobal.toast_success
									: MyGlobal.toast_fail);
					Log.v(TAG, "Connect Result: " + result);
					theActivity.dialog.cancel();
					break;
				}
			}
		}
	}

	@Override
	public void exc() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void HandlerMessage(Message msg) {
	}

}
