package com.translatemodule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.translatemodule.bean.TransBean;
import com.translatemodule.bean.TranslationBean;
import com.yutils.JsonManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  百度和有道集成翻译管理类
 */
public class TranslateManager {
    public static final String TAG = "TranslateManager";
    private static final String BAIUD_API = "http://api.fanyi.baidu.com/";
    private static final String YOUDAO_API = "http://fanyi.youdao.com/";
    // private String baiDuAppId = "20170507000046511";
    // private String securityKey = "d302ZyY9lAXS52_yg3aG";
    // private String youDaoKey = "36078788";
    // private String youDaoSecurityKey = "yangchaojang";
    private String baiDuAppId = null;
    private String securityKey = null;
    private String youDaoKey = null;
    private String youDaoSecurityKey = null;


//    public static void main(String[] args) {
//        String query = "高度";
//        TranslateManager translateManager = new TranslateManager();
//        translateManager.getBaiDu(query, "auto", "en");
//        // System.out.println(api.getTransResult(query, "auto", "en"));
//
//    }

    /****
     * 百度 api
     *
     * @param appId       百度appId
     * @param securityKey 密钥
     *****/
    public void initBaidu(String appId, String securityKey) {
        this.baiDuAppId = appId;
        this.securityKey = securityKey;
    }

    /****
     * 有道 api
     *
     * @param youDaoKey   youDaoKey
     * @param securityKey 有道秘钥
     *****/
    public void initYouDao(String youDaoKey, String securityKey) {
        this.youDaoKey = youDaoKey;
        this.youDaoSecurityKey = securityKey;
    }

    /****
     * 百度翻译
     *
     * @param body 翻译的内容
     * @param from 要翻译的语言
     * @param to   翻译成的语言 百度
     ***/
    public Observable<TransBean> getBaiDu(String body, String from, String to) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BAIUD_API).build();
        return retrofit.create(TranslateService.class)
                .getBaiDu(buildBaiParams(body, from, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*****
     * 百度翻译  翻译成英语
     *
     * @param body 翻译的内容
     * @return Observable
     ****/
    public Observable<TransBean> getBaiDuEn(String body) {
        return getBaiDu(body, "auto", "en");
    }

    /*****
     * 百度翻译  翻译成汉语
     *
     * @param body 翻译的内容
     * @return Observable
     ****/
    public Observable<TransBean> getBaiDuZh(String body) {
        return getBaiDu(body, "auto", "zh");
    }

    /****
     * 有道翻译
     *
     * @param query 要翻译的语言 必须是UTF-8编码  自动检测语言 ，目前支持中英文
     ***/
    public Observable<TranslationBean> getYouDao(String query) throws UnsupportedEncodingException {
        return getYouDao(query, "translate").map(new Func1<String, TranslationBean>() {
            @Override
            public TranslationBean call(String s) {
                if (s.isEmpty()) return null;
                return JsonManager.jsonToBean(s, TranslationBean.class);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /****
     * 有道翻译
     *
     * @param query 要翻译的语言 必须是UTF-8编码  自动检测语言 ，目前支持中英文
     * @param only  可选参数，dict表示只获取词典数据，translate表示只获取翻译数据，默认为都获取
     * @return Observable<String>
     ***/
    private Observable<String> getYouDao(String query, String only) throws UnsupportedEncodingException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(YOUDAO_API).build();
        return retrofit.create(TranslateService.class)
                .getYouDaoString(buildYouParams(query, only))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /****
     * 有道翻译
     *
     * @param query 要翻译的语言 必须是UTF-8编码  自动检测语言 ，目前支持中英文
     * @param only  可选参数，dict表示只获取词典数据，translate表示只获取翻译数据，默认为都获取
     * @return Observable<JSONObject>
     ***/
    public Observable<JSONObject> getYouDaoJson(String query, String only) throws UnsupportedEncodingException {
        return getYouDao(query, only).map(new Func1<String, JSONObject>() {
            @Override
            public JSONObject call(String s) {
                if (s.isEmpty()) return null;
                return JSON.parseObject(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /****
     * 封装参数
     *
     * @param query 查询内容
     * @param from  要翻译的语言
     * @param to    翻译成的语言 百度
     * @param to    翻译成的语言 百度
     * @return Map<String, String>
     ***/
    private Map<String, String> buildBaiParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", baiDuAppId);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        String src = baiDuAppId + query + salt + securityKey; // 加密前的原文
        params.put("sign", get32MD5(src));
        return params;
    }

    /****
     * 封装参数
     *
     * @param query 要翻译的语言 必须是UTF-8编码  自动检测语言 ，目前支持中英文
     * @return Map<String,String>
     ***/
    private Map<String, String> buildYouParams(String query, String only) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put("type", "data");
        params.put("doctype", "json");
        params.put("version", "1.1");
        params.put("keyfrom", youDaoKey);
        params.put("key", youDaoSecurityKey);
        params.put("q", URLEncoder.encode(query, "UTF-8"));// 要翻译的文本，必须是UTF-8编码，字符长度不能超过200个字符，需要进行urlencode编码
        params.put("only", only);//dict表示只获取词典数据，translate表示只获取翻译数据，默认为都获取
        return params;
    }

    /**
     * MD5 32位加密方法一 小写
     *
     * @param string
     * @return
     */
    public static String get32MD5(String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = string.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
