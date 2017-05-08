package com.yang.sharelogin.platform.weibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.yang.sharelogin.R;
import com.yang.sharelogin.bean.Config;
import com.yang.sharelogin.bean.WBShareContent;
import com.yang.sharelogin.listener.StateListener;
import com.yang.sharelogin.platform.weibo.listener.AsyncRequestListener;
import com.yang.sharelogin.platform.weibo.listener.AuthListener;
import com.yang.sharelogin.platform.weibo.openapi.UsersAPI;
import com.yang.sharelogin.util.ImageUtils;
import com.yang.sharelogin.util.WXUtil;

/**
 * Created by lujun on 2015/9/7.
 */
public class AssistActivity extends Activity implements IWeiboHandler.Response {
    
    private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
                                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                                        + "follow_app_official_microblog,invitation_write";

    private static final String TAG = "AssistActivity";

    private String appid;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private UsersAPI mUsersAPI;
    private IWeiboShareAPI wbShareAPI;

    private Intent mIntent;
    private Bundle mBundle;
    private LinearLayout loadingLayout;

    private int apiType;
    private boolean hasText = false;
    private boolean hasImage = false;
    private boolean hasWebpage = false;
    private boolean hasMusic = false;
    private boolean hasVideo = false;
    private boolean hasVoice = false;

    private static final int DEFAULT_DURATION = 10;
    public static final String KEY_SHARE_TYPE = "key_share_type";
    private static final String IMG_PATH = "/TPShareLogin/";
    private static final String IMG_NAME = "tmpshareimg.png";
    private static final int THUMB_SIZE = 116;//32k限制

