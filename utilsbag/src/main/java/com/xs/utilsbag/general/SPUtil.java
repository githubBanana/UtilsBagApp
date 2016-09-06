package com.xs.utilsbag.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.xs.utilsbag.UtilsBagApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author CY
 * @since 2014-1-8
 */
public class SPUtil
{

    public static void cleanAllSharedPreferences()
    {
        //cleanSharedPreferences(SHARE_NAME_HTTPDATA);
        //cleanSharedPreferences(SHARE_NAME_OBJECTDATA);
        //cleanSharedPreferences(SHARE_NAME_NORMALDATA);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(UtilsBagApplication.getContext());
        preferences.edit().putString("key_user", null).commit();
    }

    private static void cleanSharedPreferences(String spName)
    {
        SharedPreferences sharedPreferences = UtilsBagApplication.getContext()
                .getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();
    }

    /**
     * 保存对象
     */
    public static void saveObj(Context context, Object obj)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            // 初始化对象流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入流中，最后到baos流中
            oos.writeObject(obj);// 对象需要序列化 实现Serializable接口
            // 将对象字节转为64位字符串
            String base64Str = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            SharedPreferences shared = context
                    .getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE);
            shared.edit().putString(obj.getClass().getSimpleName(), base64Str).commit();
            Log.e("Shared", obj.getClass().getSimpleName() + "  --");
            oos.close();
            baos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读取对象
     */
    public static Object readObj(Context context, String objName)
    {

        SharedPreferences shared = context
                .getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE);
        String base64Str = shared.getString(objName, null);
        if ( base64Str == null )
        {
            return null;
        }
        byte[] objData = Base64.decode(base64Str, Base64.DEFAULT);

        ByteArrayInputStream bais = new ByteArrayInputStream(objData);
        try
        {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object            obj = ois.readObject();
            bais.close();
            ois.close();
            objData = null;
            return obj;
        } catch (StreamCorruptedException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存对象
     */
    public static synchronized void saveObjByKey(Context context, Object obj, String key)
    {

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 初始化对象流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入流中，最后到baos流中
            oos.writeObject(obj);// 对象需要序列化 实现Serializable接口
            // 将对象字节转为64位字符串
            String base64Str = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            SharedPreferences shared = context
                    .getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE);
            shared.edit().putString(key, base64Str).commit();
            oos.close();
            baos.close();
            //			Log.e("Shared", key + "  --");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读取对象
     */
    public static Object readObjByDB(Context context, String base64Str)
    {
        if ( base64Str == null )
        {
            return null;
        }

        try
        {
            byte[]               objData = Base64.decode(base64Str, Base64.DEFAULT);
            ByteArrayInputStream bais    = new ByteArrayInputStream(objData);
            ObjectInputStream    ois     = new ObjectInputStream(bais);
            Object               obj     = ois.readObject();
            bais.close();
            ois.close();
            objData = null;
            return obj;
        } catch (StreamCorruptedException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取对象
     */
    public static Object readObjByKey(Context context, String key)
    {

        SharedPreferences shared = context
                .getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE);
        String base64Str = shared.getString(key, null);
        if ( base64Str == null )
        {
            return null;
        }

        try
        {
            byte[]               objData = Base64.decode(base64Str, Base64.DEFAULT);
            ByteArrayInputStream bais    = new ByteArrayInputStream(objData);
            ObjectInputStream    ois     = new ObjectInputStream(bais);
            Object               obj     = ois.readObject();
            bais.close();
            ois.close();
            objData = null;
            return obj;
        } catch (StreamCorruptedException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存Http请求的数据
     */
    public static void saveHttpData(Context context, String key, String value)
    {
        context.getSharedPreferences(SHARE_NAME_HTTPDATA, Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }

    /**
     * 读取Http请求的数据
     */
    public static String readHttpData(Context context, String key)
    {
        return context.getSharedPreferences(SHARE_NAME_HTTPDATA, Context.MODE_PRIVATE)
                .getString(key, null);
    }

    /**
     * 保存普通数据
     */
    public static void saveNormalData(Context context, String key, String value)
    {
        context.getSharedPreferences(SHARE_NAME_NORMALDATA, Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }

    public static String readNormalData(Context context, String key)
    {
        return context.getSharedPreferences(SHARE_NAME_NORMALDATA, Context.MODE_PRIVATE)
                .getString(key, null);
    }

    public static int readNormalData(Context context, String key,int def)
    {
        return context.getSharedPreferences(SHARE_NAME_NORMALDATA, Context.MODE_PRIVATE)
                .getInt(key, def);
    }

    public static String readObjData(Context context, String key)
    {
        return context.getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE)
                .getString(key, null);
    }

    public static void saveObjlData(Context context, String key, String value)
    {
        context.getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }

    public static void saveIntData(Context context, String key, int value)
    {
        context.getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE).edit()
                .putInt(key, value).commit();
    }

    public static int readIntData(String key)
    {
        return UtilsBagApplication.getContext()
                .getSharedPreferences(SHARE_NAME_OBJECTDATA, Context.MODE_PRIVATE)
                .getInt(key, -1);
    }

    public static void saveNormalData(Context context, String key, int value)
    {
        context.getSharedPreferences(SHARE_NAME_NORMALDATA, Context.MODE_PRIVATE).edit()
                .putInt(key, value).apply();
    }

    public static int readNormalIntData(Context context, String key)
    {
        return context.getSharedPreferences(SHARE_NAME_NORMALDATA, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }


    public static void saveEntity(Context context, Object obj, String key)
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String            json        = new Gson().toJson(obj);
        String            base        = Base64.encodeToString(json.getBytes(), Base64.DEFAULT);
        preferences.edit().putString(key, base).apply();
    }

    public static Object readEntity(Context context, String key, Class<?> cla)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String            base        = preferences.getString(key, null);
        if ( base == null )
        {
            return null;
        }
        String json = new String(Base64.decode(base, Base64.DEFAULT));
        return new Gson().fromJson(json, cla);
    }

    public static final String SHARE_NAME_HTTPDATA   = "SHARE_NAME_HTTPDATA";
    public static final String SHARE_NAME_OBJECTDATA = "SHARE_NAME_OBJECTDATA";
    public static final String SHARE_NAME_NORMALDATA = "SHARE_NAME_NORMALDATA";
    public static final String SHARE_NAME_CIRCLEDATA = "SHARE_NAME_CIRCLEDATA";
}
