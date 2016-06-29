package com.xs.utilsbag.bm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.xs.utilsbag.file.FileUtils;
import com.xs.utilsbag.file.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @version V1.0 <图片缩放工具类>
 * @author: Xs
 * @date: 2016-06-28 10:53
 * @email Xs.lin@foxmail.com
 */
public class BmScaleUtil {
    private static final String TAG = BmScaleUtil.class.getSimpleName();
    /** Need to know

     1.int inSampleSize
     If set to a value > 1, requests the decoder to subsample the original image, returning a smaller image to
     save memory. The sample size is the number of pixels in either dimension that correspond to a single pixel
     in the decoded bitmap. For example, inSampleSize == 4 returns an image that is 1/4 the width/height of the
     original, and 1/16 the number of pixels. Any value <= 1 is treated the same as 1. Note: the decoder uses a
     final value based on powers of 2, any other value will be rounded down to the nearest power of 2.

     2.boolean inJustDecodeBounds
     If set to true, the decoder will return null (no bitmap), but the out... fields will still be set, allowing
     the caller to query the bitmap without having to allocate the memory for its pixels.

     **/

    /**
     * 计算缩放值inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) {
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 按比例缩放图片 from 资源文件
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options);
        return createScaleBitmap(src, reqWidth, reqHeight);//生成适配控件大小的bitmap
    }

    /**
     * 按比例缩放图片 from SD File文件
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName,
                                                     int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    /**
     * 缩放图片 指定缩放比例
     * @param pathName
     * @param inSampleSize 裁剪比例
     * @param limit 裁剪界限
     * @return
     */
    public static Bitmap decodeSampledBitmap(String pathName,int inSampleSize,int limit) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪前 --> height:"+options.outHeight+" width:"+options.outWidth );
        if (options.outHeight >= limit || options.outWidth >= limit)
            options.inSampleSize = inSampleSize;
        else
            options.inSampleSize = 1;
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪后 --> height:"+options.outHeight+" width:"+options.outWidth );
        return src;
    }


    /**
     * 缩放图片质量
     * @param image
     * @return
     */
    public static Bitmap compressImageToBitmap(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.e(TAG, "compressImageToBitmap: "+baos.toByteArray().length/(float)1024 );
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options <= 0 )
                break;
            options -= 10;// 每次都减少10
            options = options <= 0 ? 0 : options;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 放大或缩小图片
     * @param bitmap 源图片
     * @param ratio 放大或缩小的倍数，大于1表示放大，小于1表示缩小
     * @return Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, float ratio) {
        if (ratio < 0f) {return bitmap;}
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * 先裁剪再压缩
     * @param path
     * @param cutInSampleSize
     * @param cutLimit
     * @param compressLimitMb
     * @param context
     * @return
     */
    public static File cutAndCompress(String path,int cutInSampleSize,int cutLimit,float compressLimitMb, Context context)
    {
        /*----裁剪---*/
        final String cacheFileName = "upload_cache.png";
        boolean cut = false;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪前 --> height:"+options.outHeight+" width:"+options.outWidth );
        if (options.outHeight >= cutLimit || options.outWidth >= cutLimit) {
            options.inSampleSize = cutInSampleSize;
            cut = true;
        }
        else {
            options.inSampleSize = 1;
            cut = false;
        }
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(path, options);
        Log.e(TAG, "decodeSampledBitmap: 尺寸裁剪后 --> height:"+options.outHeight+" width:"+options.outWidth );

        /*----压缩---*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        if (baos.toByteArray().length / (float)1024 > (compressLimitMb * 1024)) {
            Log.e(TAG, "compressImageToFile: ing "+baos.toByteArray().length/(float)(1024*1024) );
            baos.reset();
            src.compress(Bitmap.CompressFormat.JPEG,70,baos);//30 0.039826393 70 0.07471275 90 0.15085125  99 0.46049404
            Log.e(TAG, "compressImageToFile: ing2 "+baos.toByteArray().length/(float)(1024*1024) );
            File file = new File(FileUtils.getCacheDir(context),cacheFileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                baos.writeTo(fileOutputStream);//把压缩后的数据baos存放到FileOutputStream中
                IOUtils.close(baos);
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "裁剪+压缩后: "+(file.length() /(float)(1024*1024)));
            return file;

        } else {
            if (cut) {//已裁剪 无压缩
                File photoFile = new File(FileUtils.getCacheDir(context), cacheFileName);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(photoFile);
                    if (src != null) {
                        if (src.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                            out.flush();
                        }
                    }
                } catch (FileNotFoundException e) {
                    photoFile.delete();
                    e.printStackTrace();
                } catch (IOException e) {
                    photoFile.delete();
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return photoFile;
            } else {//无裁剪无压缩
                return new File(path);
            }
        }


    }



}
