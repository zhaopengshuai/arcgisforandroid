package com.example.renshaole.testarcgis.widget;

import android.content.Context;

import cn.jucheng.jclibs.tools.ShareUtils;

public class MyShareUtils extends ShareUtils {
	protected MyShareUtils(Context context) {
		super(context);
		initDatas();
	}

	public static MyShareUtils getInstances(Context context) {
		if (_this == null) {
			_this = new MyShareUtils(context);
		}
		return _this;
	}

	private static MyShareUtils _this = null;
	private static final Boolean C_D_DEBUG = false;
	private static final Boolean C_D_SHEBEI_LANYA = true;
	private static final Boolean C_D_SHEBEI_WIFI = true;
	private static final Boolean C_D_SHEBEI_USB = false;
	private static final String C_D_DEVICE_ADDRESS = "";
	private static final String C_D_DEVICE_NAME = "";

	/** 数据初始化 */
	private void initDatas() {
		// 默认数据
		if (!sharedPreferences.contains(MyGlobal1.C_DEBUG))
			setData(MyGlobal1.C_DEBUG, C_D_DEBUG);
		if (!sharedPreferences.contains(MyGlobal1.C_SHEBEI_LANYA))
			setData(MyGlobal1.C_SHEBEI_LANYA, C_D_SHEBEI_LANYA);
		if (!sharedPreferences.contains(MyGlobal1.C_SHEBEI_WIFI))
			setData(MyGlobal1.C_SHEBEI_WIFI, C_D_SHEBEI_WIFI);
		if (!sharedPreferences.contains(MyGlobal1.C_SHEBEI_USB))
			setData(MyGlobal1.C_SHEBEI_USB, C_D_SHEBEI_USB);
		if (!sharedPreferences.contains(MyGlobal1.C_DEVICE_ADDRESS))
			setData(MyGlobal1.C_DEVICE_ADDRESS, C_D_DEVICE_ADDRESS);
		if (!sharedPreferences.contains(MyGlobal1.C_DEVICE_NAME))
			setData(MyGlobal1.C_DEVICE_NAME, C_D_DEVICE_NAME);
	}

}
