package com.xs.utilsbag.general;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-09-01 11:59
 * @email Xs.lin@foxmail.com
 */
public class GsonUtil {

    /**
     * 将bean对象转化为map
     * @param entity
     * @return
     */
    public Map<String ,Object> objToMap(Object entity) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(entity), type);
    }

}
