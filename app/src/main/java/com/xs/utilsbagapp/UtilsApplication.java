package com.xs.utilsbagapp;

import android.app.Application;

import com.xs.utilsbag.exception.CrashHandler;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-07-05 16:27
 * @email Xs.lin@foxmail.com
 */
public class UtilsApplication extends Application {
    private static final String TAG = "UtilsApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        CrashHandler.instance().init(this);
    }
}
