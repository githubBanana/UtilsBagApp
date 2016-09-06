package com.xs.utilsbag;

import android.app.Application;
import android.content.Context;

import com.xs.utilsbag.exception.CrashHandler;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-09-06 15:00
 * @email Xs.lin@foxmail.com
 */
public class UtilsBagApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        CrashHandler.instance().init(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
