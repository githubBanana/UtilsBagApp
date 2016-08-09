package com.xs.utilsbag.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-07-05 16:54
 * @email Xs.lin@foxmail.com
 */
public class TimeUtil {

    /**
     * 获取当前详细时间 string格式
     * @return
     */
    public static String getCurrTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

}
