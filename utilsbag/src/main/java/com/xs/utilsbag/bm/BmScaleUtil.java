package com.xs.utilsbag.bm;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
     * 缩放图片 from 资源文件
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
     * 缩放图片 from SD File文件
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
     * 计算图片占用内存大小 Mb
     * @param bitmap
     * @return
     */
    public static int calculateBitmapSizeMb(Bitmap bitmap) {
        final int bSize = bitmap.getByteCount();//byte
        final int kSize = bSize / 1024;//Kb
        final int mSize = kSize / 1024;//Mb
        return mSize;
    }

}
