package com.translatemodule.bean;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  有道翻译实体类
 * 0 - 正常
 * 　20 - 要翻译的文本过长
 * 　30 - 无法进行有效的翻译
 * 　40 - 不支持的语言类型
 * 　50 - 无效的key
 * 　60 - 无词典结果，仅在获取词典结果生效
 */
public class TranslationBean {
    private String[] translation;
    private String query;
    private Integer errorCode;
    private String web;
}
