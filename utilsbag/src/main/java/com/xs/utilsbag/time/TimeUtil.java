package com.xs.utilsbag.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-07-05 16:54
 * @email Xs.lin@foxmail.com
 */
public class TimeUtil {

    /**
     * 获取时间戳
     * @return
     */
    public static long getCurrTime() {
        return  (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取当前详细时间 string格式
     * @return
     */
    public static String getCurrFormatTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currTime = format.format(date);
        return currTime;
    }

}
