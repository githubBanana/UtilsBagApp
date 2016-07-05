package com.xs.utilsbagapp.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xs.utilsbag.phone.ScreenUtil;
import com.xs.utilsbagapp.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final ImageView mBtn = (ImageView) findViewById(R.id.btn_touch);
        final TextView mTv = (TextView) findViewById(R.id.tv_log);
        final StringBuilder log = new StringBuilder();
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.append(ScreenUtil.dip2px(mBtn.getHeight()));
                mTv.setText(log.toString());
            }
        });
    }
}
