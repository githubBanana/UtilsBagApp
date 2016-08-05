package com.xs.utilsbag.file;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @version V1.0 <程序访问自己私有目录工具>
 * @author: Xs
 * @date: 2016-07-18 17:01
 * @email Xs.lin@foxmail.com
 */
public class PrivDirOperateUtil {

    private static final String LOG_DIR = "log";
    private static final String LOG_SUFFIX = ".log";
    private static final String PIC_DIR = "pic";
    private static final String PIC_SUFFIX = ".png";
    private static final String DOWNLOAD_DIR = "download";
    private static final String DOWNLOAD_APK_SUFFIX = ".apk";

    /**新建目录
     * return : /data/data/com.xs.utilsbagapp/app_xxx/
     */
    final static String getDir(String dirName,Context context) {
        File file = context.getDir(dirName,Context.MODE_PRIVATE);
        if (file.exists()) {
            return file.getAbsolutePath()+File.separator;
        }
        return null;
    }

    /**
     * return : /data/data/com.xs.utilsbagapp/cache/
     */
    final static String getCache(Context context) {
        return context.getCacheDir()+File.separator;
    }

    /**
     * return : /data/data/com.xs.utilsbagapp/files/
     */
     final static String getFilesDir(Context context) {
         return context.getFilesDir()+File.separator;
     }

    /**
     * newFile
     * @param DirNmae
     * @param name
     * @param context
     * @return
     */
    final static String newFile(String DirNmae,String name,Context context) {
        File logDir = new File(getCache(context)+DirNmae);
        if (!logDir.exists() || !logDir.isDirectory()) {
            logDir.mkdirs();
        }
        String logRealName = logDir.getAbsolutePath()+File.separator+name;
        File log = new File(logRealName);
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return log.getAbsolutePath();
    }

    /**
     * new 一个log/png/apk文件
     * @param logName
     * @param context
     * @return
     */
    public static String newLogFile(String logName,Context context) {
        return newFile(LOG_DIR,logName+LOG_SUFFIX,context);
    }
    public static String newPngFile(String pngName,Context context) {
        return newFile(PIC_DIR,pngName+PIC_SUFFIX,context);
    }
    public static String newApkFile(String apkName,Context context) {
        return newFile(DOWNLOAD_DIR,apkName+DOWNLOAD_APK_SUFFIX,context);
    }

    public static void doLogW(String fileName,String message,Context context) {
        FileInputStream in = null;
        try {
            in = context.openFileInput(newLogFile(fileName,context));
            in.read(message.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e("ds", "doLogW: "+in.toString() );
            IOUtils.close(in);

        }
    }
}
