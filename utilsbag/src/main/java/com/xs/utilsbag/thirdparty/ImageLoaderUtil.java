package com.xs.utilsbag.thirdparty;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xs.utilsbag.file.FileUtils;

import java.io.File;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-03-29 16:22
 * @email Xs.lin@foxmail.com
 */
public class ImageLoaderUtil {
    private static final String TAG = "ImageLoaderUtil";

    public static DisplayImageOptions mNormalImageOptions;

    public static void init(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 10);
        MemoryCache memoryCache;
        File cacheDir = new File(FileUtils.getCacheDir(context),"imageloader/Cache");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }
        mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
                .resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                defaultDisplayImageOptions(mNormalImageOptions)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiskCache(cacheDir))

                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(memoryCache)
                        // .memoryCacheSize(memoryCacheSize)
                .tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public  static void showRoundComponent(String uri, ImageView iv){
        DisplayImageOptions build = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer((int) 27.5f))
                .showImageOnLoading(android.R.color.transparent)
                /**
                 * 根据实际情况设置默认图片
                 */
            /*    .showImageForEmptyUri(R.mipmap.ic_default_head)
                .showImageOnFail(R.mipmap.ic_default_head)*/
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(uri,iv,build);
    }
}
