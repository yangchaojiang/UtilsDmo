package com.translatemodule.bean;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 百度翻译实体类
 */
public class TransBodyBean {
    public static final String TAG = "TransBodyBean";
    private String src;
    private String dst;

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return this.src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getDst() {
        return this.dst;
    }

}
