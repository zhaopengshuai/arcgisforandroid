package com.example.renshaole.testarcgis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.renshaole.testarcgis.global.MyBaseActivity;
import com.example.renshaole.testarcgis.widget.MyMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jucheng.jclibs.socket.MyGlobal;
import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.IPString;
import cn.jucheng.jclibs.tools.MyToast;


/**
 * 连接网络设备
 */
public class ConnectIPActivity extends MyBaseActivity {

	private static Handler mHandler = null;
	@SuppressWarnings("unused")
	private static String TAG = "ConnectIPActivity";

	//	@BindView(R.id.editTextInputClientAddr)
	EditText inputClientApp;
	/**
	 * ip
	 */
//	@BindView(R.id.editTextInputIp)
	EditText inputIp;
	/**
	 *
	 */
//	@BindView(R.id.editTextInputPort)
	EditText inputPort;

	Button btnConnect;
	/**
	 *
	 */

	@BindView(R.id.relativeLayout1)
    LinearLayout relativeLayout1;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_connectip);
		ButterKnife.bind(this);

		inputClientApp =findViewById(R.id.editTextInputClientAddr);
		inputIp =findViewById(R.id.editTextInputIp);
		inputPort= findViewById(R.id.editTextInputPort);
		btnConnect = findViewById(R.id.buttonConnectIP);
		btnConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean valid = false;
				int port = 9100;
				String ip = "";
				String clientAddr = "";
				try {
					clientAddr = inputClientApp.getText().toString();
					ip = inputIp.getText().toString();
					if (null == IPString.IsIPValid(ip))
						throw new Exception("不合法的IP地址！");
					port = Integer.parseInt(inputPort.getText().toString());
					valid = true;
				} catch (NumberFormatException e) {
					MyToast.showToast(ConnectIPActivity.this, "不合法的端口号！");
					valid = false;
				} catch (Exception e) {
					MyToast.showToast(ConnectIPActivity.this, "不合法的IP地址！");
					valid = false;
				}
				if (valid) {
					//保存到xml文件中
					setCurrentEntityAddrCode(clientAddr);
					datas.setData(MyGlobal.PREFERENCES_IPADDRESS, ip);
					datas.setData(MyGlobal.PREFERENCES_PORTNUMBER, port);
					dialog.setMessage(MyGlobal.toast_connecting + " " + ip + ":"
							+ port);
					dialog.setIndeterminate(true);
					dialog.setCancelable(false);
					dialog.show();
					WorkService.workThread.connectNet(ip, port);
					Intent intent = new Intent(ConnectIPActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		});
		dialog = new ProgressDialog(this);

		mHandler = new MHandler(this);
		WorkService.addHandler(mHandler);

		String clientAddr = getCurrentEntityAddrCode();
		inputClientApp.setText(clientAddr);
		inputIp.setText(datas.getData(MyGlobal.PREFERENCES_IPADDRESS, ""));
		inputPort.setText(""
				+ datas.getIntData(MyGlobal.PREFERENCES_PORTNUMBER, 9100));

		hideSoftKeyBoard();
	}

	/**
	 * 关闭软键盘
	 */
	public void hideSoftKeyBoard() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 强制隐藏软键盘
		manager.hideSoftInputFromWindow(inputPort.getWindowToken(), 0);
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

	@OnClick({R.id.back_network, R.id.buttonConnectIP})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.back_network://返回
				finish();
				break;
			case R.id.buttonConnectIP://连接
				boolean valid = false;
				int port = 9100;
				String ip = "";
				String clientAddr = "";
				try {
					clientAddr = inputClientApp.getText().toString();
					if (null == IPString.IsIPValid(ip))
						throw new Exception("不合法的IP地址！");
					port = Integer.parseInt(inputPort.getText().toString());
					ip = inputIp.getText().toString();
					valid = true;
				} catch (NumberFormatException e) {
					MyToast.showToast(this, "不合法的端口号！");
					valid = false;
				} catch (Exception e) {
					MyToast.showToast(this, "不合法的IP地址！");
					valid = false;
				}
				if (valid) {
					//将clientAddr存储到xml文件中
					setCurrentEntityAddrCode(clientAddr);
					datas.setData(MyGlobal.PREFERENCES_IPADDRESS, ip);
					datas.setData(MyGlobal.PREFERENCES_PORTNUMBER, port);
					dialog.setMessage(MyGlobal.toast_connecting + " " + ip + ":"
							+ port);
					dialog.setIndeterminate(true);
					dialog.setCancelable(false);
					dialog.show();
					WorkService.workThread.connectNet(ip, port);
				}
				break;
		}
	}

	private static class MHandler extends Handler {

		private WeakReference<ConnectIPActivity> mActivity;

		public MHandler(ConnectIPActivity activity) {
			mActivity = new WeakReference<ConnectIPActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			ConnectIPActivity theActivity = mActivity.get();
			switch (msg.what) {
				case MyGlobal.MSG_WORKTHREAD_SEND_CONNECTNETRESULT: {
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

	public  String getCurrentEntityAddrCode() {
		String entiyAddrCode = "";
		try {
			/*DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(getAssets().open("src/main/ClientConfig.xml"));
			Element element = document.getDocumentElement();*/
			String path = Environment.getExternalStorageDirectory().toString();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			InputStream inputStream;
			inputStream =new FileInputStream(path + "/ClientConfig.xml");
			Document document=builder.parse(inputStream);
			Element element = document.getDocumentElement();
			entiyAddrCode = element.getElementsByTagName("EntityAddrCode").item(0).getTextContent();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  entiyAddrCode;
	}

	public void setCurrentEntityAddrCode(String entityAddrCode) {
		try {
			String path = Environment.getExternalStorageDirectory().toString();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			InputStream inputStream;
			inputStream =new FileInputStream(path + "/ClientConfig.xml");
			Document document=builder.parse(inputStream);
			Element element = document.getDocumentElement();
			NodeList nodeList = element.getElementsByTagName("EntityAddrCode");
			nodeList.item(0).setTextContent(entityAddrCode);
			saveXml(document, path+ "/ClientConfig.xml");
			System.out.println("XML file updated successfully");
			MyMessage.MSG_ENTITYADDRCODE = entityAddrCode;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveXml(Document document, String filePath){

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {

			TransformerFactory transfactory = TransformerFactory.newInstance();
			Transformer transformer = transfactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");// 设置输出采用的编码方式
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");// 是否自动添加额外的空白
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");// 是否忽略XML声明

			FileOutputStream fos = new FileOutputStream(filePath);
			Source source = new DOMSource(document);
			Result result = new StreamResult(fos);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}