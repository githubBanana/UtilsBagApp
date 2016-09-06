package com.xs.utilsbag.encry;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-09-06 15:17
 * @email Xs.lin@foxmail.com
 */
public class DESUtil {

    /**
     * 8个字符
     */
    public static final String DEFAULT_KEY = "defaultk";

    /**
     * 解密
     *
     * @param message
     * @return
     */
    public static String decryptDoNet(String message) {
        if (TextUtils.isEmpty(message))
            return "";
        byte[] bytesrc = Base64.decode(message.getBytes(), Base64.DEFAULT);
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(DEFAULT_KEY.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(DEFAULT_KEY.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        } catch (Exception e) {
            Log.e("DES", "Exception decryptDoNet=" + message);
            e.printStackTrace();
            return message;
        }
    }

    /**
     * 加密
     *
     * @param message
     * @return
     */
    public static String encryptAsDoNet(String message) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(DEFAULT_KEY.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(DEFAULT_KEY.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptbyte = cipher.doFinal(message.getBytes());
            return new String(Base64.encode(encryptbyte, Base64.DEFAULT));
        } catch (Exception e) {
            return message;
        }
    }

    /**
     * 加密Map
     *
     * @return
     */
    public static Map<String, Object> encryptAsDoMap(Map<String, Object> map) {

        String json = new Gson().toJson(map);
        String encryptContent = DESUtil.encryptAsDoNet(json);
        map.clear();
        Log.e("RequestHelperSample", "encryptMap start:" + json);
        map.put("json", encryptContent);
        Log.e("RequestHelperSample", "encryptMap end:" + encryptContent);
        return map;
    }

    /**
     * 解密Map
     *
     * @param encryptInfo
     * @return
     */
    public static Map<String, Object> decryptAsDoMap(String encryptInfo) {
        String decryptInfo = DESUtil.decryptDoNet(encryptInfo);
        return getMapForJson(decryptInfo);
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.getString(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


}
