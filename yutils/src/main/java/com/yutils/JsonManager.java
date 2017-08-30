/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.yutils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.util.TypeUtils;
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
        Log.d("JsonManager", result);
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
        Log.v("JsonManager", result);
        return result;
    }


    /**
     * 价格json字符转换JSONObject
     * @param   jsonString   需要转换的字符
     *@return     JSONObject
     */
    public static JSONObject parseJsonObject(String jsonString){
        return JSONObject.parseObject(jsonString);
    }

    /**
     * 价格json字符转换JSONObject
     * @param   jsonString   需要转换的字符
     *@return     JSONObject
     */
    public static JSONArray parseJsonArray(String jsonString){
        return JSONArray.parseArray(jsonString);
    }

}


