package com.xs.utilsbag.file;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-09-26 16:45
 * @email Xs.lin@foxmail.com
 */
public class FileSeparate {


    /**
     * 合并文件
     *
     * @param c
     * @param partFileList 小文件名集合
     * @param dst 目标文件路径
     * @throws IOException
     *
     * @author zuolongsnail
     */
    public static void mergeApkFile(Context c, ArrayList<String> partFileList, String dst) throws IOException {
        Log.e("info", "mergeApkFile: "+new File(dst).exists() );
        if (!new File(dst).exists()) {
            OutputStream out = new FileOutputStream(dst);
            byte[] buffer = new
                    byte[1024];
            FileInputStream in = null;
            int readLen = 0;
            for(int i=0;i<partFileList.size();i++){
                Log.e("info", "for: "+i );
                // 获得输入流
//                in = c.getAssets().open(partFileList.get(i));
//                in = c.openFileInput(partFileList.get(i));
                in = new FileInputStream(partFileList.get(i));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                while((readLen = bufferedInputStream.read(buffer)) != -1){
                    Log.e("info", "while: "+readLen );
                    out.write(buffer, 0, readLen);
                }
                out.flush();
                bufferedInputStream.close();
                in.close();
            }
            // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了
            out.close();
        }
    }

    /**
     * 指定大小分割文件
     */
    public static void Separate(Context context) throws Exception {
        String localPath = FileUtils.getCacheDir(context);
        String localName = "222.mp4";
        // 以每个小文件1024*1024字节即1M的标准来分割
        int split = 1024 * 1024;
        byte[] buf = new
                byte[1024];
        int num = 1;
        // 建立输入流
        File inFile = new File(localPath+localName);
        FileInputStream fis = new FileInputStream(inFile);
        BufferedInputStream bu=new BufferedInputStream(fis);
        while (true) {
            // 以"demo"+num+".db"方式来命名小文件即分割后为demo1.db，demo2.db，。。。。。。
            String filePath = localPath+num+".mp4";
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            for (int i = 0; i < split / buf.length; i++) {
                int read = bu.read(buf);
                if (read != -1)
                    fos.write(buf, 0, read);
                // 判断大文件读取是否结束
                if (read < buf.length) {
                    bu.close();
                    fis.close();
                    fos.close();
                    return;
                }
            }
            fos.close();
            num++;
        }
    }

    /**
     * 文件分割任务
     */
    class SeparateTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
