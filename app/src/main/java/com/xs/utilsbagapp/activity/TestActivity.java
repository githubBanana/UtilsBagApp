package com.xs.utilsbagapp.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xs.utilsbag.app.CacheManager;
import com.xs.utilsbag.bm.BmCutAndCompressUtil;
import com.xs.utilsbag.encry.DESUtil;
import com.xs.utilsbag.file.FileUtils;
import com.xs.utilsbag.general.SPUtil;
import com.xs.utilsbag.phone.ScreenUtil;
import com.xs.utilsbag.time.UnixTimeStamp;
import com.xs.utilsbagapp.R;

import java.io.File;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = TestActivity.class.getSimpleName();

    private TextView        mTvShow;
    private TextView        mTvLogShow;
    private Button          mBtnCacheSize,mBtnClearCache;
    private Button          mBtnTestDip2px;
    private Button          mBtnTestCrashLog;
    private ImageView       mIvImg;
    private Button          mBtnTimeStamp;
    private Button          mBtnSPUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTvShow = (TextView) findViewById(R.id.tv_show);
        mTvLogShow = (TextView) findViewById(R.id.tv_logshow);
        mBtnCacheSize = (Button) findViewById(R.id.btn_cachesize);
        mBtnClearCache = (Button) findViewById(R.id.btn_clearcache);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mBtnTestCrashLog = (Button) findViewById(R.id.btn_test_crashlog);
        mBtnTestDip2px = (Button) findViewById(R.id.btn_test_dip2px);
        mBtnTimeStamp = (Button) findViewById(R.id.btn_timestamp);
        mBtnSPUtil = (Button) findViewById(R.id.btn_sputils);

        mBtnTestDip2px.setOnClickListener(this);
        mBtnTestCrashLog.setOnClickListener(this);
        mBtnTimeStamp.setOnClickListener(this);
        mBtnCacheSize.setOnClickListener(this);
        mBtnClearCache.setOnClickListener(this);
        mBtnSPUtil.setOnClickListener(this);

        Glide.with(mIvImg.getContext()).load("http://upfiles.b0.upaiyun.com/support/third/tupianchuli/helpex1.jpg").asBitmap().centerCrop().placeholder(android.R.drawable.ic_menu_edit)
                .override(300,300).into(mIvImg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_dip2px:
                /**
                 * 控件 getHeight() 时获取到的高宽是px值了
                 */
                mTvShow.setText(String.valueOf(ScreenUtil.dip2px(mIvImg.getHeight())));
                Log.e(TAG, "getExternalStorageDirectory: "+ Environment.getExternalStorageDirectory() );
                Log.e(TAG, "onClick: "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) );
//                PrivDirOperateUtil.doLogW("myname","dssdsd1234\n\n",this);

                break;
            case R.id.btn_test_crashlog:
//                testException();
//                String path = "/storage/emulated/0/DCIM/Camera/abc.jpg";
                String path = "/storage/emulated/0/DCIM/Camera/abc.jpg";

                new BmCutAndCompressUtil().justDo(path, new BmCutAndCompressUtil.IBmCutCompressCallBack() {
                    @Override
                    public void onSuccess(File file) {
                        Log.e(TAG, "onSuccess: "+file.getAbsolutePath() );
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.e(TAG, "onSuccess w h : width:"+bitmap.getWidth()+" height:"+bitmap.getHeight() );
                        Toast.makeText(TestActivity.this,file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                        final ImageView mIv = (ImageView) findViewById(R.id.ivtest);
                        mIv.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        e.printStackTrace();
                    }
                },this);
                break;

            case R.id.btn_timestamp:
                testTimeStamp();
//                testException();
                break;
            case R.id.btn_cachesize:
                testCacheManager_getCacheSize();
                break;
            case R.id.btn_clearcache:
                testCacheManager_clearCache();
                break;
            case R.id.btn_sputils:
                SPUtil.saveNormalData(this,"username", DESUtil.encryptAsDoNet("linxiaoshenglinxiaoxiayuping"));
                SPUtil.saveNormalData(this,"password",DESUtil.encryptAsDoNet("ABCabc966500"));
                Log.e(TAG, "onClick: "+DESUtil.decryptDoNet(SPUtil.readNormalData(this,"username"))+"\n"+
                DESUtil.decryptDoNet(SPUtil.readNormalData(this,"password"))+"\n"+
                DESUtil.decryptDoNet(SPUtil.readNormalData(this,"tttt")));
                break;
        }
    }

    /**
     *  时间戳测试
     */
    private void testTimeStamp() {
//        Log.e(TAG, "testTimeStamp: "+ TimeUtil.getCurrTime());
//        Log.e(TAG, "testTimeStamp: "+ UnixTimeStamp.getCurrTime());

        UnixTimeStamp.stampToNormal(UnixTimeStamp.getCurrTime());
//        UnixTimeStamp.normalToStamp("2016-08-08 09:36:15");
    }


    /**
     * 测试异常情况
     */
    private void testException() {
        int a = 10;
        int b = 0;
        Toast.makeText(TestActivity.this,a / b,Toast.LENGTH_LONG).show();
    }

    /**
     * 测试计算缓存大小
     */

    private void testCacheManager_getCacheSize() {
        String[] caches = new String[]{FileUtils.getLogDir(this),FileUtils.getCacheDir(this),getCacheDir().getAbsolutePath()};
        Log.e(TAG, "testCacheManager_getCacheSize: "+getCacheDir().getAbsolutePath() );
/*        Log.e(TAG, "testCacheManager: "+Environment.getExternalStorageDirectory().getAbsolutePath() );
        Log.e(TAG, "getExternalStoragePath: "+FileUtils.getExternalStoragePath(this) );
        Log.e(TAG, "getCachePath: "+FileUtils.getPrivaCachePath(this) );
        Log.e(TAG, "getCacheDir: "+FileUtils.getCacheDir(this) );
        Log.e(TAG, "getIconDir: "+FileUtils.getIconDir(this) );
        Log.e(TAG, "getLogDir: "+FileUtils.getLogDir(this) );
        Log.e(TAG, "getDownloadDir: "+FileUtils.getDownloadDir(this) );*/

        new CacheManager().justCheckSize(caches, new CacheManager.OnListenCacheManager() {
            @Override
            public void onSuccess(String size) {
                mTvLogShow.setText(size);
            }
        });
    }

    /**
     * 测试清除缓存
     */
    private void testCacheManager_clearCache() {
        String[] caches = new String[]{FileUtils.getLogDir(this),FileUtils.getCacheDir(this),getCacheDir().getAbsolutePath()};

        new CacheManager().justClear(caches, new CacheManager.OnListenClearCache() {
            @Override
            public void onSuccess() {
            }
        });


    }


}
