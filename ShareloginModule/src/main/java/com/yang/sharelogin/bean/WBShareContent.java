package com.yang.sharelogin.bean;

import android.os.Bundle;

/**
 * Created by lujun on 2015/9/7.
 */
public class WBShareContent {

    // 提供两种API接口
    // Upload上传图片并发布一条微博
    // UPLOAD_URL_TEXT发布一条微博同时指定上传的图片或图片url
    public static final int UPLOAD = 1;
    public static final int UPLOAD_URL_TEXT = 2;
    
    public static final int COMMON_SHARE = 3;
    public static final int API_SHARE = 4;
    
    // 网页、音乐、视频、声音
    public static final int WEBPAGE = 101;
    public static final int MUSIC = 102;
    public static final int VIDEO = 103;
    public static final int VOICE = 104;

    public WBShareContent(){
        mBundle = new Bundle();
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public WBShareContent setStatus(String status) {
        this.status = status;
        this.hasText = true;
        mBundle.putString("status", status);
        return this;
    }

    public WBShareContent setVisible(int visible) {
        this.visible = visible;
        mBundle.putInt("visible", visible);
        return this;
    }

    public WBShareContent setList_id(String list_id) {
        this.list_id = list_id;
        mBundle.putString("list_id", list_id);
        return this;
    }

    public WBShareContent setImage_path(String path) {
        this.image_path = path;
        this.hasImage = true;
        mBundle.putString("image_path", path);
        return this;
    }

    public WBShareContent setImage_url(String image_url) {
        this.image_url = image_url;
        this.hasImage = true;
        mBundle.putString("image_url", image_url);
        return this;
    }

    public WBShareContent setUrl(String url) {
        this.url = url;
        mBundle.putString("url", url);
        return this;
    }

    public WBShareContent setPic_id(String pic_id) {
        this.pic_id = pic_id;
        mBundle.putString("pic_id", pic_id);
        return this;
    }

    public WBShareContent setLat(float lat) {
        this.lat = lat;
        mBundle.putFloat("lat", lat);
        return this;
    }

    public WBShareContent setLongt(float longt) {
        this.longt = longt;
        mBundle.putFloat("longt", longt);
        return this;
    }

    public WBShareContent setAnnotations(String annotations) {
        this.annotations = annotations;
        mBundle.putString("annotations", annotations);
        return this;
    }

    public WBShareContent setRip(String rip) {
        this.rip = rip;
        mBundle.putString("rip", rip);
        return this;
    }

    public WBShareContent setWbShareApiType(int wbShareApiType) {
        this.wbShareApiType = wbShareApiType;
        mBundle.putInt("wbShareApiType", wbShareApiType);
        return this;
    }

    private String status;/// 要发布的微博文本内容，必须做URLencode，内容不超过140个汉字

    private int visible;// 微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0

    private String list_id;// 微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
    
    private String image_path;/// 本地图片path，imgObg, webpageObj、music、video

    private String image_url;/// 远程图片的URL

    private String url;// 图片的URL地址，必须以http开头

    private String pic_id;// 已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过9个

    private float lat;// 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0

    private float longt;// 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0

    private String annotations;// 元数据

    private String rip;// 开发者上报的操作用户真实IP，形如：211.156.0.1

    private int share_method = COMMON_SHARE;// 3-普通方式分享，4-API方式分享

    private Bundle mBundle;

    private int wbShareApiType;

    // share content 2
    private String title;/// webpage、music、video、voice标题

    private String description;/// webpage、music、video、voice描述

    private String actionUrl; /// webpage、music、video、voice URL

    private String dataUrl; /// music、video、voice

    private String dadtaHdUrl; /// music、video、voice

    private int duration; /// music、video、voice

    private String defaultText; /// webpage、music、video、voice默认文案

    private int content_type = -1;// 分享内容类型，包括网页、音乐等

    private boolean hasText = false;

    private int share_type;

    private boolean hasImage = false;

    public WBShareContent setTitle(String title) {
        this.title = title;
        mBundle.putString("title", title);
        return this;
    }

    public WBShareContent setDescription(String description) {
        this.description = description;
        mBundle.putString("description", description);
        return this;
    }

    public WBShareContent setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
        mBundle.putString("actionUrl", actionUrl);
        return this;
    }

    public WBShareContent setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
        mBundle.putString("dataUrl", dataUrl);
        return this;
    }

    public WBShareContent setDadtaHdUrl(String dadtaHdUrl) {
        this.dadtaHdUrl = dadtaHdUrl;
        mBundle.putString("dadtaHdUrl", dadtaHdUrl);
        return this;
    }

    public WBShareContent setDuration(int duration) {
        this.duration = duration;
        mBundle.putInt("duration", duration);
        return this;
    }

    public WBShareContent setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        mBundle.putString("defaultText", defaultText);
        return this;
    }

    public WBShareContent setShare_method(int method){
        this.share_method = method;
        mBundle.putInt("share_method", method);
        return this;
    }

    public WBShareContent setShare_type(int share_type) {
        this.share_type = share_type;
        mBundle.putInt("share_type", share_type);
        return this;
    }

    public WBShareContent setContent_type(int content_type) {
        this.content_type = content_type;
        mBundle.putInt("content_type", content_type);
        return this;
    }

}
