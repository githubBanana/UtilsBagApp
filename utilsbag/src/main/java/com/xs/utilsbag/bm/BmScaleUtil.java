package com.xs.utilsbag.bm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

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
     * @param inSampleSize
     * @return
     */
    public static Bitmap decodeSampledBitmap(String pathName,int inSampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return src;
    }

    /**
     * 按质量压缩图片 并转化为File
     * @param image
     * @param context
     * @return
     */
    public static File compressImageToFile(Bitmap image, Context context)
    {
        File file = new File(FileUtils.getCacheDir(context),"yasuo.png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options <= 0 )
                break;
            options -= 10;// 每次都减少10
            options = options <= 0 ? 0 : options;
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//        return bitmap;
        try {
            baos.writeTo(fileOutputStream);//把压缩后的数据baos存放到FileOutputStream中
            IOUtils.close(baos);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
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




}
