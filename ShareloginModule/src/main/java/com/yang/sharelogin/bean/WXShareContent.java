package com.yang.sharelogin.bean;

/**
 * Created by lujun on 2015/9/8.
 */
public class WXShareContent {

    //分享场景
    public static final int WXSession = 0;// 会话
    public static final int WXTimeline = 1;// 朋友圈
    public static final int WXFavorite = 2;// 收藏

    /**
     * 暂只提供对Text, Image, Music, Video, WebPage, Appdata的支持
     */
    public enum share_type{Text, Image, Music, Video, WebPage, Appdata, Emoji};

    public WXShareContent(){
        this.type = share_type.Text;
    }

    public WXShareContent setText(String text) {
        this.text = text;
        return this;
    }

    public WXShareContent setImage_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    public WXShareContent setTitle(String title) {
        this.title = title;
        return this;
    }

    public WXShareContent setDescription(String description) {
        this.description = description;
        return this;
    }

    public WXShareContent setWeb_url(String web_url) {
        this.web_url = web_url;
        return this;
    }

    public WXShareContent setType(share_type type) {
        this.type = type;
        return this;
    }

    public WXShareContent setScene(int scene) {
        this.scene = scene;
        return this;
    }

    public WXShareContent setVideo_url(String video_url) {
        this.video_url = video_url;
        return this;
    }

    public WXShareContent setMusic_url(String music_url) {
        this.music_url = music_url;
        return this;
    }
    public WXShareContent setApp_data_path(String app_data_path) {
        this.app_data_path = app_data_path;
        return this;
    }

    private int scene = WXSession;// 默认发送到会话

    private share_type type;

    private String text;

    private String image_url;

    private String title;

    private String description;

    private String web_url;

    private String video_url;

    private String music_url;

    private String app_data_path;

    public int getScene() {
        return scene;
    }

    public share_type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getWeb_url() {
        return web_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getMusic_url() {
        return music_url;
    }

    public String getApp_data_path() {
        return app_data_path;
    }


}
