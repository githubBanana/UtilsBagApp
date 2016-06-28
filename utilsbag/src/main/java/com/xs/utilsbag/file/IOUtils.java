package com.xs.utilsbag.file;

import java.io.Closeable;
import java.io.IOException;

/**
 * 项目 : ShanMoDaYe_Android
 * 作者 : HYC
 * 时间 : 2015/11/2 20:19
 * 功能 : todo
 */
public class IOUtils {

        /** 关闭流 */
        public static boolean close(Closeable io) {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    //LogUtils.e(e);
                }
            }
            return true;
        }
}
