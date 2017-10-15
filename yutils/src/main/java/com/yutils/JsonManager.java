/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.yutils;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author yangjiang
 * @date 2016/12/7
 * E-Mail:1007181167@qq.com
 * Description:[JSON解析管理类]  使用 fastjson
 **/
public class JsonManager {

    private  static  final String TAG=JsonManager.class.getName();
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
    public static <T> T jsonToBean(@NonNull String json,@NonNull Class<T> cls) {
        return JSON.parseObject(json, cls);
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return T
     */
    public static <T> T jsonToBean(@NonNull String json,@NonNull Type cls) {
        return JSON.parseObject(json, cls);
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @return T
     */
    public static <T> T jsonToBean(@NonNull String json) {
        TypeReference typeReference = new TypeReference<T>() {};
        return JSON.parseObject(json, typeReference.getType());
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return T
     */
    public static <T> T jsonToBean(@NonNull byte [] json,@NonNull Class<T> cls) {
        return JSON.parseObject(json, cls);
    }


    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return T     TypeReference typeReference = new TypeReference<JsonBean>() {};
     */
    public static <T> T jsonToBean(@NonNull byte [] json,@NonNull Type cls) {
        return JSON.parseObject(json, cls);
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json     需要转化json字符
     * @return T     TypeReference typeReference = new TypeReference<JsonBean>() {};
     */
    public static <T> T jsonToBean(@NonNull byte [] json) {
        TypeReference typeReference = new TypeReference<T>() {};
        return JSON.parseObject(json, typeReference.getType());
    }
    /**
     * 将json字符串转换成java List对象
     *
     * @param json     需要转化json字符
     * @param cls      cls  返回实例对象类型
     * @return List<T>
     */
    public static <T> List<T> jsonToList(@NonNull String json,@NonNull Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj  转化的对象实例
     * @return String  JSON 字符
     */
    public static String beanToJson(@NonNull Object obj) {
        String result = JSON.toJSONString(obj);
        Logger.d(TAG, result);
        return result;
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj  转化的对象实例
     * @param    filter 序列化处理
     * @return String  JSON 字符
     */
    public static String beanToJson(@NonNull Object obj,@NonNull SimplePropertyPreFilter filter) {
        String result = JSON.toJSONString(obj, filter);
        Logger.d(TAG, result);
        return result;
    }


    /**
     * 价格json字符转换JSONObject
     * @param   jsonString   需要转换的字符
     *@return     JSONObject
     */
    public static JSONObject parseJsonObject(@NonNull String jsonString){
        return JSONObject.parseObject(jsonString);
    }

    /**
     * 价格json字符转换JSONObject
     * @param   jsonString   需要转换的字符
     *@return     JSONObject
     */
    public static JSONArray parseJsonArray(@NonNull String jsonString){
        return JSONArray.parseArray(jsonString);
    }

}


