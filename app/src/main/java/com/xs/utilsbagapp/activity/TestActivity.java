package com.xs.utilsbagapp.activity;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xs.utilsbag.file.PrivDirOperateUtil;
import com.xs.utilsbag.phone.ScreenUtil;
import com.xs.utilsbagapp.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = TestActivity.class.getSimpleName();

    private TextView        mTvShow;
    private Button          mBtnTestDip2px;
    private Button          mBtnTestCrashLog;
    private ImageView       mIvImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTvShow = (TextView) findViewById(R.id.tv_show);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mBtnTestCrashLog = (Button) findViewById(R.id.btn_test_crashlog);
        mBtnTestDip2px = (Button) findViewById(R.id.btn_test_dip2px);

        mBtnTestDip2px.setOnClickListener(this);
        mBtnTestCrashLog.setOnClickListener(this);

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
                PrivDirOperateUtil.doLogW("myname","dssdsd1234\n\n",this);


/*
                File file = new File(getCacheDir()+File.separator+"test.png");
                FileInputStream in = null;
                FileOutputStream out = null;
                in = new FileInputStream(file);
                out = new FileOutputStream()*/


                break;
            case R.id.btn_test_crashlog:
                testException();
                break;
        }
    }

    /**
     * 测试异常情况
     */
    private void testException() {
        int a = 10;
        int b = 0;
        try {
            Toast.makeText(TestActivity.this,a / b,Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            e.printStackTrace();
            a = 11;
        }
        a = 12;
        Log.e(TAG, "testException: "+a );
    }


}
