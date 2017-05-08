package com.yang.sharelogin;

/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 登陸分享管理類
 */
public class TPManager {

    private static TPManager mInstance;

    public static TPManager getInstance() {
        if (mInstance == null) {
            synchronized (TPManager.class) {
                if (mInstance == null) {
                    mInstance = new TPManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * Init share config, you should call thie method in your own Application class
     *
     * @param wbRedirectUrl 微博回调地址
     * @param wbAppKey      微博App Key
     * @param wbAppSecret   微博 App Secret
     * @param qqAppId       QQ AppId
     * @param qqAppSecret   QQ AppSecret
     * @param wxAppId       微信 AppId
     * @param wxAppSecret   微信 AppSecret
     */
    public void initAppConfig(String wbRedirectUrl,
                              String wbAppKey, String wbAppSecret,
                              String qqAppId, String qqAppSecret,
                              String wxAppId, String wxAppSecret) {
        WBRedirectUrl = wbRedirectUrl;
        WBAppId = wbAppKey;
        WBAppSecret = wbAppSecret;
        QQAppId = qqAppId;
        QQAppSecret = qqAppSecret;
        WXAppId = wxAppId;
        WXAppSecret = wxAppSecret;
    }

    public String getWBAppId() {
        return WBAppId;
    }

    public String getWBAppSecret() {
        return WBAppSecret;
    }

    public String getWBRedirectUrl() {
        return WBRedirectUrl;
    }

    public String getQQAppId() {
        return QQAppId;
    }

    public String getQQAppSecret() {
        return QQAppSecret;
    }

    public String getWXAppId() {
        return WXAppId;
    }

    public String getWXAppSecret() {
        return WXAppSecret;
    }

    private String WBAppId = "";
    private String WBAppSecret = "";
    private String WBRedirectUrl = "";
    private String QQAppId = "";
    private String QQAppSecret = "";
    private String WXAppId = "";
    private String WXAppSecret = "";
}
