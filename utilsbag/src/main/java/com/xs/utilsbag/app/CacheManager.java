package com.xs.utilsbag.app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;

/**
 * @version V1.0 <子线程清除缓存>
 * @author: Xs
 * @date: 2016-08-18 11:02
 * @email Xs.lin@foxmail.com
 */
public class CacheManager {

    private OnListenCacheManager _listenCheck;
    private OnListenClearCache  _listenClear;

    public void justCheckSize(final String[] dirPath,OnListenCacheManager listen) {
        this._listenCheck = listen;
        new ExectorCheckTask().execute(dirPath);
    }

    public void justClear(final String[] dirpath,OnListenClearCache listen){
        this._listenClear = listen;
        new ExectorClearTask().execute(dirpath);

    }

    /**
     * 检查文件大小任务
     */
    class ExectorCheckTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            File[] files = new File[strings.length];
            try {
                for (int i = 0; i < strings.length; i++) {
                    String path = strings[i];
                    files[i] = new File(path);
                }
                return getCacheSize(files);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (string == null) {
            } else {
                _listenCheck.onSuccess(string);
            }
        }
    }

    /**
     * 清除缓存任务
     */
    class ExectorClearTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            File[] files = new File[params.length];
            for (int i = 0; i < params.length; i++) {
                files[i] = new File(params[i]);
            }
            deleteFolderFile(files);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            _listenClear.onSuccess();
        }
    }

    private String getCacheSize(File... files) throws Exception {
        return getFormatSize(getFolderSize(files));
    }

    /**
     *  获取文件
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * @param files
     * @return
     * @throws Exception
     */
    private long getFolderSize(File... files) throws Exception {
        long size = 0;
        for (File file :
                files) {
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    Log.e("info", "getFolderSize: "+fileList[i].getAbsolutePath() );
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param
     */
    private void deleteFilesByDirectory(String... dirStr) {
        for (String path:
             dirStr) {
            File directory = new File(path);
            if (directory != null && directory.exists() && directory.isDirectory()) {
                for (File item : directory.listFiles()) {
                    item.delete();
                }
            }
        }
    }

    /**
     * 删除指定目录下文件或目录
     * @param files
     */
    public static void deleteFolderFile(File... files) {
        if (files.length <= 0)
            return;
        for (File file :
                files) {
            try {
                if (file.isDirectory()) {// 目录
                    if (file.listFiles().length == 0) {//目录为空
                        file.delete();
                    } else {
                        File filess[] = file.listFiles();
                        deleteFolderFile(filess);
                    }
                } else { //文件
                    file.delete();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    /**
     * 监听接口
     */
    public interface OnListenCacheManager {
        void onSuccess(String size);
    }
    public interface OnListenClearCache {
        void onSuccess();
    }

}
