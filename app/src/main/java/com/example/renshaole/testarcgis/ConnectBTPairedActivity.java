package com.example.renshaole.testarcgis;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.renshaole.testarcgis.global.MyBaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.MyToast;

/**
 *  连接已配对蓝牙
 */
public class ConnectBTPairedActivity extends MyBaseActivity implements
        OnItemClickListener {

	private ProgressDialog dialog;
	private static ListView listView;
	public static final String ICON = "ICON";
	public static final String PRINTERNAME = "PRINTERNAME";
	public static final String PRINTERMAC = "PRINTERMAC";
	private static List<Map<String, Object>> boundedPrinters;

	private static Handler mHandler = null;
	@SuppressWarnings("unused")
	private static String TAG = "ConnectBTMacActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_connectbtpaired);
		ButterKnife.bind(this);

		dialog = new ProgressDialog(this);
		boundedPrinters = getBoundedPrinters();
		listView = (ListView) findViewById(R.id.listViewSettingConnect);
		listView.setAdapter(new SimpleAdapter(this, boundedPrinters,
				R.layout.list_item_printernameandmac,
				new String[]{PRINTERNAME},
				new int[]{R.id.tvListItemPrinterName}));
		listView.setOnItemClickListener(this);

		mHandler = new MHandler(this);
		WorkService.addHandler(mHandler);
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
	protected void onDestroy() {
		super.onDestroy();
		WorkService.delHandler(mHandler);
		mHandler = null;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {
		String address = (String) boundedPrinters.get(position).get(PRINTERMAC);
		dialog.setMessage(MyGlobal.toast_connecting + " " + address);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
		WorkService.workThread.connectBt(address);
	}

	private List<Map<String, Object>> getBoundedPrinters() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			return list;
		}

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ICON, android.R.drawable.stat_sys_data_bluetooth);
				map.put(PRINTERNAME, "    " + device.getName());
				map.put(PRINTERMAC, device.getAddress());
				list.add(map);
			}
		}
		return list;
	}

	@OnClick(R.id.back_already_bluetooth)
	public void onViewClicked() {
		this.finish();
	}

	private static class MHandler extends Handler {

		private WeakReference<ConnectBTPairedActivity> mActivity;

		public MHandler(ConnectBTPairedActivity activity) {
			mActivity = new WeakReference<ConnectBTPairedActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			ConnectBTPairedActivity theActivity = mActivity.get();
			switch (msg.what) {
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
					int result = msg.arg1;
					MyToast.showToast(theActivity,
							(result == 1) ? MyGlobal.toast_success
									: MyGlobal.toast_fail);
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