    private int mShareType = Config.SHARE_CLIENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpsl_view_loading);
        loadingLayout = (LinearLayout) findViewById(R.id.tpsl_ll_progress);

        //
        String redirectUrl = "";
        final int type = getIntent().getIntExtra(Config.KEY_OF_TYPE, 0x1000);
        if ((appid = getIntent().getStringExtra(Config.KEY_OF_APPID)) == null
                || (redirectUrl = getIntent().getStringExtra(Config.KEY_OF_REDIRECT_URL)) == null
                || type == 0x1000){
            finish();
        }

        // 创建微博分享实例
        wbShareAPI = WeiboShareSDK.createWeiboAPI(this, appid);
        wbShareAPI.registerApp();
        mIntent = new Intent(Config.KEY_OF_WB_BCR_ACTION);
        if (savedInstanceState != null){
            wbShareAPI.handleWeiboResponse(getIntent(), this);
        }

        mBundle = getIntent().getBundleExtra(Config.KEY_OF_BUNDLE);
        if (mBundle == null){
            mIntent.putExtra(Config.KEY_OF_WB_BCR, "bundle null!");
            onSendBroadCast();
        }

        if (type == Config.SHARE_TYPE
                && mBundle.getInt("share_method", 0) == WBShareContent.COMMON_SHARE){
            int content_type = mBundle.getInt("content_type", -1);
            mShareType = mBundle.getInt("share_type", Config.SHARE_CLIENT);
            if (!getBundleString("status").equals("")){
                hasText = true;
            }
            if (content_type == WBShareContent.WEBPAGE){
                hasWebpage = true;
            }else if (content_type == WBShareContent.MUSIC){
                hasMusic = true;
            }else if (content_type == WBShareContent.VIDEO){
                hasVideo = true;
            } else if (content_type == WBShareContent.VOICE){
                hasVoice = true;
            }
            if (!getBundleString("image_path").equals("") || !getBundleString("image_url").equals("")){
                if (hasWebpage || hasMusic || hasVideo || hasVoice) {
                    hasImage = false;
                }else {
                    hasImage = true;
                }
                if (!getBundleString("image_url").equals("") && getBundleString("image_path").equals("")){
                    // remote image
                    loadingLayout.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!ImageUtils.checkSDCardAvailable()){
                                return;
                            }
                            Bitmap bitmap = WXUtil.getBitmapFromUrl(getBundleString("image_url"));
                            if (!hasWebpage && !hasMusic && !hasVideo && !hasVoice){
                                ImageUtils.savePhotoToSDCard(bitmap,
                                        Environment.getExternalStorageDirectory() + IMG_PATH, IMG_NAME);
                            }else {
                                ImageUtils.savePhotoToSDCard(WXUtil.scaleCenterCrop(bitmap, THUMB_SIZE, THUMB_SIZE),
                                        Environment.getExternalStorageDirectory() + IMG_PATH, IMG_NAME);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBundle.putString("image_path",
                                            Environment.getExternalStorageDirectory() + IMG_PATH + IMG_NAME);
                                    loadingLayout.setVisibility(View.GONE);
                                    sendMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
                                }
                            });
                        }
                    }).start();
                    return;
                }
            }
            sendMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
            return;
        }

        mAuthInfo = new AuthInfo(this, appid, redirectUrl, SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        AuthListener listener = new AuthListener();
        listener.setStateListener(new StateListener<Bundle>() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(AssistActivity.this, accessToken);
                if(type == Config.LOGIN_TYPE
                        && mBundle.getInt("share_method", 0) == WBShareContent.API_SHARE) {
                    mBundle = getIntent().getBundleExtra(Config.KEY_OF_BUNDLE);
                    if (mBundle == null){
                        mIntent.putExtra(Config.KEY_OF_WB_BCR, "bundle null!");
                        onSendBroadCast();
                    }
                    apiType = mBundle.getInt("wbShareApiType", WBShareContent.UPLOAD);
                    share(accessToken);
                }else if (type == Config.LOGIN_TYPE){
                    getUserInfo(accessToken);
                }
            }

            @Override
            public void onError(String err) {
                Log.i(TAG, err);
                mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                onSendBroadCast();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel()");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                onSendBroadCast();
            }
        });
        mSsoHandler.authorize(listener);
    }

    /**
     * 微博获取用户信息
     * @param accessToken
     */
    private void getUserInfo(final Oauth2AccessToken accessToken){
        if (accessToken != null && accessToken.isSessionValid()){
            mUsersAPI = new UsersAPI(this, appid, accessToken);
            AsyncRequestListener listener = new AsyncRequestListener();
            listener.setStateListener(new StateListener<String>() {
                @Override
                public void onComplete(String s) {
                    // 返回格式如下
                    /*{
                      "user_data":{},
                      "verify_data":{}   \"
                    }*/
                    String verifyData = "\"uid\":\"" + accessToken.getUid() + "\","
                            + "\"access_token\":\"" + accessToken.getToken() + "\","
                            + "\"refresh_token\":\"" + accessToken.getRefreshToken() + "\","
                            + "\"phone_num\":\"" + accessToken.getPhoneNum() + "\","
                            + "\"expires_in\":\"" + Long.toString(accessToken.getExpiresTime()) + "\"";
                    String result = "{\"user_data\":" + s + "," + "\"verify_data\":{" +  verifyData + "}}";
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, result);
                    onSendBroadCast();
                }

                @Override
                public void onError(String err) {
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                    onSendBroadCast();
                }

                @Override
                public void onCancel() {
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                    onSendBroadCast();
                }
            });
            mUsersAPI.show(Long.parseLong(accessToken.getUid()), listener);
        }else {
            mIntent.putExtra(Config.KEY_OF_WB_BCR, "accessToken null or invalid!");
            onSendBroadCast();
        }
    }

    private void share(Oauth2AccessToken accessToken){
        if (accessToken != null && accessToken.isSessionValid() && mBundle != null) {
            WeiboParameters params = new WeiboParameters(appid);
            // 详见http://open.weibo.com/wiki/2/statuses/upload_url_text
//        params.put("source", appid); // 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
            params.put("access_token", accessToken.getToken());// 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
            params.put("status", getBundleString("status"));
            params.put("visible",mBundle.getInt("visible", 0));
            params.put("list_id", getBundleString("list_id"));
            if (apiType == WBShareContent.UPLOAD){
                params.put("pic", BitmapFactory.decodeFile(getBundleString("image_path")));
            }else {
                params.put("url", getBundleString("url"));
            }
//        params.put("pic_id", getBundleString("pic_id", ""));
//        params.put("lat", mBundle.getFloat("lat", 0.0f));
//        params.put("long", mBundle.getFloat("longt", 0.0f));
//        params.put("annotations",  getBundleString("annotations", ""));
//        params.put("rip", getBundleString("rip", ""));

            AsyncRequestListener listener = new AsyncRequestListener();
            listener.setStateListener(new StateListener() {
                @Override
                public void onComplete(Object o) {
                    Log.i(TAG, o.toString());
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, o.toString());
                    onSendBroadCast();
                }

                @Override
                public void onError(String err) {
                    Log.i(TAG, err);
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                    onSendBroadCast();
                }

                @Override
                public void onCancel() {
                    Log.i(TAG, "onCancel()");
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                    onSendBroadCast();
                }
            });
            String api = "https://api.weibo.com/2/statuses";
            api += apiType == WBShareContent.UPLOAD ? "/upload.json" : "/upload_url_text.json";
            AsyncWeiboRunner runner = new AsyncWeiboRunner(this);
            runner.requestAsync(
                    api,
                    params,
                    "POST",
                    listener);
        }else {
            mIntent.putExtra(Config.KEY_OF_WB_BCR, "accessToken null or invalid!");
            onSendBroadCast();
        }
    }

    private String getBundleString(String key){
        if (mBundle.getString(key) == null || TextUtils.isEmpty(mBundle.getString(key))){
            return "";
        }
        return mBundle.getString(key);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode){
            case WBConstants.ErrorCode.ERR_OK:
                Log.i(TAG, "share success!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share success!");
                onSendBroadCast();
                break;

            case WBConstants.ErrorCode.ERR_CANCEL:
                Log.i(TAG, "share canceled!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share canceled!");
                onSendBroadCast();
                break;

            case WBConstants.ErrorCode.ERR_FAIL:
                Log.i(TAG, "share failed!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share failed!");
                onSendBroadCast();
                break;

            default:
                Log.i(TAG, "unknown error!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "unknown error!");
                onSendBroadCast();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wbShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void onSendBroadCast(){
        sendBroadcast(mIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                             boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        if (mShareType == Config.SHARE_CLIENT) {
            if (wbShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = wbShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
                } else {
                    sendSingleMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
                }
            } else {
                Log.i(TAG, "not support api!");
            }
        } else if (mShareType == Config.SHARE_ALL_IN_ONE) {
            sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
        }

        // 注释掉此行AssistActivity将阻塞,直至微博给回调,在等到回调之前最好加个loading
//        mIntent.putExtra(Config.KEY_OF_WB_BCR, "weibo has send!");
//        onSendBroadCast();
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                  boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }

        // 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 发送请求消息到微博，唤起微博分享界面
        if (mShareType == Config.SHARE_CLIENT) {
            wbShareAPI.sendRequest(AssistActivity.this, request);
        } else if (mShareType == Config.SHARE_ALL_IN_ONE) {
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            wbShareAPI.sendRequest(this, request, mAuthInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException( WeiboException arg0 ) {
                }

                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                   boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        wbShareAPI.sendRequest(AssistActivity.this, request);
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getBundleString("status");
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        // 设置缩略图。
        // 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeFile(getBundleString("image_path"));
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = getBundleString("title");
        mediaObject.description = getBundleString("description");

        // 设置 Bitmap 类型的图片到视频对象里,设置缩略图。
        // 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeFile(getBundleString("image_path"));
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = getBundleString("actionUrl");
        mediaObject.defaultText = getBundleString("defaultText");
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = getBundleString("title");
        musicObject.description = getBundleString("description");
        Bitmap bitmap = BitmapFactory.decodeFile(getBundleString("image_path"));

        // 设置 Bitmap 类型的图片到视频对象里,设置缩略图。
        // 注意：最终压缩过的缩略图大小不得超过 32kb。
        musicObject.setThumbImage(bitmap);
        musicObject.actionUrl = getBundleString("actionUrl");
        musicObject.dataUrl = getBundleString("dataUrl");
        musicObject.dataHdUrl = getBundleString("dataHdUrl");
        musicObject.duration = mBundle.getInt("duration", DEFAULT_DURATION);
        musicObject.defaultText = getBundleString("defaultText");
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = getBundleString("title");
        videoObject.description = getBundleString("description");
        Bitmap bitmap = BitmapFactory.decodeFile(getBundleString("image_path"));
        // 设置 Bitmap 类型的图片到视频对象里，设置缩略图。
        // 注意：最终压缩过的缩略图大小不得超过 32kb。

        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            System.out.println("size: "+ os.toByteArray().length );
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = getBundleString("actionUrl");
        videoObject.dataUrl = getBundleString("dataUrl");
        videoObject.dataHdUrl = getBundleString("dataHdUrl");
        videoObject.duration = mBundle.getInt("duration", DEFAULT_DURATION);
        videoObject.defaultText = getBundleString("defaultText");
        return videoObject;
    }

    /**
     * 创建多媒体（音频）消息对象。
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = getBundleString("title");
        voiceObject.description = getBundleString("description");
        // 设置 Bitmap 类型的图片到视频对象里，设置缩略图。
        // 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeFile(getBundleString("image_path"));
        voiceObject.setThumbImage(bitmap);
        voiceObject.actionUrl = getBundleString("actionUrl");
        voiceObject.dataUrl = getBundleString("dataUrl");
        voiceObject.dataHdUrl = getBundleString("dataHdUrl");
        voiceObject.duration = mBundle.getInt("duration", DEFAULT_DURATION);
        voiceObject.defaultText = getBundleString("defaultText");
        return voiceObject;
    }
}
