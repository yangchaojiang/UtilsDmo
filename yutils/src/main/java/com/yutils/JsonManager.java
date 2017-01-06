/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.yutils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.util.TypeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yangjiang on 2016/12/7.
 * E-Mail:1007181167@qq.com
 * Description:[JSON解析管理类]  使用 fastjson
 **/
public class JsonManager {

    static {
        TypeUtils.compatibleWithJavaBean = true;
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return T
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return List<T>
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj  转化的对象实例
     * @return String  JSON 字符
     */
    public static String beanToJson(Object obj) {
        String result = JSON.toJSONString(obj);
        Log.d("json", result);
        return result;
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj  转化的对象实例
     * @param    filter 序列化处理
     * @return String  JSON 字符
     */
    public static String beanToJson(Object obj, SimplePropertyPreFilter filter) {
        String result = JSON.toJSONString(obj, filter);
        Log.v("json", result);
        return result;
    }

    /**
     * 判断JSONObject中是否包含指定的key
     */
    public static String getJsonValue(JSONObject jsonObject, String str) {
        try {
            if (jsonObject.has(str)) {
                return jsonObject.getString(str);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}


