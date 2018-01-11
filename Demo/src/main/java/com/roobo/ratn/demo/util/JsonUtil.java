package com.roobo.ratn.demo.util;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    public static String toJsonString(Object value) {
        String Str = JSON.toJSONString(value);
        return Str;
    }

    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}