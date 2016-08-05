package com.xs.utilsbag.bm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.xs.utilsbag.file.FileUtils;
import com.xs.utilsbag.file.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @version V1.0 <裁剪 + 一次压缩工具>
 * @author: Xs
 * @date: 2016-08-05 14:22
 * @email Xs.lin@foxmail.com
 */
public class BmCutAndCompressUtil {
    private static final String TAG = "BmCutAndCompressUtil";

    private int     _cutInSampleSize     = 4;   //裁剪1/x         4
    private int     _cutLimit            = 2000;//裁剪临界值      2000
    private float   _compressLimitMb     = 3f;  //压缩放临界值    3f
    private File    _outFile             = null;//输出文件
    private Context context;

    public void justDo(String inputFilePath,IBmCutCompressCallBack iBmCutCompressCallBack,Context context) {
        this.context = context;
        new CutTask(iBmCutCompressCallBack).execute(inputFilePath);
    }

    class CutTask extends AsyncTask<String,Void,Exception> {

        private IBmCutCompressCallBack iBmCutCompressCallBack;

        public CutTask(IBmCutCompressCallBack iBmCutCompressCallBack) {
            this.iBmCutCompressCallBack = iBmCutCompressCallBack;
        }

        @Override
        protected Exception doInBackground(String... strings) {
            return cutAndCompress(strings[0]);
        }

        @Override
        protected void onPostExecute(Exception e) {
            if (e == null)
                iBmCutCompressCallBack.onSuccess(_outFile);
            else {
                iBmCutCompressCallBack.onFailed(e);
            }
            super.onPostExecute(e);
        }

        /**
         * 先判断裁剪后判断压缩质量
         *
         * @param inputFilePath 文件来源
         * @return
         */
        public Exception cutAndCompress(String inputFilePath) {
            /**
             * 裁剪部分
             */
            final String cacheFileName = "upload_cache"+System.currentTimeMillis()+".png";
            boolean cut = false;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(inputFilePath, options);
            Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪前 --> height:" + options.outHeight + " width:" + options.outWidth);
            if (options.outHeight >= _cutLimit || options.outWidth >= _cutLimit) {
                options.inSampleSize = _cutInSampleSize;
                cut = true;
            } else {
                options.inSampleSize = 1;
                cut = false;
            }
            options.inJustDecodeBounds = false;
            Bitmap src = BitmapFactory.decodeFile(inputFilePath, options);
            Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪后 --> height:" + options.outHeight + " width:" + options.outWidth);

            /**
             * 一次压缩部分
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            if (baos.toByteArray().length / (float) 1024 > (_compressLimitMb * 1024)) {
                Log.e(TAG, "压缩前大小 " + baos.toByteArray().length / (float) (1024 * 1024));
                baos.reset();
                src.compress(Bitmap.CompressFormat.JPEG, 70, baos);//30 0.039826393 70 0.07471275 90 0.15085125  99 0.46049404
                Log.e(TAG, "压缩后大小 " + baos.toByteArray().length / (float) (1024 * 1024));
                _outFile = new File(FileUtils.getCacheDir(context), cacheFileName);
                FileOutputStream fileOutputStream = null;
                try {
                    if (!_outFile.exists())
                        _outFile.createNewFile();
                    fileOutputStream = new FileOutputStream(_outFile);
                    baos.writeTo(fileOutputStream);//把压缩后的数据baos存放到FileOutputStream中
                    IOUtils.close(baos);
                    fileOutputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return e;
                } catch (IOException e) {
                    _outFile.delete();
                    e.printStackTrace();
                    return e;
                }
                Log.e(TAG, "裁剪+压缩后大小: " + (_outFile.length() / (float) (1024 * 1024)));
                return null;

            } else {
                if (cut) {//裁剪 + 无压缩
                    _outFile = new File(FileUtils.getCacheDir(context), cacheFileName);
                    FileOutputStream out = null;
                    try {
                        if (!_outFile.exists())
                            _outFile.createNewFile();
                        out = new FileOutputStream(_outFile);
                        if (src.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                            out.flush();
                            out.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return e;
                    } catch (IOException e) {
                        _outFile.delete();
                        e.printStackTrace();
                        return e;
                    }
                    Log.e(TAG, "裁剪+无压缩后大小: " + (_outFile.length() / (float) (1024 * 1024)));
                    return null;
                } else {//无裁剪+无压缩
                    _outFile = new File(inputFilePath);
                    if (!_outFile.exists())
                        try {
                            _outFile.createNewFile();
                        } catch (IOException e) {
                            _outFile.delete();
                            e.printStackTrace();
                            return e;
                        }
                    Log.e(TAG, "无裁剪+无压缩后大小: " + (_outFile.length() / (float) (1024 * 1024)));
                    return null;
                }
            }

        }
    }

    /**
     * 接口回调
     */
    public interface IBmCutCompressCallBack {
        void onSuccess(File file);
        void onFailed(Exception e);
    }




}
