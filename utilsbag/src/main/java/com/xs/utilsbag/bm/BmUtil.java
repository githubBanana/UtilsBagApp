package com.xs.utilsbag.bm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-06-28 17:53
 * @email Xs.lin@foxmail.com
 */
public class BmUtil {


    /**
     * 计算图片占用内存大小 Mb
     * @param bitmap
     * @return
     */
    public static float calculateBitmapSizeMb(Bitmap bitmap) {
        final int bSize = bitmap.getByteCount();//byte
        final float kSize = bSize / (float)1024;//Kb
        final float mSize = kSize / (float)1024;//Mb
        return mSize;
    }

    /**
     * 旋转图片
     * @param bitmap 源图片
     * @param angle 旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @return Bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 创建logo(给图片加水印),
     * @param bitmaps 原图片和水印图片
     * @param left 左边起点坐标
     * @param top 顶部起点坐标t
     * @return Bitmap
     */
    public static Bitmap createLogo(Bitmap[] bitmaps, int left, int top) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmaps[0].getWidth(), bitmaps[0].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        for (int i = 0; i < bitmaps.length; i++) {
            if (i == 0) {
                canvas.drawBitmap(bitmaps[0], 0, 0, null);
            } else {
                canvas.drawBitmap(bitmaps[i], left, top, null);
            }
            canvas.save(Canvas.ALL_SAVE_FLAG);canvas.restore();
        }
        return newBitmap;
    }


    /**
     * Save image to the SD card
     * @param path
     * @param photoName
     * @param photoBitmap
     */
    public static void savePhotoToSDCard(String path, String photoName, Bitmap photoBitmap) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName); //在指定路径下创建文件
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
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
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将bitmap转化为圆形bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap toOvalBitmap(Bitmap bitmap) {
        Bitmap output=Bitmap.createBitmap(bitmap.getHeight(),bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(output);
        Paint paint=new Paint();
        Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        RectF rectF=new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect,rectF, paint);
        return output;
    }
    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static  Bitmap getBitmap(String u) {
        Bitmap mBitmap = null;
        try {
            java.net.URL url;
            url = new java.net.URL(u);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            InputStream is;
            is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 将base64转换成bitmap图片
     *
     * @param str base64字符
     * @return bitmap
     */
    public static Bitmap getBitmapFromByte(String str) {
        if (str == null) {
            return null;
        }
        Bitmap bitmap = null;
        byte[] bytes = Base64.decode(str,Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 将bitmap转换成base64字符
     *
     * @param bitmap
     * @return base64 字符
     */
    public static String bitmaptoString(Bitmap bitmap, int bitmapQuality) {

        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
//		 string = Base64.encodeToString(bytes, Base64.DEFAULT);
        string = Base64.encodeToString(bytes,Base64.DEFAULT);
//		string = Base64.encode(bytes);
        return string;
    }


    /**
     * 回收bitmap
     * @param bitmap
     */
    public static void bmRecycle(Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }




}
