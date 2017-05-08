package com.translatemodule.bean;

import java.util.List;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:   百度翻译实体类
 */
public class TransBean {
    private String from;

    private String to;

    private List<TransBodyBean> trans_result;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return this.to;
    }

    public void setTrans_result(List<TransBodyBean> trans_result) {
        this.trans_result = trans_result;
    }

    public List<TransBodyBean> getTrans_result() {
        return this.trans_result;
    }
}
