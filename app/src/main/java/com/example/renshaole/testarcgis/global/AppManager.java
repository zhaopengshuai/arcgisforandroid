package com.example.renshaole.testarcgis.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

import cn.jucheng.jclibs.socket.WorkService;
import cn.jucheng.jclibs.tools.MyLog;

/** 应用程序Activity管理类 */
public class AppManager {
	private static final String TAG = "AppManager";
	/** 存放activity实例的堆栈 */
	private static Stack<MyBaseActivity> activityStack = new Stack<MyBaseActivity>();
	private static AppManager instance;
	public String xingMing = "";
	public String xueHao = "";
	public int width = 0;

	private AppManager() {
	}

	/** 单一实例 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/** 添加Activity到堆栈 */
	public void addActivity(MyBaseActivity activity) {
		activityStack.add(activity);
	}

	/** 获取当前Activity（堆栈中最后一个压入的） */
	public Activity currentActivity() {
		try {
			Activity activity = activityStack.lastElement();
			return activity;
		} catch (Exception e) {
			MyLog.e(TAG, "currentActivity():  " + e.getMessage());
			MyLog.e(TAG, "currentActivity():  " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = currentActivity();
		if (activity != null)
			finishActivity((MyBaseActivity) activity);
	}

	/** 结束指定的Activity */
	public void finishActivity(MyBaseActivity activity) {
		try {
			if (activity != null) {
				// synchronized(activity){
				Boolean isFinishActivity = false;
				Iterator<MyBaseActivity> keys = activityStack.iterator();
				MyBaseActivity key = null;
				while (keys.hasNext()) {
					key = (MyBaseActivity) keys.next();
					if (key == activity) {
						keys.remove();
						isFinishActivity = true;
					}
				}
				if (!isFinishActivity)
					MyLog.d(TAG,
							"AppManager.java:finishActivity(activity)结束Activity失败！");
				// activityStack.remove(activity);
				activity.finish();
				activity = null;
			}

		} catch (Exception e) {
			MyLog.e(TAG, "finishActivity()1:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/** 结束指定类名的Activity */
	public void finishActivity(Class<?> cls) {
		try {
			for (MyBaseActivity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.e(TAG, "finishActivity()2:  " + e.getMessage());
		}
	}

	/** 结束所有Activity */
	public void finishAllActivity() {
		try {
			Iterator<MyBaseActivity> keys = activityStack.iterator();
			MyBaseActivity key = null;
			while (keys.hasNext()) {
				key = (MyBaseActivity) keys.next();
				keys.remove();
				if (key != null) {
					key.finish();
					key = null;
				}
			}
			activityStack.clear();
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.e(TAG, "finishAllActivity()3:  " + e.getMessage());
		}

	}

	/** 退出应用程序 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			// OgreSampleBrowserActivity.setMythis(null);

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.e(TAG, "AppExit():  " + e.getMessage());
		} finally {
			WorkService.workThread.disconnectBt();
			WorkService.workThread.disconnectNet();
			WorkService.workThread.disconnectUsb();
			MyLog.onExit();
		}
	}
}