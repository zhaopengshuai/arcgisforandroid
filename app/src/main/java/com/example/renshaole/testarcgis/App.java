package com.example.renshaole.testarcgis;

import android.app.Application;
import android.os.Handler;


/**
 *
 * Created by xiemingtian on 2017/6/19.
 */

public class App extends Application {
    // 共享变量
    private Handler mHandler = null;

    // set方法
    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    // get方法
    public Handler getmHandler() {
        return mHandler;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }




}
