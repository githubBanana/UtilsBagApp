package com.xs.utilsbag.exception;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.xs.utilsbag.R;
import com.xs.utilsbag.file.FileUtils;
import com.xs.utilsbag.phone.PhoneUtil;
import com.xs.utilsbag.time.TimeUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @version V1.0 <全局捕获异常>
 * @author: Xs
 * @date: 2016-07-05 15:51
 * @email Xs.lin@foxmail.com
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler mCrashHandler = null;
    private CrashHandler(){}
    private Context mContext;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler ;
    public static CrashHandler instance() {
        if (mCrashHandler == null) {
            synchronized (CrashHandler.class) {
                mCrashHandler = new CrashHandler();
            }
        }
        return mCrashHandler;
    }

    public void init(Application app) {
        mContext = app.getApplicationContext();
        // 获取系统默认的异常处理器
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 重新设置异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handlerException(throwable) && mUncaughtExceptionHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    private boolean handlerException(final Throwable t) {
        if (t == null)
            return false;

        t.printStackTrace();
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append(PhoneUtil.getMobileInfo(mContext))
                .append(PhoneUtil.getVersionInfo(mContext))
                .append(getErrorInfo(t));
        Log.e("CrashHandler", "handlerException: "+errorBuilder.toString() );
        FileUtils.writeStringToFile(TimeUtil.getCurrTime(),errorBuilder.toString(),mContext);
        System.exit(0);
        return true;
    }

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        StringBuilder error = new StringBuilder();
        error.append(mContext.getString(R.string.error_info)+"\n");
        error.append(writer.toString());
        return error.toString();
    }

}
